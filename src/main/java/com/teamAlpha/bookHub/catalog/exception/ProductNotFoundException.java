package com.teamAlpha.bookHub.catalog.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Integer productId){
        super(("Couldn't find any product with id "+ productId +" in our records. Try again"));
    }
}
