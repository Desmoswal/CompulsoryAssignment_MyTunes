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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import mytunes.BE.*;
import mytunes.BE.PlaylistLibrary;

/**
 * FXML Controller class
 *
 * @author Kristof
 */
public class NewPlaylistPopupController implements Initializable {
    PlaylistLibrary libPl;
    SongLibrary libSong;

    @FXML
    private Button btnCreatePlaylist;
    @FXML
    private TextField txtNewPlaylistName;
    @FXML
    private Label labNewPlaylistError;
    
    private FXMLDocumentController mainController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        libPl = PlaylistLibrary.getInstance();
        libSong = SongLibrary.getInstance();
    }    

    @FXML
    private void createPlaylist(ActionEvent event) {
        if(!txtNewPlaylistName.getText().isEmpty()) {
            libPl.addPlaylist(new Playlist(txtNewPlaylistName.getText(),libSong.getSongList()));
            
            if(mainController != null)
            {
                mainController.updatePlaylistTable();
            }
            
            Stage curStage = (Stage) btnCreatePlaylist.getScene().getWindow();
            curStage.close();
        } else {
            labNewPlaylistError.setText("Please input a name for the playlist.");
        }
    }
    
    public void setController(FXMLDocumentController controller)
    {
        mainController = controller;
    }
}
