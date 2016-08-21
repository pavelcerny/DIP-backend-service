package cz.cvut.fel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.service.exception.InternalServerErrorException;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;
import cz.cvut.fel.model.DtoFromClient;
import cz.cvut.fel.model.DtoTextAndContext;
import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/poi")
public class PoiDialogController {

    private static final String USERNAME = "8df4159f-bfda-48f4-9827-c331200caebd";
    private static final String PASSWORD = "lpULJnXdW2mx";
    private static final String WORKSPACE_ID = "e3101b6f-4808-4630-afbe-b07744997c20";
    private static final String CONVERSATION_VERSION = "2016-07-11";


    @RequestMapping("/rest/init")
    @ResponseBody
    public MessageResponse initDialog() {

        ConversationService service = new ConversationService(CONVERSATION_VERSION);
        service.setUsernameAndPassword(USERNAME, PASSWORD);

        MessageRequest newMessage = new MessageRequest.Builder()
                .inputText("")
                // Replace with the context obtained from the initial request
                //.context(...)
                .build();

        String workspaceId = WORKSPACE_ID;

        MessageResponse response = service
                .message(workspaceId, newMessage)
                .execute();

        //System.out.println(response);

        return response;
    }

    @RequestMapping(value = "/rest/continue", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageResponse continueDialog(@RequestBody DtoFromClient dtoFromClient) {

        ConversationService service = new ConversationService(CONVERSATION_VERSION);
        service.setUsernameAndPassword(USERNAME, PASSWORD);

        MessageRequest newMessage = new MessageRequest.Builder()
                .inputText((String) dtoFromClient.getInput().get("text"))
                .context(dtoFromClient.getContext())
                .build();

        String workspaceId = WORKSPACE_ID;

        MessageResponse response = service
                .message(workspaceId, newMessage)
                .execute();

        return response;
    }

    @RequestMapping
    public String initPoi(Model model){
        //init new conversation with Watson
        MessageResponse response = initDialog();

        //get Watson's reply
        String inFromWatson = getFromWatsonUnified(response);

        //get context details
        String inContext = getContextInJson(response);

        //send it to view
        model.addAttribute("inFromWatson", inFromWatson)
                .addAttribute("inContext", inContext)
                .addAttribute("dtoTextAndContext", new DtoTextAndContext());
        return "poi";
    }

    @RequestMapping(value="/continue", method=RequestMethod.POST)
    public String continuePoi(@ModelAttribute DtoTextAndContext dtoTextAndContext, Model model) {
        //parse input from JSON to objects
        Map<String, Object> input = new HashMap<>();
        input.put("text",dtoTextAndContext.getInput());

        HashMap<String,Object> context;
        try {
            context =
                    new ObjectMapper().readValue(dtoTextAndContext.getContext(), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            context = new HashMap<>();
        }

        //continue dialog with Watson
        DtoFromClient dtoFromClient = new DtoFromClient(input, context);
        MessageResponse response = continueDialog(dtoFromClient);

        //get Watson's reply
        String inFromWatson = getFromWatsonUnified(response);

        //get context details
        String inContext = getContextInJson(response);

        //send it to view
        model.addAttribute("inFromWatson", inFromWatson)
                .addAttribute("inContext", inContext)
                .addAttribute("dtoTextAndContext", new DtoTextAndContext());
        return "poi";
    }

    private String getFromWatsonUnified(MessageResponse response) {
        ArrayList<String> texts = (ArrayList<String>) response.getOutput().get("text");
        StringBuilder unifiedText = new StringBuilder();
        for (String text:texts) {
            unifiedText.append("\n");
            unifiedText.append(text);
        }
        return unifiedText.toString();
    }

    private String getContextInJson(MessageResponse response) {
        Map<String, Object> contextObject = response.getContext();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(contextObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
