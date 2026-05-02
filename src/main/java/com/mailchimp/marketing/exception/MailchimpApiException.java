package com.mailchimp.marketing.exception;

public class MailchimpApiException extends RuntimeException {

    private final int statusCode;

    public MailchimpApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public MailchimpApiException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
