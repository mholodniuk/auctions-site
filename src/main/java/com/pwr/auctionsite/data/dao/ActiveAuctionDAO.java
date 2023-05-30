package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.benchmark.TrackExecutionTime;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.mapper.ActiveAuctionRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class ActiveAuctionDAO {
    private final JdbcTemplate template;
    private final ActiveAuctionRowMapper rowMapper;

    @TrackExecutionTime
    public List<ActiveAuctionDTO> findAllPaged(int offset, int limit) {
        var sql = """
                SELECT *
                FROM active_auctions_v
                ORDER BY modified_at DESC
                LIMIT ?
                OFFSET ?
                """;
        return template.query(sql, rowMapper, limit, offset);
    }

    @TrackExecutionTime
    public void placeBid(Long auctionId, Long userId, BigDecimal bidValue) {
        var sql = """
                CALL place_bid(?, ?, ?)
                """;
        template.update(sql, auctionId, userId, bidValue);
    }

    @TrackExecutionTime
    public void buyNow(Long auctionId, Long userId) {
        var sql = """
                CALL buy_now(?, ?)
                """;
        template.update(sql, auctionId, userId);
    }
}
