package br.com.negocio.treinos;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private String email;
    private String cpf;
    
    private List<Treino> treinos;
    private List<Meta> metas;
    private List<PlanoTreino> planos;
    private List<Notificacao> notificacoes;

    public Usuario(String nome, int idade, double peso, double altura, String email, String cpf) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        this.cpf = cpf;
        this.treinos = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.planos = new ArrayList<>();
        this.notificacoes = new ArrayList<>();
    }

    public List<Notificacao> getNotificacoes() { return notificacoes; }
    public void adicionarNotificacao(Notificacao notificacao) { this.notificacoes.add(notificacao); }

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

    public List<Treino> getTreinos() { return treinos; }
    public void adicionarTreino(Treino treino) { this.treinos.add(treino); }
    public void removerTreino(Treino treino) { this.treinos.remove(treino); }
    
    public Treino buscarTreinoPorId(int idTreino) {
        for (Treino t : treinos) {
            if (t.getIdTreino() == idTreino) return t;
        }
        return null;
    }

    public List<Meta> getMetas() { return metas; }
    public void adicionarMeta(Meta meta) { this.metas.add(meta); }
    public void removerMeta(Meta meta) { this.metas.remove(meta); }
    
    public Meta buscarMetaPorId(int idMeta) {
        for (Meta m : metas) {
            if (m.getIdMeta() == idMeta) return m;
        }
        return null;
    }

    public List<PlanoTreino> getPlanos() { return planos; }
    public void adicionarPlanoTreino(PlanoTreino plano) { this.planos.add(plano); }
    public void removerPlanoTreino(PlanoTreino plano) { this.planos.remove(plano); }
    
    public PlanoTreino buscarPlanoPorId(int idPlano) {
        for (PlanoTreino p : planos) {
            if (p.getIdPlano() == idPlano) return p;
        }
        return null;
    }

    public Usuario buscarCliente(String cpf2) {
        throw new UnsupportedOperationException("Unimplemented method 'buscarCliente'");
    }

	public void buscarCliente(Usuario usuarioAtual) {
		throw new UnsupportedOperationException("Unimplemented method 'buscarCliente'");
	}
}