package com.dataexp.graph.logic.component.config;

import java.util.ArrayList;
import java.util.List;

public class WashTemplate {

    private int version;

    private List<String> functionParams = new ArrayList<String>();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getFunctionParams() {
        return functionParams;
    }

    public void setFunctionParams(List<String> functionParams) {
        this.functionParams = functionParams;
    }
}
