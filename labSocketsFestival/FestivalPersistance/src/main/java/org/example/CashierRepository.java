package org.example;

public interface CashierRepository extends Repository<Integer, Cashier>{

    Cashier getCashierByUserPassword(String username,String password);

}
