package com.nbp.tim3.exception;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException() {
        super("Order not found!");
    }
}
