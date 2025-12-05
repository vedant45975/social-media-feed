package com.luv2code.spring_boot_library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
}





