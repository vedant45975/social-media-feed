package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.dto.CreatePostRequest;
import com.luv2code.spring_boot_library.model.Post;
import com.luv2code.spring_boot_library.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public Post createPost(Long userId, CreatePostRequest request) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    public List<Post> getFeed(Long currentUserId) {
        return postRepository.findAllOrderByCreatedAtDesc(currentUserId);
    }

    public Optional<Post> getPostById(Long postId, Long currentUserId) {
        return postRepository.findById(postId, currentUserId);
    }
}

