package Trains;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.List;
import javax.swing.JComboBox;
public class TrainGareMappin {
    private int TrainMark;
    private int RoutId;
    private int GareNom;
    private Timestamp DateDepartMapping;
    private Timestamp dateArriveMapping;
    private int NDS;
    private List<TrainGareMappin> routeGareMappings;
 private int MappingId;

    public int getMappingId() {
        return MappingId;
    }

    public void setMappingId(int MappingId) {
        this.MappingId = MappingId;
    }

    public int getGenerateMappingId() {
        return generateMappingId;
    }

    public void setGenerateMappingId(int generateMappingId) {
        this.generateMappingId = generateMappingId;
    }
private int generateMappingId;


    TrainGareMappin(int routeId, int gareId, LocalDateTime adjustedDepartureTime, int newSequenceNumber, double i, LocalDateTime valueOf) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }



    public int getTrainMark() {
        return TrainMark;
    }

    public void setTrainMark(int TrainMark) {
        this.TrainMark = TrainMark;
    }

    public int getRoutId() {
        return RoutId;
    }

    public void setRoutId(int RoutId) {
        this.RoutId = RoutId;
    }

    public int getGareNom() {
        return GareNom;
    }

    public void setGareNom(int GareNom) {
        this.GareNom = GareNom;
    }

    public Timestamp getDateDepartMapping() {
        return DateDepartMapping;
    }

    public void setDateDepartMapping(Timestamp DateDepartMapping) {
        this.DateDepartMapping = DateDepartMapping;
    }

        public Timestamp getDateArriveMapping() {
        return dateArriveMapping;
    }

    public void setDateArriveMapping(Timestamp dateArriveMapping) {
        this.dateArriveMapping = dateArriveMapping;
    }

    public int getNDS() {
        return NDS;
    }

    public void setNDS(int NDS) {
        this.NDS = NDS;
    }

    public int getDstncKM() {
        return DstncKM;
    }

    public void setDstncKM(int DstncKM) {
        this.DstncKM = DstncKM;
    }
    private int DstncKM;
 // Fixed the declaration here

    public TrainGareMappin() {
        this.TrainMark = TrainMark;
        this.RoutId = RoutId;
        this.GareNom = GareNom;
        this.DateDepartMapping = DateDepartMapping;
        this.dateArriveMapping = dateArriveMapping;
        this.NDS = NDS;
        this.routeGareMappings = routeGareMappings;
        this.DstncKM = DstncKM;
         this.MappingId = MappingId;
    }



    // Getters and setters for other fields...
public static void reverseRouteMapping(List<TrainGareMappin> trainGareMappings) {
    Collections.reverse(trainGareMappings);
    updateSequenceNumbers(trainGareMappings);
}

public static void updateSequenceNumbers(List<TrainGareMappin> trainGareMappings) {
    for (int i = 0; i < trainGareMappings.size(); i++) {
        trainGareMappings.get(i).setNDS(i + 1);
    }
}


    public List<TrainGareMappin> getRouteGareMapping() {
        return routeGareMappings;
    }

    public void setRouteGareMappings(List<TrainGareMappin> routeGareMappings) {
        this.routeGareMappings = routeGareMappings;
    }
}
