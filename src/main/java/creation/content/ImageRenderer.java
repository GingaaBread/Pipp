package creation.content;

import creation.page.PageAssembler;
import creation.page.PageCreator;
import creation.content.text.TextRenderer;
import error.ContentException;
import error.MissingMemberException;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import processing.Processor;

import java.io.File;
import java.io.IOException;

/**
 * The ImageRender class is used to render images on the document.
 * Users can specify the identifier of the image including its file ending (for example, "dog.png") and the renderer
 * will look up the project's img/ folder looking for an image with that identifier.
 * Supported formats are png, jpg, jpeg, gif, bmp and tiff.
 * Note that this will throw an exception if the rendered result would display an image that did not fit on a page.
 *
 * @version 1.0
 * @since 1.0
 */
public class ImageRenderer {

    /**
     * Path to the image (img/) folder that contains all images the user wants to include in the document
     */
    private static final String IMAGE_INPUT_PATH = "src/main/resources/img/";

    /**
     * Prevents instantiation
     */
    private ImageRenderer() {
        throw new UnsupportedOperationException("Cannot instantiate helper class");
    }

    /**
     * Renders the image with the specified identifier on the document using the specified content alignment.
     * If no alignment is specified, the style guide's default image alignment is used. Uses either the size value
     * as a relative size in regard to the default image size, or uses the width and height attributes as an absolute
     * image scale. If neither attribute is set, the default image resolution is used.
     * Throws exceptions if the image with the specified id cannot be found, if the image does not fit on the page using
     * the used dimensions, or if the conversion fails.
     *
     * @param imageId        the identifier of the image including its file ending relative to the project's img/ folder
     * @param imageAlignment the desired alignment of the image, or null if the style guide's default should be used
     * @param imageSize      the relative size of the image as an Integer (25% = 25) or null if undesired
     * @param imageWidth     the absolute width of the image in points (25pt = 25) or null if undesired
     * @param imageHeight    the absolute height of the image in points (25pt = 25) or null if undesired
     */
    public static void render(@NonNull String imageId, @NonNull ContentAlignment imageAlignment, Integer imageSize,
                              Float imageWidth, Float imageHeight) {
        try {
            final float availableWidth = Processor.getAvailableContentWidth();
            final float leading = 1.2f * TextRenderer.getMaxFontSizeOfCurrentLine() * Processor.getSpacing();

            int width;
            int height;
            PDImageXObject imageObject = tryCreateImageObject(imageId);

            width = imageObject.getWidth();
            height = imageObject.getHeight();

            if (imageSize != null) {
                float scale = imageSize / 100.0f;
                width = (Math.round(imageObject.getWidth() * scale));
                height = (Math.round(imageObject.getHeight() * scale));
            } else {
                if (imageWidth != null) width = (Math.round(imageWidth));
                if (imageHeight != null) height = (Math.round(imageHeight));
            }

            var targetYPosition = PageCreator.currentYPosition - height;

            // Check if the image does not fit on the current page anymore
            if (targetYPosition < Processor.getMargin()) {
                PageCreator.createNewPage();
                targetYPosition = PageCreator.currentYPosition - height;

                // Check if the image is too large to even fit on an empty page
                if (targetYPosition < Processor.getMargin())
                    throw new ContentException("1: Image with ID '" + imageId + "' is too large to fit on a single page.");
            }

            if (width > availableWidth)
                throw new ContentException("2: Image with ID '" + imageId + "' is too wide to fit on a page.");

            final var contentStream = new PDPageContentStream(PageAssembler.getDocument(), PageCreator.getCurrent(),
                    PDPageContentStream.AppendMode.APPEND, false);

            final float targetXPosition = switch (imageAlignment) {
                case LEFT -> Processor.getMargin();
                case RIGHT -> availableWidth - width + Processor.getMargin();
                case CENTER -> Processor.getMargin() + (availableWidth / 2) - (width / 2f);
            };

            contentStream.drawImage(imageObject, targetXPosition, targetYPosition, width, height);
            PageCreator.currentYPosition -= leading + height;
            PageCreator.currentPageIsEmpty = false;

            contentStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("Image could not be rendered.");
        }
    }

    /**
     * If the image at the path exists, yields the image as an image object.
     *
     * @param imageId the id of the image in the img/ folder
     * @return the image as an image object
     */
    private static PDImageXObject tryCreateImageObject(@NonNull final String imageId) {
        try {
            var file = new File(IMAGE_INPUT_PATH + imageId);
            return PDImageXObject.createFromFileByExtension(file, PageAssembler.getDocument());
        } catch (IllegalArgumentException | IOException e) {
            throw new MissingMemberException("9: Image with the image id '" + imageId + "' does not exist in the " +
                    "img/ folder. Make sure it also has its file ending defined.");
        }
    }

}
