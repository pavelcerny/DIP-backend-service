package cz.cvut.fel.model;

/**
 * Created by cerny on 22.08.2016.
 */
public class DtoBackendToClient {
    private String watsonOutputText;
    private String watsonOutputContext;
    private boolean allowUserInput;
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

    public boolean getAllowUserInput() {
        return allowUserInput;
    }

    public void setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }
}
