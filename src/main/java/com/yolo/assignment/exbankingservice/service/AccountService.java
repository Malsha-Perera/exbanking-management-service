package com.yolo.assignment.exbankingservice.service;

import com.yolo.assignment.exbankingservice.AccountServiceGrpc;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Account;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.DepositRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.DepositResponse;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.GetBalanceRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.GetBalanceResponse;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.SendRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.SendResponse;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Transaction;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.WithdrawRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.WithdrawResponse;
import com.yolo.assignment.exbankingservice.constants.Messages;
import com.yolo.assignment.exbankingservice.repository.DatabaseRepository;
import com.yolo.assignment.exbankingservice.validator.AccountValidatorService;
import com.yolo.assignment.exbankingservice.validator.RequestValidatorService;
import io.envoyproxy.pgv.ValidationException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@GrpcService
@RequiredArgsConstructor
public class AccountService extends AccountServiceGrpc.AccountServiceImplBase {

    private final DatabaseRepository databaseRepository;
    private final AccountValidatorService accountValidatorService;
    private final RequestValidatorService requestValidatorService;

    @Override
    public void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        try {
            requestValidatorService.validateDepositRequest(request);
            log.info("AccountService deposit request {}", request);
            Account account = databaseRepository.getAccount(request.getAccountNumber());
            accountValidatorService.validateAccountForDeposit(account);

            account = account.toBuilder().setBalance(account.getBalance() + request.getAmount()).build();
            Account updated = databaseRepository.updateAccount(account);

            DepositResponse response = DepositResponse.newBuilder().setAccount(updated).build();
            log.info("AccountService deposit response {}", response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(Messages.INTERNAL_SERVER_ERROR).withCause(e).asRuntimeException());
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        try {
            requestValidatorService.validateWithdrawRequest(request);
            log.info("AccountService withdraw request {}", request);
            Account account = databaseRepository.getAccount(request.getAccountNumber());
            accountValidatorService.validateAccountForWithdrawal(account, request.getAmount());

            account = account.toBuilder().setBalance(account.getBalance() - request.getAmount()).build();
            Account updated = databaseRepository.updateAccount(account);

            WithdrawResponse response = WithdrawResponse.newBuilder().setAccount(updated).build();
            log.info("AccountService withdraw response {}", response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(Messages.INTERNAL_SERVER_ERROR).withCause(e).asRuntimeException());
        }

    }

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {
        try {
            requestValidatorService.validateGetBalanceRequest(request);
            log.info("AccountService getBalance request {}", request);
            Account account = databaseRepository.getAccount(request.getAccountNumber());
            accountValidatorService.validateAccountForBalanceInquiry(account);

            GetBalanceResponse response = GetBalanceResponse.newBuilder().setAccount(account).build();
            log.info("AccountService getBalance response {}", response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(Messages.INTERNAL_SERVER_ERROR).withCause(e).asRuntimeException());
        }
    }

    @Override
    @Transactional
    public void send(SendRequest request, StreamObserver<SendResponse> responseObserver) {
        try {
            requestValidatorService.validateSendRequest(request);
            log.info("AccountService send request {}", request);
            Account senderAccount = databaseRepository.getAccount(request.getSenderAccountNo());
            Account receiverAccount = databaseRepository.getAccount(request.getReceiverAccountNo());
            accountValidatorService.validateAccountsForFundTransfer(senderAccount, receiverAccount, request.getAmount());

            senderAccount = senderAccount.toBuilder()
                    .setBalance(senderAccount.getBalance() - request.getAmount())
                    .build();
            databaseRepository.updateAccount(senderAccount);

            receiverAccount = receiverAccount.toBuilder()
                    .setBalance(receiverAccount.getBalance() + request.getAmount())
                    .build();
            databaseRepository.updateAccount(receiverAccount);

            Transaction transaction = Transaction.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setSenderAccountNo(senderAccount.getAccountNumber())
                    .setReceiverAccountNo(receiverAccount.getAccountNumber())
                    .setAmount(request.getAmount())
                    .setCurrencyId(request.getCurrencyId())
                    .setTimestamp(String.valueOf(System.currentTimeMillis()))
                    .setRemarks(request.getRemarks())
                    .build();
            log.info("AccountService send transferring transaction {}", transaction);

            Transaction persisted = databaseRepository.createTransaction(transaction);
            SendResponse response = SendResponse.newBuilder().setTransaction(persisted).build();
            log.info("AccountService  send response {}", response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(Messages.INTERNAL_SERVER_ERROR).withCause(e).asRuntimeException());
        }
    }
}
