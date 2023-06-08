package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Query("update User set blocked = :block WHERE id = :user_id")
    void setBlockedUser(@Param("user_id") Long userId, @Param("block") boolean block);
}
