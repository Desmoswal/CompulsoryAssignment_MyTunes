/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import mytunes.BE.Song;
import java.util.List;
import mytunes.BE.Playlist;


/**
 *  Abstract class which helps dealing with saving and reading song libraries and playlists
 * @author Desmoswal
 */
public abstract class FileManager
{
    
    public FileManager()
    {
        
    }
    
    public abstract void saveAll(List<Song> songList);
    
    public abstract List<Song> getAll(); 
    
    public abstract void savePlaylists (List<Playlist> playList);
    
    public abstract List<Playlist> getPlaylists(); 
}