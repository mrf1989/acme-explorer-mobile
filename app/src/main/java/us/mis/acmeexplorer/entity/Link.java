package us.mis.acmeexplorer.entity;

import java.util.ArrayList;
import java.util.List;

import us.mis.acmeexplorer.R;
import us.mis.acmeexplorer.TripsActivity;
import us.mis.acmeexplorer.constant.Strings;

public class Link {
    private final String linkName;
    private final Class linkToClass;
    private final int linkImageResource;

    public Link(String linkName, Class linkToClass, int linkImageResource) {
        this.linkName = linkName;
        this.linkToClass = linkToClass;
        this.linkImageResource = linkImageResource;
    }

    public String getLinkName() {
        return linkName;
    }

    public Class getLinkToClass() {
        return linkToClass;
    }

    public int getLinkImageResource() {
        return linkImageResource;
    }

    public static List<Link> createLinks() {
        List<Link> links = new ArrayList<>();
        links.add(new Link(Strings.AVAILABLE_TRIPS, TripsActivity.class, R.drawable.available_trips));
        links.add(new Link(Strings.SELECTED_TRIPS, TripsActivity.class, R.drawable.selected_trips));
        return links;
    }
}
