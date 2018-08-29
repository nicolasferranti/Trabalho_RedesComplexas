/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mestrado.trabalhotopicosmestrado;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author nicolasferranti
 */
public class csvParser {

    ArrayList<String> categorias;
    ArrayList<classURI> classes;

    public csvParser() {
        categorias = new ArrayList<>();
        classes = new ArrayList<>();
    }

    public void toCSV(String path) throws FileNotFoundException, UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        sb.append("name,qtdSubClasses,haveFather,qtdBrothers,qtdIndividuals");

        for (classURI c : classes) {
            for (String cat : c.categorias) {
                this.categorias.add(cat);
                sb.append("," + cat);
            }
        }
        sb.append("\n");
        for (classURI c : classes) {
            sb.append(c.name);
            sb.append("," + c.qtdSubClasses);
            if (c.haveFather) {
                sb.append(",1");
            } else {
                sb.append(",0");
            }
            sb.append("," + c.qtdBrothers);
            sb.append("," + c.qtdIndividuals);
            for (String cat : this.categorias) {
                if (c.categorias.contains(cat)) {
                    sb.append(",1");
                } else {
                    sb.append(",0");
                }
            }
            sb.append("\n");
        }

        System.out.println("--------------- CSV -----------------");
        System.out.println(sb.toString());

        PrintWriter writer = new PrintWriter(path+"out.csv", "UTF-8");
        writer.println(sb.toString());
        writer.close();

        // write a csv file with the profile
    }
}
