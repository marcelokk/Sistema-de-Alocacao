package model;

public class Preferencia {
	private String curso;
	private String sala;
	private int prioridade;
	
	public Preferencia(String curso, String sala, int prioridade) {
		this.sala = sala;
		this.curso = curso;
		this.prioridade = prioridade;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
}
