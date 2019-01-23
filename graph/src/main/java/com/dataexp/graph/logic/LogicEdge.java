package com.dataexp.graph.logic;

import java.util.Objects;

/**
 * 逻辑图的边
 * @author: Bing.Li
 * @since: 2019-01-23 14:17
 */
public class LogicEdge {

    private final int id;
    private String name;
    private final OutputPort outputPort;
    private final InputPort inputPort;
    public LogicEdge(OutputPort outputPort, InputPort inputPort, int id) {
        this.outputPort = outputPort;
        this.inputPort = inputPort;
        this.id = id;
        this.name = outputPort + "_" + inputPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogicEdge logicEdge = (LogicEdge) o;
        return outputPort.equals(logicEdge.outputPort) &&
                inputPort.equals(logicEdge.inputPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputPort, inputPort);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    public InputPort getInputPort() {
        return inputPort;
    }
}
