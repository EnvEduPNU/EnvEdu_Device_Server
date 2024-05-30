package com.example.deviceserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @CrossOrigin(origins = "*")
    @GetMapping("/test")
    public ResponseEntity<?> testMethod() {
        return ResponseEntity.ok().body("yes");  // HTTP 200 OK 상태 코드 반환
    }
}
