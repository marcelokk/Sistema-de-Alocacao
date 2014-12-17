package singleton;

import model.Preferencia;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.activation.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import model.Andar;
import model.Bloco;
import model.CursoAno;
import model.Disciplina;
import model.Recurso;
import model.RecursoHasSala;
import model.Sala;
import model.Turma;
import model.Horario;
import model.HorarioSala;
import model.Usuario;

/**
 * Classe para acesso ao banco de dados, ela implementa o design pattern
 * singleton, para garantir apenas uma conexao ao banco ativa
 *
 * @author Marcelo
 */
public class Banco extends BDConnection {

	private static Banco banco;

	/**
	 * Contrutor, acerta o path ate o banco de dados e faz conexao
	 */
	// O path esta na pasta bin do apache
	private Banco() {
		super("jdbc:sqlite:C:\\Users\\Marcelo\\Desktop\\Sistema\\banco.db", "", "");

		this.conectar();
		try {
			con.prepareStatement("pragma foreign_keys = ON;").execute();
			con.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna a instancia do banco de dados
	 *
	 * @return o banco de dados
	 */
	public static Banco getInstance() {
		if (banco == null) {
			banco = new Banco();
		}
		return banco;
	}

	/**
	 * insere no banco de dados os cursos do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// nao precisa fazer checagem
	public void cursoAno(ArrayList<CursoAno> lista) throws SQLException {
		String sql = ("INSERT INTO CursoAno VALUES(?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (CursoAno c : lista) {
			//System.err.println(c.getCodigo() + " " + c.getNome() + " " + c.getPeriodo());
			pstm.setString(1, c.getCodigo());
			pstm.setString(2, c.getNome());
			pstm.setInt(3, c.getPeriodo());

			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco de dados as disciplinas do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// nao precisa fazer checagem
	public void disciplina(ArrayList<Disciplina> lista) throws SQLException {
		String sql = ("INSERT INTO Disciplina VALUES(?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Disciplina d : lista) {

			pstm.setString(1, d.getCodigo());
			pstm.setString(2, d.getNome());
			pstm.setInt(3, d.getCreditos());

			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco de dados as salas do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// precisa checar o andar
	public void sala(ArrayList<Sala> lista) throws SQLException {
		String sql = ("INSERT INTO Sala VALUES(?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Sala s : lista) {
			pstm.setInt(1, s.getAndar());
			pstm.setString(2, s.getSala());
			pstm.setInt(3, s.getCapacidade());

			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco de dados os andares do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// precisa checar o bloco
	public void andar(ArrayList<Andar> lista) throws SQLException {
		String sql = ("INSERT INTO Andar VALUES(?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Andar a : lista) {
			pstm.setInt(1, a.getId());
			pstm.setInt(2, a.getBloco());
			pstm.setInt(3, a.getAndar());

			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco os blocos do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// nao precisa fazer checagem
	public void bloco(ArrayList<Bloco> lista) throws SQLException {
		String sql = ("INSERT INTO Bloco VALUES(?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Bloco b : lista) {
			pstm.setInt(1, b.getId());
			pstm.setString(2, b.getCodigo());
			pstm.setString(3, b.getInstituto());
			pstm.addBatch();
		}

		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco os recursos do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// nao precisa fazer checagem
	public void recurso(ArrayList<Recurso> lista) throws SQLException {
		String sql = ("INSERT INTO Recurso VALUES(?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Recurso r : lista) {
			pstm.setInt(1, r.getCodigo());
			pstm.setString(2, r.getDescricao());
			//System.err.println(r.getCodigo() + " " + r.getDescricao());
			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco de dados as turmas do array list
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// precisa checar curso e disciplina
	public void turma(ArrayList<Turma> lista) throws SQLException {
		String sql = ("INSERT INTO Turma VALUES(?,?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Turma t : lista) {
			pstm.setInt(1, t.getId());
			pstm.setString(2, t.getCurso());
			pstm.setString(3, t.getDisciplina());
			pstm.setInt(4, t.getInscritos());
			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere os horarios no banco de dados
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// nao precisa fazer checagem
	public void horario(ArrayList<Horario> lista) throws SQLException {
		String sql = ("insert into horario values(?,?,?);");
		PreparedStatement pstm = con.prepareStatement(sql);

		for (Horario h : lista) {
			pstm.setInt(1, h.getId());
			pstm.setInt(2, h.getInicio());
			//pstm.setInt(3, h.getFim());
			pstm.setInt(3, h.getDia());

			pstm.addBatch();
		}
		pstm.executeBatch();
		con.commit();
	}

	/**
	 * insere no banco de dados os horarios das aulas nas salas. essa funcao e'
	 * utilizada para inserir os dados depois da heuristica executar
	 *
	 * @param horario
	 * @param sala
	 * @param turma
	 * @throws SQLException
	 */
	// precisa checar sala, horario turma, 
	public void inserir(int horario, String sala, int turma) throws SQLException {
		String sql = ("INSERT INTO HORARIO_SALA VALUES(?,?,?,?,?);");

		PreparedStatement pstm = con.prepareStatement(sql);

		pstm.setString(1, sala);
		pstm.setInt(2, horario);
		pstm.setInt(3, turma);
		pstm.setString(4, "sem data");
		pstm.setInt(5, 0);

		pstm.executeUpdate();
		con.commit();
	}

	/**
	 * insere um usuario no banco, para que ele possa acessar o sistema
	 *
	 * @param u
	 * @throws java.sql.SQLException
	 */
	// nao precisa fazer checagem
	public void inserirUsuario(Usuario u) throws SQLException {
		String sql = ("INSERT INTO Usuario VALUES(?,?,?,?);");

		PreparedStatement pstm = con.prepareStatement(sql);

		pstm.setInt(1, u.getNumero_usp());
		pstm.setString(2, u.getNome());
		pstm.setString(3, u.getEmail());
		pstm.setString(4, u.getSenha());

		pstm.executeUpdate();
		con.commit();
	}

	/**
	 * atualiza os dados do usuario passado por parametro
	 *
	 * @param u
	 */
	public void updateUsuario(Usuario u) throws SQLException {
		String sql = ("update usuario set nome = ?, senha = ?, numusp = ? where email = ?");
		PreparedStatement pstm = con.prepareStatement(sql);

		pstm.setString(1, u.getNome());
		pstm.setString(2, u.getSenha());
		pstm.setInt(3, u.getNumero_usp());
		pstm.setString(4, u.getEmail());

		pstm.executeUpdate();
		con.commit();
	}

	/**
	 * Insere no banco os horarios das turmas. A lista passada contem as turmas,
	 * e o banco ja deve conter os horarios.
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// precisa checar turma e horario
	public void horario_turma(ArrayList<Turma> lista) throws SQLException {
		// TABELA Horario
		ResultSet result = this.query("select id, inicio, diasem from horario");
		ArrayList<Horario> listaHorarios = new ArrayList();
		while (result.next()) {
			Horario h = new Horario(result.getInt(1), result.getInt(2), result.getInt(3)); // public Horario(int id, int inicio, int dia) 
			listaHorarios.add(h);
		}

		String sql = ("INSERT INTO Horario_Turma VALUES(?,?);");
		for (Turma t : lista) {

			for (Horario h : t.getHorarios()) {
				for (Horario h2 : listaHorarios) {
					/*
					 System.out.println("hora " + h.getDia() + " (" + h.getInicio() / 60 + ":" + h.getInicio() % 60 + ")" + h2.getDia() + "("
					 + h2.getInicio() / 60 + ":" + h2.getInicio() % 60 + ")");
					 */
					if (h.getDia() == h2.getDia() && h.getInicio() - h2.getInicio() == 0) {
						//System.err.println("id hora " + h2.getId() + " codigo turma " + t.getId());
						PreparedStatement pstm = con.prepareStatement(sql);
						pstm.setInt(1, t.getId());
						pstm.setInt(2, h2.getId());
						//pstm.execute();
						pstm.executeUpdate();
						break;
					}
				}
			}
		}
		con.commit();
	}

	/**
	 * insere as preferencias no banco de dados
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// precisa checar curso e sala
	public void preferencia(ArrayList<Preferencia> lista) throws SQLException {
		// TABELA SALAS
		Statement psts = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet salas = psts.executeQuery("SELECT nome FROM Sala ORDER BY nome");
		//ArrayList<Integer> listaSalas = new ArrayList();
		ArrayList<String> listaNomeSalas = new ArrayList();
		while (salas.next()) {
			//listaSalas.add(salas.getInt(1));
			listaNomeSalas.add(salas.getString(1));
		}

		String sql = ("INSERT INTO Preferencia VALUES(?,?,?);");

		// falta verificar se a sala nao existe
		for (Preferencia p : lista) {
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, p.getCurso());

			int i = 0;
			for (String s : listaNomeSalas) {
				if (p.getSala().equalsIgnoreCase(s)) {
					//pstm.setInt(2, listaSalas.get(i));
					pstm.setString(2, p.getSala());
				}
				i++;
			}
			pstm.setInt(3, p.getPrioridade());
			//pstm.execute();
			pstm.executeUpdate();
		}
		con.commit();
	}

	/**
	 * insere os recursos nas salas no banco de dados
	 *
	 * @param lista
	 * @throws SQLException
	 */
	// precisa checar as tabelas recurso e sala
	public void recursoHasSala(ArrayList<RecursoHasSala> lista) throws SQLException {
		// TABELA SALAS
		Statement psts = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet salas = psts.executeQuery("SELECT nome FROM Sala");
		//ArrayList<Integer> listaSalas = new ArrayList();
		ArrayList<String> listaNomeSalas = new ArrayList();
		while (salas.next()) {
			//listaSalas.add(salas.getInt(1));
			listaNomeSalas.add(salas.getString(1));
		}

		String sql = ("INSERT INTO Recurso_Has_Sala VALUES(?,?);");

		// falta verificar se a sala existe
		for (RecursoHasSala r : lista) {
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setInt(1, r.getRecurso());

			int i = 0;
			for (String s : listaNomeSalas) {
				if (r.getSala().equalsIgnoreCase(s)) {
					//pstm.setInt(2, listaSalas.get(i));
					pstm.setString(2, r.getSala());
				}
				i++;
			}
			//pstm.execute();
			pstm.executeUpdate();
		}
		con.commit();
	}

	/**
	 * executa uma query no banco. O resultado da query esta no ResultSet
	 * retornado pela funcao
	 *
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public ResultSet query(String query) throws SQLException {
		Statement pstt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet result = pstt.executeQuery(query);
		con.commit();
		return result;
	}

	/**
	 * Retorna um array list com os horarios das aulas
	 *
	 * @return
	 */
	public ArrayList<HorarioSala> getHorarioSala() {
		ArrayList<HorarioSala> lista = new ArrayList();
		try {
			Statement psts = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = psts.executeQuery("SELECT * FROM horario_sala");

			while (result.next()) {
				HorarioSala h = new HorarioSala();
				h.setHorario(result.getInt(2));
				h.setSala(result.getString(1));
				h.setTurma(result.getInt(3));
				lista.add(h);
			}
			con.commit();
		} catch (Exception e) {
		} finally {
			return lista;
		}
	}

	/**
	 * Checa o banco pelo login e senha entrados. Se o usuario existir, retorna
	 * ele, senao retorna null
	 *
	 * @param email
	 * @param senha
	 * @return
	 */
	public Usuario login(String email, String senha) {
		try {
			Statement psts = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = psts.executeQuery("SELECT * FROM Usuario u where u.email = \"" + email + "\" and u.senha = \"" + senha + "\";");

			if (result.next()) {
				Usuario u = new Usuario();
				u.setNumero_usp(result.getInt(1));
				u.setNome(result.getString(2));
				u.setEmail(result.getString(3));
				u.setSenha(result.getString(4));
				con.commit();
				return u;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * reseta o banco de dados
	 */
	public void reset() {
		String[] drops = {
			"delete from Horario_Sala;",
			"delete from Turma_has_recurso;",
			"delete from Recurso_has_Sala;",
			"delete from Recurso;",
			"delete from Horario_Turma;",
			"delete from Reserva;",
			"delete from Turma;",
			"delete from Semestre;",
			"delete from Preferencia;",
			"delete from Sala;",
			"delete from Andar;",
			"delete from Bloco;",
			"delete from CursoAno;",
			"delete from Disciplina;",
			"delete from Tipo_reserva;",
			"delete from Horario;",};

		try {
			for (String s : drops) {
				//System.err.println(s);
				PreparedStatement pstm = con.prepareStatement(s);
				pstm.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// reseta os contadores de ID
		Andar.reset();

		Bloco.reset();

		Recurso.reset();

		Horario.reset();

		Turma.reset();

		System.err.println(
				"Fim do Reset " + drops.length);
	}

	/**
	 * Busca o nome no banco para saber de que tabela o nome pertence
	 *
	 * @param query
	 * @return
	 */
	public int busca_nome(String query) {
		try {
			Statement psts = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = psts.executeQuery("SELECT codigo FROM CursoAno c where c.codigo = \'" + query + "\'");

			if (result.next()) {
				System.out.println("Busca acha que e' turma");
				return 1;
			} else {
				psts = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				result = psts.executeQuery("SELECT nome FROM Sala s where s.nome = \'" + query + "\'");

				if (result.next()) {
					System.out.println("Busca acha que e' sala");
					return 0;
				}
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// se nao achou, tenta sala
		return 0;
	}
}
