package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.LogicNode;

public class LogicNodeFactory {

    public static LogicNode createNode(int id, String type,String name, int x, int y) {
        switch(type) {
            case "FileSink":
                return new FileSink(id, name, x, y);
            case "FileSource":
                return new FileSink(id, name, x, y);
            default:
                return null;
        }
    }
}
