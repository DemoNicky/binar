package com.binarfood.binarapp.Entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_order_detail")
@Data
public class OrderDetail {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @ManyToMany
    @JoinTable(name = "ordered_and_product", joinColumns = {
            @JoinColumn(name = "id_order_detail", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "id_product", referencedColumnName = "id")
            })
    private List<Product> product;

}
