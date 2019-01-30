package com.dataexp.graph.logic;

import com.dataexp.graph.logic.serial.SerialInputPort;
import com.dataexp.graph.logic.serial.SerialPort;

import java.util.*;

public class InputPort extends LogicPort<OutputPort, SerialInputPort> {

    public InputPort() {
    }

    public InputPort(BaseLogicNode parentNode, int id, String name) {
        super(parentNode, id, name);
    }

    @Override
    public SerialInputPort genSerialPort() {
        {
            SerialInputPort sp = new SerialInputPort();
            setSerialPort(sp);
            return sp;
        }
    }
}
