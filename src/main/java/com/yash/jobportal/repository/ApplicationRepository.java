package com.yash.jobportal.repository;

import com.yash.jobportal.dto.ApplicationRequest;
import com.yash.jobportal.model.JobApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<JobApplication> mapper = (rs, rowNum) -> new JobApplication(
            rs.getLong("id"), rs.getLong("job_id"), rs.getString("job_title"),
            rs.getString("company"), rs.getString("candidate_name"),
            rs.getString("email"), rs.getString("phone"), rs.getString("resume_link"),
            rs.getString("cover_letter"), rs.getString("status"),
            rs.getTimestamp("applied_at").toLocalDateTime()
    );

    public ApplicationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<JobApplication> findAll(String email) {
        if (email == null || email.isBlank()) {
            return jdbc.query("""
                    SELECT a.*, j.title AS job_title, j.company
                    FROM applications a JOIN jobs j ON a.job_id=j.id
                    ORDER BY a.applied_at DESC
                    """, mapper);
        }
        return jdbc.query("""
                SELECT a.*, j.title AS job_title, j.company
                FROM applications a JOIN jobs j ON a.job_id=j.id
                WHERE LOWER(a.email)=LOWER(?) ORDER BY a.applied_at DESC
                """, mapper, email);
    }

    public Optional<JobApplication> findById(Long id) {
        return jdbc.query("""
                SELECT a.*, j.title AS job_title, j.company
                FROM applications a JOIN jobs j ON a.job_id=j.id WHERE a.id=?
                """, mapper, id).stream().findFirst();
    }

    public JobApplication create(ApplicationRequest request) {
        jdbc.update("""
                INSERT INTO applications(job_id, candidate_name, email, phone, resume_link, cover_letter, status, applied_at)
                VALUES (?, ?, ?, ?, ?, ?, 'SUBMITTED', CURRENT_TIMESTAMP)
                """, request.jobId(), request.candidateName(), request.email(),
                request.phone(), request.resumeLink(), request.coverLetter());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return findById(id).orElseThrow();
    }

    public boolean updateStatus(Long id, String status) {
        return jdbc.update("UPDATE applications SET status=? WHERE id=?", status, id) > 0;
    }

    public boolean delete(Long id) {
        return jdbc.update("DELETE FROM applications WHERE id=?", id) > 0;
    }
}
