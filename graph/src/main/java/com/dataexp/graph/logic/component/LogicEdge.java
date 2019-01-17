package com.dataexp.graph.logic.component;

import java.util.List;
import java.util.Objects;

public class LogicEdge {

    private final String edgeId;
    private final LogicPort sourcePort;
    private final LogicPort targetPort;

    public LogicEdge(LogicPort sourcePort, LogicPort targetPort) {
        this.sourcePort = sourcePort;
        this.targetPort = targetPort;
        this.edgeId = sourcePort + "_" + targetPort;
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
