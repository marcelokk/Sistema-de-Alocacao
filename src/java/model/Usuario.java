package model;

public class Usuario {

    private int numero_usp;
    private String email;
    private String nome;
    private String senha;

    public int getNumero_usp() {
        return numero_usp;
    }

    public void setNumero_usp(int numero_usp) {
        this.numero_usp = numero_usp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
