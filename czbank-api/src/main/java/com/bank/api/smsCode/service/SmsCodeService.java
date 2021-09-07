package com.bank.api.smsCode.service;

import com.bank.common.vo.Result;

public interface SmsCodeService {

    Result checkSmsCode(String mobile,String smsCode);
}
