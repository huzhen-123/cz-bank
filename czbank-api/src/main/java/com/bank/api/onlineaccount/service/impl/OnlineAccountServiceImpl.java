package com.bank.api.onlineaccount.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bank.api.bankaccount.entity.BankAccount;
import com.bank.api.bankaccount.service.IBankAccountService;
import com.bank.api.onlineaccount.dto.LoginDto;
import com.bank.api.onlineaccount.dto.OnlineAccountDto;
import com.bank.api.onlineaccount.entity.OnlineAccount;
import com.bank.api.onlineaccount.mapper.OnlineAccountMapper;
import com.bank.api.onlineaccount.service.IOnlineAccountService;
import com.bank.api.verifycode.service.VerifyCodeService;
import com.bank.common.JWT.JwtUtil;
import com.bank.common.UUID.UUIDGenerator;
import com.bank.common.encryption.AES_CBC;
import com.bank.common.encryption.MD5Util;
import com.bank.common.enums.LoginTypeEnum;
import com.bank.common.redis.RedisUtil;
import com.bank.common.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 网银账户 服务实现类
 * </p>
 *
 * @author huzhen
 * @since 2021-08-12
 */
@Service
@Transactional
public class OnlineAccountServiceImpl extends ServiceImpl<OnlineAccountMapper, OnlineAccount> implements IOnlineAccountService {

    @Resource
    RedisUtil redisUtil;
    @Resource
    OnlineAccountMapper onlineAccountMapper;

    @Resource
    VerifyCodeService verifyCodeService;
    @Resource
    IBankAccountService bankAccountService;

    @Override
    public Result createAccountCZ(OnlineAccountDto onlineAccountDto) {
        //判断交易密码是否通过校验
        Object bankAccountObj = redisUtil.get(onlineAccountDto.getBankAccount());
        if (bankAccountObj == null) {
            return Result.error("请重新校验交易密码");
        }
        OnlineAccount onlineAccount = getOne(new QueryWrapper<OnlineAccount>()
                .eq("mobile", onlineAccountDto.getMobile())
                .eq("del_flag", false));
        //该手机号是否被注册
        if (onlineAccount != null) {
            Result.error("该手机号已被注册");
        }

        OnlineAccount account = new OnlineAccount();
        account.setIdCard(onlineAccountDto.getIdCard());
        account.setMobile(onlineAccountDto.getMobile());
        account.setPassword(MD5Util.encrypt(onlineAccountDto.getPassword()));
        account.setNickname(onlineAccountDto.getNickname());
        account.setUserId(onlineAccountDto.getUserId());
        account.setCreateTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());
        account.setDelFlag(false);
        this.save(account);
        //更新银行账户所绑定的网银账户
        BankAccount bankAccount = (BankAccount) bankAccountObj;
        bankAccount.setUpdateTime(LocalDateTime.now());
        bankAccount.setOnlineAccount(account.getId());
        bankAccountService.updateById(bankAccount);
        return Result.ok("注册成功");
    }

    @Override
    public Result createAccount(OnlineAccountDto onlineAccountDto) {
        OnlineAccount onlineAccount = getOne(new QueryWrapper<OnlineAccount>()
                .eq("mobile", onlineAccountDto.getMobile())
                .eq("del_flag", false));
        //该手机号是否被注册
        if (onlineAccount != null) {
            Result.error("该手机号已注册，请前往登录");
        }
        //该手机号已绑定稠州银行账户
        BankAccount bankAccount = bankAccountService.getOne(new QueryWrapper<BankAccount>()
                .eq("mobile", onlineAccountDto.getMobile())
                .eq("del_flag", false));
        onlineAccount = new OnlineAccount();
        //关联数据
        if (bankAccount != null) {
            onlineAccount.setIdCard(bankAccount.getIdCard());
            onlineAccount.setUserId(bankAccount.getUserId());
        }
        onlineAccount.setMobile(onlineAccountDto.getMobile());
        onlineAccount.setPassword(onlineAccountDto.getPassword());
        onlineAccount.setCreateTime(LocalDateTime.now());
        onlineAccount.setUpdateTime(LocalDateTime.now());
        onlineAccount.setDelFlag(false);
        this.save(onlineAccount);
        //与网银账户绑定
        if (bankAccount != null) {
            bankAccount.setUpdateTime(LocalDateTime.now());
            bankAccount.setOnlineAccount(onlineAccount.getId());
            bankAccountService.updateById(bankAccount);
        }
        return Result.ok("注册成功");

    }

    @Override
    public Result login(LoginDto loginDto) {
        OnlineAccountDto param = new OnlineAccountDto();
        //根据不同参数查询账户
        if (loginDto.getLoginType() == LoginTypeEnum.MOBILE.getCode()) {
            param.setMobile(loginDto.getMobile());
        } else if (loginDto.getLoginType() == LoginTypeEnum.NICK_NAME.getCode()) {
            param.setNickname(loginDto.getNickName());
        } else if (loginDto.getLoginType() == LoginTypeEnum.BANK_ACCOUNT.getCode()) {
            param.setBankAccount(loginDto.getAccount());
        }
        OnlineAccountDto onlineAccountDto = onlineAccountMapper.getAccount(param);
        if (onlineAccountDto == null) {
            return Result.error("该用户不存在");
        }
        String dePassword = MD5Util.decrypt(loginDto.getPassword());
        if (!dePassword.equals(MD5Util.decrypt(onlineAccountDto.getPassword()))) {
            return Result.error("密码错误");
        }
        //生成token
        String token = JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("password", onlineAccountDto.getPassword())
                .sign(Algorithm.HMAC256("BANK"));
        redisUtil.set(token, onlineAccountDto, 2 * 3600);
        JSONObject object = new JSONObject();
        object.put("token", token);
        return Result.ok(object);
    }

    @Override
    public Result updatePassword(OnlineAccountDto onlineAccountDto) {
        QueryWrapper<OnlineAccount> wrapper = new QueryWrapper<>();
        OnlineAccount onlineAccount = this.getOne(wrapper.eq("mobile", onlineAccountDto.getMobile())
                .eq("del_flag", false));
        if (onlineAccount == null) {
            return Result.error("该手机号未绑定");
        }
        onlineAccount.setPassword(onlineAccountDto.getPassword());
        onlineAccount.setUpdateTime(LocalDateTime.now());
        this.updateById(onlineAccount);
        return Result.error("更换密码成功");
    }

    public static void main(String[] args) {
        System.out.printf(JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("password", "onlineAccountDto.getPassword()")
                .sign(Algorithm.HMAC256("BANK")));
    }
}
