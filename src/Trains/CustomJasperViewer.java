package Trains;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class CustomJasperViewer extends JasperViewer {

    public CustomJasperViewer(JasperPrint jasperPrint) {
        super(jasperPrint);
    }

    @Override
    public void dispose() {
        setVisible(false); // Instead of disposing, just hide the window
    }
}