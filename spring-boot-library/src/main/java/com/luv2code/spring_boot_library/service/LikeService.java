package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.model.Like;
import com.luv2code.spring_boot_library.repository.LikeRepository;
import com.luv2code.spring_boot_library.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public void toggleLike(Long userId, Long postId) {
        // Check if post exists
        postRepository.findById(postId, userId).orElseThrow(() -> 
            new RuntimeException("Post not found"));

        // Check if like already exists
        if (likeRepository.findByUserIdAndPostId(userId, postId).isPresent()) {
            // Unlike
            likeRepository.delete(userId, postId);
        } else {
            // Like
            Like like = new Like();
            like.setUserId(userId);
            like.setPostId(postId);
            likeRepository.save(like);
        }
    }
}





