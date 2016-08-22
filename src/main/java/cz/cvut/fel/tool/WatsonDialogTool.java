package cz.cvut.fel.tool;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import java.util.Map;

/**
 * Comunicates with Watson Conversational Service
 * and returns JSON
 * Created by cerny on 22.08.2016.
 */
public class WatsonDialogTool {
    private static final String USERNAME = "8df4159f-bfda-48f4-9827-c331200caebd";
    private static final String PASSWORD = "lpULJnXdW2mx";
    private static final String CONVERSATION_VERSION = "2016-07-11";
    private String workspaceId;

    public WatsonDialogTool(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public MessageResponse initDialogWithWatson() {
        ConversationService service = getNewConversationService();
        MessageRequest message = buildInitialMessage();

        return getResponse(service, message, workspaceId);
    }

    public MessageResponse continueDialogWithWatson(String text, Map<String, Object> context) {
        ConversationService service = getNewConversationService();
        MessageRequest newMessage = buildMessage(text, context);

        return getResponse(service, newMessage, workspaceId);
    }

    private ConversationService getNewConversationService() {
        ConversationService service = new ConversationService(CONVERSATION_VERSION);
        service.setUsernameAndPassword(USERNAME, PASSWORD);
        return service;
    }

    private MessageResponse getResponse(ConversationService service, MessageRequest newMessage, String workspaceId) {
        return (MessageResponse) service
                .message(workspaceId, newMessage)
                .execute();
    }

    private MessageRequest buildInitialMessage() {
        return new MessageRequest.Builder()
                .inputText("")
                .build();
    }

    private MessageRequest buildMessage(String text, Map<String, Object> context) {
        return new MessageRequest.Builder()
                .inputText(text)
                .context(context)
                .build();
    }

}
