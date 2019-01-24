package com.dataexp.graph.logic;

import com.dataexp.graph.logic.component.*;

public class ComponentFactory {

    public static BaseLogicNode createNode(int id, String type, String name, int x, int y) {
        switch (type) {
            case "FileAbstractSink":
                return new FileAbstractSink(id, name, x, y);
            case "FileAbstractSource":
                return new FileAbstractSource(id, name, x, y);
            case "FilterNodeBase":
                return new FilterNodeBase(id, name, x, y);
            case "FormatterNodeBase":
                return new FormatterNodeBase(id, name, x, y);
            case "SplitNodeBase":
                return new SplitNodeBase(id, name, x, y);
            case "UnionNodeBase":
                return new UnionNodeBase(id, name, x, y);
            case "WashNodeBase":
                return new WashNodeBase(id, name, x, y);
            default:
                return null;
        }
    }

    public static BaseLogicNode createNode(int id, String type, int x, int y) {
        switch (type) {
            case "FileAbstractSink":
                return new FileAbstractSink(id, x, y);
            case "FileAbstractSource":
                return new FileAbstractSource(id, x, y);
            case "FilterNodeBase":
                return new FilterNodeBase(id, x, y);
            case "FormatterNodeBase":
                return new FormatterNodeBase(id, x, y);
            case "SplitNodeBase":
                return new SplitNodeBase(id, x, y);
            case "UnionNodeBase":
                return new UnionNodeBase(id, x, y);
            case "WashNodeBase":
                return new WashNodeBase(id, x, y);
            default:
                return null;
        }
    }

}
