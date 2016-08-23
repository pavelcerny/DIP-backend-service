package cz.cvut.fel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import cz.cvut.fel.model.*;
import cz.cvut.fel.tool.WatsonDialogTool;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public MessageResponse continueDialogWithWatson(String text, String contextJson) {
        Map<String, Object> context = getMapFromJson(contextJson);
        return watsonDialogTool.continueDialogWithWatson(text, context);
    }

    @RequestMapping("/init")
    public String getDialogView(Model model) {
        //init
        MessageResponse response = initDialogWithWatson();
        DrawInClientDto drawInClientDto = convertToDrawInClientDto(response);

        model.addAttribute("drawInClientDto", drawInClientDto)
                .addAttribute("formBackingDto", new FormBackingDto());

        return "poi-dialog";
    }

    @RequestMapping("/continue")
    public String getDialogView(@ModelAttribute FormBackingDto formBackingDto, Model model) throws Exception {
        //continue
        String inputString;
        switch (formBackingDto.getAction()){
            case "back":
                inputString = "jít krok zpět";
                break;
            case "reset":
                //volej resetování
                return getDialogView(model);
            case "turnOnNavigation":
                return getFinishedView(model);
            case "send":
                inputString = formBackingDto.getInput();
                break;
            default:
                throw new Exception(String.format("button action=%s is not implemented yet",formBackingDto.getAction()));
        }

        return getDialogView(inputString,formBackingDto.getContext(),model);
    }

    public String getDialogView(String inputString, String contextJsonString, Model model){
        //continue
        MessageResponse response = continueDialogWithWatson(inputString,contextJsonString);
        DrawInClientDto drawInClientDto = convertToDrawInClientDto(response);

        model.addAttribute("drawInClientDto", drawInClientDto)
                .addAttribute("formBackingDto", new FormBackingDto());

        return "poi-dialog";
    }

    public String getFinishedView(Model model) {
        return "poi-finished";
    }

    private Map<String, Object> getMapFromJson(String jsonString) {
        HashMap<String,Object> context;
        try {
            context =
                    new ObjectMapper().readValue(jsonString, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            context = new HashMap<>();
        }
        return context;
    }

    private String getJsonFromMap(Map<String, Object> jsonObject) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private DrawInClientDto convertToDrawInClientDto(MessageResponse response) {
        String contextJsonString = getJsonFromMap(response.getContext());
        String text = response.getTextConcatenated("\n");
        boolean supressUserInput = isSupressedUserInput(response);
        ArrayList<Button> buttonsToDraw = getButtons(response);

        return new DrawInClientDto(text,contextJsonString,supressUserInput,buttonsToDraw);
    }

    private ArrayList<Button> getButtons(MessageResponse response) {
        ArrayList<Button> buttonsToDraw = new ArrayList<>();
        if (response.getOutput().containsKey("buttons")){
            ArrayList<String> buttons = (ArrayList<String>) response.getOutput().get("buttons");
            for (String button :
                    buttons) {
                Button buttonToDraw;
                String label;
                switch (button){
                    case "turnOnNavigation":
                        String street = (String) response.getContext().get("TargetStreet");
                        double landregistryNumber = (double) response.getContext().get("TargetLandregistryNumber");

                        label = String.format("Spustit navigaci na cíl %s %.0f", street, landregistryNumber);
                        buttonToDraw = new Button("turnOnNavigation", label);
                        break;
                    default:
                        buttonToDraw = new Button("notSpecified","Neznámé tlačítko není rozpoznáno PoiDiallogControllerem");
                }
                buttonsToDraw.add(buttonToDraw);
            }
        }
        return buttonsToDraw;
    }

    private boolean isSupressedUserInput(MessageResponse response) {
        boolean supressUserInput = false;
        if (response.getOutput().containsKey("supressInput")){
            supressUserInput = (boolean) response.getOutput().get("supressInput");
        }
        return supressUserInput;
    }

}
