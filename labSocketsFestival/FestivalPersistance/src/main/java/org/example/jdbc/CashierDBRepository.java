package org.example.jdbc;

import org.example.Artist;
import org.example.Cashier;
import org.example.CashierRepository;
import org.example.exceptions.RepositoryException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CashierDBRepository implements CashierRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public CashierDBRepository(JdbcUtils jdbcUtils) {
        logger.info("Initializing CashierRepository with: {} ",jdbcUtils);
        dbUtils=jdbcUtils;
    }

    @Override
    public Cashier findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Cashier> findAll() {
        Connection con= dbUtils.getConnection();
        List<Cashier> cashiers =new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from cashiers")){
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Cashier cashier=new Cashier();
                    int id=resultSet.getInt("id");
                    String name=resultSet.getString("username");
                    String password=resultSet.getString("password");
                    cashier.setId(id);
                    cashier.setUsername(name);
                    cashier.setPassword(password);
                    cashiers.add(cashier);
                }
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("ERROR DB"+ e);
        }
        logger.traceExit(cashiers);
        return cashiers;
    }

    @Override
    public Cashier save(Cashier entity) {
        return null;
    }

    @Override
    public Cashier delete(Integer integer) {
        return null;
    }

    @Override
    public Cashier update(Cashier entity) {
        return null;
    }

    @Override
    public Cashier getCashierByUserPassword(String username, String password) {
        logger.traceEntry("find by username and password task {}");
        Connection con= dbUtils.getConnection();
        Cashier cashier=null;
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from cashiers where username=? and password=?")){
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    cashier=new Cashier();
                    cashier.setUsername(resultSet.getString("username"));
                    cashier.setPassword(resultSet.getString("password"));
                    cashier.setId(resultSet.getInt("id"));
                }
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("ERROR DB"+ e);
        }
        logger.traceExit(cashier);
        if(cashier==null)
            throw new RepositoryException("no cashier has been found with the given username and password");
        return cashier;
    }
}
