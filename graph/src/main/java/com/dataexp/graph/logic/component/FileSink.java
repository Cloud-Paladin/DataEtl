package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.AbstractSinkNode;

import java.util.List;

public class FileSink extends AbstractSinkNode {

    public FileSink(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public String genNodeConfig() {
        return null;
    }

    @Override
    public void initNodeConfig(String config) {

    }

    public FileSink(int id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public String getDefaultName() {
        return "文件输出";
    }

    @Override
    public List<String> getExceptions() {
        return null;
    }

    @Override
    public List<String> getWarnings() {
        return null;
    }
}