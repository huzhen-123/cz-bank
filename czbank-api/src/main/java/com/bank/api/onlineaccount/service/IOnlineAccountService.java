package com.bank.api.onlineaccount.service;

import com.bank.api.onlineaccount.dto.LoginDto;
import com.bank.api.onlineaccount.dto.OnlineAccountDto;
import com.bank.api.onlineaccount.entity.OnlineAccount;
import com.bank.common.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网银账户 服务类
 * </p>
 *
 * @author huzhen
 * @since 2021-08-12
 */
public interface IOnlineAccountService extends IService<OnlineAccount> {

    /**
     * 创建账户(已有稠州银行账户)
     * @param onlineAccountDto
     * @return
     */
    Result createAccountCZ(OnlineAccountDto onlineAccountDto);


    /**
     * 创建账户(已有稠州银行账户)
     * @param onlineAccountDto
     * @return
     */
    Result createAccount(OnlineAccountDto onlineAccountDto);

    /**
     * 登录
     * @param loginDto
     * @return
     */
    Result login(LoginDto loginDto);

    /**
     * 更换密码
     * @param onlineAccountDto
     * @return
     */
    Result updatePassword(OnlineAccountDto onlineAccountDto);
}
