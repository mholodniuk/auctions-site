package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.benchmark.TrackExecutionTime;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.mapper.ActiveAuctionRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class ActiveAuctionDAO {
    private final JdbcTemplate template;
    private final ActiveAuctionRowMapper rowMapper;

    @TrackExecutionTime
    public List<ActiveAuctionDTO> findAll() {
        var sql = """
                SELECT *
                FROM active_auctions_v
                """;
        return template.query(sql, rowMapper);
    }
}
