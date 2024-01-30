package com.yolo.assignment.exbankingservice.service;

import com.yolo.assignment.exbankingservice.ExbankingManagementService.Currency;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Audit;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Account;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.AccountType;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.ContactInfo;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.Telephone;
import com.yolo.assignment.exbankingservice.ExbankingManagementService.User;
import com.yolo.assignment.exbankingservice.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final DatabaseRepository databaseRepository;

    private static final String ACTIVE_ACCOUNT_NUMBER_ONE = "2345678";
    private static final String ACTIVE_ACCOUNT_NUMBER_TWO = "13579";
    private static final String INACTIVE_ACCOUNT_NUMBER = "987654";

    @PostConstruct
    public void init() {
        User userOne = databaseRepository.createUser(buildUserOne());
        User userTwo = databaseRepository.createUser(buildUserTwo());
        databaseRepository.saveAccount(buildActiveAccountOne(userOne.getId()));
        databaseRepository.saveAccount(buildActiveAccountTwo(userTwo.getId()));
        databaseRepository.saveAccount(buildInactiveAccount(userOne.getId()));

        databaseRepository.addCurrency(addUSDCurrency());
        databaseRepository.addCurrency(addEuroCurrency());
    }

    private User buildUserOne() {
        User.Builder userBuilder = User.newBuilder().setId(UUID.randomUUID().toString()).setFirstName("Thilini")
                .setLastName("Perera").setPassportNo("N456789").setAddress("Al Barsha, Dubai");

        Telephone.Builder telephoneBuilder = Telephone.newBuilder().setCountryCode("+971").setNumber("555-123-4567");

        ContactInfo.Builder contactInfoBuilder = ContactInfo.newBuilder().setEmail("thilini.pererae@gmail.com")
                .setTelephone(telephoneBuilder.build());
        userBuilder.setContactInfo(contactInfoBuilder.build());
        userBuilder.setAuditInfo(databaseRepository.generateActiveAuditInfoForNewlyCreatingObjs());

        return userBuilder.build();
    }

    private User buildUserTwo() {
        User.Builder userBuilder = User.newBuilder().setId(UUID.randomUUID().toString()).setFirstName("Chamara")
                .setLastName("Asanka").setPassportNo("N445359").setAddress("Al Barsha, Sharjah");

        Telephone.Builder telephoneBuilder = Telephone.newBuilder().setCountryCode("+971").setNumber("555-356-4732");

        ContactInfo.Builder contactInfoBuilder = ContactInfo.newBuilder().setEmail("chamara.asanka@gmail.com")
                .setTelephone(telephoneBuilder.build());
        userBuilder.setContactInfo(contactInfoBuilder.build());
        userBuilder.setAuditInfo(databaseRepository.generateActiveAuditInfoForNewlyCreatingObjs());

        return userBuilder.build();
    }

    private Account buildActiveAccountOne(String userId) {
        Account.Builder accountBuilder = Account.newBuilder();

        accountBuilder.setId(UUID.randomUUID().toString()).setUserId(userId).setAccountNumber(ACTIVE_ACCOUNT_NUMBER_ONE)
                .setAccountType(AccountType.SAVINGS).setBalance(1000).setCurrencyId("USD")
                .setAuditInfo(databaseRepository.generateActiveAuditInfoForNewlyCreatingObjs());

        return accountBuilder.build();
    }

    private Account buildActiveAccountTwo(String userId) {
        Account.Builder accountBuilder = Account.newBuilder();

        accountBuilder.setId(UUID.randomUUID().toString()).setUserId(userId).setAccountNumber(ACTIVE_ACCOUNT_NUMBER_TWO)
                .setAccountType(AccountType.SAVINGS).setBalance(5000).setCurrencyId("USD")
                .setAuditInfo(databaseRepository.generateActiveAuditInfoForNewlyCreatingObjs());

        return accountBuilder.build();
    }

    private Account buildInactiveAccount(String userId) {
        Account.Builder accountBuilder = Account.newBuilder();

        Audit audit = databaseRepository.generateInactiveAuditInfoForNewlyCreatingObjs();

        accountBuilder.setId(UUID.randomUUID().toString()).setUserId(userId).setAccountNumber(INACTIVE_ACCOUNT_NUMBER)
                .setAccountType(AccountType.SAVINGS).setBalance(1000).setCurrencyId("USD")
                .setAuditInfo(audit);

        return accountBuilder.build();
    }

    private Currency addUSDCurrency() {
        return Currency.newBuilder().setId("001").setName("American Dollar")
                .setCode("USD").setDescription("American Dollar").setPrecision(2).build();
    }

    private Currency addEuroCurrency() {
        return Currency.newBuilder().setId("002").setName("Euro")
                .setCode("EURO").setDescription("Euro").setPrecision(2).build();
    }

}