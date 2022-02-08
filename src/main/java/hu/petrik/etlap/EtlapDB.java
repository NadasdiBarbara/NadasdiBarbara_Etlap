package hu.petrik.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapDB {
    Connection conn;

    public EtlapDB() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/etlapdb", "root", "");
    }

    public List<Etlap> getEtlap() throws SQLException {
        List<Etlap> etlapok = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM etlap INNER JOIN kategoria ON etlap.kategoria_id = kategoria.id;";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("etlap.id");
            String nev = result.getString("etlap.nev");
            String leiras = result.getString("etlap.leiras");
            int ar = result.getInt("etlap.ar");
            String kategoria = result.getString("kategoria.nev");
            Etlap etlap = new Etlap(id, nev, leiras, ar, kategoria);
            etlapok.add(etlap);
        }
        return etlapok;
    }

    public List<Etlap> getSzurtEtlap(String kategoriaMegkotes) throws SQLException {
        List<Etlap> etlapList = new ArrayList<>();
        String sql = "SELECT * FROM etlap INNER JOIN kategoria ON etlap.kategoria_id = kategoria.id WHERE kategoria.nev = ?;";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,kategoriaMegkotes);
        ResultSet result = stmt.executeQuery();
        while (result.next()) {
            int id = result.getInt("etlap.id");
            String nev = result.getString("etlap.nev");
            String leiras = result.getString("etlap.leiras");
            int ar = result.getInt("etlap.ar");
            String kategoria = result.getString("kategoria.nev");
            Etlap etlap = new Etlap(id, nev, leiras, ar, kategoria);
            etlapList.add(etlap);
        }
        return etlapList;
    }


    public boolean etelTorlese(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }

    public int etelHozzaadasa(String nev, String leiras, int ar, int kategoria) throws SQLException {
        String sql = "INSERT INTO etlap (nev, leiras, ar, kategoria_id) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nev);
        stmt.setString(2, leiras);
        stmt.setInt(3, ar);
        stmt.setInt(4, kategoria);
        return stmt.executeUpdate();
    }

    public boolean etelEmelesForint(int id, int emeles) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar + ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, emeles);
        stmt.setInt(2, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }

    public boolean etelEmelesSzazalek(int id, int szazalek) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar * ((100 + ?) / 100) WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, szazalek);
        stmt.setInt(2, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }

    public List<Kategoria> getKategoria() throws SQLException {
        List<Kategoria> kategoriak = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM kategoria";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            Kategoria kategoria = new Kategoria(id, nev);
            kategoriak.add(kategoria);
        }
        return kategoriak;
    }

    public int kategoriaHozzaadasa(String nev) throws SQLException {
        String sql = "INSERT INTO kategoria (nev) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nev);
        return stmt.executeUpdate();
    }

    public boolean kategoriaTorlese(int id) throws SQLException {
        String sql = "DELETE FROM kategoria WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int erintettSorok = stmt.executeUpdate();
        return erintettSorok == 1;
    }
}
