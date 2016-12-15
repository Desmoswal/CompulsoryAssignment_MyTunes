/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;
import java.util.ArrayList;

/**
 * Stores information about the PlaylistLibrary
 * which contains every playlist.
 * @author Kristof
 */
public class PlaylistLibrary {
    private ArrayList<Playlist> playlistList;
    private static PlaylistLibrary libPl;
    
    private PlaylistLibrary() {
        this.playlistList = new ArrayList<>();
    }
    
    public ArrayList<Playlist> getPlaylists() {
        return playlistList;
    }
    
    public void addPlaylist(Playlist playlist) {
        playlistList.add(playlist);
    }
    
    public void removePlaylist(Playlist playlist) {
        playlistList.remove(playlist);
    }
    
    public static void createInstance() {
        if(libPl == null) {
            libPl = new PlaylistLibrary();
        }
    }
    public static PlaylistLibrary getInstance() {
        return libPl;
    }
}
