package frontend.ast;

import creation.ContentAlignment;
import creation.ImageRenderer;
import error.IncorrectFormatException;
import error.MissingMemberException;
import lombok.Getter;
import lombok.Setter;
import processing.Processor;

@Getter
@Setter
public class Image extends BodyNode {

    /**
     * The id of the image, relative to the img/ folder.
     * For example, "Dog.png".
     */
    private String id;

    /**
     * The width of the image in either millimeters or inches.
     */
    private String width;

    /**
     * The height of the image in either millimeters or inches.
     */
    private String height;

    /**
     * The size of the image, as an alternative to setting the width and height.
     * If used, this is a relative percentage of the image's default size.
     */
    private String size;

    /**
     * The alignment of the image. If not used, the default image alignment type of the style guide is used.
     */
    private String alignment;

    @Override
    public void handleBodyElement() {
        Integer imageSize = null;
        if (size != null) {
            if (size.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");
            if (!size.endsWith("%")) throw new IncorrectFormatException("15: Positive integer percentage expected.");

            var withoutPercentageCharacter = size.substring(0, size.length() - 1);
            try {
                var asInt = Integer.parseInt(withoutPercentageCharacter);

                if (asInt < 1) throw new IncorrectFormatException("15: Positive integer percentage expected.");
                else imageSize = asInt;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("15: Positive integer percentage expected.");
            }
        }

        Float imageWidth = null;
        if (width != null) {
            if (width.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");

            try {
                float unit;
                if (width.endsWith("in")) {
                    width = width.substring(0, width.length() - 2);
                    unit = Processor.getPointsPerInch();
                } else {
                    unit = Processor.getPointsPerMM();
                }

                float asNumber = Float.parseFloat(width);
                if (asNumber < 0) throw new IncorrectFormatException("2: Non-negative decimal expected.");
                else imageWidth = asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        }

        Float imageHeight = null;
        if (height != null) {
            if (height.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");

            try {
                float unit;
                if (height.endsWith("in")) {
                    height = height.substring(0, height.length() - 2);
                    unit = Processor.getPointsPerInch();
                } else {
                    unit = Processor.getPointsPerMM();
                }

                float asNumber = Float.parseFloat(height);
                if (asNumber < 0) throw new IncorrectFormatException("2: Non-negative decimal expected.");
                else imageHeight = asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        }

        ContentAlignment imageAlignment;
        if (alignment != null) {
            if (alignment.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");
            imageAlignment = switch (alignment) {
                case "Left" -> ContentAlignment.LEFT;
                case "Right" -> ContentAlignment.RIGHT;
                case "Center" -> ContentAlignment.CENTER;
                default -> throw new IncorrectFormatException("6: Content alignment type expected.");
            };
        } else imageAlignment = Processor.usedStyleGuide.defaultImageAlignment();

        ImageRenderer.render(id, imageAlignment, imageSize, imageWidth, imageHeight);
    }

    @Override
    protected void checkForWarnings() {

    }
}
