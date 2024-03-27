package com.nbp.tim3.dto.openinghours;

import com.nbp.tim3.model.OpeningHours;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public OpeningHoursResponse(List<OpeningHours> openingHours) {

        openingHours.forEach(openingHours1 -> {
            switch (openingHours1.getDayOfWeek()) {
                case "Monday":
                    this.mondayOpen = openingHours1.getOpeningTime();
                    this.mondayClose = openingHours1.getClosingTime();
                    break;
                case "Tuesday":
                    this.tuesdayOpen = openingHours1.getOpeningTime();
                    this.tuesdayClose = openingHours1.getClosingTime();
                    break;
                case "Wednesday":
                    this.wednesdayOpen = openingHours1.getOpeningTime();
                    this.wednesdayClose = openingHours1.getClosingTime();
                    break;
                case "Thursday":
                    this.thursdayOpen = openingHours1.getOpeningTime();
                    this.thursdayClose = openingHours1.getClosingTime();
                    break;
                case "Friday":
                    this.fridayOpen = openingHours1.getOpeningTime();
                    this.fridayClose = openingHours1.getClosingTime();
                    break;
                case "Saturday":
                    this.saturdayOpen = openingHours1.getOpeningTime();
                    this.saturdayClose = openingHours1.getClosingTime();
                    break;
                case "Sunday":
                    this.sundayOpen = openingHours1.getOpeningTime();
                    this.sundayClose = openingHours1.getClosingTime();
                    break;
            }
        });

    }

}
