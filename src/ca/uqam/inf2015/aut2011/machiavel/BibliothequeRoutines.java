/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam.inf2015.aut2011.machiavel;

import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.TreeMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 * @author SERVER
 */
public class BibliothequeRoutines {

    protected static String genererDossierIntrants() throws IOException {
        String nomDossier = new java.io.File(".").getCanonicalPath();
        nomDossier += "\\intrant";
        return nomDossier;
    }

    protected static String genererDossierExtrants() throws IOException {
        String nomDossier = new java.io.File(".").getCanonicalPath();
        nomDossier += "\\extrant";
        return nomDossier;
    }

    protected static String extraireNomFichier(String nomFichier) throws IOException {
        String resultat = "";
        int j = nomFichier.length() - 1;
        for (int i = 0;; i++) {
            if (nomFichier.charAt(j) != '\\') {
                resultat += nomFichier.charAt(j);
                j--;
            } else {
                break;
            }

        }
        resultat = inverserChaine(resultat);

        return resultat;
    }

    protected static String inverserChaine(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--) {
            dest.append(source.charAt(i));
        }
        return dest.toString();
    }

    protected static JsonNode calculAmortissement(JsonNode sourceNode) {
        TreeMapper mapper = new TreeMapper();
        ArrayNode amortissement = mapper.arrayNode();

        String id = "id";
        String description = "description";
        String montant = "montant";
        String nombreAnnee = "nombreAnnee";
        String frequenceRemboursement = "frequenceRemboursement";
        String tauxInteret = "tauxInteret";
        String frequenceComposition = "frequenceComposition";

        String id_valeur = "";
        String description_valeur = "";
        double montant_valeur = 0;
        double nombreAnnee_valeur = 0;
        double frequenceRemboursement_valeur = 0;
        double tauxInteret_valeur = 0;
        double frequenceComposition_valeur = 0;
        double nombrePaiementsMensuels = 0;
        double versementPeriodique = 0;

        id_valeur = sourceNode.path(id).getTextValue();
        description_valeur = sourceNode.path(description).getTextValue();
        montant_valeur = sourceNode.path(montant).getValueAsDouble();
        System.out.println("montant_valeur=" + montant_valeur);
        nombreAnnee_valeur = sourceNode.path(nombreAnnee).getValueAsDouble();
        frequenceRemboursement_valeur = sourceNode.path(frequenceRemboursement).getValueAsDouble();
        tauxInteret_valeur = sourceNode.path(tauxInteret).getValueAsDouble();
        frequenceComposition_valeur = sourceNode.path(frequenceComposition).getValueAsDouble();
        nombrePaiementsMensuels = nombreAnnee_valeur * frequenceRemboursement_valeur;
        
        double tauxParPeriode=tauxInteret_valeur/frequenceComposition_valeur;
       
        double tauxMensuel = tauxParPeriode/(12/frequenceComposition_valeur);
      
              
        System.out.println("Taux mensuel=" + tauxMensuel);

        double numerateur = (montant_valeur * (tauxMensuel*0.01));

        double denumerateur = (1 - Math.pow(1 + (tauxMensuel*0.01), -nombrePaiementsMensuels));

        versementPeriodique = numerateur / denumerateur;//encore mal calculer ,donc a revoir,une fois le calcul est juste,le reste est bon.


        ((ObjectNode) sourceNode).put("versementPeriodique", versementPeriodique);

        ((ObjectNode) sourceNode).putArray("amortissement");

        ObjectNode periode = mapper.objectNode();
        for (int i = 1; i < nombrePaiementsMensuels + 1; i++) {
            periode = mapper.objectNode();
            periode.put("période", i);
            periode.put("capitalDebut", montant_valeur);
            periode.put("versementTotal", versementPeriodique);
            periode.put("versementInteret", montant_valeur * tauxMensuel*0.01);
            periode.put("versementCapital", versementPeriodique - (montant_valeur * tauxMensuel*0.01));
            periode.put("capitalFin", montant_valeur - versementPeriodique);

            ((ArrayNode) sourceNode.path("amortissement")).add(periode);


            montant_valeur -= versementPeriodique;
        }
        System.out.println("id= " + id_valeur);
        System.out.println("description= " + description_valeur);
        System.out.println("montant= " + montant_valeur);
        System.out.println("nombreAnnee= " + nombreAnnee_valeur);
        System.out.println("frequenceRemboursement= " + frequenceRemboursement_valeur);
        System.out.println("tauxInteret= " + tauxInteret_valeur);
        System.out.println("frequenceComposition= " + frequenceComposition_valeur);
        System.out.println("versementPeriodique= " + sourceNode.path("versementPeriodique").getValueAsDouble());
        System.out.println(versementPeriodique);

        return sourceNode;
    }
}
