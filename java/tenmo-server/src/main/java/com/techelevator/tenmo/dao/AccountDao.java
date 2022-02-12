package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewBalance(int userId);
//    Account addToBalance(BigDecimal amountToAdd, int id);
//    Account subtractFromBalance(BigDecimal amountToSubtract, int id);
//    Account findUserById(int userId);
    Account findAccountById (int id);

   // Account getBalance();


}
