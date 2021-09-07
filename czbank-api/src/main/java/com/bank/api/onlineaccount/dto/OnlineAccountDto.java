package com.bank.api.onlineaccount.dto;

import com.bank.api.onlineaccount.entity.OnlineAccount;
import lombok.Data;

@Data
public class OnlineAccountDto extends OnlineAccount {
    private String confirmPassword;
    private String bankAccount;
}
