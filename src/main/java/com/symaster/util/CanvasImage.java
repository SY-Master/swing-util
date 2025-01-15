package com.symaster.util;

import java.awt.*;

public class CanvasImage {
    private Image image;
    private Graphics graphics;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }
}
