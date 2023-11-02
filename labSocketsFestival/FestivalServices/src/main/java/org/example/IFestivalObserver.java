package org.example;

import java.rmi.Remote;
import java.util.List;

public interface IFestivalObserver extends Remote{
    void updateLista(List<Concert> concerts, List<Artist> artists) throws FestivalException;
}
