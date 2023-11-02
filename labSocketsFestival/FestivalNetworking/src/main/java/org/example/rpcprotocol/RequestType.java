package org.example.rpcprotocol;

public enum RequestType {

    LOGIN,
    LISTA_CONCERTE,

    LISTA_CONCERTE_DATA,

    ADD_ORDER,

    FIND_CONCERT_DLS,

    UPDATE_CONCERT_TICKETS,
    FIND_ARTIST,
    LOGOUT;

    private RequestType(){

    }

}
