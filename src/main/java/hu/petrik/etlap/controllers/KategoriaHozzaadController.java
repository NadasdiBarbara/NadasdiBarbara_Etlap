package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.EtlapDB;
import hu.petrik.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class KategoriaHozzaadController extends Controller {

    @FXML
    private TextField txtFieldKategoria;

    private EtlapDB db;
    private List<Kategoria> kategoriaList;

    public void initialize() {
        try {
            db = new EtlapDB();
            kategoriaList = db.getKategoria();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void kattegoriaHOzzaadButtonClick(ActionEvent actionEvent) {
        String nev = txtFieldKategoria.getText().toString().trim();
        if (nev.isEmpty()) {
            alert("Név megadása kötelező");
            return;
        }

        try {
            int i = 0;
            int listaHossza = kategoriaList.size();
            while (i < listaHossza && !kategoriaList.get(i).getNev().equals(nev.toLowerCase())) {
                i++;
            }
            if (i < listaHossza) {
                alert(nev + " már benne van a listában");
                txtFieldKategoria.setText("");
            } else {
                EtlapDB db = new EtlapDB();
                int siker = db.kategoriaHozzaadasa(nev.toLowerCase());
                if (siker == 1) {
                    alert("Kategória hozzáadása: SIKERES");
                    txtFieldKategoria.setText("");
                } else {
                    alert("Kategória hozzáadása: SIKERTELEN");
                }
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }
}
