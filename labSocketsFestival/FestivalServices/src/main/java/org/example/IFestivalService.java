package org.example;

import java.util.Collection;

public interface IFestivalService {
    public void login(String username,String passowrd,IFestivalObserver festivalObserver) throws FestivalException, Exception;

    public Artist findOne(Integer id) throws Exception;

    public Iterable<Concert> findAllConcerts() throws Exception;

    public Collection<Concert> getConcertsByDate(String date) throws Exception;

    public Concert getConcertByDateLocationStart(String date,String location,String startTime) throws Exception;

    public Concert updateConcertTickets(Integer id, Integer numberOfTickets) throws Exception;

    public void save(Integer concertID,String buyerName, Integer numberOfTickets) throws Exception;

    void logout(Cashier cashier,IFestivalObserver client) throws Exception;
}
