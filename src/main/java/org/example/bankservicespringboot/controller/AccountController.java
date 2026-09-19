package org.example.bankservicespringboot.controller;

import org.example.bankservicespringboot.dto.AmountRequest;
import org.example.bankservicespringboot.dto.BalanceChangeResponse;
import org.example.bankservicespringboot.entity.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.example.bankservicespringboot.service.AccountService;

@RestController
@RequestMapping("/accounts")

public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable("id") Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account request) {
        return accountService.createAccount(request.getOwner_name(), request.getBalance());
    }

    @PutMapping("/{id}/deposit")
    public BalanceChangeResponse deposit(@PathVariable Long id, @RequestBody AmountRequest request) {
            return accountService.deposit(id, request.getAmount());
    }

    @PutMapping("/{id}/withdraw")
    public BalanceChangeResponse withdraw(@PathVariable Long id, @RequestBody AmountRequest request) {
        return accountService.withdraw(id, request.getAmount());
    }


}
