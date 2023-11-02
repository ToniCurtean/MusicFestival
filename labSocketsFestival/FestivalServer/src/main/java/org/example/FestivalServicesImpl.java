package org.example;

import org.example.jdbc.ArtistDBRepository;
import org.example.jdbc.CashierDBRepository;
import org.example.jdbc.ConcertDBRepository;
import org.example.jdbc.OrderDBRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FestivalServicesImpl implements  IFestivalService{

    private ArtistDBRepository artistDBRepository;

    private CashierDBRepository cashierDBRepository;

    private ConcertDBRepository concertDBRepository;

    private OrderDBRepository orderDBRepository;

    private Map<String,IFestivalObserver> loggedCashiers;

    private final int defaultThreadsNo=5;

    public FestivalServicesImpl(ArtistDBRepository artistDBRepository, CashierDBRepository cashierDBRepository, ConcertDBRepository concertDBRepository, OrderDBRepository orderDBRepository) {
        this.artistDBRepository = artistDBRepository;
        this.cashierDBRepository = cashierDBRepository;
        this.concertDBRepository = concertDBRepository;
        this.orderDBRepository = orderDBRepository;
        loggedCashiers=new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(String username, String passowrd, IFestivalObserver client) throws FestivalException{
        Cashier cashier=cashierDBRepository.getCashierByUserPassword(username,passowrd);
        if(cashier!=null){
            loggedCashiers.put(cashier.getUsername(),client);
        }else
            throw new FestivalException("Couldn't log in");
    }

    @Override
    public Artist findOne(Integer id){
        return artistDBRepository.findOne(id);
    }

    @Override
    public Iterable<Concert> findAllConcerts() {
        return concertDBRepository.findAll();
    }

    @Override
    public Collection<Concert> getConcertsByDate(String date) {
        return concertDBRepository.getConcertsByDate(date);
    }

    @Override
    public Concert getConcertByDateLocationStart(String date, String location, String startTime) {
        return concertDBRepository.getConcertByDateLocationStart(date,location,startTime);
    }

    @Override
    public Concert updateConcertTickets(Integer id, Integer numberOfTickets) {
        Concert concert=concertDBRepository.findOne(id);
        int newTicketsAvailable=concert.getTicketsAvailable()-numberOfTickets;
        int newTicketsSold=concert.getTicketsSold()+numberOfTickets;
        concertDBRepository.updateConcertTickets(id,newTicketsAvailable,newTicketsSold);
        return concertDBRepository.findOne(id);
    }

    @Override
    public void save(Integer concertID, String buyerName, Integer numberOfTickets) {
        Order order=new Order(concertID,buyerName,numberOfTickets);
        Concert concert=concertDBRepository.findOne(order.getConcertId());
        concertDBRepository.updateConcertTickets(concert.getId(),concert.getTicketsAvailable()-numberOfTickets, concert.getTicketsSold()+numberOfTickets);

        orderDBRepository.save(order);

        Iterable<Cashier> cashiers= cashierDBRepository.findAll();

        List<Artist> artists=new ArrayList<>();
        for(Concert c:concertDBRepository.findAll()){
            Artist artist=artistDBRepository.findOne(c.getArtistId());
            artists.add(artist);
        }
        ExecutorService executor=Executors.newFixedThreadPool(defaultThreadsNo);
        for(Cashier cashier:cashiers){
            IFestivalObserver client=loggedCashiers.get(cashier.getUsername());
            System.out.println("SUNT AICII!!");
            if(client!=null)
                executor.execute(() -> {
                    try {
                        client.updateLista((List<Concert>) concertDBRepository.findAll(),(List<Artist>)artists);
                    } catch (FestivalException e) {
                        System.err.println("Error notifying cashier " + e);
                    }
            });
        }
        executor.shutdown();
    }

    public synchronized void logout(Cashier cashier, IFestivalObserver client) throws FestivalException{
        IFestivalObserver localClient=loggedCashiers.remove(cashier.getUsername());
        if (localClient==null)
            throw new FestivalException("User "+cashier.getId().toString()+" is not logged in.");
    }
}
