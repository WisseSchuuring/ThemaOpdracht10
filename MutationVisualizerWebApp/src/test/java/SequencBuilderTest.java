import model.SequenceBuilder;

class SequenceBuilderTest {
    public static void main(String[] args) {

        SequenceBuilder builder = new SequenceBuilder("ATGGTACGCTACGACTCAGCACACATCAGCAGCATG");

        System.out.println(builder.createAminoAcid(builder.dna));

        System.out.println(builder.createComplementStrand(builder.dna));

        System.out.println(builder.createRNAStrand(builder.dna));
    }
}