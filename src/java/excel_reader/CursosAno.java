package excel_reader;

import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import model.CursoAno;

public class CursosAno extends ExcelReader {

    private ArrayList<CursoAno> cursos;
	
    public CursosAno(String nome) {
        super(nome, 0);
        cursos = new ArrayList();
		nCols = 3;
    }

    @Override
    protected void readColumns(Row row) {
		if(!checkExcel(row)){
			System.err.println("Curso, linhas " + row.getRowNum() + " e' null");
			return;
		}
		
        // ****** Tabela CursoAno ******
        // sigla
        Cell tempCell = row.getCell(0);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);		
        String sigla = tempCell.getStringCellValue().trim();

        // curso
        tempCell = row.getCell(1);
		tempCell.setCellType(Cell.CELL_TYPE_STRING);		
        String curso = tempCell.getStringCellValue().trim();

        // perido
        tempCell = row.getCell(2);
		tempCell.setCellType(Cell.CELL_TYPE_NUMERIC);		
        Double valor = tempCell.getNumericCellValue();
        Integer periodo = valor.intValue();

        CursoAno c = new CursoAno();
        c.setCodigo(sigla);
        c.setNome(curso);
        c.setPeriodo(periodo);
        cursos.add(c);
    }

    @Override
    protected void print() {
        for (CursoAno c : cursos) {
            System.out.println("Codigo " + c.getCodigo());
            System.out.println("Nome " + c.getNome());
            System.out.println("Perido " + c.getPeriodo());
        }
    }

    public ArrayList<CursoAno> getCursos() {
        return cursos;
    }
}
