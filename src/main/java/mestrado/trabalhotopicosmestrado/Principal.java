/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mestrado.trabalhotopicosmestrado;

import java.util.ArrayList;

/**
 *
 * @author nicolasferranti
 */
public class Principal {
                                    //// SET BLAZEGRAPH URL
    public Blazegraph bz = new Blazegraph("", "Nicolas");
    public Spotlight spt = new Spotlight("http://model.dbpedia-spotlight.org/en/annotate");
    public DBpedia dbp = new DBpedia();

    public classURI profileComposer(String className) {
        classURI cURI = new classURI(className);

        String labelComment = this.bz.extractLabelAndComment(cURI.name);
        if (labelComment != null) {
            ArrayList<String> recursos = this.spt.anota(labelComment);
            for (String recurso : recursos) {
                cURI.categorias.addAll(this.dbp.getCategoryByResource(recurso));
            }
        }

        return cURI;
    }

    public void execute(String pathToSave) {
        //System.out.println(this.bz.consultaClasses());
        csvParser csvP = new csvParser();
        for (String className : this.bz.consultaClasses()) {
            System.out.println("Processando a classe "+ className);
            System.out.println("Aguarde ...");
            csvP.classes.add( this.profileComposer(className));
            System.out.println("Classe inserida com sucesso");
        }
        System.out.println("Finalizado a construção do perfil");
        System.out.println("Escrevendo dados no CSV ...");
        csvP.toCSV();
        System.out.println("CSV armazenado em : "+ pathToSave);
    }

    public static void main(String[] args) {
        
        String pathToSave = "/home/nicolasferranti/NetBeansProjects/TrabalhoTopicosMestrado/target";
        Principal p = new Principal();
        
        //p.profileComposer("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address");

        //bz.consultaClasses();
        String lblcomm = p.bz.extractLabelAndComment("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address");
        System.out.println(p.profileComposer("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address").categorias.toString());
        //String lblcomm = p.bz.extractLabelAndComment("http://www.w3.org/1999/02/22-rdf-syntax-ns#List");
        //System.out.println("LABEL:" + lblcomm);
        //System.out.println(p.spt.anota(lblcomm).toString());
        //System.out.println(p.dbp.getCategoryByResource(p.spt.anota(lblcomm).get(0)).toString());
    }
}
