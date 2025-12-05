package com.luv2code.spring_boot_library.controller;

import com.luv2code.spring_boot_library.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "http://localhost:3000")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle/{postId}")
    public ResponseEntity<?> toggleLike(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long postId) {
        try {
            likeService.toggleLike(userId, postId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Like toggled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}





