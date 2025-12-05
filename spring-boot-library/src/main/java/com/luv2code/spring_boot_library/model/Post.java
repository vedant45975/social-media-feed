package com.luv2code.spring_boot_library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private Long likeCount;
    private Boolean isLiked;
}





