package com.dataexp.graph.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputPort extends LogicPort<InputPort> {

    public OutputPort(){

    }

     public OutputPort(BaseLogicNode parentNode, int id, String name) {
        super(parentNode, id, name);
    }

}
