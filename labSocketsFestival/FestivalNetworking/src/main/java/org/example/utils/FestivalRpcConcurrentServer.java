package org.example.utils;

import org.example.IFestivalService;
import org.example.rpcprotocol.ClientRpcReflectionWorker;

import java.net.Socket;

public class FestivalRpcConcurrentServer extends AbsConcurrentServer{

    private IFestivalService festivalService;

    public FestivalRpcConcurrentServer(int port,IFestivalService festivalService) {
        super(port);
        this.festivalService=festivalService;
        System.out.println("Festival- FestivalRpcConcurrentServer ");
    }

    @Override
    protected Thread createWorker(Socket client){
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(festivalService, client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services...");
    }

}
