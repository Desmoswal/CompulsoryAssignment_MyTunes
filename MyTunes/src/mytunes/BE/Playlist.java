/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.util.ArrayList;

/**
 *
 * @author Kristof
 */
public class Playlist {
    private String name;
    private ArrayList<Song> songs;

    public Playlist(String name, ArrayList<Song> songs)
    {
        this.name = name;
        this.songs = songs;
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Song> getSongs()
    {
        return songs;
    }
 
    
    
}
