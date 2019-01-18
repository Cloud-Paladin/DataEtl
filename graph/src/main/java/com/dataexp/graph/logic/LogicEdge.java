package com.dataexp.graph.logic;

import java.util.Objects;

public class LogicEdge {

    private final int id;
    private final String name;
    private final LogicPort sourcePort;
    private final LogicPort targetPort;

    public LogicEdge(LogicPort sourcePort, LogicPort targetPort, int id) {
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
}
