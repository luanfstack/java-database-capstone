package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final Service service;

    @Autowired
    public AdminController(Service service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> adminLogin(
        @RequestBody Admin admin
    ) {
        String token = service.validateAdmin(
            admin.getUsername(),
            admin.getPassword()
        );
        Map<String, Object> response = new HashMap<>();

        if (
            token != null &&
            !token.startsWith("Invalid") &&
            !token.startsWith("Error")
        ) {
            response.put("status", "success");
            response.put("token", token);
        } else {
            response.put("status", "failure");
            response.put("message", token);
        }

        return ResponseEntity.ok(response);
    }
}
