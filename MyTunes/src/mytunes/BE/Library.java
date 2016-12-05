/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.util.ArrayList;
import java.io.Serializable;

/**
 *
 * @author KDM
 */
public class Library implements Serializable{
    private ArrayList<Song> songlist = new ArrayList<>();
    
    public Library() {
        songlist.add(new Song("../fasz.mp3","Artist","Title","0.00",1));
        
    }
    
    public ArrayList<Song> getSongList() {
        return songlist;
    }
    
    public void addSong(Song song) {
        songlist.add(song);
    }
    
    public void removeSong(Song song) {
        songlist.remove(song);
    }
    
}
