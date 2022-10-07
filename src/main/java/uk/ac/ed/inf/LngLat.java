package uk.ac.ed.inf;


import java.util.*;

public record LngLat(double lng, double lat){

    // Use of steam comparison here allows for boundaries to be changed with ease for future development.
    public boolean inCentralArea() {
        Location location;
        location = Location.getInstance();

        ArrayList<Coordinate> locations =  location.getData();

        double minLng = locations.stream().min(Comparator.comparing(Coordinate::getLng))
                .orElseThrow(NoSuchElementException::new).getLng();
        double maxLng = locations.stream().max(Comparator.comparing(Coordinate::getLng))
                .orElseThrow(NoSuchElementException::new).getLng();
        double minLat = locations.stream().min(Comparator.comparing(Coordinate::getLat))
                .orElseThrow(NoSuchElementException::new).getLat();
        double maxLat = locations.stream().max(Comparator.comparing(Coordinate::getLat))
                .orElseThrow(NoSuchElementException::new).getLat();

        return ((lng >= minLng && lng <= maxLng) || (lat >= minLat && lat <= maxLat ));
    }

    public double distanceTo(LngLat lngLat) {
        return Math.hypot(lngLat.lat - lat, lngLat.lng - lng);
    }

    public boolean closeTo(LngLat lngLat) {
        return this.distanceTo(lngLat) < 0.00015;
    }

    public LngLat nextPosition(Direction direction){
        double angleRadians = Math.toRadians(direction.angle);
        double destinationLng = lng + Math.cos(angleRadians) * 0.00015;
        double destinationLat = lat + Math.sin(angleRadians) * 0.00015;
        LngLat lngLat = new LngLat(destinationLng, destinationLat);
        return lngLat;
    }
};
