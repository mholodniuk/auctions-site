package com.pwr.auctionsite.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "shipping_address")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(name = "street_number", nullable = false)
    private String streetNumber;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER)
    private User user;
}
