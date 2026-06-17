package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 * 用户通过用户名/邮箱 + 密码进行登录
 */
@Data
public class LoginRequest {

    /**
     * 登录账号：可以是用户名或邮箱
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
