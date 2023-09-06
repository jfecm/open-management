package com.jfecm.openmanagement.exception;

public final class ErrorMessageConstants {

    private ErrorMessageConstants() {
    }

    public static final String NULL_PRODUCT_DATA = "Product data cannot be null.";
    public static final String PRODUCT_NAME_ALREADY_EXISTS = "A product with the same name already exists.";
    public static final String PRODUCT_NOT_FOUND = "No product found with ID ";
    public static final String USERNAME_NOT_EXIST = "The specified username does not exist";
    public static final String INVALID_PRODUCT_PRICE = "Product price must be a positive value.";
    public static final String INVALID_PRODUCT_QUANTITY = "Product quantity must be a non-negative integer.";
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource.";
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred on the server.";
}
