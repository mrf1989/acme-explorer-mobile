package us.mis.acmeexplorer.service;

import java.util.List;

public class GoogleMapsResponse {
    private List<Candidate> candidates;
    private String status;

    public GoogleMapsResponse() {
    }

    public GoogleMapsResponse(List<Candidate> candidates, String status) {
        super();
        this.candidates = candidates;
        this.status = status;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Candidate {
        private Geometry geometry;

        public Candidate() {
        }

        public Candidate(Geometry geometry) {
            super();
            this.geometry = geometry;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }

    public class Geometry {
        private Location location;
        private Viewport viewport;

        public Geometry() {
        }

        public Geometry(Location location, Viewport viewport) {
            super();
            this.location = location;
            this.viewport = viewport;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Viewport getViewport() {
            return viewport;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }
    }

    public class Location {
        private Double lat;
        private Double lng;

        public Location() {
        }

        public Location(Double lat, Double lng) {
            super();
            this.lat = lat;
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    public class Viewport {
        private Northeast northeast;
        private Southwest southwest;

        public Viewport() {
        }

        public Viewport(Northeast northeast, Southwest southwest) {
            super();
            this.northeast = northeast;
            this.southwest = southwest;
        }

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }
    }

    public class Northeast {
        private Double lat;
        private Double lng;

        public Northeast() {
        }

        public Northeast(Double lat, Double lng) {
            super();
            this.lat = lat;
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    public class Southwest {
        private Double lat;
        private Double lng;

        public Southwest() {
        }

        public Southwest(Double lat, Double lng) {
            super();
            this.lat = lat;
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
}
