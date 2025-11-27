package br.com;

import br.com.gui.GerenciadorTelas;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciadorTelas.getInstance().iniciar();
        });
    }
}