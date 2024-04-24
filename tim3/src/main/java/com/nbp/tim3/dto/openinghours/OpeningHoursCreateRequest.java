package com.nbp.tim3.dto.openinghours;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OpeningHoursCreateRequest implements Serializable {

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime mondayOpen;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime mondayClose;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime tuesdayOpen;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime tuesdayClose;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime wednesdayOpen;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime wednesdayClose;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime thursdayOpen;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime thursdayClose;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime fridayOpen;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime fridayClose;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime saturdayOpen;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime saturdayClose;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime sundayOpen;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime sundayClose;



    @AssertTrue(message = "Closing hours can not be before opening hours on the same day!")
    @Schema(hidden = true)
    public boolean isOpeningBeforeClosing() {
        if((mondayOpen !=null && mondayClose!= null && mondayOpen.isAfter(mondayClose)) ||
                (tuesdayOpen != null && tuesdayClose!= null &&    tuesdayOpen.isAfter(tuesdayClose))||
                (wednesdayOpen!=null && wednesdayClose!=null && wednesdayOpen.isAfter(wednesdayClose))||
                (thursdayOpen != null && thursdayClose!=null && thursdayOpen.isAfter(thursdayClose)) ||
                (fridayOpen != null && fridayClose!=null && fridayOpen.isAfter(fridayClose)) ||
                (saturdayOpen != null && saturdayClose!=null && saturdayOpen.isAfter(saturdayClose)) ||
                (sundayOpen != null && sundayClose!= null && sundayOpen.isAfter(sundayClose)))
            return false;

        return  true;
    }

    @AssertTrue(message = "Both opening and closing hours for the same day must be specified!")
    @Schema(hidden = true)
    public boolean isBothDefinedOrUndefined() {
        if((mondayOpen == null && mondayClose != null)
                || (mondayOpen != null && mondayClose == null)
                || (tuesdayOpen == null && tuesdayClose != null)
                || (tuesdayOpen != null && tuesdayClose == null)
                || (wednesdayOpen == null && wednesdayClose != null)
                || (wednesdayOpen != null && wednesdayClose == null)
                || (thursdayOpen == null && thursdayClose != null)
                || (thursdayOpen != null && thursdayClose == null)
                || (fridayOpen == null && fridayClose != null)
                || (fridayOpen != null && fridayClose == null)
                || (saturdayOpen == null && saturdayClose != null)
                || (saturdayOpen != null && saturdayClose == null)
                || (sundayOpen == null && sundayClose != null)
                || (sundayOpen != null && sundayClose == null))
            return false;

        return  true;
    }

    @AssertTrue(message = "There must be at least an hour between opening and closing times!")
    @Schema(hidden = true)
    public boolean isPeriodBetweenOpeningAndClosingValid() {
        if(mondayOpen!=null && mondayClose != null && Duration.between(mondayOpen,mondayClose).getSeconds()<3600)
            return  false;
        if(tuesdayOpen!=null && tuesdayClose != null && Duration.between(tuesdayOpen,tuesdayClose).getSeconds()<3600)
            return  false;
        if(wednesdayOpen!=null && wednesdayClose != null && Duration.between(wednesdayOpen,wednesdayClose).getSeconds()<3600)
            return  false;
        if(thursdayOpen!=null && thursdayClose != null && Duration.between(thursdayOpen,thursdayClose).getSeconds()<3600)
            return  false;
        if(fridayOpen!=null && fridayClose != null && Duration.between(fridayOpen,fridayClose).getSeconds()<3600)
            return  false;
        if(saturdayOpen!=null && saturdayClose != null && Duration.between(saturdayOpen,saturdayClose).getSeconds()<3600)
            return  false;
        if(sundayOpen!=null && sundayClose != null && Duration.between(sundayOpen,sundayClose).getSeconds()<3600)
            return  false;

        return true;
    }


}
