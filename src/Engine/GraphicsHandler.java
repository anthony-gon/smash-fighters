package Engine;

import GameObject.ImageEffect;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsHandler {
    private Graphics2D g;

    public Graphics2D getGraphics() {
        return g;
    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    // Draws image at the specified (x, y) position without scaling
    public void drawImage(BufferedImage image, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    // Draws image at the specified (x, y) position with width and height (scaling)
    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        g.drawImage(image, x, y, width, height, null);
    }

    // Draws image with various image effects like flipping horizontally or vertically
    public void drawImage(BufferedImage image, int x, int y, int width, int height, ImageEffect imageEffect) {
        switch (imageEffect) {
            case NONE:
                drawImage(image, x, y, width, height);
                break;
            case FLIP_HORIZONTAL:
                g.drawImage(image, x + width, y, -width, height, null);
                break;
            case FLIP_VERTICAL:
                g.drawImage(image, x, y + height, width, -height, null);
                break;
            case FLIP_H_AND_V:
                g.drawImage(image, x + width, y + height, -width, -height, null);
                break;
        }
    }

    // Draws an outlined rectangle
    public void drawRectangle(int x, int y, int width, int height, Color color) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.drawRect(x, y, width, height);
        g.setColor(oldColor);
    }

    // Draws an outlined rectangle with a specified border thickness
    public void drawRectangle(int x, int y, int width, int height, Color color, int borderThickness) {
        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();
        g.setStroke(new BasicStroke(borderThickness));
        g.setColor(color);
        g.drawRect(x, y, width, height);
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    // Draws a filled rectangle with the specified color
    public void drawFilledRectangle(int x, int y, int width, int height, Color color) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(oldColor);
    }

    // Draws a filled rectangle with a border
    public void drawFilledRectangleWithBorder(int x, int y, int width, int height, Color fillColor, Color borderColor, int borderThickness) {
        drawFilledRectangle(x, y, width, height, fillColor);
        drawRectangle(x, y, width, height, borderColor, borderThickness);
    }

    // Draws a string with the specified font and color
    public void drawString(String text, int x, int y, Font font, Color color) {
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
        g.setFont(oldFont);
        g.setColor(oldColor);
    }

    // Draws a string with an outline
    public void drawStringWithOutline(String text, int x, int y, Font font, Color textColor, Color outlineColor, float outlineThickness) {
        // Remember original settings
        Color originalColor = g.getColor();
        Stroke originalStroke = g.getStroke();
        RenderingHints originalHints = g.getRenderingHints();
        g.setStroke(new BasicStroke(outlineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Create a glyph vector from the text
        GlyphVector glyphVector = font.createGlyphVector(g.getFontRenderContext(), text);

        // Get the shape object
        Shape textShape = glyphVector.getOutline();
        AffineTransform at = new AffineTransform();
        at.setToTranslation(Math.round(x), Math.round(y));
        textShape = at.createTransformedShape(textShape);

        // Activate anti-aliasing for text rendering (for better quality)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw the outline
        g.setColor(outlineColor);
        g.draw(textShape);

        // Fill the text shape
        g.setColor(textColor);
        g.fill(textShape);

        // Reset to original settings
        g.setColor(originalColor);
        g.setStroke(originalStroke);
        g.setRenderingHints(originalHints);
    }

    // Draws a filled polygon based on the specified x and y points and a color
    public void drawFilledPolygon(int[] xPoints, int[] yPoints, Color color) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drawFilledPolygon'");
    }

    // Scales the graphics context around a center point
    public void scale(double scaleX, double scaleY, int centerX, int centerY) {
        // Save the current transform
        AffineTransform oldTransform = g.getTransform();

        // Create a new transform for scaling
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY);
        transform.scale(scaleX, scaleY);
        transform.translate(-centerX, -centerY);

        // Apply the scaling transform
        g.transform(transform);

        // Restore the original transform after scaling
        g.setTransform(oldTransform);
    }

    public void translate(int xShakeOffset, int yShakeOffset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'translate'");
    }
}
