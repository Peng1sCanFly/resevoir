
package projectcsi.reservoir;

public class Reservation {

    private int reservationId;
    private String eventName;
    private String location;
    private String date;
    private String time;
    private String endTime;
    private int numberOfTickets;
    private double pricePerTicket;
    private double totalCost;
    private String section;

    // Existing constructor without seating.
    public Reservation(
            String eventName,
            String location,
            String date,
            String time,
            int numberOfTickets,
            double pricePerTicket) {

        this(0, eventName, location, date, time,
                "", numberOfTickets, pricePerTicket, "");
    }

    // Existing constructor with seating.
    public Reservation(
            String eventName,
            String location,
            String date,
            String time,
            int numberOfTickets,
            double pricePerTicket,
            String section) {

        this(0, eventName, location, date, time,
                "", numberOfTickets, pricePerTicket, section);
    }

    // Constructor for reservations stored in MySQL.
    public Reservation(
            int reservationId,
            String eventName,
            String location,
            String date,
            String time,
            String endTime,
            int numberOfTickets,
            double pricePerTicket,
            String section) {

        this.reservationId = reservationId;
        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.endTime = endTime;
        this.numberOfTickets = numberOfTickets;
        this.pricePerTicket = pricePerTicket;
        this.totalCost = numberOfTickets * pricePerTicket;
        this.section = section;
    }

    public int getReservationId() {
        return reservationId;
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

    public String getEndTime() {
        return endTime;
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

    public boolean hasSection() {
        return section != null && !section.isEmpty();
    }
}
