/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mestrado.trabalhotopicosmestrado;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author nicolasferranti
 */
public class Principal {
                                    //// SET BLAZEGRAPH URL
    public Blazegraph bz = new Blazegraph("http://138.121.71.37:9999", "Nicolas");
    public Blazegraph bz2 = new Blazegraph("http://138.121.71.37:9999", "Nicolas2");
    public Spotlight spt = new Spotlight("http://model.dbpedia-spotlight.org/en/annotate");
    public DBpedia dbp = new DBpedia();

    public classURI profileComposer(String className, Blazegraph bzP) {
        classURI cURI = new classURI(className);

        // processando categorias
        String labelComment = bzP.extractLabelAndComment(cURI.name);
        if (labelComment != null) {
            ArrayList<String> recursos = this.spt.anota(labelComment);
            for (String recurso : recursos) {
                cURI.categorias.addAll(this.dbp.getCategoryByResource(recurso));
            }
        }
        
        // CHECA PAI
        if( bzP.getFather(className) != null){
            cURI.haveFather = true;
        }
        
        cURI.qtdBrothers = bzP.getCountBrothers(className);
        cURI.qtdSubClasses = bzP.getQtdSons(className);
        cURI.qtdIndividuals = bzP.getQtdIndividuals(className);
        
        cURI.printAll();
        
        return cURI;
    }

    public void execute(String pathToSave) throws FileNotFoundException, UnsupportedEncodingException {
        classURI cUri;
        csvParser csvP = new csvParser();
        for (String className : bz.consultaClasses()) {
            System.out.println("Processando a classe "+ className);
            System.out.println("Aguarde ...");
            csvP.classes.add( this.profileComposer(className, bz));
            System.out.println("Classe inserida com sucesso");
        }
        for (String className : bz2.consultaClasses()) {
            System.out.println("Processando a classe "+ className);
            System.out.println("Aguarde ...");
            cUri = this.profileComposer(className, bz2);
            csvP.classes.add(cUri);
            System.out.println("Classe inserida com sucesso");
        }
        
        System.out.println("Finalizado a construção do perfil");
        System.out.println("Escrevendo dados no CSV ...");
        csvP.toCSV(pathToSave);
        System.out.println("CSV armazenado em : "+ pathToSave);
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        
        String pathToSave = "/home/nicolasferranti/NetBeansProjects/TrabalhoTopicosMestrado/target";
        Principal p = new Principal();
        p.execute(pathToSave);
        //System.out.println(p.profileComposer("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address").categorias.toString());

        //System.out.println(p.bz.consultaClasses().toString());
        //String lblcomm = p.bz.extractLabelAndComment("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address");
        //p.profileComposer("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Address", p.bz2);
        //System.out.println(p.bz.getQtdSons("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Academic"));
        //System.out.println(p.bz.getQtdIndividuals("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#InProceedings"));
        //String lblcomm = p.bz.extractLabelAndComment("http://www.w3.org/1999/02/22-rdf-syntax-ns#List");
        //System.out.println("LABEL:" + lblcomm);
        //System.out.println(p.spt.anota(lblcomm).toString());
        //System.out.println(p.dbp.getCategoryByResource(p.spt.anota(lblcomm).get(0)).toString());
    }
}
