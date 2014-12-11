package model;

public class RecursoHasSala {
	private int recurso;
	private String sala;

	public RecursoHasSala(int recurso, String sala) {
		this.sala = sala;
		this.recurso = recurso;
	}
	
	public int getRecurso() {
		return recurso;
	}

	public void setRecurso(int recurso) {
		this.recurso = recurso;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}
}
