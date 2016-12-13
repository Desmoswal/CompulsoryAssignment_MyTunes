/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.util.UUID;

/**
 *
 * @author Kristof
 */
public class Song {
    private String path;
    private String artist;
    private String title;
    private String time;
    private String genre;
    private final String uuid;

    public Song(String path, String artist, String title, String genre, String time)
    {
        this.path = path;
        this.artist = artist;
        this.title = title;
        this.time = time;
        this.genre = genre;
        this.uuid = UUID.randomUUID().toString();
    }
    
    public Song(String path, String artist, String title, String genre, String time, String uuid) {
        this.path = path;
        this.artist = artist;
        this.title = title;
        this.time = time;
        this.genre = genre;
        this.uuid = uuid;
    }

    public String getUUID() {
        return uuid;
    }

    public String getPath()
    {
        return path;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTime()
    {
        return time;
    }
    
    public String getGenre() {
        return genre;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }
    
}
