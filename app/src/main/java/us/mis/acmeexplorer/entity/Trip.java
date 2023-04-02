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
    private String from;
    private String to;
    private String description;
    private Double price;
    private Date startDate;
    private Date endDate;
    private boolean isSelected;
    private String imageURI;

    public Trip(String from, String to, String description, Double price,
                Date startDate, Date endDate, String imageURI) {
        this.from = from;
        this.to = to;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isSelected = false;
        this.imageURI = imageURI;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
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

            Date startDate = new Date();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            numRandom = getRandomNum(3, 30);
            startCalendar.add(Calendar.DAY_OF_YEAR, numRandom);
            startDate = startCalendar.getTime();

            Date endDate = (Date) startDate.clone();
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            numRandom = getRandomNum(6, 15);
            endCalendar.add(Calendar.DAY_OF_YEAR, numRandom);
            endDate = endCalendar.getTime();

            numRandom = getRandomNum(0, 100);
            String uri = Images.uri[numRandom % Images.uri.length];
            String description = Descriptions.lorem[numRandom % Descriptions.lorem.length];

            Trip trip = new Trip(tripFrom, tripTo, description, price, startDate, endDate, uri);
            trips.add(trip);
        }
        return trips;
    }
}
