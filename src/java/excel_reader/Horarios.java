package excel_reader;

import java.util.ArrayList;
import java.util.Date;
import model.Horario;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Horarios extends ExcelReader {

	private ArrayList<Horario> horarios;
	private int nhorarios = 0;

	public Horarios(String nome) {
		super(nome, 2);
		horarios = new ArrayList();
		nCols = 1;
	}

	@Override
	protected void readColumns(Row row) {
		if(!checkExcel(row)){
			System.err.println("Horario, linhas " + row.getRowNum() + " e' null");
			return;
		}		
		
		Cell tempCell = row.getCell(0);
		if (row.getCell(0).getDateCellValue() == null) {
			return;
		}

		// ****** Horario ******
		// inicio;
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		Date inicio = tempCell.getDateCellValue();

		/*
		// fim
		tempCell = row.getCell(1);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		Date fim = tempCell.getDateCellValue();
		System.out.println("horas " + inicio.toString());
		*/
		
		//System.out.println("horas " + inicio.toString() + " minutos " + fim.toString());
		// tira espacos
		//inicio = inicio.replace(" \t", "");
		//fim = fim.replace(" \t", "");
		String[] tempo = inicio.toString().split(" ");
		tempo = tempo[3].split(":");
		Integer aux = Integer.parseInt(tempo[0]) * 60;
		aux += Integer.parseInt(tempo[1]);

		/*
		tempo = fim.toString().split(" ");
		tempo = tempo[3].split(":");
		Integer aux2 = Integer.parseInt(tempo[0]) * 60;
		aux2 += Integer.parseInt(tempo[1]);
		*/
		
		// aula termina antes de comecar
		//if (aux > aux2) {
		//    System.out.println("ERRO: ");
		//}
		Horario h = new Horario();
		h.setInicio(aux);
		//h.setFim(aux2);
		h.setDia(0);
		horarios.add(h);
		/*
		 for (int i = 0; i < 7; i++) {
		 Horario h = new Horario();
		 h.setInicio(aux);
		 h.setFim(aux2);
		 h.setDia(i);
		 horarios.add(h);
		 }
		 */
		nhorarios++;
	}

	@Override
	protected void print() {
		//*
		for (Horario h : horarios) {
			System.out.println("id " + h.getId());
			System.out.println("inicio " + h.getInicio());
			System.out.println("fim " + h.getFim());
		}
		//*/
	}

	public ArrayList<Horario> getHorarios() {
		//*
		if (nhorarios == horarios.size()) {
			System.out.println("FIX " + nhorarios);
			fix();
		}
		//*/
		return horarios;
	}

	private void fix() {
		for (int j = 1; j < 7; j++) {
			for (int i = 0; i < nhorarios; i++) {
				System.out.print("a");
				Horario h = new Horario();
				Horario h2 = horarios.get(i);

				h.setInicio(h2.getInicio());
				h.setFim(h2.getFim());
				h.setDia(j);
				horarios.add(h);
			}
			System.out.println();
		}
	}
}
