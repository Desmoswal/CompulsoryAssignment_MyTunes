/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.util.ArrayList;

/**
 *  Contains an ArrayList of songs
 * Every song is going to be added to this library
 * @author KDM
 */
public class SongLibrary {
    private ArrayList<Song> songlist;
    private static SongLibrary libSong;
    
    private SongLibrary() {
        this.songlist = new ArrayList<>();
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
    
    public static void createInstance() {
        if(libSong == null) {
            libSong = new SongLibrary();
        }
    }
    
    public static SongLibrary getInstance() {
        return libSong;
    }
}
