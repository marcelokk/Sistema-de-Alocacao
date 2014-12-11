package model;

public class Horario {
	
	public static int SEGUNDA = 0;
	public static int TERCA = 1;
	public static int QUARTA = 2;
	public static int QUINTA = 3;
	public static int SEXTA = 4;
	public static int SABADO = 5;
	public static int DOMINGO = 6;	
	
	private static int nextId = 0;
	private int id;
	private Integer inicio;
	private Integer fim;
	private Integer dia;
	
	public Horario() {
		this.id = this.nextId;
		this.nextId++;
	}
	
	public Horario(int id, int inicio, int dia) {
		this.id = id;
		this.inicio = inicio;
		this.dia = dia;
	}

	public Horario(int dia, String inicio, String fim) {
		this.dia = dia;
		
		// tira espacos
		inicio = inicio.replace(" \t", "");
		fim = fim.replace(" \t", "");
		
		String[] tempo = inicio.split(":");
		Integer aux = Integer.parseInt(tempo[0]) * 60;
		aux += Integer.parseInt(tempo[1]);
		
		tempo = fim.split(":");
		Integer aux2 = Integer.parseInt(tempo[0]) * 60;
		aux2 += Integer.parseInt(tempo[1]);		
		
		// aula termina antes de comecar
		if(aux > aux2) {
			System.out.println("ERRO: ");
		}		
		this.inicio = aux;
		this.fim = aux2;
	}	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getInicio() {
		return inicio;
	}

	public void setInicio(Integer inicio) {
		this.inicio = inicio;
	}

	public Integer getFim() {
		return fim;
	}

	public void setFim(Integer fim) {
		this.fim = fim;
	}
	
	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}	
	
	public static void reset() {
		nextId = 0;
	}
}
