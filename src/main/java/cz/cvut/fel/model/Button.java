package cz.cvut.fel.model;

/**
 * Created by cerny on 22.08.2016.
 */
public class Button {
    private String action, label;

    public Button(String action, String label) {
        this.action = action;
        this.label = label;
    }

    public Button() {
    }

    public String getAction() {

        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
