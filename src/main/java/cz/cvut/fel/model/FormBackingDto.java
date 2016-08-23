package cz.cvut.fel.model;

/**
 * Created by cerny on 23.08.2016.
 */
public class FormBackingDto {
    private String input;
    private String context;
    private String action;

    public FormBackingDto() {
    }

    public FormBackingDto(String input, String context, String action) {
        this.input = input;
        this.context = context;
        this.action = action;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
