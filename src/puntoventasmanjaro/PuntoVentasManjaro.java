/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puntoventasmanjaro;

import Views.Login;
import Views.Sistema;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.UIManager;

/**
 *
 * @author anvimol
 */
public class PuntoVentasManjaro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception ex) {
        }
        Login sistem = new Login();
        sistem.setVisible(true);
        //sistem.setExtendedState(MAXIMIZED_BOTH);
    }

}
