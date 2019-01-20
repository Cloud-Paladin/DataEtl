package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.SinkNode;

import java.util.List;

public class FileSink extends SinkNode {

    public FileSink(int id, String name, int x, int y) {
        super(id, name, x, y);
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
