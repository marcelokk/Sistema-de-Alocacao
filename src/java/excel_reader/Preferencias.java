package excel_reader;

import java.util.ArrayList;
import model.Preferencia;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Preferencias extends ExcelReader {

	private ArrayList<Preferencia> lista;
	private ArrayList<String> cursos;

	public Preferencias(String nome) {
		super(nome, 6);
		lista = new ArrayList();
		cursos = new ArrayList();
	}

	@Override
	protected void readColumns(Row row) {

		boolean skip = true;
		String sala = null;

		for (Cell tempCell : row) {

			if (skip == true) {
				sala = tempCell.getStringCellValue();
				skip = false;
				continue;
			}

			if (tempCell == null || tempCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				break;
			}

			Double prioridade = tempCell.getNumericCellValue();
			int p = prioridade.intValue();
			Preferencia pref = new Preferencia(cursos.get(tempCell.getColumnIndex() - 1), sala, p);
			lista.add(pref);
		}
	}

	@Override
	protected void readHeader(Row row) {

		boolean skip = true;
		for (Cell tempCell : row) {

			if (skip == true) {
				skip = false;
				continue;
			}
			String curso = tempCell.getStringCellValue();
			cursos.add(curso);
		}
	}

	@Override
	protected void print() {
		for (Preferencia p : lista) {
			System.out.println(p.getSala() + " " + p.getCurso() + " " + p.getPrioridade());
		}
	}

	public ArrayList<Preferencia> getPreferencias() {
		return lista;
	}
}
