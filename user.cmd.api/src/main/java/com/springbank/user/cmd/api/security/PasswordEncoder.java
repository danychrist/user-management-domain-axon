package com.springbank.user.cmd.api.security;

public interface PasswordEncoder {
    //below is single method returning a string
    String hashPassword(String password);
}
