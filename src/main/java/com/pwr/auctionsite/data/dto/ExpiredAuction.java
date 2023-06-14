package com.pwr.auctionsite.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ExpiredAuction(Long auctionId, BigDecimal bid) implements Serializable {
}
