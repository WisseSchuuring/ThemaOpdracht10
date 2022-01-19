// Package
package model;

// Imports
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



// Builder class
public class SequenceBuilder {
    private HashMap<String, String> codonTable;
    public String dna;

    public SequenceBuilder(String dna) {
        this.dna = dna;
        createCodonTable();
        createComplementStrand(dna);
        createRNAStrand(dna);
        createAminoAcid(dna);
    }

    // Returns the Amino Acid associated with the given codon.
    public String getAminoAcid(String codon) {
        return codonTable.get(codon);
    }


    // Translates the given DNA sequences into an Amino Acid sequence.
    public String createAminoAcid(String dna) {
        StringBuilder firstBuilder = new StringBuilder();
        StringBuilder secondBuilder = new StringBuilder();

        for (int i = 0; i < dna.length(); i++) {
            char nucleotides = dna.charAt(i);
            firstBuilder.append(nucleotides);
        }
        String[] str = firstBuilder.toString().split("(?<=\\G...)");
        List<String> al;
        al = Arrays.asList(str);
        for (String s : al) {
            secondBuilder.append(getAminoAcid(s));
        }
        return secondBuilder.toString();
    }


    // Creates a strand complementary to the given DNA sequence.
    public String createComplementStrand(String dna) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < dna.length(); i++) {
            char nucleotide = dna.charAt(i);
            if (nucleotide == 'T') {
                builder.append('A');
            }
            if (nucleotide == 'A') {
                builder.append('T');
            }
            if (nucleotide == 'C') {
                builder.append('G');
            }
            if (nucleotide == 'G') {
                builder.append('C');
            }
        }
        return builder.toString();
    }

    // Creates the RNA sequence of the given DNA sequence.
    public String createRNAStrand(String dna) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < dna.length(); i += 1) {
            char nucleotide = dna.charAt(i);
            if (nucleotide == 'T') {
                builder.append('A');
            }
            if (nucleotide == 'A') {
                builder.append('U');
            }
            if (nucleotide == 'C') {
                builder.append('G');
            }
            if (nucleotide == 'G') {
                builder.append('C');
            }
        }
        return builder.toString();
    }

    // Creates a HashMap containing the codons and their respective Amino Acids.
    private void createCodonTable() {
        codonTable = new HashMap<>();
        codonTable.put("ATA", "I");
        codonTable.put("ATC", "I");
        codonTable.put("ATT", "I");
        codonTable.put("ATG", "M");
        codonTable.put("ACA", "T");
        codonTable.put("ACC", "T");
        codonTable.put("ACG", "T");
        codonTable.put("ACT", "T");
        codonTable.put("AAC", "N");
        codonTable.put("AAT", "N");
        codonTable.put("AAA", "K");
        codonTable.put("AAG", "K");
        codonTable.put("AGC", "S");
        codonTable.put("AGT", "S");
        codonTable.put("AGA", "R");
        codonTable.put("AGG", "R");
        codonTable.put("CTA", "L");
        codonTable.put("CTC", "L");
        codonTable.put("CTG", "L");
        codonTable.put("CTT", "L");
        codonTable.put("CCA", "P");
        codonTable.put("CCC", "P");
        codonTable.put("CCG", "P");
        codonTable.put("CCT", "P");
        codonTable.put("CAC", "H");
        codonTable.put("CAT", "H");
        codonTable.put("CAA", "Q");
        codonTable.put("CAG", "Q");
        codonTable.put("CGA", "R");
        codonTable.put("CGC", "R");
        codonTable.put("CGG", "R");
        codonTable.put("CGT", "R");
        codonTable.put("GTA", "V");
        codonTable.put("GTC", "V");
        codonTable.put("GTG", "V");
        codonTable.put("GTT", "V");
        codonTable.put("GCA", "A");
        codonTable.put("GCC", "A");
        codonTable.put("GCG", "A");
        codonTable.put("GCT", "A");
        codonTable.put("GAC", "D");
        codonTable.put("GAT", "D");
        codonTable.put("GAA", "E");
        codonTable.put("GAG", "E");
        codonTable.put("GGA", "G");
        codonTable.put("GGC", "G");
        codonTable.put("GGG", "G");
        codonTable.put("GGT", "G");
        codonTable.put("TCA", "S");
        codonTable.put("TCC", "S");
        codonTable.put("TCG", "S");
        codonTable.put("TCT", "S");
        codonTable.put("TTC", "F");
        codonTable.put("TTT", "F");
        codonTable.put("TTA", "L");
        codonTable.put("TTG", "L");
        codonTable.put("TAC", "Y");
        codonTable.put("TAT", "Y");
        codonTable.put("TAA", "_");
        codonTable.put("TAG", "_");
        codonTable.put("TGC", "C");
        codonTable.put("TGT", "C");
        codonTable.put("TGA", "_");
        codonTable.put("TGG", "W");
    }
}
