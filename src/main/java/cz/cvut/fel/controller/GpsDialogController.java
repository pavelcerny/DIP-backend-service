package cz.cvut.fel.controller;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.service.exception.InternalServerErrorException;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;
import cz.cvut.fel.model.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GpsDialogController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/initgpsdialog")
    public MessageResponse initDialog() {

        ConversationService service = new ConversationService("2016-07-11");
        service.setUsernameAndPassword("8df4159f-bfda-48f4-9827-c331200caebd", "lpULJnXdW2mx");

        MessageRequest newMessage = new MessageRequest.Builder()
                .inputText("")
                // Replace with the context obtained from the initial request
                //.context(...)
                .build();

        String workspaceId = "e3101b6f-4808-4630-afbe-b07744997c20";

        MessageResponse response = service
                .message(workspaceId, newMessage)
                .execute();

        System.out.println(response);

        try {
            // Your code goes here
        } catch (IllegalArgumentException e) {
            // Missing or invalid parameter
        } catch (BadRequestException e) {
            // Missing or invalid parameter
        } catch (UnauthorizedException e) {
            // Access is denied due to invalid credentials
        } catch (InternalServerErrorException e) {
            // Internal Server Error
        }


        return response;
    }
}
