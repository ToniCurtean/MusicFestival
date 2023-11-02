package org.example.rpcprotocol;

import org.example.*;
import org.example.dto.CashierDTO;
import org.example.dto.DTOUtils;
import org.example.dto.ListDTO;
import org.example.dto.OrderDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IFestivalService {

    private String host;

    private int port;

    private IFestivalObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qResponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host,int port){
        this.host=host;
        this.port=port;
        this.qResponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws Exception {
        try {
            this.connection = new Socket(this.host, this.port);
            this.output = new ObjectOutputStream(this.connection.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(this.connection.getInputStream());
            this.finished = false;
            this.startReader();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATE;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.closeConnection();
    }
    private void closeConnection() {
        this.finished = true;

        try {
            this.input.close();
            this.output.close();
            this.connection.close();
            this.client = null;
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
    private void handleUpdate(Response response) {    //todo
        if (response.type() == ResponseType.UPDATE) {
            System.out.println("Sunt in proxy si urmeaza sa apelez client.updated_Concert()");
            ListDTO dto=(ListDTO) response.data();
            try {
                this.client.updateLista(dto.getConcerts(),dto.getArtists());
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }
    private class ReaderThread implements Runnable {
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qResponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }



    @Override
    public void login(String username, String passowrd, IFestivalObserver festivalObserver) throws FestivalException, Exception {
        this.initializeConnection();
        CashierDTO dto= DTOUtils.getDTO(new Cashier(username,passowrd));
        Request req=new Request.Builder().type(RequestType.LOGIN).data(dto).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.OK){
            this.client=festivalObserver;
        }
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new FestivalException(err);
        }
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new FestivalException("Error sending object " + e);
        }
    }

    private Response readResponse() throws Exception {
        Response response = null;

        try {
            response=qResponses.take();
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

        return response;
    }

    @Override
    public Artist findOne(Integer id) throws Exception {
        Request req=new Request.Builder().type(RequestType.FIND_ARTIST).data(id).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.OK){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
        Artist artist=(Artist) response.data();
        return artist;
    }

    @Override
    public Iterable<Concert> findAllConcerts() throws Exception {
        Request req=new Request.Builder().type(RequestType.LISTA_CONCERTE).data(null).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
        Concert[] concerts=(Concert[]) response.data();
        return new ArrayList<>(Arrays.asList(concerts));
    }

    @Override
    public Collection<Concert> getConcertsByDate(String date) throws Exception {
        Request req=new Request.Builder().type(RequestType.LISTA_CONCERTE_DATA).data(date).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
        Concert[] concerts=(Concert[])response.data();
        return new ArrayList<>(Arrays.asList(concerts));
    }

    @Override
    public Concert getConcertByDateLocationStart(String date, String location, String startTime) throws Exception {
        Request req=new Request.Builder().type(RequestType.FIND_CONCERT_DLS).data(new String[]{date,location,startTime}).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
        Concert concert=(Concert) response.data();
        return concert;
    }

    @Override
    public Concert updateConcertTickets(Integer id, Integer numberOfTickets) throws Exception {
        Request req=new Request.Builder().type(RequestType.UPDATE_CONCERT_TICKETS).data(new Integer[]{id,numberOfTickets}).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
        Concert concert=(Concert) response.data();
        return concert;
    }

    @Override
    public void save(Integer concertID, String buyerName, Integer numberOfTickets) throws Exception {
        OrderDTO dto=DTOUtils.getDTO(new Order(concertID,buyerName,numberOfTickets));
        Request req=new Request.Builder().type(RequestType.ADD_ORDER).data(dto).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
    }

    @Override
    public void logout(Cashier cashier, IFestivalObserver client) throws Exception {
        CashierDTO dto=DTOUtils.getDTO(cashier);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(dto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
    }
}
