package com.babakalizada.common.dto;

import lombok.Data;

@Data
public class ApiError<E> {
    private Integer status;
    private ErrorDetails<E> exception;
}