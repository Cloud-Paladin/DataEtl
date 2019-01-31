package com.dataexp.graph.logic.serial;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Bing.Li
 * @create: 2019-01-30
 */
public class SerialNode {
    private String type;
    private int id;
    private String name;
    private int x,y;
    private List<Integer> inputPortList = new ArrayList<>();
    private List<Integer> outputPortList = new ArrayList<>();
    private String config;

    public SerialNode() {
    }

    public SerialNode(String type,int id, String name, int x, int y, List<Integer> inputPortList, List<Integer> outputPortList, String config) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.inputPortList = inputPortList;
        this.outputPortList = outputPortList;
        this.config = config;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Integer> getInputPortList() {
        return inputPortList;
    }

    public void setInputPortList(List<Integer> inputPortList) {
        this.inputPortList = inputPortList;
    }

    public List<Integer> getOutputPortList() {
        return outputPortList;
    }

    public void setOutputPortList(List<Integer> outputPortList) {
        this.outputPortList = outputPortList;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
