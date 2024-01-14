package frontend.ast;

import creation.ContentAlignment;
import creation.ImageRenderer;
import error.IncorrectFormatException;
import error.MissingMemberException;
import lombok.Getter;
import lombok.Setter;

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

        ImageRenderer.render(id, ContentAlignment.CENTER, imageSize);
    }

    @Override
    protected void checkForWarnings() {

    }
}
