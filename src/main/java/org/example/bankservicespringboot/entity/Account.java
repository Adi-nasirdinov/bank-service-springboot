package org.example.bankservicespringboot.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name="accounts")

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_name", nullable = false, length = 50)
    private String owner_name;

    @Column(nullable = false)
    private BigDecimal balance;



    protected Account() {}

    public Account(String owner_name, BigDecimal balance) {
        this.owner_name = owner_name;
        this.balance = balance;
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
