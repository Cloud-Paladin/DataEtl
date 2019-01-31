package com.dataexp.graph.logic;

import com.dataexp.graph.logic.serial.SerialInputPort;

public class InputPort extends LogicPort<OutputPort, SerialInputPort> {

    public InputPort() {
    }

    public InputPort(BaseLogicNode parentNode, int id, String name) {
        super(parentNode, id, name);
    }

    @Override
    public SerialInputPort genSerialPort() {
        SerialInputPort sp = new SerialInputPort();
        setSerialPortAttr(sp);
        return sp;
    }

    @Override
    public void deSerialPortAttr(SerialInputPort port) {
        super.deSerialPortAttr(port);
    }
}
