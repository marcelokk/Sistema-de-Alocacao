package excel_reader;

import java.util.ArrayList;
import model.Disciplina;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Disciplinas extends ExcelReader {

	private ArrayList<Disciplina> disciplinas;
	
	public Disciplinas(String nome) {
		super(nome, 1);
		disciplinas = new ArrayList();
		nCols = 3;
	}

	@Override
	protected void readColumns(Row row) {
		if(!checkExcel(row)){
			System.err.println("Disciplina, linhas " + row.getRowNum() + " e' null");
			return;
		}		
		
		//  ****** disciplinas ******
		// sigla da disciplina
		Cell tempCell = row.getCell(0);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);
		String sigla = tempCell.getStringCellValue().trim();

		// nome da discipina
		tempCell = row.getCell(1);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);		
		String disciplina = tempCell.getStringCellValue().trim();

		// creditos
		tempCell = row.getCell(2);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);		
		Double valor = tempCell.getNumericCellValue();
		Integer creditos = valor.intValue();
		
		// nova disciplina
		Disciplina d = new Disciplina();
		d.setCodigo(sigla);
		d.setNome(disciplina);
		d.setCreditos(creditos);
		disciplinas.add(d);
	}
	
	@Override
	protected void print() {
		for(Disciplina d : disciplinas) {
			System.out.println("codigo " + d.getCodigo());
			System.out.println("nome " + d.getNome());
			System.out.println("creditos " + d.getCreditos());
		}
	}

	public ArrayList<Disciplina> getDisciplinas() {
		return disciplinas;
	}
}
