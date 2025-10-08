package br.com.treinos;

import java.time.LocalDateTime;

/**
 * Representa um relatório gerado pelo sistema 
 */
public class Relatorio {

    private String titulo;
    private String conteudo;
    private LocalDateTime dataGeracao;

    public Relatorio(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataGeracao = LocalDateTime.now();
    }
    
    public void exportarParaPDF() {
        System.out.println("Exportando relatório \"" + titulo + "\" para PDF...");
        // Futuramente, aqui entraria a lógica com a biblioteca iText ou JasperReports
    }
    
    public void exportarParaCSV() {
        System.out.println("Exportando relatório \"" + titulo + "\" para CSV...");
        // Futuramente, aqui entraria a lógica com a biblioteca Apache POI
    }

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public String getConteudo() { return conteudo; }
    public LocalDateTime getDataGeracao() { return dataGeracao; }
}