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
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.BE.PlaylistLibrary;

/**
 * FXML Controller class
 *
 * @author Kristof
 */
public class DeletePlaylistPopupController implements Initializable {

    @FXML
    private Button btnDeletePlaylistYes;
    @FXML
    private Button btnDeletePlaylistNo;
    
    private Playlist selectedPlaylist;
    private PlaylistLibrary libPl = PlaylistLibrary.getInstance();
    private FXMLDocumentController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void deletePlaylist(ActionEvent event) 
    {
        if(selectedPlaylist != null)
        {
            libPl.removePlaylist(selectedPlaylist);
        }
        
        if(mainController != null)
        {
            mainController.updatePlaylistTable();
        }
        Stage stage = (Stage) btnDeletePlaylistYes.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void closeWindow(ActionEvent event) 
    {
        Stage stage = (Stage) btnDeletePlaylistNo.getScene().getWindow();
        stage.close();
    }
    
    public void setSelectedPlaylist(Playlist selectedPlaylist)
    {
        this.selectedPlaylist = selectedPlaylist;
    }
    
    public void setController(FXMLDocumentController controller)
    {
        mainController = controller;
    }
}
