package com.yolo.assignment.exbankingservice.validator;

import com.yolo.assignment.exbankingservice.ExbankingManagementService.Account;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.GetBalanceRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.WithdrawRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.DepositRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.SendRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Status;
import com.yolo.assignment.exbankingservice.constants.Messages;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidatorService {

    public void validateAccountForDeposit(Account account) {
        validateAccountExistence(account, Messages.INCORRECT_ACCOUNT_NUMBER);
        validateAccountStatus(account, Messages.INACTIVE_ACCOUNT);
    }

    public void validateAccountForWithdrawal(Account account, int withdrawingAmount) {
        validateAccountExistence(account, Messages.INCORRECT_ACCOUNT_NUMBER);
        validateAccountStatus(account, Messages.INACTIVE_ACCOUNT);
        validateFunds(account.getBalance(), withdrawingAmount);
    }


    public void validateAccountForBalanceInquiry(Account account) {
        validateAccountExistence(account, Messages.INCORRECT_ACCOUNT_NUMBER);
        validateAccountStatus(account, Messages.INACTIVE_ACCOUNT);
    }

    public void validateAccountsForFundTransfer(Account senderAccount, Account receiverAccount, int transferringAmount) {
        validateAccountExistence(senderAccount, Messages.INCORRECT_SENDER_ACCOUNT_NUMBER);
        validateAccountExistence(receiverAccount, Messages.INCORRECT_RECEIVER_ACCOUNT_NUMBER);
        validateAccountStatus(senderAccount, Messages.INACTIVE_SENDER_ACCOUNT);
        validateAccountStatus(receiverAccount, Messages.INACTIVE_RECEIVER_ACCOUNT);
        validateFunds(senderAccount.getBalance(), transferringAmount);
    }

    public void validateAccountExistence(Account account, String description) {
        if (!isAccountValid(account)) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(description), metadata);
        }
    }
    
    private void validateAccountStatus(Account account, String description) {
        if (!isActiveAccount(account)) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(description), metadata);
        }
    }

    private void validateFunds(int balance, int amount) {
        if (isInsufficientFunds(balance, amount)) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.INSUFFICIENT_FUNDS), metadata);
        }
    }

    private boolean isAccountValid(Account account) {
        return account != null;
    }

    private boolean isActiveAccount(Account account) {
        return account != null && account.getAuditInfo().getStatus() == Status.ACTIVE;
    }

    private boolean isInsufficientFunds(int balance, int amount) {
        return balance < amount;
    }
}
