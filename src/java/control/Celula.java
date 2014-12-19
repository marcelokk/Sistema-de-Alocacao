package control;

public class Celula {
	private String conteudo;
	private int pula;
	private int tamanho;
	
	public Celula(String conteudo, int tamanho, int pula) {
		this.conteudo = conteudo;
		this.pula = pula;
		this.tamanho = tamanho;
	}
	
	public Celula() {
		conteudo = "";
		pula = 0;
		tamanho = 1;
	}
	
	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public int getPula() {
		return pula;
	}

	public void setPula(int pula) {
		this.pula = pula;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
}
