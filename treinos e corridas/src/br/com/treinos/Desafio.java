package br.com.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um desafio competitivo entre usuários, com um objetivo
 * e um período definidos.
 */
public class Desafio {

    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    // A lista armazena objetos do tipo ParticipacaoDesafio, 
    // que conectam um Usuário ao seu progresso neste desafio específico.
    private List<ParticipacaoDesafio> participacoes;

    /**
     * Construtor para criar um novo Desafio.
     *
     * @param nome O nome do desafio (ex: "Correr 100km em Novembro").
     * @param descricao Um texto explicando as regras do desafio.
     * @param dataInicio A data em que o desafio começa.
     * @param dataFim A data em que o desafio termina.
     */
    public Desafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim) {
        // Validação para garantir que a data de início não seja posterior à data de fim
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de término.");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.participacoes = new ArrayList<>();
    }

    /**
     * Adiciona um novo usuário como participante do desafio.
     * Cria uma nova instância de ParticipacaoDesafio para rastrear o progresso.
     *
     * @param usuario O usuário a ser adicionado ao desafio.
     */
    public void adicionarParticipante(Usuario usuario) {
        // Verifica se o usuário já está participando para evitar duplicatas.
        // A lógica de verificação percorre a lista de participações e checa se algum
        // usuário associado é igual ao que está sendo adicionado.
        boolean jaParticipa = participacoes.stream()
                                           .anyMatch(p -> p.getUsuario().equals(usuario));
        
        if (!jaParticipa) {
            this.participacoes.add(new ParticipacaoDesafio(usuario, this));
            System.out.println(usuario.getNome() + " foi adicionado(a) ao desafio: " + this.nome);
        } else {
            System.out.println(usuario.getNome() + " já está participando do desafio: " + this.nome);
        }
    }

    // --- Getters e Setters ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * Retorna a lista de participações do desafio, que contém os usuários
     * e seus respectivos progressos.
     * * @return Uma lista de objetos ParticipacaoDesafio.
     */
    public List<ParticipacaoDesafio> getParticipacoes() {
        return participacoes;
    }
}
