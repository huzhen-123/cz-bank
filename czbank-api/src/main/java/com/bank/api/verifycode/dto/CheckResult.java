package com.bank.api.verifycode.dto;

import lombok.Data;

@Data
public class CheckResult {
    private Boolean state;
    private String msg;

    public CheckResult(Boolean state, String msg) {
        this.state = state;
        this.msg = msg;
    }
}
