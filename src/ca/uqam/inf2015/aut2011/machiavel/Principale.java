/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam.inf2015.aut2011.machiavel;

import java.io.File;
import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.TreeMapper;

/**
 *
 * @author SERVER
 */
public class Principale {

    public static void main(String[] args) throws JsonParseException, IOException {
        String dossierIntrants = "";
        String dossierExtrants = "";
        
        if (args.length == 2) {
            dossierIntrants = args[0];
            dossierExtrants = args[1];
        } else if (args.length == 0) {
            dossierIntrants = BibliothequeRoutines.genererDossierIntrants();
            dossierExtrants = BibliothequeRoutines.genererDossierExtrants();
            System.out.println("Le dossier des intrants par defaut sera utilise:" + dossierIntrants);
            System.out.println("Le dossier des extrants par defaut sera utilise:" + dossierExtrants);
        } else {
            System.out.println("Le nombre d'arguments est invalide");
            System.exit(1);


        }
        File repertoire = new File(dossierIntrants);
        File[] fichiers_Dans_dossierIntrants = repertoire.listFiles();


        if (fichiers_Dans_dossierIntrants.length == 0) {
            System.out.println("Le dossier: " + dossierIntrants + "____est vide");
            System.exit(1);
        }

        System.out.println("Les fichiers dans le dossier des intrants sont:");

        for (int positionFichierListe = 0; positionFichierListe < fichiers_Dans_dossierIntrants.length; positionFichierListe++) {

            System.out.println(fichiers_Dans_dossierIntrants[positionFichierListe].toString());

            if (fichiers_Dans_dossierIntrants[positionFichierListe].canRead() == false) {
                System.out.println("Ce fichier ne peut pas etre lu,veuiller le remplacer ou le supprimer");
                System.exit(1);
            } else {
                System.out.println("Ce fichier peut etre lu correctement");
                System.out.println();
            }


        }

        System.out.println("-------------------------------------------");



        int positionFichierTraiter = 0;

        TreeMapper mapper = new TreeMapper();
        JsonNode[] rootNodes;
        rootNodes = new JsonNode[fichiers_Dans_dossierIntrants.length];



        do {

            rootNodes[positionFichierTraiter] = mapper.readTree(fichiers_Dans_dossierIntrants[positionFichierTraiter]);

            rootNodes[positionFichierTraiter] = BibliothequeRoutines.calculAmortissement(rootNodes[positionFichierTraiter]);

            String NomFichierSortie = dossierExtrants + "\\" + BibliothequeRoutines.extraireNomFichier(fichiers_Dans_dossierIntrants[positionFichierTraiter].toString());

            File FichierSortie = new File(NomFichierSortie);
            mapper.writeTree(rootNodes[positionFichierTraiter], FichierSortie);

            System.out.println("-------------------------------------------");
            positionFichierTraiter++;
        }//fin do
        while (positionFichierTraiter < fichiers_Dans_dossierIntrants.length);

    }//fin du main
}
