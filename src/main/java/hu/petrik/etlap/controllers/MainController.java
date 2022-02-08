package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.Etlap;
import hu.petrik.etlap.EtlapDB;
import hu.petrik.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class MainController extends Controller {
    @FXML
    private TableView<Etlap> etlapTableView;
    @FXML
    private TableColumn<Etlap, String> colKategoria;
    @FXML
    private TableColumn<Etlap, Integer> colAr;
    @FXML
    private TableColumn<Etlap, String> colNev;
    @FXML
    private TextArea textAreaEtelLeiras;
    @FXML
    private Spinner<Integer> spinnerForintNoveles;
    @FXML
    private Spinner<Integer> sppinnerSzazalekNoveles;
    @FXML
    private TableView<Kategoria> tableViewKategoria;
    @FXML
    private TableColumn<Kategoria, String> colKategoriaCol;
    private EtlapDB db;
    private List<Kategoria> kategoriaList;


    public void initialize() {

        try {
            db = new EtlapDB();
            kategoriaList = db.getKategoria();
        } catch (SQLException e) {
            hibaKiir(e);
        }
        colNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        colAr.setCellValueFactory(new PropertyValueFactory<>("ar"));
        colKategoriaCol.setCellValueFactory(new PropertyValueFactory<>("nev"));
        try {
            db = new EtlapDB();
            etlapUjratoltese();
            kategoriaUjratoltese();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void ujFelveteleButtonClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("hozzaad-view.fxml", "Étel hozzáadása", 320, 300);
            hozzadas.getStage().setOnCloseRequest(event -> etlapUjratoltese());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void etlapTorlesButtonClick(ActionEvent actionEvent) {
        int selectedIndex = etlapTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Etlap torlendoEtel = etlapTableView.getSelectionModel().getSelectedItem();
        if (!confirm("Biztosan törölni szeretnéd az étlapról:" + torlendoEtel.getNev())) {
            return;
        }
        try {
            db.etelTorlese(torlendoEtel.getId());
            alert("Sikeres törlés");
            etlapUjratoltese();
            textAreaEtelLeiras.setText("");
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void emelesSzazalekButtonClick(ActionEvent actionEvent) {
        int emeles = 0;
        try {
            emeles = sppinnerSzazalekNoveles.getValue();
        } catch (NullPointerException e) {
            alert("Az emeléshez a százalék megadása kötelező");
            return;
        } catch (Exception e) {
            alert("Az ár 5 és 50 közötti szám lehet");
            return;
        }
        if (emeles < 5 || emeles > 50) {
            alert("Az ár 5 és 50 közötti szám lehet");
            return;
        }

        Etlap emelesEtel = etlapTableView.getSelectionModel().getSelectedItem();

        if (!confirm("Biztos szeretné emelni a " + emelesEtel.getNev() + " árát?")) {
            return;
        }
        try {
            if (db.etelEmelesSzazalek(emelesEtel.getId(), emeles)) {
                alertWait("Emelés: SIKERES");
                etlapUjratoltese();
                textAreaEtelLeiras.setText("");
            } else {
                alert("Emelés: SIKERTELEN");
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void emelesForintButtonClick(ActionEvent actionEvent) {
        int emeles = 0;
        try {
            emeles = spinnerForintNoveles.getValue();
        } catch (NullPointerException e) {
            alert("Az emeléshez az ár megadása kötelező");
            return;
        } catch (Exception e) {
            alert("Az ár 50 és 3000 közötti szám lehet");
            return;
        }
        if (emeles < 50 || emeles > 3000) {
            alert("Az ár 50 és 3000 közötti szám lehet");
            return;
        }

        Etlap emelesEtel = etlapTableView.getSelectionModel().getSelectedItem();

        if (!confirm("Biztos szeretné emelni a " + emelesEtel.getNev() + " árát?")) {
            return;
        }
        try {
            if (db.etelEmelesForint(emelesEtel.getId(), emeles)) {
                alertWait("Emelés: SIKERES");
                etlapUjratoltese();
                textAreaEtelLeiras.setText("");
            } else {
                alert("Emelés: SIKERTELEN");
            }

        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void eventEtelClick(MouseEvent event) {
        int selectedIndex = etlapTableView.getSelectionModel().getSelectedIndex();
        if (!(selectedIndex == -1)) {
            Etlap kiirandoLeiras = etlapTableView.getSelectionModel().getSelectedItem();
            textAreaEtelLeiras.setText(kiirandoLeiras.getLeiras());
        }
    }

    private void etlapUjratoltese() {
        try {
            List<Etlap> etlapLista = db.getEtlap();
            etlapTableView.getItems().clear();
            for (Etlap etlap : etlapLista) {
                etlapTableView.getItems().add(etlap);
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onHozzaadasClick(ActionEvent actionEvent) {
        try {
            Controller kategoriaHozzadas = ujAblak("kategoriahozzaad-view.fxml", "Kategória hozzáadása", 320, 300);
            kategoriaHozzadas.getStage().setOnCloseRequest(event -> kategoriaUjratoltese());
            kategoriaHozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void kategoriaTorlesButtonClick(ActionEvent actionEvent) {
        int selectedIndex = tableViewKategoria.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Kategoria torlendoKategoria = tableViewKategoria.getSelectionModel().getSelectedItem();
        if (!confirm("Biztosan törölni szeretnéd a kategóriák közül: " + torlendoKategoria.getNev())) {
            return;
        }
        try {
            db.kategoriaTorlese(torlendoKategoria.getId());
            alert("Sikeres törlés");
            kategoriaUjratoltese();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    private void kategoriaUjratoltese() {
        try {
            List<Kategoria> kategoriaLista = db.getKategoria();
            tableViewKategoria.getItems().clear();
            for (Kategoria kategoria : kategoriaLista) {
                tableViewKategoria.getItems().add(kategoria);
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }
}