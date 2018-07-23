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
public class classURI {

    public ArrayList<String> categorias;
    public int qtdSubClasses;
    public boolean haveFather;
    public int qtdBrothers;
    public int qtdIndividuals;
    public String name;
    
    public classURI(String nome){
        this.name = nome;
        categorias = new ArrayList<>();
        qtdSubClasses = 0;
        haveFather = false;
        qtdBrothers = 0;
        qtdIndividuals = 0;
    }
}
