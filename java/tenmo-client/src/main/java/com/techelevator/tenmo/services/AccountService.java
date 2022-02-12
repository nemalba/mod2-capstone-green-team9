package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public AccountService(String baseUrl, String authToken) {
        this.baseUrl = baseUrl;
        this.authToken = authToken;
    }

    public BigDecimal viewCurrentBalance(){

        BigDecimal account = null;
        ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "balance/" ,
        HttpMethod.GET, makeEntity(), BigDecimal.class);
        account = response.getBody();
        return account;

    }

    public User[] viewAllUsers(){
        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "users/",
                HttpMethod.GET, makeEntity(), User[].class);
        return response.getBody();
    }

    public HttpEntity<Void> makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}

