
import Trains.GaresBeans;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Administrator
 */
public class Personelle   {
    private int persID;
    private String persNom;
    private String persPrenom;

    //Override
    public String toString() {
        return "PersID: " + persID +
                ", PersNom: " + persNom +
                ", PersPrenom: " + persPrenom +
                ", Username: " + username +
                ", Password: " + password +
                ", PersVille: " + persVille +
                ", PersAdress: " + persAdress +
                ", PersTel: " + persTel +
                ", PersCin: " + persCin +
                ", Option: " + option;
    }

    public Personelle(int persID, String persNom, String persPrenom, String username, String password, String persVille, String persAdress, String persTel, String persCin) {
        this.persID = persID;
        this.persNom = persNom;
        this.persPrenom = persPrenom;
        this.username = username;
        this.password = password;
        this.persVille = persVille;
        this.persAdress = persAdress;
        this.persTel = persTel;
        this.persCin = persCin;
    }
    private String username;
    private String password;
    private String persVille;
    private String persAdress;
    private String persTel;
    private String persCin;
    private String option;

    public int getPersID() {
        return persID;
    }

    public void setPersID(int persID) {
        this.persID = persID;
    }

    public String getPersNom() {
        return persNom;
    }

    public void setPersNom(String persNom) {
        this.persNom = persNom;
    }

    public String getPersPrenom() {
        return persPrenom;
    }

    public void setPersPrenom(String persPrenom) {
        this.persPrenom = persPrenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersVille() {
        return persVille;
    }

    public void setPersVille(String persVille) {
        this.persVille = persVille;
    }

    public String getPersAdress() {
        return persAdress;
    }

    public void setPersAdress(String persAdress) {
        this.persAdress = persAdress;
    }

    public String getPersTel() {
        return persTel;
    }

    public void setPersTel(String persTel) {
        this.persTel = persTel;
    }

    public String getPersCin() {
        return persCin;
    }

    public void setPersCin(String persCin) {
        this.persCin = persCin;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Personelle(int persID, String persNom, String persPrenom, String username, String password, String persVille, String persAdress, String persTel, String persCin, String option) {
        this.persID = persID;
        this.persNom = persNom;
        this.persPrenom = persPrenom;
        this.username = username;
        this.password = password;
        this.persVille = persVille;
        this.persAdress = persAdress;
        this.persTel = persTel;
        this.persCin = persCin;
        this.option = option;
    }
   
}
