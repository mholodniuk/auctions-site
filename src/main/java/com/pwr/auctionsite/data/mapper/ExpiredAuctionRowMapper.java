package com.pwr.auctionsite.data.mapper;

import com.pwr.auctionsite.data.dto.ExpiredAuction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class ExpiredAuctionRowMapper implements RowMapper<ExpiredAuction> {
    @Override
    public ExpiredAuction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ExpiredAuction(rs.getLong("id"), rs.getBigDecimal("current_bid"));
    }
}
