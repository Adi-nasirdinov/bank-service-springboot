package org.example.bankservicespringboot.service;

import org.example.bankservicespringboot.dto.BalanceChangeResponse;
import org.example.bankservicespringboot.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    BalanceChangeResponse deposit(Long id,  BigDecimal amount);
    BalanceChangeResponse withdraw(Long id, BigDecimal amount);

    Account getAccount(Long id);
    Account createAccount(String owner_name, BigDecimal balance);
}

