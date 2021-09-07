package com.bank.common.enums;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * @author huzhen
 * @date 2021/8/18
 */
public enum LoginTypeEnum {
    /**
     *登录账号类型枚举 0-手机号 1-昵称 2-银行账号
     **/
    MOBILE(0,"mobile"),
    NICK_NAME(1,"nickName"),
    BANK_ACCOUNT(2,"bankAccount")
    ;

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String name;

    LoginTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Integer getCodeByName(String name){
        if(StringUtils.isEmpty(name)){
            return null;
        }
        for (LoginTypeEnum loginTypeEnum : values()) {
            if(name.equals(loginTypeEnum.getName())){
                return loginTypeEnum.getCode();
            }
        }
        return null;
    }
}
