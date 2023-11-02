package org.example;


import java.util.Objects;

public class Order extends Entity<Integer> {

    private Integer concertId;

    private String buyerName;
    private Integer numberOfTickets;

    public Order(Integer concertId, String buyerName, Integer numberOfTickets) {
        this.concertId = concertId;
        this.buyerName = buyerName;
        this.numberOfTickets = numberOfTickets;
    }

    public Integer getConcertId() {
        return concertId;
    }

    public void setConcertId(Integer concertId) {
        this.concertId = concertId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return concertId.equals(order.concertId) && buyerName.equals(order.buyerName) && numberOfTickets.equals(order.numberOfTickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concertId, buyerName, numberOfTickets);
    }
}
