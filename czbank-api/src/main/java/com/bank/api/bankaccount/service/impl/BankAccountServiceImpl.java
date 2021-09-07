package com.bank.api.bankaccount.service.impl;

import com.bank.api.bankaccount.dto.BankAccountDto;
import com.bank.api.bankaccount.entity.BankAccount;
import com.bank.api.bankaccount.mapper.BankAccountMapper;
import com.bank.api.bankaccount.service.IBankAccountService;
import com.bank.common.encryption.MD5Util;
import com.bank.common.redis.RedisUtil;
import com.bank.common.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 银行账户表 服务实现类
 * </p>
 *
 * @author huzhen
 * @since 2021-08-11
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

    @Resource
    BankAccountMapper bankAccountMapper;
    @Resource
    RedisUtil redisUtil;


    @Override
    public Result bankAccountCheck(BankAccountDto bankAccountDto) {
        QueryWrapper<BankAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_num",bankAccountDto.getAccountNum());
        wrapper.eq("del_flag",0);
        BankAccount bankAccount = bankAccountMapper.selectOne(wrapper);
        if(bankAccount == null){
            return Result.error("未查询到此账户，请检擦账户是否正确");
        }
        //密码解密
        String password = MD5Util.decrypt(bankAccount.getPassword());
        //验证交易密码
        if(!password.equals(MD5Util.decrypt(bankAccountDto.getPassword()))){
            return Result.error("密码错误");
        }
        BankAccountDto accountDto = new BankAccountDto();
        accountDto.setId(bankAccount.getId());
        accountDto.setUserName(bankAccount.getUserName());
        accountDto.setAccountNum(bankAccount.getAccountNum());
        accountDto.setMobile(bankAccount.getMobile());
        accountDto.setUserId(bankAccount.getUserId());
        accountDto.setIdCard(bankAccount.getIdCard());
        //用于注册接口，判断是否校验过交易密码
        redisUtil.set(bankAccount.getAccountNum(),bankAccount,60);
        return Result.ok(accountDto);
    }
}
