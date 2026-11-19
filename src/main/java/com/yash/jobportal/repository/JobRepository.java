package com.yash.jobportal.repository;

import com.yash.jobportal.dto.JobRequest;
import com.yash.jobportal.model.Job;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class JobRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Job> mapper = (rs, rowNum) -> new Job(
            rs.getLong("id"), rs.getString("title"), rs.getString("company"),
            rs.getString("location"), rs.getString("job_type"),
            rs.getBigDecimal("salary"), rs.getString("description"),
            rs.getDate("posted_date").toLocalDate()
    );

    public JobRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Job> findAll(String keyword, String location, String jobType) {
        String sql = """
                SELECT * FROM jobs
                WHERE (? = '' OR LOWER(title) LIKE LOWER(CONCAT('%', ?, '%'))
                    OR LOWER(company) LIKE LOWER(CONCAT('%', ?, '%'))
                    OR LOWER(description) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? = '' OR LOWER(location) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? = '' OR LOWER(job_type) = LOWER(?))
                ORDER BY posted_date DESC, id DESC
                """;
        return jdbc.query(sql, mapper,
                keyword, keyword, keyword, keyword,
                location, location, jobType, jobType);
    }

    public Optional<Job> findById(Long id) {
        return jdbc.query("SELECT * FROM jobs WHERE id = ?", mapper, id)
                .stream().findFirst();
    }

    public Job create(JobRequest request) {
        jdbc.update("""
                INSERT INTO jobs(title, company, location, job_type, salary, description, posted_date)
                VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE)
                """, request.title(), request.company(), request.location(),
                request.jobType(), request.salary(), request.description());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return findById(id).orElseThrow();
    }

    public boolean update(Long id, JobRequest request) {
        return jdbc.update("""
                UPDATE jobs SET title=?, company=?, location=?, job_type=?, salary=?, description=?
                WHERE id=?
                """, request.title(), request.company(), request.location(),
                request.jobType(), request.salary(), request.description(), id) > 0;
    }

    public boolean delete(Long id) {
        return jdbc.update("DELETE FROM jobs WHERE id=?", id) > 0;
    }
}
