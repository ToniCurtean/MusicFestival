package org.example;

import org.example.jdbc.*;
import org.example.utils.AbstractServer;
import org.example.utils.FestivalRpcConcurrentServer;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/festivalserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find festivalserver.properties "+e);
            return;
        }
        JdbcUtils jdbcUtils=new JdbcUtils(serverProps);
        CashierDBRepository cashierDBRepository=new CashierDBRepository(jdbcUtils);
        ArtistDBRepository artistDBRepository=new ArtistDBRepository(jdbcUtils);
        ConcertDBRepository concertDBRepository=new ConcertDBRepository(jdbcUtils);
        OrderDBRepository orderDBRepository=new OrderDBRepository(jdbcUtils);

        IFestivalService festServerImpl=new FestivalServicesImpl(artistDBRepository,cashierDBRepository,concertDBRepository,orderDBRepository);
        int defaultPort = 55555;
        int festivalServerPort= defaultPort;
        try {
            festivalServerPort = Integer.parseInt(serverProps.getProperty("festival.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+ defaultPort);
        }
        System.out.println("Starting server on port: "+festivalServerPort);
        AbstractServer server = new FestivalRpcConcurrentServer(festivalServerPort, festServerImpl);
        try {
            server.start();
        } catch (org.example.utils.ServerException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.stop();
            } catch (org.example.utils.ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}