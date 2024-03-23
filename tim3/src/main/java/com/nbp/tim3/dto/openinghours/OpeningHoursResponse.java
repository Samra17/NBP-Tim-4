package com.nbp.tim3.dto.openinghours;

import com.nbp.tim3.model.OpeningHours;

import java.time.LocalTime;

public class OpeningHoursResponse {
    private LocalTime mondayOpen;
    private LocalTime mondayClose;
    private LocalTime tuesdayOpen;
    private LocalTime tuesdayClose;
    private LocalTime wednesdayOpen;
    private LocalTime wednesdayClose;
    private LocalTime thursdayOpen;
    private LocalTime thursdayClose;
    private LocalTime fridayOpen;
    private LocalTime fridayClose;
    private LocalTime saturdayOpen;
    private LocalTime saturdayClose;
    private LocalTime sundayOpen;
    private LocalTime sundayClose;

    public OpeningHoursResponse(OpeningHours openingHours) {
        /*this.mondayOpen = openingHours.getMondayOpen();
        this.mondayClose = openingHours.getMondayClose();
        this.tuesdayOpen = openingHours.getTuesdayOpen();
        this.tuesdayClose = openingHours.getTuesdayClose();
        this.wednesdayOpen = openingHours.getWednesdayOpen();
        this.wednesdayClose = openingHours.getWednesdayClose();
        this.thursdayOpen = openingHours.getThursdayOpen();
        this.thursdayClose = openingHours.getThursdayClose();
        this.fridayOpen = openingHours.getFridayOpen();
        this.fridayClose = openingHours.getFridayClose();
        this.saturdayOpen = openingHours.getSaturdayOpen();
        this.saturdayClose = openingHours.getSaturdayClose();
        this.sundayOpen = openingHours.getSundayOpen();
        this.sundayClose = openingHours.getSundayClose();*/
    }
    public OpeningHoursResponse(LocalTime mondayOpen, LocalTime mondayClose, LocalTime tuesdayOpen, LocalTime tuesdayClose, LocalTime wednesdayOpen, LocalTime wednesdayClose, LocalTime thursdayOpen, LocalTime thursdayClose, LocalTime fridayOpen, LocalTime fridayClose, LocalTime saturdayOpen, LocalTime saturdayClose, LocalTime sundayOpen, LocalTime sundayClose) {
        this.mondayOpen = mondayOpen;
        this.mondayClose = mondayClose;
        this.tuesdayOpen = tuesdayOpen;
        this.tuesdayClose = tuesdayClose;
        this.wednesdayOpen = wednesdayOpen;
        this.wednesdayClose = wednesdayClose;
        this.thursdayOpen = thursdayOpen;
        this.thursdayClose = thursdayClose;
        this.fridayOpen = fridayOpen;
        this.fridayClose = fridayClose;
        this.saturdayOpen = saturdayOpen;
        this.saturdayClose = saturdayClose;
        this.sundayOpen = sundayOpen;
        this.sundayClose = sundayClose;
    }

    public OpeningHoursResponse() {
    }

    public LocalTime getMondayOpen() {
        return mondayOpen;
    }

    public void setMondayOpen(LocalTime mondayOpen) {
        this.mondayOpen = mondayOpen;
    }

    public LocalTime getMondayClose() {
        return mondayClose;
    }

    public void setMondayClose(LocalTime mondayClose) {
        this.mondayClose = mondayClose;
    }

    public LocalTime getTuesdayOpen() {
        return tuesdayOpen;
    }

    public void setTuesdayOpen(LocalTime tuesdayOpen) {
        this.tuesdayOpen = tuesdayOpen;
    }

    public LocalTime getTuesdayClose() {
        return tuesdayClose;
    }

    public void setTuesdayClose(LocalTime tuesdayClose) {
        this.tuesdayClose = tuesdayClose;
    }

    public LocalTime getWednesdayOpen() {
        return wednesdayOpen;
    }

    public void setWednesdayOpen(LocalTime wednesdayOpen) {
        this.wednesdayOpen = wednesdayOpen;
    }

    public LocalTime getWednesdayClose() {
        return wednesdayClose;
    }

    public void setWednesdayClose(LocalTime wednesdayClose) {
        this.wednesdayClose = wednesdayClose;
    }

    public LocalTime getThursdayOpen() {
        return thursdayOpen;
    }

    public void setThursdayOpen(LocalTime thursdayOpen) {
        this.thursdayOpen = thursdayOpen;
    }

    public LocalTime getThursdayClose() {
        return thursdayClose;
    }

    public void setThursdayClose(LocalTime thursdayClose) {
        this.thursdayClose = thursdayClose;
    }

    public LocalTime getFridayOpen() {
        return fridayOpen;
    }

    public void setFridayOpen(LocalTime fridayOpen) {
        this.fridayOpen = fridayOpen;
    }

    public LocalTime getFridayClose() {
        return fridayClose;
    }

    public void setFridayClose(LocalTime fridayClose) {
        this.fridayClose = fridayClose;
    }

    public LocalTime getSaturdayOpen() {
        return saturdayOpen;
    }

    public void setSaturdayOpen(LocalTime saturdayOpen) {
        this.saturdayOpen = saturdayOpen;
    }

    public LocalTime getSaturdayClose() {
        return saturdayClose;
    }

    public void setSaturdayClose(LocalTime saturdayClose) {
        this.saturdayClose = saturdayClose;
    }

    public LocalTime getSundayOpen() {
        return sundayOpen;
    }

    public void setSundayOpen(LocalTime sundayOpen) {
        this.sundayOpen = sundayOpen;
    }

    public LocalTime getSundayClose() {
        return sundayClose;
    }

    public void setSundayClose(LocalTime sundayClose) {
        this.sundayClose = sundayClose;
    }
}
