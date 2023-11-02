package org.example.rpcprotocol;

public enum ResponseType {

    OK,
    ERROR,
    GET_CONCERTE,
    GET_CONCERTE_DATA,
    ORDER_ADDED,

    GET_CONCERT_DLS,

    UPDATED_CONCERT_TICKETS,
    GET_ARTIST,
    UPDATE;


    private ResponseType(){

    }
}
