package payload;

public class BookingDates {
    private String checkin;
    private String checkout;

    // Getters and Setters
    public String getCheckin() {
        return checkin;
    }

    public BookingDates() {

    }

    public BookingDates(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }
}
