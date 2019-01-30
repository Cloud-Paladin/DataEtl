package com.dataexp.graph.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputPort extends LogicPort<OutputPort> {

    public InputPort() {
    }

    public InputPort(BaseLogicNode parentNode, int id, String name) {
        super(parentNode, id, name);
    }
}
