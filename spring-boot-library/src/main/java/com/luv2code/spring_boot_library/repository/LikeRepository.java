package com.luv2code.spring_boot_library.repository;

import com.luv2code.spring_boot_library.model.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class LikeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Like> likeRowMapper = new RowMapper<Like>() {
        @Override
        public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
            Like like = new Like();
            like.setId(rs.getLong("id"));
            like.setUserId(rs.getLong("user_id"));
            like.setPostId(rs.getLong("post_id"));
            like.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return like;
        }
    };

    public Like save(Like like) {
        String sql = "INSERT INTO likes (user_id, post_id, created_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, like.getUserId(), like.getPostId(), LocalDateTime.now());
        
        // Get the generated ID
        String selectSql = "SELECT * FROM likes WHERE user_id = ? AND post_id = ?";
        return jdbcTemplate.queryForObject(selectSql, likeRowMapper, like.getUserId(), like.getPostId());
    }

    public void delete(Long userId, Long postId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
        jdbcTemplate.update(sql, userId, postId);
    }

    public Optional<Like> findByUserIdAndPostId(Long userId, Long postId) {
        String sql = "SELECT * FROM likes WHERE user_id = ? AND post_id = ?";
        try {
            Like like = jdbcTemplate.queryForObject(sql, likeRowMapper, userId, postId);
            return Optional.ofNullable(like);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}





