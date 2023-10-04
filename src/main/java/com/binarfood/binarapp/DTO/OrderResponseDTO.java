package com.binarfood.binarapp.DTO;

import com.binarfood.binarapp.Entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@Builder
public class OrderResponseDTO {

    private Date orderTime;

    private String destinationAddress;

    private Boolean completed;

}
