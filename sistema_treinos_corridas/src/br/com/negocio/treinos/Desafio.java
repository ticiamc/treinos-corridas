package br.com.negocio.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Desafio {

    private String nome;
    private String descricao;
    // Campos que guardam o período em que o desafio está rolando:
    private LocalDate dataInicio;
    private LocalDate dataFim;

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

    public void adicionarParticipante(Usuario usuario) {
        boolean jaParticipa = participacoes.stream()
                                           .anyMatch(p -> p.getUsuario().equals(usuario));
        
        if (!jaParticipa) {
            this.participacoes.add(new ParticipacaoDesafio(usuario, this));
            System.out.println(usuario.getNome() + " entrou no desafio: " + this.nome);
        } else {
            System.out.println(usuario.getNome() + " já está nesse desafio.");
        }
    }

    // --- Getters --- "janelas" públicas e seguras que permitem a outras partes do código ler o valor de um atributo que está protegido como privado dentro de uma classe

    public String getNome() {
        return nome;
    }
    
    public List<ParticipacaoDesafio> getParticipacoes() {
        return participacoes;
    }

    public String getDescricao() {
        return descricao;
    }

       
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    
    public LocalDate getDataFim() {
        return dataFim;
    }
}