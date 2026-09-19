    package org.example.bankservicespringboot.service;

    import org.example.bankservicespringboot.dto.BalanceChangeResponse;
    import org.example.bankservicespringboot.entity.Account;
    import org.example.bankservicespringboot.exceptions.AccountNotFoundException;
    import org.example.bankservicespringboot.exceptions.InsufficientFoundsException;
    import org.springframework.stereotype.Service;
    import org.example.bankservicespringboot.repository.AccountRepository;

    import java.math.BigDecimal;
    @Service
    public class AccountServiceImpl  implements AccountService {
        final private AccountRepository repository;

        public AccountServiceImpl(AccountRepository repository) {
            this.repository = repository;
        }

        public Account createAccount(String owner_name,BigDecimal initialBalance) {
            if(initialBalance.compareTo(BigDecimal.ZERO)<=0){
                throw new IllegalArgumentException("Новый баланс нн может быть ниже нуля");
            }
            Account account = new Account(owner_name, initialBalance);
            return repository.save(account);
        }

        public synchronized BalanceChangeResponse deposit(Long id,  BigDecimal amount) {

            Account account = repository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));

            if(amount.compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("Сумма должна быть положительной");

            }


            BigDecimal balanceBefore = account.getBalance();
            BigDecimal balanceAfter = balanceBefore.add(amount);
            account.setBalance(balanceAfter);
            repository.save(account);



            return new BalanceChangeResponse(id, balanceBefore, balanceAfter);
        }

        public synchronized BalanceChangeResponse withdraw(Long id,  BigDecimal amount) {
            Account account = repository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));

            if(amount.compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("Сумма должна быть положительной");

            }

            BigDecimal balanceBefore = account.getBalance();

            if (balanceBefore.compareTo(amount) < 0) {
                throw new InsufficientFoundsException(id, amount, balanceBefore);
            }

            BigDecimal balanceAfter = balanceBefore.subtract(amount);
            account.setBalance(balanceAfter);
            repository.save(account);


            return new BalanceChangeResponse(id, balanceBefore, balanceAfter);




        }
        public Account getAccount(Long id){

            return repository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));
        }

    }
