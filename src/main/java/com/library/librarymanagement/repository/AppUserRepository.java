package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AppUser> userRowMapper = new RowMapper<AppUser>() {
        @Override
        @NonNull
        public AppUser mapRow(@NonNull java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            AppUser u = new AppUser();
            u.setId(rs.getLong("id"));
            u.setName(rs.getString("name"));
            u.setEmail(rs.getString("email"));
            u.setStudentNo(rs.getString("student_no"));
            u.setDeleted(rs.getBoolean("deleted"));
            return u;
        }
    };

    public AppUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("null")
    public List<AppUser> findAll() {
        String sql = "SELECT id, name, email, student_no, deleted FROM app_user WHERE deleted = false";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @SuppressWarnings("null")
    public List<AppUser> findPage(int limit, int offset) {
        String sql = "SELECT id, name, email, student_no, deleted FROM app_user " +
                "WHERE deleted = false ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, userRowMapper, limit, offset);
    }

    public long countActive() {
        String sql = "SELECT COUNT(*) FROM app_user WHERE deleted = false";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    @SuppressWarnings("null")
    public Optional<AppUser> findById(Long id) {
        String sql = "SELECT id, name, email, student_no, deleted FROM app_user WHERE id = ? AND deleted = false";
        List<AppUser> list = jdbcTemplate.query(sql, userRowMapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    public int save(AppUser user) {
        String sql = "INSERT INTO app_user (name, email, student_no, deleted) VALUES (?, ?, ?, false)";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getStudentNo());
    }

    public int update(AppUser user) {
        String sql = "UPDATE app_user SET name = ?, email = ?, student_no = ? WHERE id = ? AND deleted = false";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getStudentNo(),
                user.getId());
    }

    public int delete(Long id) {
        // soft delete: mark as deleted to preserve FK integrity
        String sql = "UPDATE app_user SET deleted = true WHERE id = ? AND deleted = false";
        return jdbcTemplate.update(sql, id);
    }
}