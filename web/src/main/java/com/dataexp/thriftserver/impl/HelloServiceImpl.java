package com.dataexp.thriftserver.impl;

import com.dataexp.thriftserver.Hello;
import org.apache.thrift.TException;

public class HelloServiceImpl implements Hello.Iface {

    @Override
    public String helloString(String param) throws TException {
        System.out.println(param);
        return "hello: " + param;
    }
}
