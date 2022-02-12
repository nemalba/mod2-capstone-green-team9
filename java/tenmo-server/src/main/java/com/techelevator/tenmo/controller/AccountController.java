package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
@Autowired
 private AccountDao accountDao;
@Autowired
private UserDao userDao;
@Autowired
private TransferDao transferDao;



    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
        int id = userDao.findIdByUsername(principal.getName());
        BigDecimal balance = accountDao.viewBalance(id);
        return balance;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUser(Principal principal){
        List<User> userList = new ArrayList<>();
        userList = userDao.findAllExceptCurrentUser(principal.getName());
        return userList;
    }

    @RequestMapping(path="/transfer", method = RequestMethod.POST)
    public void startTransfer(@RequestBody TransferRequest request, Principal principal) {
        int idFrom = userDao.findIdByUsername(principal.getName());
        transferDao.sendMoney(request, idFrom);

    }
    @RequestMapping(path = "/transfer/history/{id}", method = RequestMethod.GET)
    public List<Transfer> transferList( @PathVariable("id") int userId){
        List<Transfer> transfers = new ArrayList<>();
        return transferDao.getAllTransfer(userId);

    }

}
