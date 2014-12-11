package model;

public class Recurso {
	private static int nextCodigo = 0;
	private int codigo;
	private String descricao;
	
	public Recurso() {
		this.codigo = nextCodigo;
		nextCodigo++;
	}

	public static int getNextCodigo() {
		return nextCodigo;
	}

	public static void setNextCodigo(int nextCodigo) {
		Recurso.nextCodigo = nextCodigo;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static void reset() {
		nextCodigo = 0;
	}
}
