package model;

public class Sala {

	//private static int nextId = 0;
	//private int id;
	private String sala;
	private int capacidade;
	private int vazia;
	private int andar;

	public Sala() {
	//	this.id = nextId;
	//	nextId++;
	}
	
	/*
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	*/

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
