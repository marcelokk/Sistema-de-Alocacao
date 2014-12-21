package excel_writer;

import control.Celula;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import singleton.Banco;

public class ExcelWriter {

	public void write() {

		System.err.println("excel writer foi chamado");

		HSSFWorkbook workbook = new HSSFWorkbook();	// novo workbook
		HSSFSheet sheet = workbook.createSheet("Sala");	// comeca uma nova aba no excel

		// Style the cell with borders all around.
		HSSFCellStyle box = workbook.createCellStyle();
		box.setBorderBottom(CellStyle.BORDER_THIN);
		box.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		box.setBorderLeft(CellStyle.BORDER_THIN);
		box.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		box.setBorderRight(CellStyle.BORDER_THIN);
		box.setRightBorderColor(IndexedColors.BLACK.getIndex());

		box.setBorderTop(CellStyle.BORDER_THIN);
		box.setTopBorderColor(IndexedColors.BLACK.getIndex());

		box.setAlignment(CellStyle.ALIGN_CENTER);

		// cria o estilo para fazer a celula ficar em negrito
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle bold = workbook.createCellStyle();

		bold.setFont(font);
		bold.setAlignment(CellStyle.ALIGN_CENTER);

		bold.setBorderBottom(CellStyle.BORDER_THIN);
		bold.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		bold.setBorderLeft(CellStyle.BORDER_THIN);
		bold.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		bold.setBorderRight(CellStyle.BORDER_THIN);
		bold.setRightBorderColor(IndexedColors.BLACK.getIndex());

		bold.setBorderTop(CellStyle.BORDER_THIN);
		bold.setTopBorderColor(IndexedColors.BLACK.getIndex());

		// String default para celulas celulas se conteudo
		String default_cell = "-";

		//ArrayList<String> lista = new ArrayList();
		ArrayList<String> listaHorarios = new ArrayList();
		ArrayList<ArrayList<Celula>> lista = new ArrayList();

		Celula c;

		String[] dias = {"segunda",
			"terca",
			"quarta",
			"quinta",
			"sexta",
			"sabado"
		};

		try {
			// monta a lista de horarios
			ResultSet horarios = Banco.getInstance().query("select distinct inicio from horario");

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

			// monta a lista de salas e seus horarios
			ResultSet salas = Banco.getInstance().query("select nome from sala");

			int rownum = 0;
			int cellnum = 0;
			while (salas.next()) {
				lista.clear();
				String sala = salas.getString(1);
				ResultSet result = Banco.getInstance().query("select h.id_horario, t.codigo_curso, t.codigo_disciplina, h.nome_sala, t.codigo  from horario_sala h join turma t where h.codigo_turma = t.codigo and h.nome_sala = \'" + sala + "\' order by h.id_horario");

				// escreve nome da sala
				cellnum = 0;
				Row row = sheet.createRow(rownum++);
				Cell cell = row.createCell(cellnum++);
				cell.setCellValue("Sala: " + sala);
				sheet.addMergedRegion(new CellRangeAddress(rownum - 1, rownum - 1, cellnum - 1, cellnum - 1 + listaHorarios.size()));
				cell.setCellStyle(bold);

				// escreve os horarios
				row = sheet.createRow(rownum++);
				for (String s : listaHorarios) {
					cell = row.createCell(cellnum++);
					cell.setCellValue(s);
					cell.setCellStyle(bold);
				}

				// cada horario tem em um dia da semana
				for (int i = 0; i < dias.length; i++) {
					ArrayList<Celula> dia = new ArrayList();
					//for (int j = 0; j < listaHorarios.size(); j++) {
					//	dia.add(new Celula());
					//}
					lista.add(dia);
				}

				Celula[][] celulas = new Celula[dias.length][listaHorarios.size()];
				for (int i = 0; i < dias.length; i++) {
					for (int j = 0; j < listaHorarios.size(); j++) {
						celulas[i][j] = null;
					}
				}

				// guarda na lista o conteudo
				while (result.next()) {
					Integer id = result.getInt(1);

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
							ResultSet result_tmp = Banco.getInstance().query("select id_horario from horario_turma h where h.codigo_turma = \'" + result.getInt(5) + "\'");
							int count = 0;
							while (result_tmp.next()) {
								count++;
							}
							if (count == 3) {
								ncreditos = 2;
							} else {
								ncreditos = 3;
							}
						default:
							ncreditos = 1;
							break;
					}

					String tmp = result.getString(2) + " " + result.getString(3);

					int linha = id / listaHorarios.size(); // qual dia esta o horario
					int coluna = id % listaHorarios.size(); // me diz qual coluna esta o horario

					c = new Celula(tmp, ncreditos, false);
					celulas[linha][coluna] = c;
					//lista.get(linha).set(coluna, c);
				}

				for (int i = 0; i < dias.length; i++) {
					for (int j = 0; j < listaHorarios.size(); j++) {
						System.err.println("i " + i + " j " + j);
						if (celulas[i][j] == null) {
							lista.get(i).add(new Celula());
						} else {
							lista.get(i).add(celulas[i][j]);
							j += celulas[i][j].getTamanho() - 1;
						}
					}
				}

				/*
				// acerta o tamanho de cada linha da tabela
				for (ArrayList<Celula> a : lista) {
					int tamanho = 0;
					for (int i = 0; i < a.size(); i++) {
						tamanho += a.get(i).getTamanho();
						if (tamanho > listaHorarios.size()) {
							for (int j = i; j < a.size();) {
								a.remove(j);
							}
						}
					}
				}
				*/

				// escreve os dias da semana
				for (int i = 0; i < dias.length; i++) {
					cellnum = 0;
					row = sheet.createRow(rownum++);
					cell = row.createCell(cellnum++);
					cell.setCellValue(dias[i]);
					cell.setCellStyle(bold);

					// salva o conteudo no sheet
					for (Celula conteudo : lista.get(i)) {
						cell = row.createCell(cellnum++);
						/* cor das celulas
						 if (conteudo.equals(default_cell)) {
						 box.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
						 box.setFillPattern(CellStyle.SOLID_FOREGROUND);
						 } else {
						 box.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
						 box.setFillPattern(CellStyle.SOLID_FOREGROUND);
						 }
						 */

						cell.setCellStyle(box);
						cell.setCellValue(conteudo.getConteudo());

						if (conteudo.getTamanho() > 1) {
							int tamanho = conteudo.getTamanho();
							sheet.addMergedRegion(new CellRangeAddress(rownum - 1, rownum - 1, cellnum - 1, cellnum - 2 + tamanho));
							cellnum = cellnum + tamanho - 1;
							System.err.println("Conteudo: " + conteudo.getConteudo() + " " + (cellnum - 1) + " " + (cellnum - 2 + tamanho));
						}
						if (conteudo.getPula()) {
							break;
						}
					}
				}
				// pula uma linha entre as salas
				sheet.createRow(rownum++);
			}

			/*
			 // Aba de turmas
			 sheet = workbook.createSheet("Turma");	// comeca uma nova aba no excel

			 // monta a lista de salas e seus horarios
			 ResultSet turmas = Banco.getInstance().query("select codigo_curso from turma");

			 while (turmas.next()) {
			 ResultSet result = Banco.getInstance().query("select h.id_horario, t.codigo_curso, t.codigo_disciplina, h.nome_sala from horario_sala h join turma t where h.codigo_turma = t.codigo and t.codigo_curso = \"" + turmas.getString(1) + "\' order by h.id_horario");

			 while (result.next()) {
			 Integer i = result.getInt(1);
			 String tmp = result.getString(4) + "\n" + result.getString(3);
			 lista.set(i, tmp);
			 }
			 }
			 */
			// ajusta o tamanho das colunas
			for (int i = 0; i < listaHorarios.size(); i++) {
				sheet.autoSizeColumn(i, true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// escreve o conteudo no arquivo de saida
		try {
			FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Marcelo\\Desktop\\new.xls"));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
