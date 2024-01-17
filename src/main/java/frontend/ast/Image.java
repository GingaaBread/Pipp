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

    private static final String ERR_MSG_1 = "1: A text component cannot be blank";
    private static final String ERR_MSG_2 = "2: Non-negative decimal expected.";
    private static final String ERR_MSG_3 = "15: Positive integer percentage expected.";
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

    private Integer tryGetSize() {
        if (size != null) {
            if (size.isBlank()) throw new MissingMemberException(ERR_MSG_1);
            if (!size.endsWith("%")) throw new IncorrectFormatException(ERR_MSG_3);

            var withoutPercentageCharacter = size.substring(0, size.length() - 1);
            try {
                var asInt = Integer.parseInt(withoutPercentageCharacter);

                if (asInt < 1) throw new IncorrectFormatException(ERR_MSG_3);
                return asInt;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException(ERR_MSG_3);
            }
        } else return null;
    }

    private Float tryGetWidth() {
        if (width != null) {
            if (width.isBlank()) throw new MissingMemberException(ERR_MSG_1);

            try {
                float unit;
                if (width.endsWith("in")) {
                    width = width.substring(0, width.length() - 2);
                    unit = Processor.POINTS_PER_INCH;
                } else {
                    unit = Processor.POINTS_PER_MM;
                }

                float asNumber = Float.parseFloat(width);
                if (asNumber < 0) throw new IncorrectFormatException(ERR_MSG_2);
                return asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException(ERR_MSG_2);
            }
        } else return null;
    }

    private Float tryGetHeight() {
        if (height != null) {
            if (height.isBlank()) throw new MissingMemberException(ERR_MSG_1);

            try {
                float unit;
                if (height.endsWith("in")) {
                    height = height.substring(0, height.length() - 2);
                    unit = Processor.POINTS_PER_INCH;
                } else {
                    unit = Processor.POINTS_PER_MM;
                }

                float asNumber = Float.parseFloat(height);
                if (asNumber < 0) throw new IncorrectFormatException(ERR_MSG_2);
                return asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException(ERR_MSG_2);
            }
        } else return null;
    }

    private ContentAlignment tryGetAlignment() {
        if (alignment != null) {
            if (alignment.isBlank()) throw new MissingMemberException(ERR_MSG_1);
            return switch (alignment) {
                case "Left" -> ContentAlignment.LEFT;
                case "Right" -> ContentAlignment.RIGHT;
                case "Center" -> ContentAlignment.CENTER;
                default -> throw new IncorrectFormatException("6: Content alignment type expected.");
            };
        } else return Processor.usedStyleGuide.defaultImageAlignment();
    }

    @Override
    public void handleBodyElement() {
        Integer imageSize = tryGetSize();
        Float imageWidth = tryGetWidth();
        Float imageHeight = tryGetHeight();
        ContentAlignment imageAlignment = tryGetAlignment();

        ImageRenderer.render(id, imageAlignment, imageSize, imageWidth, imageHeight);
    }

    @Override
    protected void checkForWarnings() {
        // Does not produce warnings
    }
}
