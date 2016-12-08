/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mytunes.BE.Playlist;
import mytunes.BE.PlaylistLibrary;

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
    
    private Playlist selected;
    
    private PlaylistLibrary libPl = PlaylistLibrary.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtEditPlaylistName.setText(selected.getName());
    }    

    public void setSelected(Playlist selected) {
        this.selected = selected;
    }
    
    @FXML
    private void savePlaylist(ActionEvent event) {
        
    }
    
}
