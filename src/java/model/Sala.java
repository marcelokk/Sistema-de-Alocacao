package model;

public class Sala {

	private String sala;
	private int capacidade;
	private int vazia;
	private int andar;

	public Sala() {
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public int getVazia() {
		return vazia;
	}

	public void setVazia(int vazia) {
		this.vazia = vazia;
	}

	public int getAndar() {
		return andar;
	}

	public void setAndar(int andar) {
		this.andar = andar;
	}
}
