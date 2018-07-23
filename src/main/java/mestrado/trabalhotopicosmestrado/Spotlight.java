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
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 *
 * @author nicolasferranti
 */
public class Spotlight {

    private double confianca = 0.35;
    String url; // http://localhost:2222

    public Spotlight(String URL) {
        this.url = URL;
    }

    public ArrayList<String> anota(String text) {
        String confidence = "confidence=" + this.confianca;

        URL obj;

        try {
            obj = new URL(url);

            HttpURLConnection con;

            try {

                con = (HttpURLConnection) obj.openConnection();

                //con.setRequestMethod("POST");
                String urlParameters = "text=" + URLEncoder.encode(text) + "&" + confidence;

                // Send post request
                con.setDoOutput(true);

                DataOutputStream wr;

                wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                String html = "";
                while ((inputLine = in.readLine()) != null) {
                    html += inputLine;
                }
                in.close();

                org.jsoup.nodes.Document doc = Jsoup.parse(html);
                Elements newsHeadlines = doc.select("a");

                ArrayList<String> resourcesList = new ArrayList<>();
                for (int j = 0; j < newsHeadlines.size(); j++) {
                    resourcesList.add(newsHeadlines.get(j).attr("href"));
                }
                return resourcesList;

            } catch (IOException ex) {
                Logger.getLogger(Spotlight.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Spotlight.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
