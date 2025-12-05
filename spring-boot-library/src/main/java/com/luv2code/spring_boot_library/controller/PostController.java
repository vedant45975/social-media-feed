package com.luv2code.spring_boot_library.controller;

import com.luv2code.spring_boot_library.dto.CreatePostRequest;
import com.luv2code.spring_boot_library.model.Post;
import com.luv2code.spring_boot_library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CreatePostRequest request) {
        try {
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Post content cannot be empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Post post = postService.createPost(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage() != null ? e.getMessage() : "Failed to create post");
            e.printStackTrace(); // Log the full stack trace for debugging
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<Post> posts = postService.getFeed(userId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

