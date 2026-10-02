package projectcsi.reservoir;

public class Favorite {

    private String eventName;
    private String location;
    private String date;
    private String time;
    private double price;

    public Favorite(
            String eventName,
            String location,
            String date,
            String time,
            double price) {

        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public String getEventName() {
        return eventName;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }
}