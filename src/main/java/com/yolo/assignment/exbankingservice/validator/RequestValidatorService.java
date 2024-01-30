package com.yolo.assignment.exbankingservice.validator;

import com.yolo.assignment.exbankingservice.ExbankingManagementService.CreateUserRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.DepositRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.GetBalanceRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.SendRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.User;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.WithdrawRequest;
import com.yolo.assignment.exbankingservice.constants.Messages;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestValidatorService {

    public void validateCreateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NULL_REQUEST_ERROR), new Metadata());
        }

        User user = request.getUser();

        if (user.getFirstName().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_FIRST_NAME_ERROR), new Metadata());
        }

        if (user.getLastName().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_LAST_NAME_ERROR), new Metadata());
        }

        if (user.getPassportNo().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_PASSPORT_NUMBER_ERROR), new Metadata());
        }

        if (!user.getPassportNo().matches(Messages.PASSPORT_REGEX)) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.INVALID_PASSPORT_NUMBER_ERROR), new Metadata());
        }

        if (user.getContactInfo().getEmail().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_EMAIL_ERROR), new Metadata());
        }

        if (user.getContactInfo().getEmail().trim().isEmpty() || !user.getContactInfo().getEmail().matches(Messages.EMAIL_REGEX)) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.INVALID_EMAIL_ERROR), new Metadata());
        }

        if (user.getContactInfo().getTelephone().getCountryCode().trim().isEmpty() || user.getContactInfo().getTelephone().getNumber().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.INVALID_TELEPHONE_ERROR), new Metadata());
        }
    }


    public void validateWithdrawRequest(WithdrawRequest request) {
        if (request == null) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NULL_REQUEST_ERROR), metadata);
        }
        if (request.getAccountNumber().trim().isEmpty()) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_ACCOUNT_NUMBER_ERROR), metadata);
        }
        if (request.getAmount() <= 0) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NEGATIVE_AMOUNT_ERROR), metadata);
        }
        if (request.getCurrencyId().trim().isEmpty()) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_CURRENCY_ID_ERROR), metadata);
        }
    }

    public void validateDepositRequest(DepositRequest request) {
        if (request == null) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NULL_REQUEST_ERROR), new Metadata());
        }
        if (request.getAccountNumber().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_ACCOUNT_NUMBER_ERROR), new Metadata());
        }
        if (request.getAmount() <= 0) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NEGATIVE_AMOUNT_ERROR), new Metadata());
        }
        if (request.getCurrencyId().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_CURRENCY_ID_ERROR), new Metadata());
        }
    }

    public void validateSendRequest(SendRequest request) {
        if (request == null) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NULL_REQUEST_ERROR), new Metadata());
        }
        if (request.getSenderAccountNo().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_SENDER_ACCOUNT_ERROR), new Metadata());
        }
        if (request.getReceiverAccountNo().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_RECEIVER_ACCOUNT_ERROR), new Metadata());
        }
        if (request.getAmount() <= 0) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NEGATIVE_AMOUNT_ERROR), new Metadata());
        }
        if (request.getCurrencyId().trim().isEmpty()) {
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_CURRENCY_ID_ERROR), new Metadata());
        }
    }

    public void validateGetBalanceRequest(GetBalanceRequest request) {
        if (request == null) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.NULL_REQUEST_ERROR), metadata);
        }

        if (request.getAccountNumber().trim().isEmpty()) {
            Metadata metadata = new Metadata();
            throw new StatusRuntimeException(io.grpc.Status.FAILED_PRECONDITION.withDescription(Messages.EMPTY_ACCOUNT_NUMBER_ERROR), metadata);
        }
    }
}
