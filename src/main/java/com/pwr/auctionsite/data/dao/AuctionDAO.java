package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.benchmark.TrackExecutionTime;
import com.pwr.auctionsite.data.dto.views.ActiveAuctionDTO;
import com.pwr.auctionsite.data.dto.views.FinishedAuctionDTO;
import com.pwr.auctionsite.data.mapper.ActiveAuctionRowMapper;
import com.pwr.auctionsite.data.mapper.FinishedAuctionRowMapper;
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
public class AuctionDAO {
    private final JdbcTemplate template;
    private final NamedParameterJdbcTemplate namedTemplate;
    private final ActiveAuctionRowMapper activeAuctionRowMapper;
    private final FinishedAuctionRowMapper finishedAuctionRowMapper;

    @TrackExecutionTime
    public List<ActiveAuctionDTO> findAllPaged(String filter, String category, int offset, int limit) {
        var params = new MapSqlParameterSource();
        params.addValue("filter", filter);
        params.addValue("category", category);
        params.addValue("offset", offset);
        params.addValue("limit", limit);

        var sql = """
                SELECT *
                FROM active_auctions_v
                WHERE (:category IS NULL OR category = :category)
                      AND (:filter IS NULL
                            OR LOWER(name) LIKE LOWER(CONCAT('%', :filter, '%'))
                            OR LOWER(description) LIKE LOWER(CONCAT('%', :filter, '%')))
                ORDER BY modified_at DESC
                LIMIT :limit
                OFFSET :offset
                """;

        return namedTemplate.query(sql, params, activeAuctionRowMapper);
    }

    @TrackExecutionTime
    public List<ActiveAuctionDTO> findMyAuctions(int offset, int limit, long userId, String relationType) {
        // IN clause ensures that there are no duplicates -> should be fixed ???
        var sql = """
                SELECT
                	*
                FROM
                	active_auctions_v aav
                WHERE
                    aav.auction_id IN (SELECT auction_id FROM watchlist WHERE relation_type = ? AND user_id = ?)
                ORDER BY modified_at DESC
                LIMIT ?
                OFFSET ?
                """;

        return template.query(sql, activeAuctionRowMapper, relationType, userId, limit, offset);
    }


    // todo: if finished auction winner is null -> bid is also null
    @TrackExecutionTime
    public List<FinishedAuctionDTO> findArchivedAuctions(Long userId, int offset, int limit) {
        var params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("offset", offset);
        params.addValue("limit", limit);

        var sql = """
                SELECT *
                FROM finished_auctions_v
                WHERE (:user_id IS NULL OR winner_id = :user_id OR seller_id = :user_id)
                ORDER BY finished_at DESC
                LIMIT :limit
                OFFSET :offset
                """;

        return namedTemplate.query(sql, params, finishedAuctionRowMapper);
    }
}
