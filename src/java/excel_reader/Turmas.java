package excel_reader;

import java.util.ArrayList;
import model.Turma;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import model.Horario;

public class Turmas extends ExcelReader {

	private ArrayList<Turma> turmas;

	public Turmas(String nome) {
		super(nome, 3);
		turmas = new ArrayList();
		nCols = 4;
	}

	@Override
	protected void readColumns(Row row) {
		if(!checkExcel(row)){
			System.err.println("Turmas, linhas " + row.getRowNum() + " e' null");
			return;
		}	
		
		if(row.getCell(0).getStringCellValue().isEmpty())
			return;
		
		// ****** Turma ******
		// curso
		Cell tempCell = row.getCell(0);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);
		String curso = tempCell.getStringCellValue().trim();
		
		// sigla disciplina
		tempCell = row.getCell(1);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);
		String disciplina = tempCell.getStringCellValue().trim();

		// inscritos
		tempCell = row.getCell(2);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		Double valor = tempCell.getNumericCellValue();
		Integer inscritos = valor.intValue();

		// horarios - arrumar para dar uma parse e fazer numero de colunas variavel
		ArrayList<Horario> horarios = new ArrayList();
		int i;
		Horario h = null;
		for (i = 3; i < row.getLastCellNum(); i++) {
			if (row.getCell(i) == null || row.getCell(i).getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				break;
			}
			
			tempCell.setCellType(Cell.CELL_TYPE_STRING);
			String hora = row.getCell(i).getStringCellValue().trim();	
			
			String[] tokens = hora.split("[ ]+", 2);
			if(tokens.length == 1) {
				tokens = hora.split("\\.", 2);
			}
			
			tokens = tokens[1].split("/");
			
			String comeco = new String(tokens[0].replaceAll("\\s", ""));
			comeco = comeco.trim();
			
			String fim = new String(tokens[1].replaceAll("\\s", ""));
			fim = fim.trim();
			
			if (hora.regionMatches(true, 0, "Seg", 0, 3)) {
				h = new Horario(Horario.SEGUNDA, comeco, fim);
			} else if (hora.regionMatches(true, 0, "Ter", 0, 3)) {
				h = new Horario(Horario.TERCA, comeco, fim);
			} else if (hora.regionMatches(true, 0, "Qua", 0, 3)) {
				h = new Horario(Horario.QUARTA, comeco, fim);
			} else if (hora.regionMatches(true, 0, "Qui", 0, 3)) {
				h = new Horario(Horario.QUINTA, comeco, fim);
			} else if (hora.regionMatches(true, 0, "Sex", 0, 3)) {
				h = new Horario(Horario.SEXTA, comeco, fim);
			} else if (hora.regionMatches(true, 0, "Sab", 0, 3)) {
				h = new Horario(Horario.SABADO, comeco, fim);
			} else if (hora.regionMatches(true, 0, "Dom", 0, 3)) {
				h = new Horario(Horario.DOMINGO, comeco, fim);
			} else {
				System.out.println("excecao");
			}
			horarios.add(h);
		}

		Turma t = new Turma();
		t.setCurso(curso);
		t.setDisciplina(disciplina);
		t.setInscritos(inscritos);
		t.setHorarios(horarios);
		turmas.add(t);
	}

	@Override
	protected void print() {
		//*
		for (Turma t : turmas) {
			System.out.println("Curso " + t.getCurso());
			System.out.println("Disciplina " + t.getDisciplina());
			System.out.println("Inscritos " + t.getInscritos());
			for (Horario h : t.getHorarios()) {
				System.out.println("- " + h.getInicio() + "  " + h.getFim());
			}
		}
		//*/
	}

	public ArrayList<Turma> getTurmas() {
		return turmas;
	}
}
