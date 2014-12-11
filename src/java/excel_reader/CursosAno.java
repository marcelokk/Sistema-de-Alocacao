package excel_reader;

import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import model.CursoAno;

public class CursosAno extends ExcelReader {

    ArrayList<CursoAno> cursos;

    public CursosAno(String nome) {
        super(nome, 0);
        cursos = new ArrayList();
    }

    @Override
    protected void readColumns(Row row) {
        // ****** Tabela CursoAno ******
        // sigla
        Cell tempCell = row.getCell(0);
        String sigla = tempCell.getStringCellValue();

        // curso
        tempCell = row.getCell(1);
        String curso = tempCell.getStringCellValue();

        // perido
        tempCell = row.getCell(2);
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
