/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

/**
 *
 * @author Kristof
 */
public class Song {
    private String path;
    private String artist;
    private String title;
    private String time;

    public Song(String path, String artist, String title, String time)
    {
        this.path = path;
        this.artist = artist;
        this.title = title;
        this.time = time;
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
    
    
}
