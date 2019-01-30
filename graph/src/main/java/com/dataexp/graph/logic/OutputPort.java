package com.dataexp.graph.logic;

import com.dataexp.graph.logic.serial.SerialInputPort;
import com.dataexp.graph.logic.serial.SerialOutputPort;

import java.util.*;

public class OutputPort extends LogicPort<InputPort, SerialOutputPort> {

    public OutputPort(){

    }

     public OutputPort(BaseLogicNode parentNode, int id, String name) {
        super(parentNode, id, name);
    }

    @Override
    public SerialOutputPort genSerialPort() {
        {
            SerialOutputPort sp = new SerialOutputPort();
            setSerialPort(sp);
            return sp;
        }
    }

}
