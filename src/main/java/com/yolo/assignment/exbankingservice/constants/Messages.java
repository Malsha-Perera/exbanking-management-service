package com.yolo.assignment.exbankingservice.constants;

public final class Messages {

    private Messages(){
    }

    public static final String INCORRECT_ACCOUNT_NUMBER = "Incorrect account number";
    public static final String INCORRECT_SENDER_ACCOUNT_NUMBER = "Incorrect sender account number";
    public static final String INCORRECT_RECEIVER_ACCOUNT_NUMBER = "Incorrect receiver account number";

    public static final String INACTIVE_ACCOUNT = "Inactive account";
    public static final String INACTIVE_SENDER_ACCOUNT = "Inactive sender account";
    public static final String INACTIVE_RECEIVER_ACCOUNT = "Inactive receiver account";

    public static final String INSUFFICIENT_FUNDS = "No sufficient funds in the account";

    public static final String ACCOUNT_ALREADY_EXIST = "An account already exist with the given account number";
    public static final String ACCOUNT_NOT_FOUND = "Account Account not found for the given account number";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    public static final String NULL_REQUEST_ERROR = "Request cannot be null";
    public static final String EMPTY_ACCOUNT_NUMBER_ERROR = "Account number cannot be empty or null";
    public static final String NEGATIVE_AMOUNT_ERROR = "Amount must be greater than 0";
    public static final String EMPTY_CURRENCY_ID_ERROR = "Currency ID cannot be empty or null";
    public static final String EMPTY_SENDER_ACCOUNT_ERROR = "Sender account number cannot be empty or null";
    public static final String EMPTY_RECEIVER_ACCOUNT_ERROR = "Receiver account number cannot be empty or null";

    public static final String EMPTY_FIRST_NAME_ERROR = "First name cannot be null or empty";
    public static final String EMPTY_LAST_NAME_ERROR = "Last name cannot be null or empty";
    public static final String EMPTY_PASSPORT_NUMBER_ERROR = "Passport number cannot be null or empty";
    public static final String INVALID_PASSPORT_NUMBER_ERROR = "Invalid passport number";
    public static final String EMPTY_EMAIL_ERROR = "Email cannot be null or empty";
    public static final String INVALID_EMAIL_ERROR = "Invalid email";
    public static final String INVALID_TELEPHONE_ERROR = "Invalid telephone number";

    public static final String PASSPORT_REGEX = "^[A-Za-z][0-9]{6}$";
    public static final String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";

}
