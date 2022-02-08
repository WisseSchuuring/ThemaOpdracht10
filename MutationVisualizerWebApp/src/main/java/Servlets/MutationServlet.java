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

    // Post method that handles actions when button is pressed
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        WebContext ctx = new WebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale()
        );
        // get JSON data
        JSONObject sequenceData = new JSONObject();
        try {
            sequenceData = getSequencedata();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        // Create data for thymeleaf actions
        String testString = JSONObject.valueToString(sequenceData.get("sequence")).replace("\"", "");
        SequenceBuilder builder = new SequenceBuilder(testString);
        String dna = builder.dna;
        String protein = builder.createAminoAcid(dna);
        String positionObject = JSONObject.valueToString(sequenceData.get("position")).replace("\"", "");
        String position = Integer.toString(Integer.parseInt(positionObject) + 1);
        String positionProtein = Integer.toString((int)Math.ceil((Double.parseDouble(position))/3));
        String mutation = JSONObject.valueToString(sequenceData.get("mutation")).replace("\"", "");
        String[] mutationNucleotides = mutation.split("->");

        MutationChecker mutationChecker = new MutationChecker(dna, Integer.parseInt(positionObject),
                Integer.parseInt(positionProtein), mutationNucleotides[0].charAt(0));
        String oldProtein = mutationChecker.createOldProtein(mutationChecker.createOldSequence());
        char oldAminoAcid = oldProtein.charAt(Integer.parseInt(positionProtein) - 1);
        char newAminoAcid = protein.charAt(Integer.parseInt(positionProtein) - 1);
        String mutationType = mutationChecker.typeOfMutation(Character.toString(oldAminoAcid), Character.toString(newAminoAcid));

        // Send data to HTML file
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

    // Get method that handles website being first started
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Locale locale = request.getLocale();
        WebContext ctx = new WebContext(
                request,
                response,
                request.getServletContext(),
                locale
        );
        // Getting JSON data
        JSONObject sequenceData = new JSONObject();
        try {
            sequenceData = getSequencedata();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        // Creating data for thymeleaf
        String testString = JSONObject.valueToString(sequenceData.get("sequence")).replace("\"","");
        SequenceBuilder builder = new SequenceBuilder(testString);
        String dna = builder.dna;

        // Send data to HTML page
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

    // Method that pulls a JSON data file from external URL
    public JSONObject getSequencedata() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://bioinf.nl/DNAanimal/api/v1/sequence")).build();
        HttpResponse<String> res = client.send(request,HttpResponse.BodyHandlers.ofString());
        JSONObject sequenceObject = new JSONObject(res.body());
        return sequenceObject;
    }
}
