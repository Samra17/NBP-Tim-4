package com.nbp.tim3.exception;

public class OrderPatchInvalidException extends Exception {
    public OrderPatchInvalidException () {
        super("Only deliveryPersonId and orderStatus fields can be updated!");
    }
}
