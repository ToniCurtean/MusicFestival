package org.example;


import java.io.Serializable;

public class Cashier extends Entity<Integer> implements Comparable<Cashier>, Serializable {

    private String username;
    private String password;

    public Cashier(){

    }

    public Cashier(String username,String password) {
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(Cashier o) {
        return username.compareTo(o.username);
    }
}
