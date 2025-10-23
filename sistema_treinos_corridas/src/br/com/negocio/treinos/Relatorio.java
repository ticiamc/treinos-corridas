package br.com.negocio.treinos;

// Imports para Input/Output de arquivos
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Classe responsável por gerar e exportar diferentes relatórios com base nos dados de um usuário.
public class Relatorio {

    // --- Atributos ---
    private Usuario usuario; 

    // --- Construtor ---
    //Cria um gerador de relatórios para um usuário específico.
    public Relatorio(Usuario usuario) {
        this.usuario = usuario;
    }

    //Gera um relatório (no console) de todas as atividades (treinos) registradas pelo usuário.
    public void gerarRelatorioAtividades() {
        System.out.println("--- Relatório de Atividades para " + usuario.getNome() + " ---");
        List<Treino> treinos = usuario.getTreinos();
        
        if (treinos.isEmpty()) {
            System.out.println("Nenhum treino registrado.");
            return;
        }

        System.out.println("Total de treinos: " + treinos.size());
        for (Treino treino : treinos) {
            System.out.println("--------------------");
            System.out.println("Data: " + treino.getData());
            System.out.println("Nome: " + treino.getNome());
            System.out.println("Duração (min): " + treino.getDuracaoEmMinutos());
            
            // Verifica o tipo específico de treino para mostrar detalhes
            if (treino instanceof Corrida) {
                Corrida c = (Corrida) treino;
                System.out.println("Tipo: Corrida");
                System.out.println("Distância (m): " + c.getDistanciaEmMetros());
            } else if (treino instanceof Intervalado) {
                Intervalado i = (Intervalado) treino;
                System.out.println("Tipo: Intervalado");
                System.out.println("Séries: " + i.getSeries());
            }
            // Mostra se o treino foi planejado ou concluído (contabilizado)
            System.out.println("Status: " + (treino.isStatus() ? "Concluído" : "Planejado"));
        }
        System.out.println("--- Fim do Relatório ---");
    }

    //Gera um relatório (no console) de evolução, mostrando totais e médias das corridas do usuário. 
    public void gerarRelatorioEvolucao() {
        System.out.println("--- Relatório de Evolução para " + usuario.getNome() + " ---");
        List<Treino> treinos = usuario.getTreinos();
        
        if (treinos.isEmpty()) {
            System.out.println("Nenhum treino registrado.");
            return;
        }
        
        // Variáveis para acumular os totais
        double totalDistancia = 0;
        int totalCorridas = 0;
        
        // Itera por todos os treinos
        for (Treino treino : treinos) {
            // Acumula apenas se for uma Corrida
            if (treino instanceof Corrida) {
                totalDistancia += ((Corrida) treino).getDistanciaEmMetros();
                totalCorridas++;
            }
        }

        if(totalCorridas == 0) {
             System.out.println("Nenhuma corrida registrada para analisar evolução.");
             return;
        }

        System.out.println("Total de Corridas registradas: " + totalCorridas);
        System.out.println("Distância Total Percorrida: " + (totalDistancia / 1000) + " km");
        System.out.println("Média de Distância por Corrida: " + (totalDistancia / totalCorridas) + " m");
        System.out.println("--- Fim do Relatório ---");
    }

    // Gera um ranking de um desafio.
    public void gerarRanking(Desafio desafio) {
        System.out.println("--- Ranking do Desafio: " + desafio.getNome() + " ---");
        List<ParticipacaoDesafio> participacoes = desafio.getParticipacoes();
        
        // Simulação de ordenação
        Collections.sort(participacoes, new Comparator<ParticipacaoDesafio>() {
            @Override
            public int compare(ParticipacaoDesafio p1, ParticipacaoDesafio p2) {
                return 0; 
            }
        });

        int pos = 1;
        for (ParticipacaoDesafio p : participacoes) {
            System.out.println(pos + "º Lugar: " + p.getUsuario().getNome());
            pos++;
        }
        System.out.println("--- Fim do Ranking ---");
    }

    //Exporta o "Relatório de Atividades" para um arquivo de texto (.txt).
    public void exportar(String nomeArquivo) {
        System.out.println("Exportando relatório para: " + nomeArquivo);

        // Usa try-with-resources: garante que o arquivo (PrintWriter) seja fechado automaticamente no final, mesmo se ocorrer um erro.
        try (PrintWriter out = new PrintWriter(new FileWriter(nomeArquivo))) {
            
            // Escreve o conteúdo do relatório de atividades no ARQUIVO
            out.println("--- Relatório de Atividades para " + usuario.getNome() + " ---");
            out.println("Total de treinos: " + usuario.getTreinos().size());
            out.println(); // Linha em branco

            for (Treino treino : usuario.getTreinos()) {
                out.println("Data: " + treino.getData());
                out.println("Nome: " + treino.getNome());
                out.println("Duração (min): " + treino.getDuracaoEmMinutos());
                
                if (treino instanceof Corrida) {
                    Corrida c = (Corrida) treino;
                    out.println("Tipo: Corrida");
                    out.println("Distância (m): " + c.getDistanciaEmMetros());
                } else if (treino instanceof Intervalado) {
                    Intervalado i = (Intervalado) treino;
                    out.println("Tipo: Intervalado");
                    out.println("Séries: " + i.getSeries());
                }
                out.println("--------------------");
            }
            
            System.out.println(">>> Relatório exportado com sucesso para " + nomeArquivo);

        } catch (IOException e) {
            // Informa ao usuário se algo der errado ao tentar escrever o arquivo
            System.err.println("Erro ao exportar o arquivo: " + e.getMessage());
        }
    }
}