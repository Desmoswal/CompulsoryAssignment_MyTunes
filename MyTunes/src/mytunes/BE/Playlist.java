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
    private int size;
    private String alltime = "";

    public Playlist(String name, ArrayList<Song> songs)
    {
        this.name = name;
        this.songs = songs;
        this.size = songs.size();
    }

    public String getName()
    {
        return name;
    }
    
    public void addSong(Song song) {
        songs.add(song);
        size = songs.size();
    }
    
    public void removeSong(Song song) {
        songs.remove(song);
    }
    
    public int getSize() {
        return size;
    }

    public ArrayList<Song> getSongs()
    {
        return songs;
    }
    
    private void updateTime() {
        alltime = "";
    }
    
    public String getAlltime() {
        return alltime;
    }
    
    public void setName(String name)
    {
        this.name = name;
    } 
}
