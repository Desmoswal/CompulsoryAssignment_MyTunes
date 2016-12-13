/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.BE.PlaylistLibrary;
import mytunes.BE.Song;

/**
 * FXML Controller class
 *
 * @author Kristof
 */
public class EditPlaylistPopupController implements Initializable {

    @FXML
    private Button btnEditPlaylistSave;
    @FXML
    private TextField txtEditPlaylistName;
    
    private Playlist selectedPlaylist;
    
    private PlaylistLibrary libPl = PlaylistLibrary.getInstance();
    private FXMLDocumentController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void setSelected(Playlist selectedPlaylist) 
    {
        this.selectedPlaylist = selectedPlaylist;
        txtEditPlaylistName.setText(selectedPlaylist.getName());
    }
    
    @FXML
    private void savePlaylist(ActionEvent event) 
    {
        Playlist empty = new Playlist("", new ArrayList<Song>());
        selectedPlaylist = libPl.getPlaylists().get(libPl.getPlaylists().indexOf(selectedPlaylist));
        selectedPlaylist.setName(txtEditPlaylistName.getText());
        
        Stage stage = (Stage) txtEditPlaylistName.getScene().getWindow();
        stage.close();
        
        libPl.addPlaylist(empty);
        if(mainController != null)
        {
            mainController.updatePlaylistTable();
        }
        libPl.removePlaylist(empty);
        if(mainController != null)
        {
            mainController.updatePlaylistTable();
        }
    }
    
    public void setController(FXMLDocumentController controller)
    {
        mainController = controller;
    }
    
}
