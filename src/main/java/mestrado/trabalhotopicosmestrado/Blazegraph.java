/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mestrado.trabalhotopicosmestrado;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nicolasferranti
 */
public class Blazegraph {

    private String dbName;
    private String url;

    public Blazegraph(String URL, String DBNAME) {
        this.url = URL;
        this.dbName = DBNAME;
    }

    private JSONArray consultaBZ(String message, String tipo) {
        String url = this.url + "/blazegraph/namespace/" + this.dbName + "/sparql";

        JSONObject jsonObject;

        URL obj;
        try {
            obj = new URL(url);

            HttpURLConnection con;
            try {
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                String urlParameters = tipo + "=" + URLEncoder.encode(message);

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
                StringBuilder x = new StringBuilder();

                String output;
                while ((output = br.readLine()) != null) {
                    x.append(output);

                }

//                System.out.println("\nSending 'POST' request to URL : " + url);
//                System.out.println("Post parameters : " + urlParameters);
//                System.out.println("Response Code : " + responseCode);
//                System.out.println("Output Message : " + x); // comparar x com o id
                if ("update".equals(tipo)) {
                    return new JSONArray();
                }
                jsonObject = new JSONObject(x.toString());
                JSONArray res = (jsonObject.getJSONObject("results")).getJSONArray("bindings");

                return res;
            } catch (IOException ex) {
                Logger.getLogger(Blazegraph.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Blazegraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /* SELECIONA TODOS OS NÓS DO TIPO CLASSE */
    public ArrayList<String> consultaClasses() {
        String tipo = "query";
        String query = "select distinct ?s { ?s rdf:type owl:Class}";
        JSONArray bzReturn = this.consultaBZ(query, tipo);
        ArrayList<String> classes = new ArrayList<>();
        for (int i = 0; i < bzReturn.length(); i++) {
            classes.add(bzReturn.getJSONObject(i).getJSONObject("s").getString("value"));
        }
        return classes;
    }

    private String extractLabel(String classURI) {
        String tipo = "query";
        String query = "select ?o { <" + classURI + "> <" + Vocabulario.LABEL + "> ?o }";
        JSONArray response = this.consultaBZ(query, tipo);
        if (response.length() == 0) {
            return null;
        }
        return response.getJSONObject(0).getJSONObject("o").getString("value");
    }

    private String extractComment(String classURI) {
        String tipo = "query";
        String query = "select ?o { <" + classURI + "> <" + Vocabulario.COMMENT + "> ?o }";
        JSONArray response = this.consultaBZ(query, tipo);
        if (response.length() == 0) {
            return null;
        }
        return response.getJSONObject(0).getJSONObject("o").getString("value");
    }

    public String extractLabelAndComment(String classURI) {
        String comment = extractComment(classURI);
        String label = extractLabel(classURI);

        if (comment == null) {
            if (label == null) {
                return null;
            }
            return label;
        }
        if (label == null) {
            return comment;
        }
        return extractComment(classURI) + " " + extractLabel(classURI);
    }

    public void test() {
        System.out.println(this.consultaBZ("select ?p ?o { <http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address> ?p ?o}", "query"));
    }
}
