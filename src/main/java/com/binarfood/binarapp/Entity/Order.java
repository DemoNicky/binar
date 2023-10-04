package com.binarfood.binarapp.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_order")
@Data
public class Order {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "order_time")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date orderTime;

    @Column(name = "destination_address")
    private String destinationAddress;

    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

}
