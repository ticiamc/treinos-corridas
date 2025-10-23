package br.com.negocio.treinos;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private String email;
    private String cpf; // Adicionando CPF como identificador único

    // Relacionamentos
    private List<Treino> treinos;
    private List<Meta> metas;
    private List<PlanoTreino> planos;
    // A participação em desafios é modelada pela classe ParticipacaoDesafio

    public Usuario(String nome, int idade, double peso, double altura, String email, String cpf) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        this.cpf = cpf;
        // Inicializa as listas
        this.treinos = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.planos = new ArrayList<>();
    }

    // Getters e Setters básicos (omitidos por brevidade, mas devem existir)
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    // --- MÉTODOS DE MANIPULAÇÃO DAS LISTAS (NOVOS) ---

    // Métodos para Treinos
    public List<Treino> getTreinos() {
        return treinos;
    }

    public void adicionarTreino(Treino treino) {
        if (treino != null) {
            this.treinos.add(treino);
        }
    }

    public void removerTreino(Treino treino) {
        if (treino != null) {
            this.treinos.remove(treino);
        }
    }

    // Métodos para Metas
    public List<Meta> getMetas() {
        return metas;
    }

    public void adicionarMeta(Meta meta) {
        if (meta != null) {
            this.metas.add(meta);
            meta.setUsuario(this); // Garante a associação bidirecional
        }
    }

    public void removerMeta(Meta meta) {
        if (meta != null) {
            this.metas.remove(meta);
        }
    }

    // Métodos para Planos de Treino
    public List<PlanoTreino> getPlanos() {
        return planos;
    }

    public void adicionarPlanoTreino(PlanoTreino plano) {
        if (plano != null) {
            this.planos.add(plano);
            plano.setUsuario(this); // Garante a associação bidirecional
        }
    }

    public void removerPlanoTreino(PlanoTreino plano) {
        if (plano != null) {
            this.planos.remove(plano);
        }
    }
    
    // Método para encontrar uma meta específica pelo ID
    public Meta buscarMetaPorId(int idMeta) {
        for (Meta meta : metas) {
            if (meta.getIdMeta() == idMeta) {
                return meta;
            }
        }
        return null;
    }

    // Método para encontrar um plano específico pelo ID
    public PlanoTreino buscarPlanoPorId(int idPlano) {
        for (PlanoTreino plano : planos) {
            if (plano.getIdPlano() == idPlano) {
                return plano;
            }
        }
        return null;
    }

    // Método para encontrar um treino específico pelo ID
    public Treino buscarTreinoPorId(int idTreino) {
        for (Treino treino : treinos) {
            // Assumindo que Treino tem um getIdTreino()
            if (treino.getIdTreino() == idTreino) { 
                return treino;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Usuario [nome=" + nome + ", idade=" + idade + ", peso=" + peso + ", altura=" + altura + ", email=" + email
                + ", cpf=" + cpf + "]";
    }
}
