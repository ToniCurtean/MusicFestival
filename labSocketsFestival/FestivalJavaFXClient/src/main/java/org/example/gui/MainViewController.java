package org.example.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable, IFestivalObserver{

    private IFestivalService server;

    public Cashier cashier;

    public MainViewController(){

    }

    public MainViewController(IFestivalService service) {
        this.server = service;
        System.out.println("constructor MainViewController cu server param");
    }

    public void setCashier(Cashier cashier){
        this.cashier=cashier;
    }

    public void setServer(IFestivalService server){
        this.server = server;
    }


    @FXML
    ListView<Text> concertsView;

    private final ObservableList<Text> concerts = FXCollections.observableArrayList();

    @FXML
    ListView<String> concertsDateView;

    private final ObservableList<String> concertsDate=FXCollections.observableArrayList();

    @FXML
    TextField dateText;

    @FXML
    TextField nameText;

    @FXML
    TextField numberText;

    @FXML
    Button searchButton;

    @FXML
    Button buyButton;

    @FXML
    Button logoutButton;

    public void init(){
        concertsView.setItems(concerts);
        concertsDateView.setItems(concertsDate);
        concertsDate.clear();
        concerts.clear();
        initViews();
    }

    @FXML
    private void onSearchButtonClick(ActionEvent actionEvent){
        concertsDate.clear();
        if(dateText.getText().strip().equals(""))
            return;
        try{
            for(Concert concert: server.getConcertsByDate(dateText.getText())){
                Artist artist= server.findOne(concert.getArtistId());
                concertsDate.add(artist.getName()+" "+concert.getConcertDate()+" "+concert.getConcertLocation()+" TA: "+concert.getTicketsAvailable().toString()+" TS: "+concert.getTicketsSold()+" "+concert.getStartingTime());
            }
        }catch(FestivalException e){
            UIAlert.showMessage(null, Alert.AlertType.INFORMATION,"","No concerts have the given date!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBuyButtonClick(ActionEvent actionEvent) {
        if(nameText.getText().equals("") || numberText.getText().equals("") || concertsView.getSelectionModel().getSelectedItem()==null)
            return;
        String selected=concertsView.getSelectionModel().getSelectedItem().getText();
        String[] data= selected.split("/");
        try{
            Concert concert= server.getConcertByDateLocationStart(data[1],data[2],data[5]);
            if(concert.getTicketsAvailable()<Integer.parseInt(numberText.getText())){
                UIAlert.showMessage(null, Alert.AlertType.INFORMATION,"","There aren't as many tickets available");
                return;
            }
            server.save(concert.getId(),nameText.getText(),Integer.parseInt(numberText.getText()));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void initViews(){
        concerts.clear();
        try{
            for(Concert concert: server.findAllConcerts()){
                Text text=new Text();
                Artist artist=null;
                try{
                    artist= server.findOne(concert.getArtistId());
                }catch(Exception e){
                }
                if(concert.getTicketsAvailable()==0){
                    text.setText(artist.getName()+"/"+concert.getConcertDate()+"/"+concert.getConcertLocation()+"/"+concert.getTicketsAvailable().toString()+"/"+concert.getTicketsSold().toString()+"/"+concert.getStartingTime());
                    text.setFill(Color.RED);
                    concerts.add(text);
                }
                else{
                    text.setText(artist.getName()+"/"+concert.getConcertDate()+"/"+concert.getConcertLocation()+"/"+concert.getTicketsAvailable().toString()+"/"+concert.getTicketsSold().toString()+"/"+concert.getStartingTime());
                    concerts.add(text);
                }
            }
        }catch(Exception e){

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INIT MAIN VIEW CONTROLLER!!!");
    }

    @FXML
    public void logOut(ActionEvent actionEvent){
        logout();
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }


    public void logout() {
        try {
            this.server.logout(this.cashier, this);
        } catch (Exception var2) {
            System.out.println("Logout error " + var2);
        }
    }

    @Override
    public void updateLista(List<Concert> concertsUpdated, List<Artist> artists) throws FestivalException {
        System.out.println("OBSERVER NOTIFIED!");
        Platform.runLater(()-> {
            concerts.clear();
            try {
                for (int i=0;i<concertsUpdated.size();i++) {
                    Text text = new Text();
                    if (concertsUpdated.get(i).getTicketsAvailable() == 0) {
                        text.setText(artists.get(i).getName() + "/" + concertsUpdated.get(i).getConcertDate() + "/" + concertsUpdated.get(i).getConcertLocation() + "/" + concertsUpdated.get(i).getTicketsAvailable().toString() + "/" + concertsUpdated.get(i).getTicketsSold().toString() + "/" + concertsUpdated.get(i).getStartingTime());
                        text.setFill(Color.RED);
                        concerts.add(text);
                    } else {
                        text.setText(artists.get(i).getName() + "/" + concertsUpdated.get(i).getConcertDate() + "/" + concertsUpdated.get(i).getConcertLocation() + "/" + concertsUpdated.get(i).getTicketsAvailable().toString() + "/" + concertsUpdated.get(i).getTicketsSold().toString() + "/" + concertsUpdated.get(i).getStartingTime());
                        concerts.add(text);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

