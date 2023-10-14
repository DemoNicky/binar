package com.binarfood.binarfoodapp.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseHandling<T> {
    private T data;

    private String message;

    private String errors;

}
