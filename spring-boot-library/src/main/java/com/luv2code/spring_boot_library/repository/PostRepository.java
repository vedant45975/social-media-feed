package com.luv2code.spring_boot_library.repository;

import com.luv2code.spring_boot_library.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PostRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Post> postRowMapper = new RowMapper<Post>() {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setUserId(rs.getLong("user_id"));
            post.setUsername(rs.getString("username"));
            post.setContent(rs.getString("content"));
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            post.setLikeCount(rs.getLong("like_count"));
            post.setIsLiked(rs.getBoolean("is_liked"));
            return post;
        }
    };

    public Post save(Post post) {
        String sql = "INSERT INTO posts (user_id, content, created_at) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();

        if (keys == null || !keys.containsKey("ID")) {
            throw new RuntimeException("Failed to get generated ID for post");
        }
        
        Long generatedId = ((Number) keys.get("ID")).longValue();
        
       
        
        
        // Get the full post with username and like information
        String selectSql = "SELECT p.*, u.username, " +
                "COALESCE((SELECT COUNT(*) FROM likes WHERE post_id = p.id), 0) as like_count, " +
                "CASE WHEN EXISTS(SELECT 1 FROM likes WHERE post_id = p.id AND user_id = ?) THEN true ELSE false END as is_liked " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.id = ?";
        try {
            return jdbcTemplate.queryForObject(selectSql, postRowMapper, post.getUserId(), generatedId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve created post: " + e.getMessage(), e);
        }
    }

    public List<Post> findAllOrderByCreatedAtDesc(Long currentUserId) {
        String sql = "SELECT p.*, u.username, " +
                "COALESCE((SELECT COUNT(*) FROM likes WHERE post_id = p.id), 0) as like_count, " +
                "CASE WHEN EXISTS(SELECT 1 FROM likes WHERE post_id = p.id AND user_id = ?) THEN true ELSE false END as is_liked " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "ORDER BY p.created_at DESC";
        return jdbcTemplate.query(sql, postRowMapper, currentUserId);
    }

    public Optional<Post> findById(Long postId, Long currentUserId) {
        String sql = "SELECT p.*, u.username, " +
                "COALESCE((SELECT COUNT(*) FROM likes WHERE post_id = p.id), 0) as like_count, " +
                "CASE WHEN EXISTS(SELECT 1 FROM likes WHERE post_id = p.id AND user_id = ?) THEN true ELSE false END as is_liked " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.id = ?";
        try {
            Post post = jdbcTemplate.queryForObject(sql, postRowMapper, currentUserId, postId);
            return Optional.ofNullable(post);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

