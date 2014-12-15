package excel_reader;

import java.util.ArrayList;
import model.Recurso;
import model.RecursoHasSala;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Recursos extends ExcelReader {

	private ArrayList<Recurso> recursos;
	private ArrayList<RecursoHasSala> recursoHasSala;

	public Recursos(String nome) {
		super(nome, 5);
		recursos = new ArrayList();
		recursoHasSala = new ArrayList();
	}

	@Override
	protected void readColumns(Row row) {

		if (!checkExcel(row)) {
			System.err.println("Recursos, linhas " + row.getRowNum() + " e' null");
			// mostrar mensagem de erro
			return;
		}

		//  ****** recurso ******
		// nome da sala
		Cell tempCell = row.getCell(0);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);
		String sala = tempCell.getStringCellValue().trim();

		int i = 1;
		for (Recurso r : recursos) {
			tempCell = row.getCell(i);
			tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			Double valor = tempCell.getNumericCellValue();
			int flag = valor.intValue();

			if (flag == 1) {
				RecursoHasSala tmp = new RecursoHasSala(r.getCodigo(), sala);
				recursoHasSala.add(tmp);
			}
			i++;
		}
	}

	@Override
	protected void readHeader(Row row) {

		int count = 0;

		boolean skip = true;
		for (Cell tempCell : row) {
			count++;
			if (skip == true) {
				skip = false;
				continue;
			}
			tempCell.setCellType(Cell.CELL_TYPE_STRING);
			String descricao = tempCell.getStringCellValue().trim();
			Recurso r = new Recurso();
			r.setDescricao(descricao);
			recursos.add(r);
		}
		nCols = count;
	}

	@Override
	protected void print() {
		for (Recurso r : recursos) {
			System.out.println("Codigo " + r.getCodigo());
			System.out.println("Descricao " + r.getDescricao());
		}

		for (RecursoHasSala r : recursoHasSala) {
			System.out.println("Sala " + r.getSala() + " Recurso: " + r.getRecurso());
		}
	}

	public ArrayList<Recurso> getRecursos() {
		return recursos;
	}

	public ArrayList<RecursoHasSala> getRecursoHasSala() {
		return recursoHasSala;
	}
}
