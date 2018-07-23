/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mestrado.trabalhotopicosmestrado;

import com.github.kevinsawicki.http.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 *
 * @author nicolasferranti
 */
public class DBpedia {

    public ArrayList<String> dbpediaGet(String query) {
        StringBuilder textoEncode = new StringBuilder();

        textoEncode.append(query);

        StringBuilder requisicaoDBPedia = new StringBuilder();

        requisicaoDBPedia.append("http://dbpedia.org/sparql" + "?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+");
        try {
            requisicaoDBPedia.append(URLEncoder.encode(textoEncode.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        requisicaoDBPedia.append("+LIMIT+100&timeout=30000&debug=on");

        String resultado = HttpRequest.get(requisicaoDBPedia.toString())
                .accept("text/csv").body();

        resultado = resultado.substring(4).replaceAll("[\"\']", "");

        String[] resources = resultado.split("\n");

        ArrayList<String> related = new ArrayList<String>();
        for (String s : resources) {
            if (s.trim().isEmpty()) {
                continue;
            }
            try {
                s = URLDecoder.decode(s.trim(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            related.add(s.replace("http://dbpedia.org/resource/Category:", ""));
        }
        return related;
    }

    public ArrayList<String> getCategoryByResource(String resource) {
        return dbpediaGet("distinct ?x where {<" + resource + ">  dct:subject ?x}");
    }

}
