package Trains;




/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Administrator
 */
public class GaresBeans {

    
    private int idTtain;
    private int idGare;
    private int sequenceN;
    private String tarainMarque;
     private int mappingID;
    

    public GaresBeans(int idGare, int sequenceN, int idTtain, String tarainMarque,int mappingID) {
        this.idGare = idGare;
        this.sequenceN = sequenceN;
        this.idTtain = idTtain;
        this.tarainMarque = tarainMarque;
        this.mappingID = mappingID;
    }

    public int getMappingID() {
        return mappingID;
    }

    public void setMappingID(int mappingID) {
        this.mappingID = mappingID;
    }

    
    
    public int getIdTtain() {
        return idTtain;
    }

    public void setIdTtain(int idTtain) {
        this.idTtain = idTtain;
    }

    public int getIdGare() {
        return idGare;
    }

    public void setIdGare(int idGare) {
        this.idGare = idGare;
    }

    public int getSequenceN() {
        return sequenceN;
    }

    public void setSequenceN(int sequenceN) {
        this.sequenceN = sequenceN;
    }

    public String getTarainMarque() {
        return tarainMarque;
    }

    public void setTarainMarque(String tarainMarque) {
        this.tarainMarque = tarainMarque;
    }
    
    
    
    
    
    
    private String GareName;
    private boolean TNR1;
    private boolean TNR0;
    private boolean ligne1;
    private boolean ligne2;
    private boolean Term0;
    private boolean Term1;
    private boolean Atlas1;
    private boolean Atlas0;
    private boolean Boraq1;

    public boolean isBoraq1() {
        return Boraq1;
    }

    public void setBoraq1(boolean Boraq1) {
        this.Boraq1 = Boraq1;
    }

    public boolean isBoraq0() {
        return Boraq0;
    }

    public void setBoraq0(boolean Boraq0) {
        this.Boraq0 = Boraq0;
    }
    private boolean Boraq0;
    private String PreviousStationName;
    private String NextStationName;

    public String getPreviousStationName() {
        return PreviousStationName;
    }

    public void setPreviousStationName(String PreviousStationName) {
        this.PreviousStationName = PreviousStationName;
    }

    public String getNextStationName() {
        return NextStationName;
    }

    public void setNextStationName(String NextStationName) {
        this.NextStationName = NextStationName;
    }



    public GaresBeans() {
 
    }

    public GaresBeans(String GareName, boolean TNR1, boolean TNR0, boolean ligne1, boolean ligne2,
                      boolean Term0, boolean Term1, boolean Atlas1, boolean Atlas0, String NextStationName, String PreviousStationName,boolean Boraq1,boolean Boraq0 ) {
        this.GareName = GareName;
        this.TNR1 = TNR1;
        this.TNR0 = TNR0;
        this.ligne1 = ligne1;
        this.ligne2 = ligne2;
        this.Term0 = Term0;
        this.Term1 = Term1;
        this.Atlas1 = Atlas1;
        this.Atlas0 = Atlas0;
        this.NextStationName = NextStationName;
        this.PreviousStationName = PreviousStationName;
        this.Boraq1 = Boraq1;
        this.Boraq0 = Boraq0;
    }

    public String getGareName() {
        return GareName;
    }

    public void setGareName(String GareName) {
        this.GareName = GareName;
    }

    public boolean isTNR1() {
        return TNR1;
    }

    public void setTNR1(boolean TNR1) {
        this.TNR1 = TNR1;
    }

    public boolean isTNR0() {
        return TNR0;
    }

    public void setTNR0(boolean TNR0) {
        this.TNR0 = TNR0;
    }

    public boolean isLigne1() {
        return ligne1;
    }

    public void setLigne1(boolean ligne1) {
        this.ligne1 = ligne1;
    }

    public boolean isLigne2() {
        return ligne2;
    }

    public void setLigne2(boolean ligne2) {
        this.ligne2 = ligne2;
    }

    public boolean isTerm0() {
        return Term0;
    }

    public void setTerm0(boolean Term0) {
        this.Term0 = Term0;
    }

    public boolean isTerm1() {
        return Term1;
    }

    public void setTerm1(boolean Term1) {
        this.Term1 = Term1;
    }

    public boolean isAtlas1() {
        return Atlas1;
    }

    public void setAtlas1(boolean Atlas1) {
        this.Atlas1 = Atlas1;
    }

    public boolean isAtlas0() {
        return Atlas0;
    }

    public void setAtlas0(boolean Atlas0) {
        this.Atlas0 = Atlas0;
    }

    public String toString() {
        return "GareName: " + GareName +
                ", TNR1: " + TNR1 +
                ", TNR0: " + TNR0 +
                ", Atlas1: " + Atlas1 +
                ", Atlas0: " + Atlas0 +
                ", Term0: " + Term0 +
                ", Term1: " + Term1 +
                ", Ligne1: " + ligne1 +
                ", Ligne2: " + ligne2+
        ", NextStationName: " + NextStationName+
          ", PreviousStationName: " + PreviousStationName+
                ",Boraq1:"+ Boraq1+
                ",Boraq0:"+Boraq0;
        
        
    }
}

 
    

