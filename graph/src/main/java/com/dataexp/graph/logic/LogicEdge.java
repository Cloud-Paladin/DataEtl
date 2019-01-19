package com.dataexp.graph.logic;

import java.util.Objects;

public class LogicEdge {

    private final int id;
    private String name;
    private final OutputPort sourcePort;
    private final InputPort targetPort;
    public LogicEdge(OutputPort sourcePort, InputPort targetPort, int id) {
        this.sourcePort = sourcePort;
        this.targetPort = targetPort;
        this.id = id;
        this.name = sourcePort + "_" + targetPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicEdge logicEdge = (LogicEdge) o;
        return sourcePort.equals(logicEdge.sourcePort) &&
                targetPort.equals(logicEdge.targetPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourcePort, targetPort);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OutputPort getSourcePort() {
        return sourcePort;
    }

    public InputPort getTargetPort() {
        return targetPort;
    }
}
