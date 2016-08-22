package cz.cvut.fel.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Bean representing the following Json structure
 *  { "text": "sampleText",
 *    "context":{
 *       "sample":"structure"
 *    }
 *  }
 *
 * Created by cerny on 22.08.2016.
 */
public class JsonTextAndContext implements Serializable {
    private String text;
    private Map<String,Object> context;

    public JsonTextAndContext() {

    }

    public JsonTextAndContext(String text, Map<String, Object> context) {

        this.text = text;
        this.context = context;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
