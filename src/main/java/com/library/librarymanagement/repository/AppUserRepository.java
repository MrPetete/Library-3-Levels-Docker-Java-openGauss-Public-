package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AppUser> userRowMapper = (rs, rowNum) -> {
        AppUser u = new AppUser();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setStudentNo(rs.getString("student_no"));
        return u;
    };

    public AppUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AppUser> findAll() {
        String sql = "SELECT id, name, email, student_no FROM app_user";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public Optional<AppUser> findById(Long id) {
        String sql = "SELECT id, name, email, student_no FROM app_user WHERE id = ?";
        List<AppUser> list = jdbcTemplate.query(sql, userRowMapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    public int save(AppUser user) {
        String sql = "INSERT INTO app_user (name, email, student_no) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getStudentNo());
    }

    public int update(AppUser user) {
        String sql = "UPDATE app_user SET name = ?, email = ?, student_no = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getStudentNo(),
                user.getId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM app_user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}