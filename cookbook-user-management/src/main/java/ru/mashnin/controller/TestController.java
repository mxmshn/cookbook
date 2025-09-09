package ru.mashnin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        log.info("Test method run");
        log.info("Test method end");
        return ResponseEntity.ok("Test message");
    }

    @GetMapping("/secured")
    public ResponseEntity<?> secured() {
        log.info("secured method run");
        log.info("secured method end");
        return ResponseEntity.ok("secured message");
    }

    @GetMapping("/info")
    public ResponseEntity<?> info() {
        log.info("info method run");
        log.info("info method end");
        return ResponseEntity.ok("info message");
    }
}
