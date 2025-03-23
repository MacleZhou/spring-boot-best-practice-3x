package com.macle.study.rdf4j.model;

import lombok.Data;

@Data
public class Transaction {
    private String transactionId;
    private String fromUid;
    private String toUid;
    private double amount;
}