package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao accountDao;


    @Override
    public  void sendMoney(TransferRequest transferRequest, int userFrom) {
        int userTo = transferRequest.getToUserId();
        BigDecimal amount = transferRequest.getAmountToTransfer();
        if (userFrom != userTo) {
            if (amount.compareTo(accountDao.viewBalance(userFrom)) <= 0) {
                String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
                jdbcTemplate.update(sql, amount, userFrom);
                sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
                jdbcTemplate.update(sql, amount, userTo);
                sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                        "VALUES (2, 2, (SELECT account_id FROM accounts WHERE user_id = ?)," +
                        "(SELECT account_id FROM accounts where user_id = ?), ?)";
                jdbcTemplate.update(sql, userFrom, userTo, amount);
            }
        }
    }

    @Override
    public List<Transfer> getAllTransfer(int userId) {
        List<Transfer> allTransfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.amount, u.username AS user_from, u2.username as user_to" +
                "  FROM transfers t" +
                "  JOIN accounts a ON t.account_from = a.account_id" +
                "                JOIN users u ON a.user_id = u.user_id" +
                "   JOIN accounts a2 ON t.account_to = a2.account_id" +
                "                JOIN users u2 ON a2.user_id = u2.user_id             " +
                "   WHERE  account_from IN (SELECT account_id FROM accounts  WHERE user_id = ?)" +
                "   OR  account_to IN (SELECT account_id FROM accounts  WHERE user_id = ?); ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfers(results);
            allTransfers.add(transfer);
        }
        return allTransfers;
    }

    @Override
    public List<Transfer> getAllTransfers(int accountFrom) {

        String sql = "SELECT * FROM transfers t " +
                "JOIN accounts a ON a.account_id = t.account_from " +
                "JOIN users u ON u.user_id = a.user_id WHERE u.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
        List<Transfer> transfers = new ArrayList<>();
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
       // return null;
    }

    @Override
    public List<Transfer> getAllTransfersToMe(int accountFrom) {
        String response = null;
        String sql = "SELECT * FROM transfers t " +
                "JOIN accounts a ON a.account_id = t.account_to " +
                "JOIN users u ON u.user_id = a.user_id WHERE u.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
        List<Transfer> transfers = new ArrayList<>();
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
        //return null;
    }

    @Override
    public Transfer getTransferDetails(int id) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.*, " +
                "u.username AS userfromname, v.username AS usertoname, " +
                "ts.transfer_status_desc, tt.transfer_type_desc " +
                "FROM transfers t " +
                "JOIN accounts a ON t.account_from = a.account_id " +
                "JOIN accounts b ON t.account_to = b.account_id " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN users v ON b.user_id = v.user_id " +
                "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);

        }
        return transfer;
        //return null;
    }


    private Transfer mapRowToTransfers(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setUserFrom(rs.getString("user_from"));
         transfer.setUserTo(rs.getString("user_to"));
         transfer.setAmount(rs.getBigDecimal("amount"));
         transfer.setTransferId(rs.getInt("transfer_id"));

        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        try {
            transfer.setUserFrom(rs.getString("userfromname"));
            transfer.setUserTo(rs.getString("usertoname"));
        } catch (Exception e)
        {

        }
        try {
            transfer.setTransferType(rs.getString("transfer_type_desc"));//.setTransferType
            transfer.setTransferStatus(rs.getString("transfer_status_desc"));//setTransferStatus
        } catch (Exception e) {

        }
        return transfer;
    }
    }


