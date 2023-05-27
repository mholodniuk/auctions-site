package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
