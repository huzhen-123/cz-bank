package com.bank.api.verifycode.service.impl;

import com.bank.api.verifycode.dto.CheckResult;
import com.bank.api.verifycode.service.VerifyCodeService;
import com.bank.common.String.StrExChange;
import com.bank.common.redis.RedisConstants;
import com.bank.common.redis.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Resource
    RedisUtil redisUtil;

    @Override
    public CheckResult check(String ip, String verifyCode) {
        if(StringUtils.isEmpty(verifyCode)){
            return new CheckResult(false,"请输入验证码!");
        }
        String code=redisUtil.getStr(ip);
        //在缓存中未找到，让用户重新发送
        if(StringUtils.isEmpty(code)){
            return new CheckResult(false,"请重新发送验证码!");
        }

        String errorCountKey = ip+":"+ StrExChange.exChange(code);
        String count=redisUtil.getStr(errorCountKey);
        if(count == null){
            return new CheckResult(false,"请重新发送验证码!");
        }
        Integer errorCount = Integer.valueOf(count) ;
        if(!verifyCode.equalsIgnoreCase(code)){
            //获取该验证码错误次数，超过3次提醒重新发送
            if (errorCount >= 3){
                redisUtil.del(ip,errorCountKey);
                return new CheckResult(false,"请重新发送验证码!");
            }
            redisUtil.set(errorCountKey,errorCount+1,60);
            return new CheckResult(false,"验证码错误!");
        }
        return new CheckResult(true,"ok");
    }
}
