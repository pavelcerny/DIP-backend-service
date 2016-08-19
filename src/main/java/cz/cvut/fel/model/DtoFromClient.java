package cz.cvut.fel.model;

import java.util.Map;

/**
 * Created by cerny on 19.08.2016.
 */
public class DtoFromClient {
    private Map<String,Object> Input;
    private Map<String,Object> Context;

    public DtoFromClient() {
    }

    public DtoFromClient(Map<String, Object> input, Map<String, Object> context) {
        Input = input;
        Context = context;
    }

    public Map<String, Object> getInput() {
        return Input;
    }

    public void setInput(Map<String, Object> input) {
        Input = input;
    }

    public Map<String, Object> getContext() {
        return Context;
    }

    public void setContext(Map<String, Object> context) {
        Context = context;
    }
}
