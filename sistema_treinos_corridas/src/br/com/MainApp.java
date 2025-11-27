package br.com;

import br.com.gui.GerenciadorTelas;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Garante que a interface gráfica seja iniciada na Thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            // Inicia o Gerenciador, que cria a janela única e carrega o Login
            GerenciadorTelas.getInstance().iniciar();
        });
    }
}