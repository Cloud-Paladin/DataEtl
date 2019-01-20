package com.dataexp.graph.logic;

import com.dataexp.graph.logic.component.*;

public class ComponentFactory {

    public static LogicNode createNode(int id, String type, String name, int x, int y) {
        switch (type) {
            case "FileSink":
                return new FileSink(id, name, x, y);
            case "FileSource":
                return new FileSource(id, name, x, y);
            case "FilterNode":
                return new FilterNode(id, name, x, y);
            case "FormatterNode":
                return new FormatterNode(id, name, x, y);
            case "SplitNode":
                return new SplitNode(id, name, x, y);
            case "UnionNode":
                return new UnionNode(id, name, x, y);
            case "WashNode":
                return new WashNode(id, name, x, y);
            default:
                return null;
        }
    }

    public static LogicNode createNode(int id, String type, int x, int y) {
        switch (type) {
            case "FileSink":
                return new FileSink(id, x, y);
            case "FileSource":
                return new FileSource(id, x, y);
            case "FilterNode":
                return new FilterNode(id, x, y);
            case "FormatterNode":
                return new FormatterNode(id, x, y);
            case "SplitNode":
                return new SplitNode(id, x, y);
            case "UnionNode":
                return new UnionNode(id, x, y);
            case "WashNode":
                return new WashNode(id, x, y);
            default:
                return null;
        }
    }

}
