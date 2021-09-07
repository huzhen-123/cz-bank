package com.bank.api.verifycode.service;

import com.bank.api.verifycode.dto.CheckResult;

public interface VerifyCodeService {
    /**
     * 校验验证码
     **/
    CheckResult check(String ip, String verifyCode);
}
