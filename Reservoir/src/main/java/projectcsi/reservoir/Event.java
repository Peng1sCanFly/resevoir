package projectcsi.reservoir;

public class Event {

    private String eventName;
    private String description;
    private String location;
    private String date;
    private String time;
    private int capacity;
    private double price;

    public Event(
            String eventName,
            String description,
            String location,
            String date,
            String time,
            int capacity,
            double price) {

        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.price = price;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
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

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        return price;
    }
}