package com.bank.web.controller;


import com.bank.api.onlineaccount.dto.LoginDto;
import com.bank.api.onlineaccount.dto.OnlineAccountDto;
import com.bank.api.onlineaccount.entity.OnlineAccount;
import com.bank.api.onlineaccount.service.IOnlineAccountService;
import com.bank.api.smsCode.service.SmsCodeService;
import com.bank.api.verifycode.dto.CheckResult;
import com.bank.api.verifycode.service.VerifyCodeService;
import com.bank.common.IP.IPUtils;
import com.bank.common.redis.RedisUtil;
import com.bank.common.vo.Result;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * <p>
 * 网银账户 前端控制器
 * </p>
 *
 * @author huzhen
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/online_account")
public class OnlineAccountController {

    @Resource
    IOnlineAccountService onlineAccountService;
    @Resource
    VerifyCodeService verifyCodeService;
    @Resource
    SmsCodeService smsCodeService;
    @Resource
    RedisUtil redisUtil;

    /**
     * 创建网银账户(稠州银行)
     *
     * @param onlineAccountDto
     * @return
     */
    @RequestMapping("create_CZ")
    public Result createAccount(@RequestParam("checkState") String checkState, OnlineAccountDto onlineAccountDto) {
        //是否通过短信验证码校验
        if (!redisUtil.hasKey(checkState)) {
            return Result.error("请重新发送验证码");
        }
        if (onlineAccountDto == null) {
            return Result.error("请重新提交");
        }
        if (StringUtils.isEmpty(onlineAccountDto.getPassword()) ||
                !onlineAccountDto.getPassword().equals(onlineAccountDto.getConfirmPassword())) {
            return Result.error("密码不一致");
        }

        return onlineAccountService.createAccountCZ(onlineAccountDto);
    }

    /**
     * 创建网银账户(非稠州用户)
     *
     * @param onlineAccountDto
     * @return
     */
    @RequestMapping("create")
    public Result create(@RequestParam("checkState") String checkState, @RequestBody OnlineAccountDto onlineAccountDto) {
        //是否通过短信验证码校验
        if (!redisUtil.hasKey(checkState)) {
            return Result.error("请重新发送验证码");
        }
        if (onlineAccountDto == null) {
            return Result.error("请重新提交");
        }
        if (StringUtils.isEmpty(onlineAccountDto.getPassword()) ||
                onlineAccountDto.getPassword().equals(onlineAccountDto.getConfirmPassword())) {
            return Result.error("密码不一致");
        }

        return onlineAccountService.createAccount(onlineAccountDto);
    }

    /**
     * 登录
     *
     * @param loginDto
     * @param request
     * @return
     */
    @RequestMapping("login")
    public Result login(LoginDto loginDto, HttpServletRequest request) {
        //校验验证码
        if (loginDto == null) {
            return Result.error("请输入账号密码！");
        }
//        CheckResult result = verifyCodeService.check(IPUtils.getIpAddr(request), loginDto.getVerifyCode());
//        if (!result.getState()) {
//            return Result.error(result.getMsg());
//        }
        return onlineAccountService.login(loginDto);
    }

    /**
     * 通过手机更换密码
     *
     * @param checkState
     * @param onlineAccountDto
     * @return
     */
    @RequestMapping("updatePasswordByMobile")
    public Result updatePasswordByMobile(@RequestParam("checkState") String checkState, OnlineAccountDto onlineAccountDto) {
        //是否通过短信验证码校验
        if (!redisUtil.hasKey(checkState)) {
            return Result.error("请重新发送验证码");
        }
        if (onlineAccountDto == null) {
            return Result.error("请重新提交");
        }
        if (StringUtils.isEmpty(onlineAccountDto.getMobile())) {
            return Result.error("请输入手机号");
        }
        if (StringUtils.isEmpty(onlineAccountDto.getPassword()) ||
                onlineAccountDto.getPassword().equals(onlineAccountDto.getConfirmPassword())) {
            return Result.error("密码不一致");
        }
        return onlineAccountService.updatePassword(onlineAccountDto);
    }

}
