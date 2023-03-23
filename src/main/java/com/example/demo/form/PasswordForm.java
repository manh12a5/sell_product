package com.example.demo.form;

import lombok.Data;

@Data
public class PasswordForm {

    private String username;
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
}
