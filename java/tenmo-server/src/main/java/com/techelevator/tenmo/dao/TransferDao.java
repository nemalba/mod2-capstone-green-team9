package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.TransferRequest;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

   void sendMoney(TransferRequest transferRequest, int idFrom);
   public List<Transfer> getAllTransfer(int userId);
    public List<Transfer> getAllTransfers(int accountFrom);
    public List<Transfer> getAllTransfersToMe(int accountFrom);
     public Transfer getTransferDetails(int id);
    //public Transfer getTransferById(int transferId);

}
