package singleton;

import java.sql.*;

/**
 * Class que faz a conexao com o banco de dados
 * @author Marcelo
 */
public class BDConnection {
    protected Connection con;
    private String url, usuario, senha;

	/**
	 * Contrutor, apenas inicializa os atributos da classe
	 * @param url
	 * @param usuario
	 * @param senha 
	 */
    BDConnection(String url, String usuario, String senha) {
        con = null;
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

	/**
	 * Conecta-se ao banco
	 */
    public void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro na conexao com o banco de dados.\n");
            e.getMessage();
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }

	/**
	 * Desconecta-se do banco
	 */
    public void desconectar() {
        try {
            con.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
