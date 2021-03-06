package excel_reader;

import java.io.FileInputStream;
import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Classe abstrata para o leitor do excel, essa classe declara as funcoes base e
 * as subclasses implementam a parte especifica de cada tabela do excel
 *
 * @author Marcelo
 *
 */
public abstract class ExcelReader {

	/**
	 * nome do arquivo excel
	 */
	protected String nome;	//

	/**
	 * path para o arquivo
	 */
	protected String path;

	/**
	 * numero da aba que a tabela esta
	 */
	protected int sheet;

	protected int nCols;

	/**
	 * Construtor, arruma o path se for windows ou linux
	 *
	 * @param nome
	 * @param sheet
	 */
	public ExcelReader(String nome, int sheet) {
		if (SystemUtils.IS_OS_LINUX) {
			path = "";
		} else {
			path = "";
		}
		this.nome = nome;
		this.sheet = sheet;
	}

	/**
	 * Funcao de leitura do arquivo excel
	 *
	 * @param debug
	 */
	public void read(boolean debug) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path + nome));	// abre o arquivo excel
			XSSFSheet sheet = workbook.getSheetAt(this.sheet);							// na aba da tabela

			boolean isHeader = true;	// comeca sempre pelo header
			boolean isNull = false;

			for (Row tempRow : sheet) {	// para cada linha na tabela,

				if (tempRow == null) {	// se nao tem nada, pula
					break;
				}

				if (tempRow.getCell(0) == null || tempRow.getCell(0).getCellType() == HSSFCell.CELL_TYPE_BLANK) {
					System.err.println("Linha em branco: " + tempRow.getRowNum());
					break;
				}

				if (isHeader) {	// se for o header, pula
					this.readHeader(tempRow);
					isHeader = false;
					continue;
				}	// senao, e' uma linha valida e deve ser lida de acordo com o tipo da tabela

				this.readColumns(tempRow);
			}
		} catch (Exception e) {
			System.out.println("exception: " + e.getMessage());
			e.printStackTrace();
		}
		if (debug) {
			this.print();
		}
	}

	/**
	 * leitura do header, depede da tabela
	 *
	 * @param row
	 */
	protected void readHeader(Row row) {
	}

	/**
	 * leitura das colunas, depende da tabela
	 *
	 * @param row
	 */
	protected abstract void readColumns(Row row);

	/**
	 * printa o conteudo lido
	 */
	protected void print() {
	}

	/**
	 * Checa uma linha do excel
	 * @param row
	 * @return true se a linha esta OK, false caso contrario
	 */
	protected boolean checkExcel(Row row) {
		// checa se a linha esta em branco
		int c;
		for (c = row.getFirstCellNum(); c < nCols; c++) {
			if (row.getCell(c) == null || row.getCell(c).getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				return false;
			}
		}
		return true;
	}
}
