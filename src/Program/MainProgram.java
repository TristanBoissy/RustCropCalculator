package Program;

import Views.MainPanel;
import javax.swing.*;

/**
 * Author: Tristan Boissy
 * Date: 2021-04-23
 * Version: 1.0
 */


public class MainProgram {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainPanel());
    }
}
