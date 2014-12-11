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
import heuristica.HeuristicaInterface;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
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
public class Servlet extends HttpServlet {

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

		if (acao != null) {
			// pagina da home
			if (acao.equals("login")) {
				atualiza_lista();
				atualiza_tabela("", 0);
				url = "login.jsp";
			}
			// usuario clica em logout na home
			else if (acao.equals("logout")) {
				session.setAttribute("currentUser", null);
				url = "login.jsp";
			}
			// usuario clica no link da pagina de upload do excel
			else if (acao.equals("uploadExcel")) {
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
				url = "login.jsp";
			}
		} // usuario clica no botao de cadastro na pagina de login 
		else if (acao.equals("cadastrar")) {
			Usuario u = new Usuario();
			u.setNome(request.getParameter("nome"));
			u.setEmail(request.getParameter("email"));
			u.setNumero_usp(Integer.parseInt(request.getParameter("numerousp")));
			u.setSenha(request.getParameter("password"));
			Banco.getInstance().inserirUsuario(u);
			url = "login.jsp";
		} // usuario clica no link para edicao dos dados pessoais na home 
		else if (acao.equals("editar_dados")) {
			Usuario u = (Usuario) session.getAttribute("currentUser");
			u.setNome(request.getParameter("nome"));
			u.setNumero_usp(Integer.parseInt(request.getParameter("numerousp")));
			u.setSenha(request.getParameter("password"));
			Banco.getInstance().updateUsuario(u);
			url = "home.jsp";
		} // usuario clica no botao buscar 
		else if (acao.equals("search")) {
			int tipo = Banco.getInstance().busca_nome(request.getParameter("sala"));
			atualiza_tabela(request.getParameter("sala"), tipo);
			url = "home.jsp";
		} // usuario clica no botao de upload 
		else if (acao.equals("upload_excel")) {
			System.out.println("Upload");
			url = "uploadExcel.jsp";
			String path = UPLOAD_DIRECTORY + File.separator;

			ArrayList<String> listaNomes = new ArrayList();

			if (ServletFileUpload.isMultipartContent(request)) {
				try {
					List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

					for (FileItem item : multiparts) {
						if (!item.isFormField()) {
							String name = new File(item.getName()).getName();
							item.write(new File(path + name));
							listaNomes.add(name);
							System.out.println("Nome " + name + " path " + path + name);
						}
					}
					request.setAttribute("message", "File Uploaded Successfully");

					try {
						Banco.getInstance().reset();

						System.out.println("CursoAno " + listaNomes.get(0));
						cursos = new CursosAno(path + listaNomes.get(0));
						cursos.read(true);
						Banco.getInstance().cursoAno(cursos.getCursos());

						System.out.println("Salas");
						salas = new Salas(path + listaNomes.get(0));
						salas.read(true);
						Banco.getInstance().bloco(salas.getBlocos());
						Banco.getInstance().andar(salas.getAndar());
						Banco.getInstance().sala(salas.getSalas());

						System.out.println("Disciplinas");
						disciplinas = new Disciplinas(path + listaNomes.get(0));
						disciplinas.read(true);
						Banco.getInstance().disciplina(disciplinas.getDisciplinas());

						System.out.println("Recursos");
						recursos = new Recursos(path + listaNomes.get(0));
						recursos.read(true);
						Banco.getInstance().recurso(recursos.getRecursos());
						Banco.getInstance().recursoHasSala(recursos.getRecursoHasSala());

						System.out.println("Turmas");
						turmas = new Turmas(path + listaNomes.get(0));
						turmas.read(true);
						Banco.getInstance().turma(turmas.getTurmas());

						System.out.println("Preferencias");
						preferencias = new Preferencias(path + listaNomes.get(0));
						preferencias.read(true);
						Banco.getInstance().preferencia(preferencias.getPreferencias());

						System.out.println("Horarios");
						horarios = new Horarios(path + listaNomes.get(0));
						horarios.read(true);
						Banco.getInstance().horario(horarios.getHorarios());

						System.out.println("Horario Turma");
						Banco.getInstance().horario_turma(turmas.getTurmas());

						HeuristicaInterface heuristica = new HeuristicaInterface(path);
						heuristica.geraInput("input.txt");

						heuristica.executarMetaHeuristica();

						heuristica.lerArquivo("output.txt");
						System.out.println("Working Directory = " + System.getProperty("user.dir"));

						atualiza_lista();
						atualiza_tabela("", 0);
						url = "home.jsp";
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					request.setAttribute("message", "File Upload Failed due to " + ex);
					System.out.println("File Upload Failed due to " + ex);
				}
			} else {
				request.setAttribute("message", "Sorry this Servlet only handles file upload request");
				System.out.println("Nao e' multipart content");
			}
		} else if (acao.equals("teste")) {
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
	 * @param sala
	 * @param tipo_busca 
	 */
	private void atualiza_tabela(String sala, int tipo_busca) {
		ArrayList<String> lista = new ArrayList();
		ArrayList<String> listaHorarios = new ArrayList();
		String titulo = "Titulo";
		String sql = "";

		try {
			ResultSet horarios = Banco.getInstance().query("select distinct inicio, fim from horario");

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

				tempo = horarios.getInt(2);
				minutos = tempo % 60;
				hora = (tempo - minutos) / 60;
				String fim = null;

				if (minutos == 0) {
					fim = hora.toString() + ":00";
				} else {
					fim = hora.toString() + ":" + minutos.toString();
				}
				//System.out.println("hora: " + hora + " minutos " + minutos + " resultado " + a);
				listaHorarios.add(inicio + " - " + fim);
			}

			for (int i = 0; i < listaHorarios.size() * 7; i++) {
				lista.add(" ");
			}

			switch (tipo_busca) {
				case 0:
					while (result.next()) {
						Integer i = result.getInt(1);
						String tmp = result.getString(2) + "\n" + result.getString(3);
						lista.set(i, tmp);
					}
					break;
				case 1:
					while (result.next()) {
						Integer i = result.getInt(1);
						String tmp = result.getString(4) + "\n" + result.getString(3);
						lista.set(i, tmp);
					}
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Lista");
		int i = 0;
		int j = 0;
		for (String a : lista) {
			if (i == 7) {
				System.out.println();
				i = 0;
			}
			System.out.print(j + " " + a + " ");
			i++;
			j++;
		}

		session.setAttribute("horarios_size", listaHorarios.size());
		session.setAttribute("titulo_busca", titulo);
		session.setAttribute("currentSala", sala);
		session.setAttribute("horarios", listaHorarios);
		session.setAttribute("horario_sala", lista);
	}
}