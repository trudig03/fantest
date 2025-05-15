package com.project.fanla.controller.fan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketTestController {

    @GetMapping("/websocket-test")
    public String websocketTest() {
        return "websocket-test";
    }
}
