// Package
package model;

// Imports
import model.SequenceBuilder;

// Mutation class
public class MutationChecker {
    private int nucleotidePosition;
    private int aminoAcidPosition;
    private String dna;
    private char oldNucleotide;

    public MutationChecker(String dna, int nucPos, int aaPos, char oldChar){
        this.dna = dna;
        this.aminoAcidPosition = aaPos;
        this.nucleotidePosition  = nucPos;
        this.oldNucleotide = oldChar;
    }

    public String createOldSequence(){
        StringBuilder oldSequence = new StringBuilder(this.dna);
        oldSequence.setCharAt(this.nucleotidePosition - 1, this.oldNucleotide);
        return oldSequence.toString();
    }

    public String createOldProtein(String oldSequence){
        SequenceBuilder builder = new SequenceBuilder(oldSequence);
        return builder.createAminoAcid(oldSequence);
    }

    public String typeOfMutation(String oldAminoAcid, String newAminoAcid){
        if (newAminoAcid.equals(oldAminoAcid)){
            return "silent";
        } else if (newAminoAcid.equals("_")){
            return "nonsense";
        } else {
            return "missense";
        }
    }
}
