package com.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin("*")
public class NotificationController {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public String healthy() {
        return "OK";
    }
}
