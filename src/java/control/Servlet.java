package control;

// o path para os excel estao hardcoded
// fazer o upload mostrar mensagens
import com.google.gson.Gson;
import excel_reader.CursosAno;
import excel_reader.Disciplinas;
import excel_reader.Horarios;
import excel_reader.Preferencias;
import excel_reader.Recursos;
import excel_reader.Salas;
import excel_reader.Turmas;
import excel_writer.ExcelWriter;
import heuristica.HeuristicaInterface;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Usuario;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import singleton.Banco;

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet { // implements ServletInterface

	private String url;
	private HttpSession session;
	private CursosAno cursos = null;
	private Disciplinas disciplinas = null;
	private Horarios horarios = null;
	private Preferencias preferencias = null;
	private Recursos recursos = null;
	private Salas salas = null;
	private Turmas turmas = null;
	private final String UPLOAD_DIRECTORY = "C:\\Users\\Marcelo\\Desktop\\Sistema";

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		session = request.getSession();
		String acao = (String) request.getParameter("acao");
		url = "";

		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		if (acao != null) {
			// pagina da home
			if (acao.equals("login")) {
				atualiza_lista();
				atualiza_tabela("", 0);
				session.setAttribute("mensagem", "");	// mensagem para login errado
				url = "login.jsp";
			} // usuario clica em logout na home
			else if (acao.equals("logout")) {
				session.setAttribute("currentUser", null);
				url = "login.jsp";
			} // usuario clica no link da pagina de upload do excel
			else if (acao.equals("uploadExcel")) {
				session.setAttribute("mensagem_excel", "");
				url = "uploadExcel.jsp";
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		session = request.getSession();
		String acao = (String) request.getParameter("acao");
		url = "";

		// usuario clica no botao logar na pagina de login
		if (acao.equals("logar")) {
			String login = request.getParameter("login");
			String password = request.getParameter("password");

			Usuario u = Banco.getInstance().login(login, password);

			if (u != null) {
				session.setAttribute("currentUser", u);
				url = "home.jsp";
			} else {
				session.setAttribute("mensagem", "Login ou Senha invalidos");	// erro do login
				url = "login.jsp";
			}
		} // usuario clica no botao de cadastro na pagina de login 
		else if (acao.equals("cadastrar")) {
			Usuario u = new Usuario();
			u.setNome(request.getParameter("nome"));
			u.setEmail(request.getParameter("email"));
			u.setNumero_usp(Integer.parseInt(request.getParameter("numerousp")));
			u.setSenha(request.getParameter("password"));
			try {
				Banco.getInstance().inserirUsuario(u);
			} catch (SQLException e) {
				e.printStackTrace();
				// erro no cadastro do usuario
			}

			url = "login.jsp";
		} // usuario clica no link para edicao dos dados pessoais na home 
		else if (acao.equals("editar_dados")) {
			Usuario u = (Usuario) session.getAttribute("currentUser");
			u.setNome(request.getParameter("nome"));
			u.setNumero_usp(Integer.parseInt(request.getParameter("numerousp")));
			u.setSenha(request.getParameter("password"));

			try {
				Banco.getInstance().updateUsuario(u);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			url = "home.jsp";
		} // usuario clica no botao buscar 
		else if (acao.equals("search")) {
			int tipo = Banco.getInstance().busca_nome(request.getParameter("sala"));
			atualiza_tabela(request.getParameter("sala"), tipo);
			url = "home.jsp";
		} // usuario clica no botao de upload 
		else if (acao.equals("upload_excel")) {

			ArrayList<String> mensagem = new ArrayList();
			String erro = "";

			url = "uploadExcel.jsp";
			String path = UPLOAD_DIRECTORY + File.separator;

			ArrayList<String> listaNomes = new ArrayList();

			erro = "ERRO - Upload";
			if (ServletFileUpload.isMultipartContent(request)) {
				try {
					List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

					for (FileItem item : multiparts) {
						if (!item.isFormField()) {
							String name = new File(item.getName()).getName();
							item.write(new File(path + name));
							listaNomes.add(name);
							System.err.println("Nome " + name + " path " + path + name);
						}
					}
					mensagem.add("Upload OK");

					try {
						erro = "ERRO - Reset do Banco";
						Banco.getInstance().reset();
						mensagem.add("Reset do Banco OK");

						cursos = new CursosAno(path + listaNomes.get(0));
						cursos.read(false);
						erro = Banco.getInstance().cursoAno(cursos.getCursos());
						if (!"".equals(erro)) {
							throw new SQLException();
						}
						mensagem.add("Cursos OK");

						salas = new Salas(path + listaNomes.get(0));
						salas.read(false);
						Banco.getInstance().bloco(salas.getBlocos());
						Banco.getInstance().andar(salas.getAndar());
						erro = Banco.getInstance().sala(salas.getSalas());
						if (!"".equals(erro)) {
							throw new SQLException();
						}
						mensagem.add("Salas OK");

						disciplinas = new Disciplinas(path + listaNomes.get(0));
						disciplinas.read(false);
						erro = Banco.getInstance().disciplina(disciplinas.getDisciplinas());
						if (!"".equals(erro)) {
							throw new SQLException();
						}
						mensagem.add("Disciplinas OK");

						erro = "ERRO - Tabela Recursos";
						recursos = new Recursos(path + listaNomes.get(0));
						recursos.read(false);
						Banco.getInstance().recurso(recursos.getRecursos());
						Banco.getInstance().recursoHasSala(recursos.getRecursoHasSala());
						mensagem.add("Recursos OK");

						turmas = new Turmas(path + listaNomes.get(0));
						turmas.read(false);
						erro = Banco.getInstance().turma(turmas.getTurmas());
						if (!"".equals(erro)) {
							throw new SQLException();
						}
						mensagem.add("Turmas OK");

						erro = "ERRO - Tabela Preferencias";
						preferencias = new Preferencias(path + listaNomes.get(0));
						preferencias.read(false);
						Banco.getInstance().preferencia(preferencias.getPreferencias());
						mensagem.add("Preferencias OK");

						horarios = new Horarios(path + listaNomes.get(0));
						horarios.read(false);
						erro = Banco.getInstance().horario(horarios.getHorarios());
						if (!"".equals(erro)) {
							throw new SQLException();
						}
						mensagem.add("Horarios OK");

						erro = "ERRO - Cruzamento dos dados entre Turmas e Horarios";
						Banco.getInstance().horario_turma(turmas.getTurmas());
						mensagem.add("Horarios das Turmas OK");

						HeuristicaInterface heuristica = new HeuristicaInterface(path);
						heuristica.geraInput("input.txt");

						heuristica.executarMetaHeuristica();

						heuristica.lerArquivo("output.txt");
						System.out.println("Working Directory = " + System.getProperty("user.dir"));

						atualiza_lista();
						atualiza_tabela("", 0);
						url = "home.jsp";
					} catch (SQLException e) {
						mensagem.add(erro);
						e.printStackTrace();
					}
				} catch (Exception ex) {
					System.out.println("File Upload Failed due to " + ex);
				}
				session.setAttribute("mensagem_excel", mensagem.get(mensagem.size() - 1));
				for (String s : mensagem) {
					System.err.println(s);
				}
			} else {
				request.setAttribute("message", "Sorry this Servlet only handles file upload request");
				System.out.println("Nao e' multipart content");
			}
		} else if (acao.equals("gerar")) {
			ExcelWriter writer = new ExcelWriter();
			writer.write();
			url = "home.jsp";
		} else if (acao.equals(
				"teste")) {
			System.out.println("Este teste deu certo");
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(url);

		dispatcher.forward(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

	/**
	 * atualiza a lista de sugestao para busca na pagina home
	 */
	private void atualiza_lista() {
		try {
			ArrayList<String> listaSugestao = new ArrayList();		// lista de sugestoes
			ResultSet result = Banco.getInstance().query("select nome from sala");	// recupera o nome das salas do banco
			while (result.next()) {									// coloca todos os nomes em uma lista
				listaSugestao.add(result.getString(1));
			}

			result = Banco.getInstance().query("select codigo from cursoAno");	// recupera os codigos dos cursos
			while (result.next()) {									// coloca todos os codigos na lista de sugestoes
				listaSugestao.add(result.getString(1));
			}

			String json = new Gson().toJson(listaSugestao);		// converte a lista em JSON
			session.setAttribute("salas", json);				// coloca o JSON na sessao para a home poder acessar
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * atualiza a tabela da home, colocando na sessao o conteudo da tabela
	 *
	 * @param sala
	 * @param tipo_busca
	 */
	private void atualiza_tabela(String sala, int tipo_busca) {
		ArrayList<ArrayList> lista = new ArrayList();
		ArrayList<String> listaHorarios = new ArrayList();
		String titulo = "Titulo";
		String sql = "";

		String[] dias = {"segunda",
			"terca",
			"quarta",
			"quinta",
			"sexta",
			"sabado"
		};

		Celula c;
		try {
			ResultSet horarios = Banco.getInstance().query("select distinct inicio from horario");

			switch (tipo_busca) {
				case 0:
					titulo = "Sala Selecionada";
					sql = "h.codigo_turma = t.codigo and h.nome_sala = \"" + sala + "\"";
					break;
				case 1:
					titulo = "Turma Selecionada";
					sql = "h.codigo_turma = t.codigo and t.codigo_curso = \"" + sala + "\"";
					break;
				default:
					break;
			}
			ResultSet result = Banco.getInstance().query("select h.id_horario, t.codigo_curso, t.codigo_disciplina, h.nome_sala from horario_sala h join turma t where " + sql + " order by h.id_horario");

			while (horarios.next()) {
				Integer tempo = horarios.getInt(1);
				Integer minutos = tempo % 60;
				Integer hora = (tempo - minutos) / 60;
				String inicio = null;
				if (minutos == 0) {
					inicio = hora.toString() + ":00";
				} else {
					inicio = hora.toString() + ":" + minutos.toString();
				}
				listaHorarios.add(inicio);
			}

			// cada horario tem em um dia da semana
			for (int i = 0; i < dias.length; i++) {
				ArrayList<Celula> dia = new ArrayList();
				for (int j = 0; j < listaHorarios.size(); j++) {
					dia.add(new Celula());
				}
				lista.add(dia);
			}

			while (result.next()) {
				Integer id = result.getInt(1);	// id do horario
				String tmp = null;				// conteudo da celula

				// recupera o numero de creditos da disciplina
				ResultSet creditos = Banco.getInstance().query("select d.numero_creditos from disciplina d where d.codigo = \'" + result.getString(3) + "\'");
				creditos.next();
				Integer ncreditos = creditos.getInt(1);

				switch (ncreditos) {
					case 2:
					case 3:
						//ncreditos = ncreditos;
						break;
					case 4:
						ncreditos = 2;
						break;
					case 6:
						ncreditos = 3;
						break;
					default:
						ncreditos = 1;
						break;
				}

				switch (tipo_busca) {
					case 0:
						tmp = result.getString(2) + "\n" + result.getString(3); // codigo do curso e codigo da disciplina
						break;

					case 1:
						tmp = result.getString(4) + "\n" + result.getString(3);	// nome da sala e codigo da disciplina
						break;

					default:
						break;
				}

				int linha = id / listaHorarios.size(); // qual dia esta o horario
				int coluna = id % listaHorarios.size(); // me diz qual coluna esta o horario

				c = new Celula(tmp, ncreditos, false);
				lista.get(linha).set(coluna, c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// acerta o tamanho de cada linha da tabela
		for (ArrayList<Celula> a : lista) {
			int tamanho = 0;
			for (int i = 0; i < a.size(); i++) {
				tamanho += a.get(i).getTamanho();
				if (tamanho > listaHorarios.size()) {
					for (int j = i; j < a.size(); ) {
						a.remove(j);
					}
				}
			}
		}

		int i = 0, j = 0;
		System.out.println("Lista");
		for (ArrayList<Celula> a : lista) {
			for (Celula cell : a) {
				System.out.print("[" + i + "," + j + "]" + cell.getConteudo().replace("\n", "") + " ");
				j++;
			}
			System.out.println();
			i++;
		}

		session.setAttribute("segunda", lista.get(0));
		session.setAttribute("terca", lista.get(1));
		session.setAttribute("quarta", lista.get(2));
		session.setAttribute("quinta", lista.get(3));
		session.setAttribute("sexta", lista.get(4));
		session.setAttribute("sabado", lista.get(5));

		session.setAttribute("dias", dias);
		session.setAttribute("horarios_size", listaHorarios.size());
		session.setAttribute("titulo_busca", titulo);
		session.setAttribute("currentSala", sala);
		session.setAttribute("horarios", listaHorarios);
		//session.setAttribute("horario_sala", lista);
	}

	public void error(String message) {
		System.out.println("Servlet recebeu o erro " + message);
	}
}
