package br.com.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modela uma competição ou desafio entre os usuários.
 * Ex: "Quem corre a maior distância no mês de Novembro?".
 */
public class Desafio {

    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // A lista de quem topou o desafio.
    // Não guarda só o Usuário, mas um objeto "ParticipacaoDesafio" que
    // liga o usuário ao seu progresso específico NESTE desafio.
    private List<ParticipacaoDesafio> participacoes;

    public Desafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser depois da data de término.");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.participacoes = new ArrayList<>();
    }

    /**
     * Inscreve um usuário no desafio.
     */
    public void adicionarParticipante(Usuario usuario) {
        // Lógica pra não deixar a mesma pessoa se inscrever duas vezes.
        boolean jaParticipa = participacoes.stream()
                                           .anyMatch(p -> p.getUsuario().equals(usuario));
        
        if (!jaParticipa) {
            this.participacoes.add(new ParticipacaoDesafio(usuario, this));
            System.out.println(usuario.getNome() + " entrou no desafio: " + this.nome);
        } else {
            System.out.println(usuario.getNome() + " já está nesse desafio.");
        }
    }

    // --- Getters ---

    public String getNome() {
        return nome;
    }
    
    public List<ParticipacaoDesafio> getParticipacoes() {
        return participacoes;
    }

    public String getDescricao() {
        return descricao;
    }
}
