package us.mis.acmeexplorer.entity;

import java.util.ArrayList;
import java.util.List;

import us.mis.acmeexplorer.MainActivity;
import us.mis.acmeexplorer.R;
import us.mis.acmeexplorer.constants.Strings;

public class Link {
    private String linkName;
    private Class linkToClass;
    private int linkImageResource;

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

    public static final List<Link> createLinks() {
        List<Link> links = new ArrayList<>();
        links.add(new Link(Strings.AVAILABLE_TRIPS, MainActivity.class, R.drawable.available_trips));
        links.add(new Link(Strings.SELECTED_TRIPS, MainActivity.class, R.drawable.selected_trips));
        return links;
    }
}
