package projectcsi.reservoir;

public class Reservation {

    private String eventName;
    private String location;
    private String date;
    private String time;
    private int numberOfTickets;
    private double pricePerTicket;
    private double totalCost;

    // Seating section is optional
    private String section;

    // =====================================================
    // CONSTRUCTOR WITHOUT SEATING
    // Restaurants, movies, community events, etc.
    // =====================================================

    public Reservation(
            String eventName,
            String location,
            String date,
            String time,
            int numberOfTickets,
            double pricePerTicket) {

        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.numberOfTickets = numberOfTickets;
        this.pricePerTicket = pricePerTicket;
        this.totalCost =
                numberOfTickets * pricePerTicket;

        this.section = "";
    }

    // =====================================================
    // CONSTRUCTOR WITH SEATING
    // Concerts and football
    // =====================================================

    public Reservation(
            String eventName,
            String location,
            String date,
            String time,
            int numberOfTickets,
            double pricePerTicket,
            String section) {

        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.numberOfTickets = numberOfTickets;
        this.pricePerTicket = pricePerTicket;
        this.totalCost =
                numberOfTickets * pricePerTicket;

        this.section = section;
    }

    // =====================================================
    // GETTERS
    // =====================================================

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

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public double getPricePerTicket() {
        return pricePerTicket;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getSection() {
        return section;
    }

    // =====================================================
    // CHECK IF RESERVATION HAS SEATING
    // =====================================================

    public boolean hasSection() {

        return section != null
                && !section.isEmpty();
    }
}