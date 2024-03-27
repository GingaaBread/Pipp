package frontend.ast.structure;

import creation.content.ContentAlignment;
import creation.content.ImageRenderer;
import error.IncorrectFormatException;
import error.MissingMemberException;
import lombok.Getter;
import lombok.Setter;
import processing.Processor;

/**
 * The image body node allows the user to display an image on the document.
 * The node accepts the following input: ID (the identifier of the image), height and width, size, and alignment.
 *
 * @version 1.0
 * @since 1.0
 */
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

    /**
     * If the image node should be handled to render an image, it tries to get the specified sizes and then
     * instructs the ImageRender class to render the image.
     */
    @Override
    public void handleBodyElement() {
        ImageRenderer.render(id, tryGetAlignment(), tryGetSize(), tryGetWidth(), tryGetHeight());
    }

    /**
     * If the node contains a size value specified by the user, it tries to parse it as a percentage.
     * For example, if the size input given be the user is "52%", this yields "52" as an Integer object.
     * If there is no size value specified by the user, this yields null, instead.
     *
     * @return null if there is no size value given by the user, or the size percentage as an integer if it exists.
     */
    private Integer tryGetSize() {
        if (size == null) return null;

        if (size.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
        else if (!size.endsWith("%")) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_3);

        var withoutPercentageCharacter = size.substring(0, size.length() - 1);
        try {
            var asInt = Integer.parseInt(withoutPercentageCharacter);

            if (asInt < 1) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_3);
            return asInt;
        } catch (NumberFormatException e) {
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_3);
        }
    }

    /**
     * If the node contains a width value specified by the user, it tries to parse it as a constant in the specified
     * unit of measurement. If it ends in "in" inches are used, else millimeters are used.
     * If there is no value given by the user, this yields null, instead.
     *
     * @return null if there is no width value given by the user, or the width in the specified unit if it exists.
     */
    private Float tryGetWidth() {
        if (width == null) return null;
        else if (width.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

        try {
            float unit;
            if (width.endsWith("in")) {
                width = width.substring(0, width.length() - 2);
                unit = Processor.POINTS_PER_INCH;
            } else {
                unit = Processor.POINTS_PER_MM;
            }

            float asNumber = Float.parseFloat(width);
            if (asNumber < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
            return asNumber * unit;
        } catch (NumberFormatException e) {
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        }
    }

    /**
     * If the node contains a height value specified by the user, it tries to parse it as a constant in the specified
     * unit of measurement. If it ends in "in" inches are used, else millimeters are used.
     * If there is no value given by the user, this yields null, instead.
     *
     * @return null if there is no height value given by the user, or the height in the specified unit if it exists.
     */
    private Float tryGetHeight() {
        if (height == null) return null;
        else if (height.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

        try {
            float unit;
            if (height.endsWith("in")) {
                height = height.substring(0, height.length() - 2);
                unit = Processor.POINTS_PER_INCH;
            } else {
                unit = Processor.POINTS_PER_MM;
            }

            float asNumber = Float.parseFloat(height);
            if (asNumber < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
            return asNumber * unit;
        } catch (NumberFormatException e) {
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        }
    }

    /**
     * If the node contains an alignment value specified by the user, it tries to parse it as an enumeration constant
     * If there is no value given by the user, this yields the default image alignment of the style guide, instead.
     *
     * @return the default image alignment of the style guide if there is no alignment, or the alignment constant
     */
    private ContentAlignment tryGetAlignment() {
        if (alignment == null) return Processor.getUsedStyleGuide().defaultImageAlignment();
        else if (alignment.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

        return switch (alignment) {
            case "Left" -> ContentAlignment.LEFT;
            case "Right" -> ContentAlignment.RIGHT;
            case "Center" -> ContentAlignment.CENTRE;
            default -> throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_6);
        };
    }

    /**
     * The Image node produces an error if any field is blank
     */
    @Override
    public void checkForWarnings() {
        if (width != null && width.isBlank() ||
                height != null && height.isBlank() ||
                size != null && size.isBlank() ||
                alignment != null && alignment.isBlank() ||
                id != null && id.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
