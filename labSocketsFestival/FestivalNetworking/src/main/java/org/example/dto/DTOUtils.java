package org.example.dto;

import org.example.Cashier;
import org.example.Concert;
import org.example.Order;

public class DTOUtils {
    public static Cashier getFromDTO(CashierDTO dto){
        String username=dto.getUsername();
        String password=dto.getPassword();
        Integer id=dto.getId();
        Cashier cashier=new Cashier(username,password);
        cashier.setId(id);
        return cashier;
    }

    public static CashierDTO getDTO(Cashier cashier){
        String username= cashier.getUsername();
        String password= cashier.getPassword();
        Integer id= cashier.getId();
        CashierDTO dto=new CashierDTO(username,password);
        dto.setId(id);
        return dto;
    }

    public static Concert getFromDTO(ConcertDTO dto){
        String concertDate=dto.getConcertDate();
        String concertLocation= dto.getConcertLocation();
        Integer ticketsAvailable=dto.getTicketsAvailable();
        Integer ticketsSold=dto.getTicketsSold();
        Integer artistId=dto.getArtistId();
        String startTime=dto.getStartingTime();
        Integer id=dto.getId();
        Concert concert=new Concert(concertDate,concertLocation,ticketsAvailable,ticketsSold,artistId,startTime);
        concert.setId(id);
        return concert;
    }

    public static ConcertDTO getDTO(Concert concert){
        String concertDate=concert.getConcertDate();
        String concertLocation= concert.getConcertLocation();
        Integer ticketsAvailable=concert.getTicketsAvailable();
        Integer ticketsSold=concert.getTicketsSold();
        Integer artistId=concert.getArtistId();
        String startTime=concert.getStartingTime();
        Integer id=concert.getId();
        ConcertDTO dto=new ConcertDTO(concertDate,concertLocation,ticketsAvailable,ticketsSold,artistId,startTime);
        dto.setId(id);
        return dto;
    }

    public static Order getFromDTO(OrderDTO dto){
        Integer concertId= dto.getConcertId();
        String buyer= dto.getBuyerName();
        Integer nrOfTickets= dto.getNumberOfTickets();
        Integer id=dto.getId();
        Order order=new Order(concertId,buyer,nrOfTickets);
        order.setId(id);
        return order;
    }

    public static OrderDTO getDTO(Order order){
        Integer concertId= order.getConcertId();
        String buyer= order.getBuyerName();
        Integer nrOfTickets= order.getNumberOfTickets();
        Integer id=order.getId();
        OrderDTO dto=new OrderDTO(concertId,buyer,nrOfTickets);
        order.setId(id);
        return dto;
    }


}

