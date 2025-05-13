/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Trains;

/**
 *
 * @author Administrator
 */
public class TrainBeans {
    
    
    static TrainBeans obj;
    public static TrainBeans setObj(){
        if(obj == null){
            obj = new TrainBeans();
        }
        return obj;
    }
    
    
    
    private int TrainNumber;

    public int getTrainNumber() {
        return TrainNumber;
    }

    public void setTrainNumber(int TrainNumber) {
        this.TrainNumber = TrainNumber;
    }

    public String getTrainMarque() {
        return TrainMarque;
    }

    public void setTrainMarque(String TrainMarque) {
        this.TrainMarque = TrainMarque;
    }

    public String getTrainType() {
        return TrainType;
    }

    public void setTrainType(String TrainType) {
        this.TrainType = TrainType;
    }

    public int getTrainCapacity() {
        return TrainCapacity;
    }

    public void setTrainCapacity(int TrainCapacity) {
        this.TrainCapacity = TrainCapacity;
    }

    public int getTrainplacesNum1cls() {
        return TrainplacesNum1cls;
    }

    public void setTrainplacesNum1cls(int TrainplacesNum1cls) {
        this.TrainplacesNum1cls = TrainplacesNum1cls;
    }

    public int getTrainNumberVoiture() {
        return TrainNumberVoiture;
    }

    public void setTrainNumberVoiture(int TrainNumberVoiture) {
        this.TrainNumberVoiture = TrainNumberVoiture;
    }

    public int getTrainNumVoit1Cls() {
        return TrainNumVoit1Cls;
    }

    public void setTrainNumVoit1Cls(int TrainNumVoit1Cls) {
        this.TrainNumVoit1Cls = TrainNumVoit1Cls;
    }

    public String getTrainSpeed() {
        return TrainSpeed;
    }

    public void setTrainSpeed(String TrainSpeed) {
        this.TrainSpeed = TrainSpeed;
    }
    private String TrainMarque;
    private String TrainType;
    private int TrainCapacity;
    private int TrainplacesNum1cls;
    private int TrainNumberVoiture;
    private int TrainNumVoit1Cls;
    private String TrainSpeed;
    
 
}
