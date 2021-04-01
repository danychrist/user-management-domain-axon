package com.springbank.user.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor //Automatically generates constructor with parameter for each field in your class
public class BaseResponse {
    private String message;
}
