package com.bank.api.onlineaccount.mapper;

import com.bank.api.onlineaccount.dto.OnlineAccountDto;
import com.bank.api.onlineaccount.entity.OnlineAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 网银账户 Mapper 接口
 * </p>
 *
 * @author huzhen
 * @since 2021-08-12
 */
public interface OnlineAccountMapper extends BaseMapper<OnlineAccount> {

    OnlineAccountDto getAccount(@Param("param") OnlineAccountDto onlineAccountDto);

}
