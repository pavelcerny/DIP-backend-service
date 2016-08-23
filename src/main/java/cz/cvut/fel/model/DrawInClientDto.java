package cz.cvut.fel.model;

import java.util.ArrayList;

/**
 * Created by cerny on 22.08.2016.
 */
public class DrawInClientDto {
    private String watsonOutputText;
    private String watsonOutputContext;
    private boolean supressUserInput;
    private ArrayList<Button> buttons;

    public DrawInClientDto() {
    }

    public DrawInClientDto(String watsonOutputText, String watsonOutputContext, boolean supressUserInput, ArrayList<Button> buttons) {
        this.watsonOutputText = watsonOutputText;
        this.watsonOutputContext = watsonOutputContext;
        this.supressUserInput = supressUserInput;
        this.buttons = buttons;
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

    public boolean getSupressUserInput() {
        return supressUserInput;
    }

    public void setSupressUserInput(boolean supressUserInput) {
        this.supressUserInput = supressUserInput;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }
}
