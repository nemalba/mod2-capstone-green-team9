package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
    @RestController

    public class TransferController {

        @Autowired
        private TransferDao transferDao;

        @Autowired
        private UserDao userDao;

        @Autowired
        private AccountDao accountDao;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @RequestMapping(path = "/transfers", method = RequestMethod.GET)
        public List<Transfer> getAllTransfers(Principal principal) {
            int userId = userDao.findIdByUsername(principal.getName());
            List<Transfer> transferHistory = transferDao.getAllTransfers(userId);
            return transferHistory;
        }

//        @RequestMapping(path = "/transferTo", method = RequestMethod.GET)
//        public List<Transfer> getAllTransfersTo(Principal principal) {
//            int userId = userDao.findIdByUsername(principal.getName());
//            List<Transfer> transferHistory = transferDao.getAllTransfers(userId);
//            return transferHistory;
//        }

        @RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
        public Transfer getTransferDetail(@PathVariable int id) {
            Transfer transfer = transferDao.getTransferDetails(id);
            return transfer;
        }
    }

