/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Trains;

/**
 *
 * @author Administrator
 */
public class TrainInfo {
    private int trainplacesNum2cls;
    private int trainplacesNum1cls;
    private int trainNumVoit2Cls;
    private int trainNumVoit1Cls;

    public TrainInfo(int trainplacesNum2cls, int trainplacesNum1cls, int trainNumVoit2Cls, int trainNumVoit1Cls) {
        this.trainplacesNum2cls = trainplacesNum2cls;
        this.trainplacesNum1cls = trainplacesNum1cls;
        this.trainNumVoit2Cls = trainNumVoit2Cls;
        this.trainNumVoit1Cls = trainNumVoit1Cls;
    }

    public int getTrainplacesNum2cls() {
        return trainplacesNum2cls;
    }

    public void setTrainplacesNum2cls(int trainplacesNum2cls) {
        this.trainplacesNum2cls = trainplacesNum2cls;
    }

    public int getTrainplacesNum1cls() {
        return trainplacesNum1cls;
    }

    public void setTrainplacesNum1cls(int trainplacesNum1cls) {
        this.trainplacesNum1cls = trainplacesNum1cls;
    }

    public int getTrainNumVoit2Cls() {
        return trainNumVoit2Cls;
    }

    public void setTrainNumVoit2Cls(int trainNumVoit2Cls) {
        this.trainNumVoit2Cls = trainNumVoit2Cls;
    }

    public int getTrainNumVoit1Cls() {
        return trainNumVoit1Cls;
    }

    public void setTrainNumVoit1Cls(int trainNumVoit1Cls) {
        this.trainNumVoit1Cls = trainNumVoit1Cls;
    }
}
