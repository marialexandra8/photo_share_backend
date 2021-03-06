package com.maria.repository;

import com.maria.exception.NotFoundException;
import com.maria.model.review.Review;
import com.maria.repository.rowmapper.ReviewRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 8/21/2017.
 */
@Repository
public class ReviewRepository extends BaseRepository {
    private final static ReviewRowMapper REVIEW_ROW_MAPPER = new ReviewRowMapper();

    public Review createReview(int userId, int contestEntryId) {
        String sql = "INSERT INTO reviews(user_id, contest_entry_id) VALUES(:user_id, :contest_entry_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("contest_entry_id", contestEntryId);

        namedJdbcTemplate.update(sql, parameterSource, keyHolder);
        int id = keyHolder.getKey().intValue();
        return findById(id);
    }

    public boolean userAlreadyVotedEntry(int userId, int entryId) {
        String sql = "SELECT count(*) FROM reviews WHERE user_id = ? AND contest_entry_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, entryId}, Integer.class) > 0;
    }

    public int findLikesCount(int entryId) {
        String sql = "SELECT count(*) FROM reviews WHERE contest_entry_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{entryId}, Integer.class);
    }

    public Review findById(int id) {
        String sql = "SELECT * FROM reviews r WHERE id=?";
        List<Review> reviews = jdbcTemplate.query(sql, new Object[]{id}, REVIEW_ROW_MAPPER);
        if (reviews.isEmpty()) {
            throw new NotFoundException("Cannot find review for id" + id);
        }
        return reviews.get(0);
    }
}
