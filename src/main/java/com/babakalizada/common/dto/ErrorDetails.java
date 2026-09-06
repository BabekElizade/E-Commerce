package com.babakalizada.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorDetails<E> {
    private String path;
    private String hostName;
    private Date timestamp;
    private E message;
}
