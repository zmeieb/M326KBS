package model;

        import java.util.ArrayList;
        import java.util.List;

public class Row {
    private String row;
    private List<Seat> seats = new ArrayList<>();

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
