package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.AbstractSourceNode;

import java.util.List;

public class FileSource extends AbstractSourceNode {
    public FileSource(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public String genNodeConfig() {
        return null;
    }

    @Override
    public void initNodeConfig(String config) {

    }

    public FileSource(int id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public String getDefaultName() {
        return "文件输入";
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
