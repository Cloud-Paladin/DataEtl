package com.dataexp.graph.logic;

import com.dataexp.graph.logic.serial.SerialOutputPort;

public class OutputPort extends LogicPort<InputPort, SerialOutputPort> {

    public OutputPort() {

    }

    public OutputPort(BaseLogicNode parentNode, int id, String name) {
        super(parentNode, id, name);
    }

    @Override
    public SerialOutputPort genSerialPort() {
        SerialOutputPort sp = new SerialOutputPort();
        setSerialPortAttr(sp);
        return sp;
    }

    @Override
    public void deSerialPortAttr(SerialOutputPort port) {
        super.deSerialPortAttr(port);
    }

}
