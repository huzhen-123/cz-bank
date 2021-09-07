package com.bank.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bank.api.smsCode.service.SmsCodeService;
import com.bank.common.redis.RedisUtil;
import com.bank.common.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author huzhen
 */
@RestController
@RequestMapping("/smsCode")
public class SmsCodeController {


    @Resource
    RedisUtil redisUtil;
    @Resource
    SmsCodeService smsCodeService;

    @RequestMapping("sendCode")
    public Result getCode(@RequestParam(value = "mobile") String mobile) {
        Object oldCode = redisUtil.get(mobile + ":SMS_KEY");
        if (oldCode != null) {
            return Result.error("一分钟内不可重复发送");
        }
        //生成验证码
        int code = (int) ((Math.random() * 9 + 1) * 1000);
        redisUtil.set(mobile + ":SMS_KEY", code, 60);
        //验证错误 次数
        redisUtil.set(mobile + ":" + code, 0, 60);
        return Result.ok(code);
    }

    @RequestMapping("checkSms")
    public Result checkSmsCode(@RequestParam("mobile") String mobile, @RequestParam("smsCode") String smsCode) {
        if (StringUtils.isEmpty(mobile)) {
            return Result.error("请输入手机号");
        }
        if (StringUtils.isEmpty(smsCode)) {
            return Result.error("请输入验证码");
        }
        return smsCodeService.checkSmsCode(mobile, smsCode);
    }

    public static void main(String[] args) {
        System.out.println((int) ((Math.random() * 9 + 1) * 1000));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1",2);
        Result.ok(jsonObject);
    }
}
