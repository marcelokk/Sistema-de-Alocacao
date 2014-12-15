package excel_reader;

import java.util.ArrayList;
import model.Andar;
import model.Bloco;
import model.Sala;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Salas extends ExcelReader {

	private ArrayList<Sala> salas;
	private ArrayList<Andar> andares;
	private ArrayList<Bloco> blocos;
	
	public Salas(String nome) {
		super(nome, 4);
		salas = new ArrayList();
		andares = new ArrayList();
		blocos = new ArrayList();
		nCols = 5;
	}

	@Override
	protected void readColumns(Row row) {
		if(!checkExcel(row)){
			System.err.println("Salas, linhas " + row.getRowNum() + " e' null");
			return;
		}		
		
		// ****** Sala ******
		// nome da sala
		Cell tempCell = row.getCell(0);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);		
		String sala = tempCell.getStringCellValue().trim();

		// capacidade
		tempCell = row.getCell(1);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);		
		Double valor = tempCell.getNumericCellValue();
		Integer capacidade = valor.intValue();

		// pref vazia
		tempCell = row.getCell(2);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);		
		valor = tempCell.getNumericCellValue();
		Integer vazia = valor.intValue();

		// bloco
		tempCell = row.getCell(3);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);		
		String bloco = tempCell.getStringCellValue().trim();

		int blocoID = -1;
		
		boolean flag = true;
		for(Bloco b : blocos) {
			if(b.getCodigo().equalsIgnoreCase(bloco)) {
				flag = false;
				blocoID = b.getId();
				break;
			}
		}
		if(flag) {
			Bloco b = new Bloco();
			b.setCodigo(bloco);
			b.setInstituto("teste");
			blocoID = b.getId();
			blocos.add(b);
		}
		
		// andar
		tempCell = row.getCell(4);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);		
		valor = tempCell.getNumericCellValue();
		Integer andar = valor.intValue();
		
		int andarID = -1;
		flag = true;
		for(Andar a : andares) {
			if(a.getBloco() == blocoID && a.getAndar() == andar) {
				flag = false;
				andarID = a.getId();
				break;
			}
		}
		if(flag) {
			Andar a = new Andar();
			a.setAndar(andar);
			a.setBloco(blocoID);
			andarID = a.getId();
			andares.add(a);
		}
		
		Sala s = new Sala();
		s.setCapacidade(capacidade);
		s.setVazia(vazia);
		s.setSala(sala);
		s.setAndar(andarID);
		salas.add(s);
	}
	
	@Override
	protected void print() {
		for(Sala s : salas) {
			//System.out.println("ID " + s.getId());
			System.out.println("Sala " + s.getSala());
			System.out.println("Capacidade " + s.getCapacidade());
			System.out.println("Vazia " + s.getVazia());
		}
	}

	public ArrayList<Sala> getSalas() {
		return salas;
	}

	public ArrayList<Andar> getAndar() {
		return andares;
	}

	public ArrayList<Bloco> getBlocos() {
		return blocos;
	}
}
