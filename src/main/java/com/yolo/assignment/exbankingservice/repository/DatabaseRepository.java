package com.yolo.assignment.exbankingservice.repository;

import com.yolo.assignment.exbankingservice.ExbankingManagementService.Currency;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Account;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.AccountType;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Audit;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Status;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Transaction;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.User;
import com.yolo.assignment.exbankingservice.constants.Messages;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Log4j2
@Service
public class DatabaseRepository {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, Transaction> transactions = new HashMap<>();
    private final Map<String, Currency> currencies = new HashMap<>();

    public User createUser(User user) {
        user = user.toBuilder().setId(UUID.randomUUID().toString())
                .setAuditInfo(generateActiveAuditInfoForNewlyCreatingObjs()).build();
        users.put(user.getId(), user);
        return user;
    }

    public Account createAccount(String userId) {
        Account account = Account.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setUserId(userId)
                .setAccountNumber(generateAccountNumber())
                .setAccountType(AccountType.SAVINGS)
                .setBalance(0)
                .setCurrencyId(String.valueOf(System.currentTimeMillis()))
                .setAuditInfo(generateActiveAuditInfoForNewlyCreatingObjs())
                .build();
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public Account saveAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account updateAccount(Account account) {
        if (accounts.containsKey(account.getAccountNumber())) {
            return accounts.put(account.getAccountNumber(), account);
        }

        throw new StatusRuntimeException(io.grpc.Status.NOT_FOUND.withDescription(Messages.ACCOUNT_NOT_FOUND));
    }

    public Transaction createTransaction(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Currency addCurrency(Currency currency) {
        currencies.put(currency.getId(), currency);
        return currency;
    }

    public List<Currency> getCurrencyList() {
        return new ArrayList<>(currencies.values());
    }

    public Audit generateActiveAuditInfoForNewlyCreatingObjs() {
        long now = System.currentTimeMillis();
        return  Audit.newBuilder()
                .setCreatedAt(now)
                .setCreatedBy(currentSessionUser())
                .setModifiedAt(now)
                .setModifiedBy(currentSessionUser())
                .setStatus(Status.ACTIVE)
                .build();
    }

    public Audit generateInactiveAuditInfoForNewlyCreatingObjs() {
        long now = System.currentTimeMillis();
        return  Audit.newBuilder()
                .setCreatedAt(now)
                .setCreatedBy(currentSessionUser())
                .setModifiedAt(now)
                .setModifiedBy(currentSessionUser())
                .setStatus(Status.INACTIVE)
                .build();
    }

    //TODO need to access session object here to get the current session user
    private String currentSessionUser() {
        return "Thilini Perera";
    }

    private String generateAccountNumber() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}
