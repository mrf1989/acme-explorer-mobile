package us.mis.acmeexplorer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Filter implements Parcelable {
    private int minPrice, maxPrice;
    private long starDate, endDate;

    public Filter(int minPrice, int maxPrice, long starDate, long endDate) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.starDate = starDate;
        this.endDate = endDate;
    }

    public Filter(){
        starDate = 0;
        endDate = 0;
        minPrice = 0;
        maxPrice = Integer.MAX_VALUE;
    }

    protected Filter(Parcel in) {
        minPrice = in.readInt();
        maxPrice = in.readInt();
        starDate = in.readLong();
        endDate = in.readLong();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public long getStarDate() {
        return starDate;
    }

    public void setStarDate(long starDate) {
        this.starDate = starDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(minPrice);
        parcel.writeInt(maxPrice);
        parcel.writeLong(starDate);
        parcel.writeLong(endDate);
    }

    @Override
    public String toString() {
        return "Filter{" +
                "minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", starDate=" + starDate +
                ", endDate=" + endDate +
                '}';
    }
}
