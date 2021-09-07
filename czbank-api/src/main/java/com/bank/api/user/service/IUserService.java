package com.bank.api.user.service;

import com.bank.api.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huzhen
 * @since 2021-08-10
 */
public interface IUserService extends IService<User> {

    void addCache();
}
