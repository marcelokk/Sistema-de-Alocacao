package model;

import java.util.ArrayList;
import model.Horario;

public class Turma {
	private static int nextId = 0;
	private int id;
	private String curso;
	private String disciplina;
	private int inscritos;
	private ArrayList<Horario> horarios;

	public Turma() {
		id = nextId;
		nextId++;
		horarios = new ArrayList();
	}
	
	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public int getInscritos() {
		return inscritos;
	}

	public void setInscritos(int inscritos) {
		this.inscritos = inscritos;
	}

	public ArrayList<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(ArrayList<Horario> horarios) {
		this.horarios = horarios;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static void reset() {
		nextId = 0;
	}
}
