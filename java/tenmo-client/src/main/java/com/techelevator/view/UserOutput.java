package com.techelevator.view;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;

public class UserOutput {

    public static void displayAccountBalance(BigDecimal account){
        System.out.println("Your current account balance is: $" + account);
    }

    public static void displayTransferHistory(){
        System.out.println("-------------------------------------------\n" +
                "Transfers\n" +
                "ID          From/To                 Amount\n" +
                "-------------------------------------------") ;
    }
}
