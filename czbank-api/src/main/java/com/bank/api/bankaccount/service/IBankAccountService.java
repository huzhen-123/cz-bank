package com.bank.api.bankaccount.service;

import com.bank.api.bankaccount.dto.BankAccountDto;
import com.bank.api.bankaccount.entity.BankAccount;
import com.bank.common.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 银行账户表 服务类
 * </p>
 *
 * @author huzhen
 * @since 2021-08-11
 */
public interface IBankAccountService extends IService<BankAccount> {

    Result bankAccountCheck(BankAccountDto bankAccountDto);

}
