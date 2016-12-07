/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Kristof
 */
public class PlaylistLibrary implements Serializable {
    private ArrayList<Playlist> playlistList = new ArrayList<>();
    
    public ArrayList<Playlist> getPlaylists() {
        return playlistList;
    }
    
    public void addPlaylist(Playlist playlist) {
        playlistList.add(playlist);
    }
    
    public void removePlaylist(Playlist playlist) {
        playlistList.remove(playlist);
    }
}
