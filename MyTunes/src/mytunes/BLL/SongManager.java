/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import mytunes.BE.SongLibrary;
import mytunes.BE.Song;
import mytunes.DAL.FileManager;
import mytunes.DAL.TextFileHandler;
import java.util.List;
import mytunes.BE.Playlist;

/**
 *
 * @author Desmoswal
 */
public class SongManager
{
    private FileManager fileManager = new TextFileHandler();
    
    public void saveAll(List<Song> songList)
    {
        fileManager.saveAll(songList);
    }
    
    public List<Song> getAll()
    {
        return fileManager.getAll();
    }
    
    public void savePlaylists(List<Playlist> playList)
    {
        fileManager.savePlaylists(playList);
    }
    
    public List<Playlist> getPlaylists()
    {
        return fileManager.getPlaylists();
    }
    
}
