package us.mis.acmeexplorer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import us.mis.acmeexplorer.constant.Cities;
import us.mis.acmeexplorer.constant.Descriptions;
import us.mis.acmeexplorer.constant.Images;

public class Trip implements Serializable {
    private String id;
    private String from;
    private String to;
    private String description;
    private Double price;
    private Long startDate;
    private Long endDate;
    private boolean isSelected;
    private String imageURI;

    public Trip(String from, String to, String description, Double price,
                Long startDate, Long endDate, String imageURI) {
        this.from = from;
        this.to = to;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isSelected = false;
        this.imageURI = imageURI;
    }

    public Trip() {}

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public String getImageURI() { return imageURI; }

    public void setImageURI(String uri) { imageURI = uri; }

    @Override
    public String toString() {
        return "Trip{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isSelected=" + isSelected +
                ", imageURI=" + imageURI +
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

            Double price = (double) Math.round((ThreadLocalRandom
                    .current()
                    .nextDouble(10.0, 1000.0) * 100) / 100);

            Long startDate = new Date().getTime();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(new Date(startDate));
            numRandom = getRandomNum(3, 30);
            startCalendar.add(Calendar.DAY_OF_YEAR, numRandom);
            startDate = startCalendar.getTimeInMillis();

            Long endDate = startDate;
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(new Date(endDate));
            numRandom = getRandomNum(6, 15);
            endCalendar.add(Calendar.DAY_OF_YEAR, numRandom);
            endDate = endCalendar.getTimeInMillis();

            numRandom = getRandomNum(0, 100);
            String uri = Images.uri[numRandom % Images.uri.length];
            String description = Descriptions.lorem[numRandom % Descriptions.lorem.length];

            Trip trip = new Trip(tripFrom, tripTo, description, price, startDate, endDate, uri);
            trips.add(trip);
        }
        return trips;
    }
}
