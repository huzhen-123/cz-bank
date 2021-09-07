package com.bank.api.onlineaccount.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录参数对象
 * @author 1045547334
 */
@Data
public class LoginDto implements Serializable {
    private String account;
    private String nickName;
    private String mobile;
    private int loginType; //登录账号类型，0-手机号 1-昵称 2-银行账号
    private String verifyCode;
    private String password;

}
