package com.binarfood.binarfoodapp.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tb_merchant")
@Data
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_code", length = 5, nullable = false, unique = true)
    private String merchantCode;

    @Column(name = "merchant_name", length = 30, nullable = false, unique = true)
    private String merchantName;

    @Column(name = "merchant_location", length = 100, nullable = false, unique = true)
    private String merchantLocation;

    private Boolean open;

    private Boolean deleted;
}
