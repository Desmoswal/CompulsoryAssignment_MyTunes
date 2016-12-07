/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import mytunes.BE.Library;
import mytunes.BE.Song;
import mytunes.DAL.FileManager;
import mytunes.DAL.TextFileHandler;
import java.util.List;

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
    
}
