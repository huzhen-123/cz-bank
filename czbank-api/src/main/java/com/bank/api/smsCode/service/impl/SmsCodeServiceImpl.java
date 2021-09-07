package com.bank.api.smsCode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bank.api.smsCode.service.SmsCodeService;
import com.bank.common.UUID.UUIDGenerator;
import com.bank.common.redis.RedisUtil;
import com.bank.common.vo.Result;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SmsCodeServiceImpl implements SmsCodeService {
    @Resource
    RedisUtil redisUtil;

    @Override
    public Result checkSmsCode(String mobile,String smsCode) {
        Object code = redisUtil.get(mobile+":SMS_KEY");
        if(code == null){
            return Result.error("请重新发送验证码");
        }
        String strCode = code.toString();
        if(!strCode.equals(smsCode)){
            String countStr = redisUtil.getStr(mobile+":"+strCode);
            int errorCount = StringUtils.isEmpty(countStr)?3:Integer.parseInt(countStr);
            //错误次数超过3 提示重新发送
            if(errorCount>=3){
                redisUtil.del(mobile+":SMS_KEY",mobile+":"+strCode);
                return Result.error("请重新发送");
            };
            //错误次数加1
            errorCount++;
            redisUtil.set(mobile+":"+strCode,errorCount,60);
            return Result.error("验证码错误");
        }
        redisUtil.del(mobile+":SMS_KEY",mobile+":"+strCode);
        //保存校验成功的状态
        String checkState = UUIDGenerator.generate();
        redisUtil.set(checkState,code,60);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("checkState",checkState);
        return Result.ok(jsonObject);
    }

    public static void main(String[] args) {
        System.out.println(Integer.valueOf(null));
    }
}
