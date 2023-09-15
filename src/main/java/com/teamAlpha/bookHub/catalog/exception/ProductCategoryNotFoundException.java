package com.teamAlpha.bookHub.catalog.exception;

public class ProductCategoryNotFoundException extends RuntimeException{

    public ProductCategoryNotFoundException(Integer productCategoryId){
        super("Couldn't find any product category with id "+ productCategoryId +" in our records. Try again");
    }
}
