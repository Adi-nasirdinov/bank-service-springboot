package org.example.bankservicespringboot.repository;

import org.example.bankservicespringboot.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
