package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.benchmark.TrackExecutionTime;
import com.pwr.auctionsite.data.dto.views.UserInfoDTO;
import com.pwr.auctionsite.data.mapper.UserRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class UserDAO {
    private final NamedParameterJdbcTemplate namedTemplate;
    private final UserRowMapper userRowMapper;

    @TrackExecutionTime
    public List<UserInfoDTO> findAllPaged(String username, int offset, int limit) {
        var params = new MapSqlParameterSource();
        params.addValue("username", username);
        params.addValue("offset", offset);
        params.addValue("limit", limit);

        var sql = """
                SELECT *
                FROM user_info_v
                WHERE (:username IS NULL OR LOWER(username) LIKE LOWER(CONCAT('%', :username, '%')))
                LIMIT :limit
                OFFSET :offset
                """;

        return namedTemplate.query(sql, params, userRowMapper);
    }
}
