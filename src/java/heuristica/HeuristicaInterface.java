package heuristica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import singleton.Banco;

/**
 * Classe que faz a interface com a heuristica
 * @author Marcelo
 */
public class HeuristicaInterface {

	/**
	 * path ate o jar da heuristica
	 */
    private String path;
    
	/**
	 * Construtor, apenas inicializa os atributos da classe
	 * @param path 
	 */
    public HeuristicaInterface(String path) {
        this.path = path;
    }
    
	/**
	 * Gera o arquivo input.txt que e' a entrada para a heuristica
	 * @param input_file_name
	 * @throws SQLException 
	 */
    public void geraInput(String input_file_name) throws SQLException {

        ResultSet resultSalas = Banco.getInstance().query("select * from sala");
        ResultSet resultTurmas = Banco.getInstance().query("select * from turma");
        ResultSet resultHorarios = Banco.getInstance().query("select id from horario");
        ResultSet resultRecursos = Banco.getInstance().query("select codigo from recurso");

        ArrayList<String> listaSalas = new ArrayList();
        ArrayList<Integer> listaTurmas = new ArrayList();
        ArrayList<Integer> listaHorarios = new ArrayList();
        ArrayList<Integer> listaRecursos = new ArrayList();

        while (resultSalas.next()) {
            listaSalas.add(resultSalas.getString(2));
        }

        while (resultTurmas.next()) {
            listaTurmas.add(resultTurmas.getInt(1));
        }

        while (resultHorarios.next()) {
            listaHorarios.add(resultHorarios.getInt(1));
        }

        while (resultRecursos.next()) {
            listaRecursos.add(resultRecursos.getInt(1));
        }

        Integer nHorarios = listaHorarios.size();
        Integer nTurmas = listaTurmas.size();
        Integer nSalas = listaSalas.size();
        Integer nRecursos = listaRecursos.size();

        try {
            FileWriter file = new FileWriter(path + input_file_name);
            BufferedWriter buff = new BufferedWriter(file);

            buff.newLine();
            buff.write(nHorarios.toString() + "\t");
            buff.write(nTurmas.toString() + "\t");
            buff.write(nSalas.toString() + "\t");
            buff.write(nRecursos.toString());
            buff.write("\n\n\n");

            // cria a matriz de horarios e reseta ela com zeros
            Integer[][] matriz_horarios = new Integer[nHorarios][nTurmas];
            for (int i = 0; i < nHorarios; i++) {
                for (int j = 0; j < nTurmas; j++) {
                    matriz_horarios[i][j] = 0;
                }
            }

            // matriz de horarios / turma
            Iterator it = listaHorarios.iterator();
            while (it.hasNext()) {
                Integer id = (Integer) it.next();

                ResultSet resultado = Banco.getInstance().query("select codigo_turma from horario_turma ht where ht.id_horario = " + id);

                while (resultado.next()) {
                    matriz_horarios[id][resultado.getInt(1)] = 1;
                }
            }

            // escreve a matriz no arquivo
            for (int i = 0; i < nHorarios; i++) {
                for (int j = 0; j < nTurmas; j++) {
                    buff.write(matriz_horarios[i][j] + "\t");
                }
                buff.newLine();
            }

            buff.newLine();
            buff.newLine();

            // escrevendo a matriz de salas/recursos
            Integer[] recursos = new Integer[nRecursos];
            it = listaSalas.iterator();
            while (it.hasNext()) {
                String s = (String) it.next();

                // reseta os recursos
                for (int i = 0; i < nRecursos; i++) {
                    recursos[i] = 0;
                }

                ResultSet resultado = Banco.getInstance().query("select codigo_recurso from Recurso_Has_Sala r where r.nome_sala = " + s);

                while (resultado.next()) {
                    recursos[resultado.getInt(1)] = 1;
                }

                // escreve no arquivo
                for (int i = 0; i < nRecursos; i++) {
                    buff.write(recursos[i] + "\t");
                }
                buff.newLine();
            }

            buff.newLine();
            buff.newLine();

            // matriz de turma/recursos
            it = listaTurmas.iterator();
            while (it.hasNext()) {
                Integer codigo = (Integer) it.next();

                // reseta os recursos
                for (int i = 0; i < nRecursos; i++) {
                    recursos[i] = 0;
                }

                ResultSet resultado = Banco.getInstance().query("select codigo_recurso from Turma_Has_Recurso t where t.codigo_turma = " + codigo);

                while (resultado.next()) {
                    recursos[resultado.getInt(1)] = 1;
                }

                // escreve no arquivo
                for (int i = 0; i < nRecursos; i++) {
                    buff.write(recursos[i] + "\t");
                }
                buff.newLine();
            }

            buff.newLine();
            buff.newLine();

            // capacidade das salas
            resultSalas = Banco.getInstance().query("select * from sala");
            while (resultSalas.next()) {
                buff.write(resultSalas.getInt(3) + "\n");
            }

            buff.newLine();
            buff.newLine();

            // numero de inscritos nas turmas
            resultTurmas = Banco.getInstance().query("select * from turma");
            while (resultTurmas.next()) {
                buff.write(resultTurmas.getInt(4) + "\n");
            }

            buff.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * Faz a leitura do arquivo output.txt e preeche o banco
	 * @param arquivosaida
	 * @throws SQLException 
	 */
    public void lerArquivo(String arquivosaida) throws SQLException {

        // TABELA TURMA
        ResultSet turmas = Banco.getInstance().query("SELECT codigo FROM TURMA ORDER BY codigo");
        ArrayList<Integer> listaTurmas = new ArrayList();
        while (turmas.next()) {
            listaTurmas.add(turmas.getInt(1));
        }

        // TABELA HORARIOS
        ResultSet horarios = Banco.getInstance().query("SELECT ID FROM HORARIO ORDER BY ID");
        ArrayList<Integer> listaHorarios = new ArrayList();
        while (horarios.next()) {
            listaHorarios.add(horarios.getInt(1));
        }

        // TABELA SALAS
        ResultSet salas = Banco.getInstance().query("SELECT nome FROM sala ORDER BY nome");
        ArrayList<String> listaSalas = new ArrayList();
        while (salas.next()) {
            listaSalas.add(salas.getString(1));
        }

        System.out.println("turma: " + listaTurmas.size() + " horarios " + listaHorarios.size() + " salas " + listaSalas.size());
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + arquivosaida));

            reader.readLine();// pula #SOLUCAO
            String line;

            for (int i = 0; i < listaHorarios.size(); i++) {
                line = reader.readLine();
                String[] quebra = line.split("\t");
                for (int j = 0; j < listaSalas.size(); j++) {
                    int tmp = Integer.decode(quebra[j]);

                    if (tmp != -1) {
                        System.out.println("inseriu: " + listaHorarios.get(i));
                        Banco.getInstance().inserir(listaHorarios.get(i), listaSalas.get(j), listaTurmas.get(tmp));
                    }
                }
            }
            System.out.println("heuristica terminou");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * Executa a meta heuristica
	 */
    public void executarMetaHeuristica() {
        try {
            File f = new File(path + "MetaHeuristica.jar");

            if (f.exists()) {
                Runtime.getRuntime().exec("java -jar " + path + "MetaHeuristica.jar").waitFor();
            } else {
                System.out.println("File not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
