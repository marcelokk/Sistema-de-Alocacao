package model;

public class Andar {
	private static int nextId = 0;
	private int id;
	private int andar;
	private int bloco;

	public Andar() {
		this.id = nextId;
		nextId++;
	}
	
	public static int getNextId() {
		return nextId;
	}

	public static void setNextId(int nextId) {
		Andar.nextId = nextId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAndar() {
		return andar;
	}

	public void setAndar(int andar) {
		this.andar = andar;
	}

	public int getBloco() {
		return bloco;
	}

	public void setBloco(int bloco) {
		this.bloco = bloco;
	}
	
	public static void reset() {
		nextId = 0;
	}
}
