package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal viewBalance (int userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = new Account();
        while(results.next()) {
            account = mapRowToAccount(results);
        }
        return account.getBalance();
    }

//    @Override
//    public Account addToBalance(BigDecimal amountToAdd, int id) {
//        Account account = findAccountById(id);
//        BigDecimal newBalance = account.getBalance().add(amountToAdd);
//        System.out.println("New balance: $" + newBalance);
//        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
//        jdbcTemplate.update(sql, newBalance);
//        return account;
//    }

//    @Override
//    public Account subtractFromBalance(BigDecimal amountToSubtract, int id) {
//        return null;
//    }
//
//    @Override
//    public Account findUserById(int userId) {
//        return null;
//    }

    @Override
    public Account findAccountById(int id) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

//    @Override
//    public Account getBalance() {
//        return null;
//    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;

    }
}
