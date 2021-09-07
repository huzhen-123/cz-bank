package com.bank.web.controller;

import com.bank.api.bankaccount.dto.BankAccountDto;
import com.bank.api.bankaccount.entity.BankAccount;
import com.bank.api.bankaccount.service.IBankAccountService;
import com.bank.api.user.entity.User;
import com.bank.api.user.service.IUserService;
import com.bank.api.verifycode.dto.CheckResult;
import com.bank.api.verifycode.service.VerifyCodeService;
import com.bank.common.IP.IPUtils;
import com.bank.common.UUID.UUIDGenerator;
import com.bank.common.encryption.AES_CBC;
import com.bank.common.encryption.MD5Util;
import com.bank.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 银行账户
 */
@RestController
@RequestMapping("/bank_account")
@Api(value = "银行账户")
public class BankAccountController {

    @Resource
    private VerifyCodeService verifyCodeService;

    @Resource
    private IBankAccountService bankAccountService;
    @Resource
    private IUserService iUserService;


    @RequestMapping("test_create")
    @ApiOperation(value = "创建银行账户",notes = "创建银行账户")
    public Result create(){
        LocalDateTime now = LocalDateTime.now();
        List<BankAccount> bankAccountList = bankAccountService.list();
        for (BankAccount bankAccount : bankAccountList) {
            bankAccount.setPassword(MD5Util.encrypt(AES_CBC.decrypt(bankAccount.getPassword())));
            bankAccountService.updateById(bankAccount);
        }
        return Result.ok();
    }

    /**
     * 校验账户
     *
     * @param code
     * @param request
     * @param response
     */
    @RequestMapping("check")
    @ApiOperation(value = "银行账户密码校验", notes = "银行账户密码校验")
    public Result check(@RequestParam(value = "code") String code, @RequestBody BankAccountDto bankAccountDto,
                        HttpServletRequest request, HttpServletResponse response) {
        String ip = IPUtils.getIpAddr(request);
        CheckResult checkResult = verifyCodeService.check(ip, code);
        if (!checkResult.getState()) {
            //验证码校验失败
            return Result.error(checkResult.getMsg());
        }
        if (bankAccountDto == null) {
            return Result.error("请输入账号");
        }
        if (StringUtils.isEmpty(bankAccountDto.getAccountNum())) {
            return Result.error("请输入账号");
        }
        if (StringUtils.isEmpty(bankAccountDto.getPassword())) {
            return Result.error("请输入交易密码");
        }
        return bankAccountService.bankAccountCheck(bankAccountDto);
    }

}
