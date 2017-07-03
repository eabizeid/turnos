package models;

import java.util.List;

/**
 * Created by edu on 26/08/16.
 */
public class AvailableDaysResponse {
    public Boolean withAnSpecificDays = false;
    public List<String> anSpecificDays;
    public List<BussinessHour> businessHours;
}
