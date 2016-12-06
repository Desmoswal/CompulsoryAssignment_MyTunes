/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import mytunes.BE.Song;
import mytunes.BLL.FileSaver;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Desmoswal
 */
public class SerialFileHandler extends FileManager
{
    
    public SerialFileHandler()
    {
        fileName = fileName + ".ser";
    }

    @Override
    public void saveAll(List<Song> custList)
    {
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            oos.writeObject(custList);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Song> getAll()
    {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (List<Song>)ois.readObject();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
