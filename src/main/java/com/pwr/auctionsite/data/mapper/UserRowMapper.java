package com.pwr.auctionsite.data.mapper;

import com.pwr.auctionsite.data.dto.views.UserInfoDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<UserInfoDTO> {
    @Override
    public UserInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("user_id");
        String username = rs.getString("username");
        String fullname = rs.getString("full_name");
        String email = rs.getString("email");
        Boolean isBlocked = rs.getBoolean("is_blocked");
        String role = rs.getString("role");
        String country = rs.getString("country");
        String city = rs.getString("city");
        String street = rs.getString("street");
        String street_number = rs.getString("street_number");

        return new UserInfoDTO(userId, username, fullname, email, role, isBlocked, country, city, street, street_number);
    }
}
