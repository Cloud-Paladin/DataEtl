package com.dataexp.web.task.controller;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Log log = LogFactory.getLog(TaskController.class);

    @GetMapping
    public String getAll() {
        return "Hello World!  1122";
    }
}
