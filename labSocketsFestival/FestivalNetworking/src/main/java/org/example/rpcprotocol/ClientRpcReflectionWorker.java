package org.example.rpcprotocol;

import org.example.*;
import org.example.dto.CashierDTO;
import org.example.dto.DTOUtils;
import org.example.dto.ListDTO;
import org.example.dto.OrderDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class ClientRpcReflectionWorker implements Runnable, IFestivalObserver {

    private IFestivalService server;

    private Socket connection;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    private volatile boolean connected;

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    public ClientRpcReflectionWorker(IFestivalService server, Socket connection) {
        this.server = server;
        this.connection = connection;

        try {
            this.output = new ObjectOutputStream(connection.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(connection.getInputStream());
            this.connected = true;
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(this.connected) {
            try {
                Object request = this.input.readObject();
                Response response = this.handleRequest((Request)request);
                if (response != null) {
                    this.sendResponse(response);
                }
            } catch (IOException var4) {
                var4.printStackTrace();
            } catch (ClassNotFoundException var5) {
                var5.printStackTrace();
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }

        try {
            this.input.close();
            this.output.close();
            this.connection.close();
        } catch (IOException var6) {
            System.out.println("Error " + var6);
        }
    }


    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        this.output.writeObject(response);
        this.output.flush();
    }

    private Response handleRequest(Request request){
        Response response = null;
        String handlerName = "handle_" + request.type();
        System.out.println("HandlerName " + handlerName);

        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response)method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException var5) {
            var5.printStackTrace();
        }
        return response;
    }

    private Response handle_LOGIN(Request request){
        System.out.println("Login request..."+request.type());
        CashierDTO dto= (CashierDTO)request.data();
        Cashier cashier= DTOUtils.getFromDTO(dto);
        try{
            this.server.login(cashier.getUsername(), cashier.getPassword(), this);
            return okResponse;
        }catch(Exception e){
            this.connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_ADD_ORDER(Request request){
        System.out.println("Add order request..."+request.type());
        OrderDTO orderDTO=(OrderDTO) request.data();
        try{
            this.server.save(orderDTO.getConcertId(), orderDTO.getBuyerName(), orderDTO.getNumberOfTickets());
            return okResponse;
            //return (new Response.Builder()).type(ResponseType.ORDER_ADDED).data(new Order(orderDTO.getConcertId(), orderDTO.getBuyerName(), orderDTO.getNumberOfTickets())).build();
        } catch (Exception var4) {
            return (new Response.Builder()).type(ResponseType.ERROR).data(var4.getMessage()).build();
        }
    }

    private Response handle_LISTA_CONCERTE(Request request){
        System.out.println("LITSA CONCERTE..."+request.type());
        try{
            Iterable<Concert> concerts=server.findAllConcerts();
            int nr=0;
            for(Concert concert:concerts)
                nr+=1;
            Concert[] concertList=new Concert[nr];
            nr=0;
            for(Concert concert: concerts){
                concertList[nr]=concert;
                nr+=1;
            }
            return new Response.Builder().type(ResponseType.GET_CONCERTE).data(concertList).build();
        }catch(Exception e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_LISTA_CONCERTE_DATA(Request request){
        String data=(String) request.data();
        System.out.println("LISTA CONCERTE DATA REQUEST...");
        try{
            Iterable<Concert> concertsData=server.getConcertsByDate(data);
            int nr=0;
            for(Concert concert:concertsData)
                nr+=1;
            Concert[] concertList=new Concert[nr];
            nr=0;
            for(Concert concert: concertsData){
                concertList[nr]=concert;
                nr+=1;
            }
            return new Response.Builder().type(ResponseType.GET_CONCERTE_DATA).data(concertList).build();
        }catch(Exception e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_FIND_ARTIST(Request request){
        Integer id=(Integer) request.data();
        try{
            Artist artist=server.findOne(id);
            return new Response.Builder().type(ResponseType.GET_ARTIST).data(artist).build();
        }catch(Exception e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_FIND_CONCERT_DLS(Request request){
        String[] keywords=(String[]) request.data();
        try{
            Concert concert=server.getConcertByDateLocationStart(keywords[0],keywords[1],keywords[2]);
            return new Response.Builder().type(ResponseType.GET_CONCERT_DLS).data(concert).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handle_LOGOUT(Request request){
        System.out.println("Logout request...");
        CashierDTO dto=(CashierDTO)request.data();
        Cashier user=DTOUtils.getFromDTO(dto);
        try {
            server.logout(user, this);
            connected=false;
            return okResponse;

        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateLista(List<Concert> concerts,List<Artist> artists) throws FestivalException {
        System.out.println("inside orderAdded");
        Response response=(new Response.Builder()).type(ResponseType.UPDATE).data(new ListDTO((List<Concert>) concerts,(List<Artist>) artists)).build();
        try{
            this.sendResponse(response);
        }catch(IOException e){
            throw new FestivalException("Sending error: "+e);
        }
    }
}
