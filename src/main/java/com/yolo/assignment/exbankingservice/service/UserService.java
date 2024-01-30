package com.yolo.assignment.exbankingservice.service;

import com.yolo.assignment.exbankingservice.ExbankingManagementService.Account;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.CreateUserRequest;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.CreateUserResponse;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.User;
import com.yolo.assignment.exbankingservice.UserServiceGrpc;
import com.yolo.assignment.exbankingservice.constants.Messages;
import com.yolo.assignment.exbankingservice.repository.DatabaseRepository;
import com.yolo.assignment.exbankingservice.validator.RequestValidatorService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@Log4j2
@GrpcService
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final DatabaseRepository databaseRepository;
    private final RequestValidatorService requestValidatorService;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        try {
            requestValidatorService.validateCreateUserRequest(request);
            log.info("MockDatabaseService createUser request {}", request);
            User user = databaseRepository.createUser(request.getUser());
            Account account = databaseRepository.createAccount(user.getId());
            CreateUserResponse response = CreateUserResponse.newBuilder().setUser(user).setAccount(account).build();
            log.info("MockDatabaseService createUser response {}", response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(Messages.INTERNAL_SERVER_ERROR).withCause(e).asRuntimeException());
        }


    }
}
