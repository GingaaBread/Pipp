package processing;

import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.AST;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import processing.style.Pipp;
import processing.style.StyleGuide;
import processing.style.StyleTable;

import java.util.ArrayList;
import java.util.List;

public class Processor {

    /**
     *  Determines the Pipp versions this compiler supports.
     *  If the user tries to scan a document with a Pipp version not included in this array, an exception is thrown
     */
    public static final String[] SUPPORTED_VERSIONS = new String[] {
            "1.0"
    };

    /**
     *  Determines which stylesheet should be used during compilation
     */
    private StyleGuide usedStyleGuide;

    private PDRectangle dimensions;

    private double margin;

    private double spacing;

    private NumerationType numerationType;

    private NumerationPosition numerationPosition;

    private double numerationMargin;

    private List<Integer> skippedPages;

    /**
     *  Determines the document's configuration options specified by the user
     */
    private Configuration configuration;

    @NonNull
    public void processAST(final AST ast) {
        ast.checkForErrors();
        ast.checkForWarnings();

        System.out.println(ast);

        var styleConfiguration = ast.getConfiguration().getStyle();

        // Check if the user demands a different style than the default style
        if (styleConfiguration.getBaseStyle() != null) {
            // Try to apply that style
            usedStyleGuide = StyleTable.nameToStyleGuide(ast.getConfiguration().getStyle().getBaseStyle());
        } else {
            // Use the default style, instead
            usedStyleGuide = new Pipp();
        }

        var layout = styleConfiguration.getLayout();
        var pointsPerInch = 72;
        var pointsPerMM = 1 / (10 * 2.54f) * pointsPerInch;;

        // Check if the user demands a custom document dimension

        float height;
        if (layout.getHeight() != null) {
            // Check if the user wants to use inches
            if (layout.getHeight().endsWith("\"")) {
                var asNumber = layout.getHeight().substring(0, layout.getHeight().length() - 1);
                height = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                height = pointsPerMM * Float.parseFloat(layout.getHeight());
            }
        } else height = usedStyleGuide.pageFormat().getHeight();

        float width;
        if (layout.getWidth() != null) {
            // Check if the user wants to use inches
            if (layout.getWidth().endsWith("\"")) {
                var asNumber = layout.getWidth().substring(0, layout.getWidth().length() - 1);
                width = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                width = pointsPerMM * Float.parseFloat(layout.getWidth());
            }
        } else width = usedStyleGuide.pageFormat().getWidth();

        // Set the document dimensions
        dimensions = new PDRectangle(width, height);

        // Check if the user demands a custom margin
        if (layout.getMargin() != null) {
            margin = Double.parseDouble(layout.getMargin());
        } else margin = usedStyleGuide.margin();

        // Check if the user demands a custom spacing
        if (layout.getSpacing() != null) {
            switch (layout.getSpacing()) {
                case "Single" -> spacing = 1d;
                case "Increased" -> spacing = 1.5d;
                case "Double" -> spacing = 2d;
                default -> spacing = Double.parseDouble(layout.getSpacing());
            }
        } else spacing = usedStyleGuide.spacing();


        var numeration = styleConfiguration.getNumeration();

        // Check if the user demands a custom numeration type
        if (numeration.getNumerationType() != null) {
            numerationType = switch (numeration.getNumerationType()) {
                case "Arabic" -> NumerationType.ARABIC;
                case "Roman" -> NumerationType.ROMAN;
                default -> throw new MissingMemberException("4: " + "The specified page numeration is either " +
                        "missing or does not exist. Check if it has been imported correctly, or if you " +
                        "have misspelled the numeration type in the configuration.");
            };
        } else numerationType = usedStyleGuide.numerationType();

        // Check if the user demands a custom numeration position
        if (numeration.getPosition() != null) {
            numerationPosition = switch (numeration.getPosition()) {
                case "Top Left" -> NumerationPosition.TOP_LEFT;
                case "Top" -> NumerationPosition.TOP;
                case "Top Right" -> NumerationPosition.TOP_RIGHT;
                case "Bottom Left" -> NumerationPosition.BOTTOM_LEFT;
                case "Bottom" -> NumerationPosition.BOTTOM;
                case "Bottom Right" -> NumerationPosition.BOTTOM_RIGHT;
                default -> throw new MissingMemberException("5: " + "The specified page position is either " +
                        "missing or does not exist. Check if it has been imported correctly, or if you " +
                        "have misspelled the numeration position in the configuration.");
            };
        } else numerationPosition = usedStyleGuide.numerationPosition();

        // Check if the user demands a custom numeration margin
        if (numeration.getMargin() != null) {
            try {
                double asNumber = Double.parseDouble(numeration.getMargin());
                if (asNumber < 0) throw new IncorrectFormatException("2: Non-negative decimal expected.");
                else numerationMargin = asNumber;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        } else numerationMargin = usedStyleGuide.numerationMargin();

        // TODO
        skippedPages = new ArrayList<>();

    }





}
