package org.example.dto;

import org.example.Entity;

public class ConcertDTO extends Entity<Integer> {
    private String concertDate;
    private String concertLocation;
    private Integer ticketsAvailable;
    private Integer ticketsSold;
    private Integer artistId;

    private String startingTime;

    public ConcertDTO(String concertDate, String concertLocation, Integer ticketsAvailable, Integer ticketsSold, Integer artistId, String startingTime) {
        this.concertDate = concertDate;
        this.concertLocation = concertLocation;
        this.ticketsAvailable = ticketsAvailable;
        this.ticketsSold = ticketsSold;
        this.artistId = artistId;
        this.startingTime = startingTime;
    }

    public String getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(String concertDate) {
        this.concertDate = concertDate;
    }

    public String getConcertLocation() {
        return concertLocation;
    }

    public void setConcertLocation(String concertLocation) {
        this.concertLocation = concertLocation;
    }

    public Integer getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(Integer ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }
}
