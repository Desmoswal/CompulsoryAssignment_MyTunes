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
import mytunes.BE.Song;
import mytunes.BE.SongLibrary;

/**
 * FXML Controller class
 *
 * @author Kristof
 */
public class DeleteSongPopupController implements Initializable {

    @FXML
    private Button btnYesDeleteSong;
    @FXML
    private Button btnNoDeleteSong;
    
    private Song selectedSong;
    private SongLibrary libSong = SongLibrary.getInstance();
    private FXMLDocumentController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void deleteSong(ActionEvent event) {
        if(selectedSong != null)
            libSong.removeSong(selectedSong);
        if(mainController != null)
        {
            mainController.updateSongTable();
            mainController.updatePlaylistTable();
        }
        Stage stage = (Stage)btnNoDeleteSong.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage)btnNoDeleteSong.getScene().getWindow();
        stage.close();
    }
    
    public void setSelected(Song selected) {
        this.selectedSong = selected;
    }
    
    public void setController(FXMLDocumentController con) {
        this.mainController = con;
    }
}
