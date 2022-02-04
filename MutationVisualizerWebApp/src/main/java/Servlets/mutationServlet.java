package Servlets;

import Webconfig.WebConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import model.SequenceBuilder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "mutationServlet", urlPatterns = "/mutationVisualizer")
public class mutationServlet extends HttpServlet{
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
        String testString = "ATGCCGTGCTTAGTA";
        SequenceBuilder builder = new SequenceBuilder(testString);
        String dna = builder.dna;
        String complement = builder.createComplementStrand(dna);
        String rna = builder.createRNAStrand(dna);
        String aminoAcid = builder.createAminoAcid(dna);
        System.out.println(dna + complement + rna + aminoAcid);
        System.out.println("Button pressed");
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
        templateEngine.process("mutationVisualizer", ctx, response.getWriter());
    }
}
