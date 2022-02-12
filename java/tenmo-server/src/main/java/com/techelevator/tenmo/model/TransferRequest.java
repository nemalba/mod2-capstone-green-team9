package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferRequest {



    private int toUserId;
    private BigDecimal amountToTransfer;


    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmountToTransfer() {
        return amountToTransfer;
    }

    public void setAmountToTransfer(BigDecimal amountToTransfer) {
        this.amountToTransfer = amountToTransfer;
    }
}
