package us.mis.acmeexplorer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import us.mis.acmeexplorer.constants.Cities;

public class Trip implements Serializable {
    private String from;
    private String to;
    private String description;
    private Double price;
    private Date startDate;
    private Date endDate;

    public Trip(String from, String to, String description, Double price, Date startDate, Date endDate) {
        this.from = from;
        this.to = to;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    private static int getRandomNum(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static List<Trip> generateTrips(int num) {
        List<Trip> trips = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int numRandom = getRandomNum(0, 100);
            String tripFrom = Cities.city[numRandom % Cities.city.length];

            numRandom = getRandomNum(0, 100);
            String tripTo = Cities.city[numRandom % Cities.city.length];

            Double price = ThreadLocalRandom.current().nextDouble(10.0, 1000.0);

            Date startDate = new Date();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            numRandom = getRandomNum(3, 5);
            startCalendar.add(Calendar.DAY_OF_YEAR, numRandom);
            startDate = startCalendar.getTime();

            Date endDate = new Date();
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            numRandom = getRandomNum(6, 15);
            endCalendar.add(Calendar.DAY_OF_YEAR, numRandom);
            endDate = endCalendar.getTime();

            Trip trip = new Trip(tripFrom, tripTo, "Lorem ipsum...", price, startDate, endDate);
            trips.add(trip);
        }
        return trips;
    }
}
