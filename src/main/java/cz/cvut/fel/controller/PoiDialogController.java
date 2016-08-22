package cz.cvut.fel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import cz.cvut.fel.model.DtoBackendToClient;
import cz.cvut.fel.model.DtoJsonObjectToWatson;
import cz.cvut.fel.model.JsonTextAndContextDto;
import cz.cvut.fel.model.ReplyType;
import cz.cvut.fel.tool.WatsonDialogTool;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/poi")
public class PoiDialogController {
    private static final String WORKSPACE_ID = "e3101b6f-4808-4630-afbe-b07744997c20";
    private WatsonDialogTool watsonDialogTool = new WatsonDialogTool(WORKSPACE_ID);

    @RequestMapping(value = "/rest/watson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageResponse initDialogWithWatson() {
        return watsonDialogTool.initDialogWithWatson();
    }

    @RequestMapping(value = "/rest/watson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageResponse continueDialogWithWatson(@RequestBody JsonTextAndContextDto jsonTextAndContextDto) {
        String text = jsonTextAndContextDto.getText();
        Map<String, Object> context = jsonTextAndContextDto.getContext();
        return watsonDialogTool.continueDialogWithWatson(text, context);
    }


    @RequestMapping
    public String initPoi(Model model){
        //init new conversation with Watson
        MessageResponse response = initDialogWithWatson();

        //get Watson's reply
        String watsonOutputText = getWatsonOutputTextMerged(response);

        //get context details
        String contextInJson = getContextInJson(response);

        //experiment
        ReplyType replyType = ReplyType.USER_INPUT;

        DtoBackendToClient forClientObject = new DtoBackendToClient();
        forClientObject.setWatsonOutputText(watsonOutputText);
        forClientObject.setWatsonOutputContext(contextInJson);
        forClientObject.setReplyType(replyType);

        //send it to view
        model.addAttribute("forClientObject", forClientObject)
                .addAttribute("fromClientObject", new DtoJsonObjectToWatson());
        return "poi-dialog";
    }

//    @RequestMapping(value="/continue", method=RequestMethod.POST)
//    public String continuePoi(@ModelAttribute DtoJsonObjectToWatson fromClientObject, Model model) {
//        //parse input from JSON to objects
//        Map<String, Object> input = new HashMap<>();
//        input.put("text", fromClientObject.getInput();
//
//        HashMap<String,Object> context;
//        try {
//            context =
//                    new ObjectMapper().readValue(fromClientObject.getContext(), HashMap.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            context = new HashMap<>();
//        }
//
//        //continue dialog with Watson
//        DtoJsonObjectToWatson dtoJsonObjectToWatson = new DtoJsonObjectToWatson(input, context);
//        MessageResponse response = continueDialogWithWatson(dtoJsonObjectToWatson);
//
//        //get Watson's reply
//        String watsonOutputText = getWatsonOutputTextMerged(response);
//
//        //get context details
//        String contextInJson = getContextInJson(response);
//
//        //experiment
//        ReplyType replyType = ReplyType.USER_INPUT;
//
//        DtoBackendToClient forClientObject = new DtoBackendToClient();
//        forClientObject.setWatsonOutputText(watsonOutputText);
//        forClientObject.setWatsonOutputContext(contextInJson);
//        forClientObject.setReplyType(replyType);
//
//        //send it to view
//        model.addAttribute("forClientObject", forClientObject)
//                .addAttribute("fromClientObject", new DtoClientToBack());
//        return "poi";
//    }

    private String getWatsonOutputTextMerged(MessageResponse response) {
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
