package org.example;


import java.util.Objects;

public class Artist extends Entity<Integer> {

    private String name;
    private String musicType;

    public Artist(String name,String musicType){
        this.name=name;
        this.musicType=musicType;
    }

    public Artist(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return name.equals(artist.name) && musicType.equals(artist.musicType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, musicType);
    }
}
