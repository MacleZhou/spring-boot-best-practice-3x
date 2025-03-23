package com.macle.study.rdf4j.api.dto;

import com.macle.study.rdf4j.model.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class FraudDetectionResponse {
    private List<Transaction> transactions;
    private String errorMessage;

    public FraudDetectionResponse(List<Transaction> transactions) {
        this.transactions = transactions;
        this.errorMessage = null;
    }

    public FraudDetectionResponse(String errorMessage) {
        this.transactions = null;
        this.errorMessage = errorMessage;
    }
}