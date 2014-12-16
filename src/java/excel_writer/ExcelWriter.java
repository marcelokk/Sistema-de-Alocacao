package excel_writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import singleton.Banco;

public class ExcelWriter {

	public void write() {

		System.err.println("excel writer foi chamado");

		HSSFWorkbook workbook = new HSSFWorkbook();	// novo workbook
		HSSFSheet sheet = workbook.createSheet("Sala");	// comeca uma nova aba no excel

		ArrayList<String> lista = new ArrayList();
		ArrayList<String> listaHorarios = new ArrayList();

		String[] dias = {"segunda", "terca", "quarta", "quinta", "sexta", "sabado"};

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
				String sala = salas.getString(1);
				ResultSet result = Banco.getInstance().query("select h.id_horario, t.codigo_curso, t.codigo_disciplina, h.nome_sala from horario_sala h join turma t where h.codigo_turma = t.codigo and h.nome_sala = \'" + sala + "\' order by h.id_horario");

				// escreve nome da sala
				cellnum = 0;
				Row row = sheet.createRow(rownum++);
				Cell cell = row.createCell(cellnum++);
				cell.setCellValue("Nome da Sala");
				
				cell = row.createCell(cellnum);				
				cell.setCellValue(sala);

				// escreve os horarios
				row = sheet.createRow(rownum++);
				for (String s : listaHorarios) {
					cell = row.createCell(cellnum++);
					cell.setCellValue(s);
				}

				// reseta lista
				for (int i = 0; i < listaHorarios.size() * 7; i++) {
					lista.add("-");
				}

				// guarda na lista o conteudo
				while (result.next()) {
					Integer i = result.getInt(1);
					String tmp = result.getString(2) + "\n" + result.getString(3);
					lista.set(i, tmp);
				}

				// escreve os dias da semana
				for (int i = 0; i < dias.length; i++) {
					cellnum = 0;
					row = sheet.createRow(rownum++);
					cell = row.createCell(cellnum++);
					cell.setCellValue(dias[i]);

					// salva o conteudo no sheet
					for (int j = 0; j < listaHorarios.size(); j++) {
						cell = row.createCell(cellnum++);
						cell.setCellValue(lista.get(j + i*listaHorarios.size()));
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
