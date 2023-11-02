package org.example.dto;

import org.example.Artist;
import org.example.Concert;

import java.io.Serializable;
import java.util.List;

public class ListDTO implements Serializable {
    private List<Concert> concerts;

    private List<Artist> artists;

    public ListDTO(List<Concert> concerts,List<Artist> artists) {
        this.concerts = concerts;
        this.artists=artists;
    }

    public List<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(List<Concert> concerts) {
        this.concerts=concerts;
    }

    public void setArtists(List<Artist> artists){this.artists=artists;}

    public List<Artist> getArtists(){return artists;}

}
