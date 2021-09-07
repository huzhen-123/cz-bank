package com.bank.api.user.service.impl;

import com.bank.api.user.entity.User;
import com.bank.api.user.mapper.UserMapper;
import com.bank.api.user.service.IUserService;
import com.bank.common.redis.RedisUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huzhen
 * @since 2021-08-10
 */
@Service()
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    RedisUtil redisUtil;

    @Override
    public void addCache() {
        redisUtil.set("1","abcd");
    }
}
