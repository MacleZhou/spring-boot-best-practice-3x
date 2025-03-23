package com.macle.study.rdf4j.api;

import com.macle.study.rdf4j.api.dto.FraudDetectionResponse;
import com.macle.study.rdf4j.model.Transaction;
import com.macle.study.rdf4j.model.User;
import com.macle.study.rdf4j.service.BlazegraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

@Autowired
private BlazegraphService blazegraphService;

@PostMapping
public String addUser(@RequestBody User user) {
    try {
        blazegraphService.addUser(user);
        return"User added successfully";
    } catch (Exception e) {
        e.printStackTrace();
        return"Failed to add user: " + e.getMessage();
    }
}

@PostMapping("/transactions")
public String addTransaction(@RequestBody Transaction transaction) {
    try {
        blazegraphService.addTransaction(transaction);
        return"Transaction added successfully";
    } catch (Exception e) {
        e.printStackTrace();
        return"Failed to add transaction: " + e.getMessage();
    }
}

@GetMapping("/fraud-detection/{threshold}")
public FraudDetectionResponse detectFraud(@PathVariable double threshold) {
    try {
        List<Transaction> fraudulentTransactions = blazegraphService.detectFraudulentTransactions(threshold);
        return new FraudDetectionResponse(fraudulentTransactions);
    } catch (Exception e) {
        e.printStackTrace();
        return new FraudDetectionResponse(e.getMessage());
    }
}
}