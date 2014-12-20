package control;

public class Celula {
	private String conteudo;
	private boolean pula;
	private int tamanho;
	
	public Celula(String conteudo, int tamanho, boolean pula) {
		this.conteudo = conteudo;
		this.pula = pula;
		this.tamanho = tamanho;
	}
	
	public Celula() {
		conteudo = "";
		pula = false;
		tamanho = 1;
	}
	
	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public boolean getPula() {
		return pula;
	}

	public void setPula(boolean pula) {
		this.pula = pula;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
}
