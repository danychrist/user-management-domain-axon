package com.springbank.user.cmd.api.dto;

import com.springbank.user.core.dto.BaseResponse;

public class RegisterUserResponse extends BaseResponse {

    private String id; //for the id of the user that was just registered

    public RegisterUserResponse(String id, String message) {
        super(message);
        this.id = id;
    }
}
