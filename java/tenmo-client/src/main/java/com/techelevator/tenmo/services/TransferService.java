package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import org.apiguardian.api.API;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
//=======
import com.techelevator.tenmo.model.*;
import org.apiguardian.api.API;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
//>>>>>>> c23675662c20d52916ba37510eb5b67fe2d3433f

public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    private AuthenticatedUser currentUser;
    Transfer[] transferList = null;
    Scanner input = new Scanner(System.in);

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public TransferService(String baseUrl, AuthenticatedUser currentUser) {
        this.baseUrl = baseUrl;
        this.currentUser = currentUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void doATransfer(int recipient, BigDecimal amount) {

       // System.out.println(recipient);
     //   System.out.println(amount);
        // take the two parameters and make an object of type Transfer Request
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setToUserId(recipient);
        transferRequest.setAmountToTransfer(amount);

        // Use the rest template to make a POST call to the endpoint on the server side
        restTemplate.exchange(baseUrl + "/transfer", HttpMethod.POST, makeEntityWithBody(transferRequest), Void.class);

    }

////    public List<Transfer> transferHistory() {
////        String url = baseUrl + "/transfer/history";
////        ResponseEntity<Transfer[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, makeEntity(), Transfer[].class);
////        List<Transfer> transfers = new LinkedList<>(Arrays.asList(responseEntity.getBody()));
//
//
////<<<<<<< HEAD
//        return transfers;
//    }
//=======

//    public User[] viewAllTransfers() {
//        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "users/",
//                HttpMethod.GET, makeEntity(), User[].class);
//        return response.getBody();
//    }

    public void getALlTransfersFrom() {

        transferList = restTemplate.exchange(baseUrl + "/transfers", HttpMethod.GET, makeAuthEntity(),
                Transfer[].class).getBody();
        System.out.println("-------------------------------------------");
        System.out.println("Transfers                                  ");
        System.out.println("ID          From/To      Amount            ");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transferList) {
            Transfer transferFromMe = restTemplate.exchange(baseUrl + "transfers/" + transfer.getTransferId(),
                    HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
            System.out.println(transferFromMe.getTransferId() + "        From:" + transferFromMe.getUserFrom()
                    + "   Amount:" + transferFromMe.getAmount());
            System.out.println(transferFromMe.getTransferId() + "        To:"
                    + transferFromMe.getUserTo()+ "    Amount:" + transferFromMe.getAmount());
            displayDetails();
        }
    }


        public void getALlTransfersTo () {

            transferList = restTemplate.exchange(baseUrl + "/transfers", HttpMethod.GET, makeAuthEntity(),
                    Transfer[].class).getBody();
            System.out.println("-------------------------------------------");
            System.out.println("Transfers                                  ");
            System.out.println("ID          From/To      Amount            ");
            System.out.println("-------------------------------------------");
            for (Transfer transfer : transferList) {
                Transfer transferToMe = restTemplate.exchange(baseUrl + "transfers/" + transfer.getTransferId(),
                        HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();

                System.out.println(transferToMe.getTransferId() + "        From:"
                        + transferToMe.getUserFrom() + "   Amount:" +transferToMe.getAmount());
                System.out.println(transferToMe.getTransferId() + "        To:" +transferToMe.getUserTo()
                        + "   Amount:" +transferToMe.getAmount());
            }
            System.out.println("-------------------------------------------");
            System.out.println("-------------------------------------------");
            displayDetails();
        }

    public void displayDetails() {
        System.out.println("Please enter transfer ID to view details (0 to cancel): ");
        String transferId = input.nextLine();
        for (Transfer transfers : transferList) {
            if (Integer.parseInt(transferId) == transfers.getTransferId()) {
                Transfer display = restTemplate.exchange(baseUrl + "transfers/" + transfers.getTransferId(),
                        HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                System.out.println("-----------------------------------------------");
                System.out.println("Transfer Details                               ");
                System.out.println("-----------------------------------------------");
                System.out.println(" Id: " + display.getTransferId());
                System.out.println(" From: " + display.getUserFrom());
                System.out.println(" To: " + display.getUserTo());
                System.out.println(" Type: " + display.getTransferType());
                System.out.println(" Status: " + display.getTransferStatus());
                System.out.println(" Amount: $" + display.getAmount());

            }
        }
    }
//        public void getTransferDetails () {
//
//            System.out.println("Please enter transfer ID to view details (0 to cancel): ");
//            String transId = input.nextLine();
//            if (Integer.parseInt(transId) != 0) {
//                boolean transactionExists = false;
//                for (Transfer transfer : transferList) {
//                    if (Integer.parseInt(transId) == transfer.getTransferId()) {
//                        Transfer transfer1 = restTemplate.exchange(baseUrl + "transfers/{id}" + transfer.getTransferId(),
//                                HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
//                        transactionExists = true;
//                        System.out.println("--------------------------------------------\n" +
//                                "Transfer Details\n" +
//                                "--------------------------------------------\n" +
//                                " Id: " + transfer1.getTransferId() + "\n" +
//                                " From: " + transfer1.getAccountFrom() + "\n" +
//                                " To: " + transfer1.getAccountTo() + "\n" +
//                                " Type: " + transfer1.getTransferType() + "\n" +
//                                " Status: " + transfer1.getTransferTypeId() + "\n" +
//                                " Amount: $" + transfer1.getAmount());
//                    }
//                }
//                if (!transactionExists) {
//                    System.out.println("Not a valid transfer ID");
//                }
//            }
//        }
//>>>>>>> c23675662c20d52916ba37510eb5b67fe2d3433f



        public HttpEntity<TransferRequest> makeEntityWithBody (TransferRequest request){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            return new HttpEntity<>(request, headers);
        }

        private HttpEntity<Void> makeAuthEntity() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            return new HttpEntity<>(headers);
        }


    }

