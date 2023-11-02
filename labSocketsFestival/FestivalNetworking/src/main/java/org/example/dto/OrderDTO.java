package org.example.dto;

import org.example.Entity;

public class OrderDTO extends Entity<Integer> {
    private Integer concertId;
    private String buyerName;
    private Integer numberOfTickets;
    private Integer id;

    public OrderDTO(Integer concertId, String buyerName, Integer numberOfTickets) {
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
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
