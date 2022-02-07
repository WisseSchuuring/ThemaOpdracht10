package Servlets;

import Webconfig.WebConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import model.SequenceBuilder;
import model.MutationChecker;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import org.json.JSONObject;

@WebServlet(name = "mutationServlet", urlPatterns = "/mutationVisualizer")
public class MutationServlet extends HttpServlet{
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException{
        this.templateEngine = WebConfig.getTemplateEngine();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        WebContext ctx = new WebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale()
        );
        JSONObject sequenceData = new JSONObject();
        try {
            sequenceData = getSequencedata();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        String testString = JSONObject.valueToString(sequenceData.get("sequence")).replace("\"", "");
        SequenceBuilder builder = new SequenceBuilder(testString);
        String dna = builder.dna;
        String protein = builder.createAminoAcid(dna);
        String position = JSONObject.valueToString(sequenceData.get("position")).replace("\"", "");
        String positionProtein = Integer.toString((int)Math.ceil(Double.parseDouble(position)/3));
        String mutation = JSONObject.valueToString(sequenceData.get("mutation")).replace("\"", "");
        String[] mutationNucleotides = mutation.split("->");

        MutationChecker mutationChecker = new MutationChecker(dna, Integer.parseInt(position),
                Integer.parseInt(positionProtein), mutationNucleotides[1].charAt(0));
        String oldProtein = mutationChecker.createOldProtein(mutationChecker.createOldSequence());
        char oldAminoAcid = oldProtein.charAt(Integer.parseInt(positionProtein) - 1);
        char newAminoAcid = protein.charAt(Integer.parseInt(positionProtein) - 1);
        String mutationType = mutationChecker.typeOfMutation(Character.toString(oldAminoAcid), Character.toString(newAminoAcid));

        ctx.setVariable("dnaString", dna);
        ctx.setVariable("complementString", builder.createComplementStrand(dna));
        ctx.setVariable("rnaString", builder.createRNAStrand(dna));
        ctx.setVariable("protein", protein);
        ctx.setVariable("changeNucleotide",
                mutation + " on position " + position);
        ctx.setVariable("changeProtein", oldAminoAcid + "->" + newAminoAcid + " on position " + positionProtein);
        ctx.setVariable("mutationTitle", mutationType + ".title");
        ctx.setVariable("mutationText", mutationType + ".txt");
        templateEngine.process("mutationVisualizer", ctx, response.getWriter());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Locale locale = request.getLocale();
        WebContext ctx = new WebContext(
                request,
                response,
                request.getServletContext(),
                locale
        );
        JSONObject sequenceData = new JSONObject();
        try {
            sequenceData = getSequencedata();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        String testString = JSONObject.valueToString(sequenceData.get("sequence")).replace("\"","");
        SequenceBuilder builder = new SequenceBuilder(testString);
        String dna = builder.dna;

        ctx.setVariable("dnaString", dna);
        ctx.setVariable("complementString", builder.createComplementStrand(dna));
        ctx.setVariable("rnaString", builder.createRNAStrand(dna));
        ctx.setVariable("protein", builder.createAminoAcid(dna));
        ctx.setVariable("changeNucleotide", "-");
        ctx.setVariable("changeProtein", "-");
        ctx.setVariable("mutationTitle", "mutation.title");
        ctx.setVariable("mutationText", "mutation.txt");
        templateEngine.process("mutationVisualizer", ctx, response.getWriter());
    }

    public JSONObject getSequencedata() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://bioinf.nl/DNAanimal/api/v1/sequence")).build();
        HttpResponse<String> res = client.send(request,HttpResponse.BodyHandlers.ofString());
        JSONObject sequenceObject = new JSONObject(res.body());
        return sequenceObject;
    }
}
