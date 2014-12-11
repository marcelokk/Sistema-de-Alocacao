package model;

public class Bloco {
	
	private static int nextId = 0;
	private int id;
	private String codigo;
	private String instituto;

	public Bloco() {
		this.id = nextId;
		nextId++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getInstituto() {
		return instituto;
	}

	public void setInstituto(String instituto) {
		this.instituto = instituto;
	}
	
	public static void reset() {
		nextId = 0;
	}
}
