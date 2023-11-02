package org.example.jdbc;

import org.example.Concert;
import org.example.ConcertRepository;
import org.example.exceptions.RepositoryException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ConcertDBRepository implements ConcertRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ConcertDBRepository(JdbcUtils jdbcUtils){
        logger.info("Initializing ConcertDBRepository with {}",jdbcUtils);
        dbUtils=jdbcUtils;
    }

    @Override
    public Concert findOne(Integer integer) {
        logger.traceEntry("find concert by id task{}");
        Connection con=dbUtils.getConnection();
        Concert concert=null;
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from concerts where id=?")){
            preparedStatement.setInt(1,integer);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    concert=new Concert();
                    concert.setId(resultSet.getInt("id"));
                    concert.setConcertDate(resultSet.getString("concertDate"));
                    concert.setConcertLocation(resultSet.getString("concertLocation"));
                    concert.setTicketsAvailable(resultSet.getInt("ticketsAvailable"));
                    concert.setTicketsSold(resultSet.getInt("ticketsSold"));
                    concert.setArtistId(resultSet.getInt("artistID"));
                    concert.setStartingTime(resultSet.getString("startingTime"));
                }
            }
        }
        catch(SQLException e){
            logger.error(e);
            System.err.println("ERROR DB"+ e);
        }
        logger.traceExit(concert);
        if(concert ==null)
            throw new RepositoryException("no concert has been found with the given id!");
        return concert;
    }

    @Override
    public Iterable<Concert> findAll() {
        logger.traceEntry("get all concerts task{}");
        Connection con= dbUtils.getConnection();
        List<Concert> concerts=new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from concerts")){
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Concert concert=new Concert();
                    concert.setId(resultSet.getInt("id"));
                    concert.setConcertDate(resultSet.getString("concertDate"));
                    concert.setConcertLocation(resultSet.getString("concertLocation"));
                    concert.setTicketsAvailable(resultSet.getInt("ticketsAvailable"));
                    concert.setTicketsSold(resultSet.getInt("ticketsSold"));
                    concert.setArtistId(resultSet.getInt("artistID"));
                    concert.setStartingTime(resultSet.getString("startingTime"));
                    concerts.add(concert);
                }
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("ERROR DB"+ e);
        }
        logger.traceExit(concerts);
        return concerts;

    }

    @Override
    public Concert save(Concert entity) {
        return null;
    }

    @Override
    public Concert delete(Integer integer) {
        return null;
    }

    @Override
    public Concert update(Concert entity) {
        return null;
    }

    @Override
    public Collection<Concert> getConcertsByDate(String date) {
        logger.traceEntry("get concerts by date task {}");
        Connection con= dbUtils.getConnection();
        List<Concert> concerts=new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from concerts where concertDate=?")){
            preparedStatement.setString(1,date);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Concert concert=new Concert();
                    concert.setId(resultSet.getInt("id"));
                    concert.setConcertDate(resultSet.getString("concertDate"));
                    concert.setConcertLocation(resultSet.getString("concertLocation"));
                    concert.setTicketsAvailable(resultSet.getInt("ticketsAvailable"));
                    concert.setTicketsSold(resultSet.getInt("ticketsSold"));
                    concert.setArtistId(resultSet.getInt("artistID"));
                    concert.setStartingTime(resultSet.getString("startingTime"));
                    concerts.add(concert);
                }
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("ERROR DB" + e);
        }
        logger.traceExit(concerts);
        if(concerts.size()==0)
            throw new RepositoryException("no concerts have the given date");
        return concerts;
    }

    @Override
    public Integer updateConcertTickets(Integer id, Integer newTicketsAvailable,Integer newTicketsSold) {
        logger.traceEntry("update concert tickets by id {}");
        Connection con=dbUtils.getConnection();
        int result=0;
        try(PreparedStatement preparedStatement=con.prepareStatement("update concerts set ticketsAvailable=?,ticketsSold=? where id=?")){
            preparedStatement.setInt(1,newTicketsAvailable);
            preparedStatement.setInt(2,newTicketsSold);
            preparedStatement.setInt(3,id);
            result=preparedStatement.executeUpdate();
            logger.trace("updated {} objects",result);
        }catch(SQLException e){
            logger.error(e);
            System.err.println("ERROR DB"+ e);
        }
        if(result==0)
            throw new RepositoryException("no concert with the id given");
        return result;
    }

    @Override
    public Concert getConcertByDateLocationStart(String data, String location,String startTime) {
        logger.traceEntry("get concert by date,location and start time");
        Connection con=dbUtils.getConnection();
        Concert concert=new Concert();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from concerts where concertDate=? and concertLocation=? and startingTime=?")){
            preparedStatement.setString(1,data);
            preparedStatement.setString(2,location);
            preparedStatement.setString(3,startTime);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    concert.setId(resultSet.getInt("id"));
                    concert.setConcertDate(resultSet.getString("concertDate"));
                    concert.setConcertLocation(resultSet.getString("concertLocation"));
                    concert.setTicketsAvailable(resultSet.getInt("ticketsAvailable"));
                    concert.setTicketsSold(resultSet.getInt("ticketsSold"));
                    concert.setArtistId(resultSet.getInt("artistID"));
                    concert.setStartingTime(resultSet.getString("startingTime"));
                }
            }
        }catch(SQLException e){
            logger.error(e);
            System.out.println("ERROR DB "+e);
        }
        logger.traceExit(concert);
        if(concert==null)
            throw new RepositoryException("no concert with date,location and start time given");
        return concert;
    }
}

