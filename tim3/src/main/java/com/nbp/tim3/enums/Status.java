package com.nbp.tim3.enums;

import java.util.Locale;

public enum Status {
    NEW,CANCELLED,ACCEPTED,REJECTED,READY_FOR_DELIVERY,ACCEPTED_FOR_DELIVERY,IN_DELIVERY,DELIVERED;

    public static Status fromString(String value) {
        if (value != null) {

                if(value.equalsIgnoreCase("new") || value.equalsIgnoreCase("pending"))
                    return NEW;
                else if(value.equalsIgnoreCase("In preparation") || value.equalsIgnoreCase("in-preparation"))
                    return ACCEPTED;
                else if(value.equalsIgnoreCase("Ready for delivery") || value.equalsIgnoreCase("ready-for-delivery"))
                    return READY_FOR_DELIVERY;
                else if(value.equalsIgnoreCase("rejected"))
                    return REJECTED;
                else if (value.equalsIgnoreCase("cancelled")) {
                    return CANCELLED;

                } else if (value.equalsIgnoreCase("accepted-for-delivery") || value.equalsIgnoreCase("Accepted for delivery")) {
                    return ACCEPTED_FOR_DELIVERY;
                } else if (value.equalsIgnoreCase("In Delivery") || value.equalsIgnoreCase("in-delivery")){
                    return IN_DELIVERY;
                }
                else {
                    return DELIVERED;
                }

            
        }
        throw new IllegalArgumentException("No enum constant " + Status.class.getName() + " with value " + value);
    }
}
