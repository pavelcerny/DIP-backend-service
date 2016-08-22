package cz.cvut.fel.model;

/**
 * Created by cerny on 22.08.2016.
 */
public class DtoBackendToClient {
    private String watsonOutputText;
    private String watsonOutputContext;
    private ReplyType replyType;
    private Button[] buttons;

    public DtoBackendToClient() {
    }

    public String getWatsonOutputText() {
        return watsonOutputText;
    }

    public void setWatsonOutputText(String watsonOutputText) {
        this.watsonOutputText = watsonOutputText;
    }

    public String getWatsonOutputContext() {
        return watsonOutputContext;
    }

    public void setWatsonOutputContext(String watsonOutputContext) {
        this.watsonOutputContext = watsonOutputContext;
    }

    public ReplyType getReplyType() {
        return replyType;
    }

    public void setReplyType(ReplyType replyType) {
        this.replyType = replyType;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }
}
