package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.EtlapDB;
import hu.petrik.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class HozzaadController extends Controller {

    @FXML
    private ChoiceBox<String> choiseBKategoria;
    @FXML
    private TextArea txtAreaLeiras;
    @FXML
    private TextField txtFieldNev;
    @FXML
    private Spinner<Integer> spinnerAr;

    private EtlapDB db;
    private List<Kategoria> kategoriaList;

    public void initialize() {
        try {
            db = new EtlapDB();
            kategoriaList = db.getKategoria();
            for (Kategoria kategoria : kategoriaList) {
                choiseBKategoria.getItems().add(kategoria.getNev());
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void HozzaadClick(ActionEvent actionEvent) {
        String nev = txtFieldNev.getText().trim();
        String leiras = txtAreaLeiras.getText().trim();
        int kategoriaIndex = choiseBKategoria.getSelectionModel().getSelectedIndex();
        int ar = 0;

        if (nev.isEmpty()) {
            alert("Név megadása kötelező");
            return;
        }
        if (leiras.isEmpty()) {
            alert("Leírás megadása kötelező");
            return;
        }
        if (kategoriaIndex == -1) {
            alert("Kategória kiválasztása kötelező");
            return;
        }
        String kategoria = choiseBKategoria.getValue().toString();
        try {
            ar = spinnerAr.getValue();
        } catch (NullPointerException e) {
            alert("Ár megadása kötelező");
            return;
        } catch (Exception e) {
            alert("Az ár 1 és 99999 közötti szám lehet");
            return;
        }
        if (ar < 1 || ar > 99999) {
            alert("Az ár 1 és 99999 közötti szám lehet");
            return;
        }
        try {
            EtlapDB db = new EtlapDB();
            int kategoria_id = 0;
            int i = 0;
            int listaHossza = kategoriaList.size();
            while (i < listaHossza && !kategoriaList.get(i).getNev().equals(kategoria)) {
                i++;
            }
            int siker = db.etelHozzaadasa(nev, leiras, ar, kategoria_id);
            if (siker == 1) {
                alert("Étel hozzáadás: SIKERES");
                txtFieldNev.setText("");
                txtAreaLeiras.setText("");
                choiseBKategoria.setValue("előétel");
                spinnerAr.getValueFactory().setValue(1000);
            } else {
                alert("Étel hozzáadás: SIKERTELEN");
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }
}
