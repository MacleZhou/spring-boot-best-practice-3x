package com.macle.study.rdf4j.service;

import com.macle.study.rdf4j.model.Transaction;
import com.macle.study.rdf4j.model.User;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlazegraphService {

@Autowired
private Repository repository;

private final ValueFactory vf = SimpleValueFactory.getInstance();

public void addUser(User user) throws Exception {
    try (RepositoryConnection conn = repository.getConnection()) {
        IRI userIri = vf.createIRI("http://example.org/user/" + user.getUid());
        conn.add(userIri, vf.createIRI("http://example.org/predicate/name"), vf.createLiteral(user.getName()));
        conn.add(userIri, vf.createIRI("http://example.org/predicate/email"), vf.createLiteral(user.getEmail()));
        conn.commit();
        System.out.println("Added user: " + user.getName());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void addTransaction(Transaction transaction) throws Exception {
    try (RepositoryConnection conn = repository.getConnection()) {
        IRI transactionIri = vf.createIRI("http://example.org/transaction/" + transaction.getTransactionId());
        IRI fromUserIri = vf.createIRI("http://example.org/user/" + transaction.getFromUid());
        IRI toUserIri = vf.createIRI("http://example.org/user/" + transaction.getToUid());

        conn.add(transactionIri, vf.createIRI("http://example.org/predicate/from"), fromUserIri);
        conn.add(transactionIri, vf.createIRI("http://example.org/predicate/to"), toUserIri);
        conn.add(transactionIri, vf.createIRI("http://example.org/predicate/amount"), vf.createLiteral(transaction.getAmount()));

        conn.commit();
        System.out.println("Added transaction: " + transaction.getTransactionId());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public List<Transaction> detectFraudulentTransactions(double threshold) throws Exception {
    List<Transaction> fraudulentTransactions = new ArrayList<>();
    try (RepositoryConnection conn = repository.getConnection()) {
        String queryString = "SELECT ?transaction ?from ?to ?amount WHERE { " +
                "?transaction <http://example.org/predicate/from> ?from . " +
                "?transaction <http://example.org/predicate/to> ?to . " +
                "?transaction <http://example.org/predicate/amount> ?amount . " +
                "FILTER(?amount > " + threshold + ") " +
                "}";
        TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Resource transactionIri = (Resource) bindingSet.getValue("transaction");
                Resource fromUserIri = (Resource) bindingSet.getValue("from");
                Resource toUserIri = (Resource) bindingSet.getValue("to");
                Literal amountLiteral = (Literal) bindingSet.getValue("amount");

                Transaction transaction = new Transaction();
                transaction.setTransactionId(transactionIri.stringValue());
                transaction.setFromUid(fromUserIri.stringValue().split("/")[2]);
                transaction.setToUid(toUserIri.stringValue().split("/")[2]);
                transaction.setAmount(amountLiteral.doubleValue());

                fraudulentTransactions.add(transaction);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return fraudulentTransactions;
}
}