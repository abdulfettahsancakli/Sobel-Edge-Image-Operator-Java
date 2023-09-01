import javax.swing.*;
import java.awt.*;

public class DrawingPanelFor2Byte extends JPanel {
    int width, height;
    int[][] pixels;

    public DrawingPanelFor2Byte(int width, int height, int[][] pixel) {
        this.width = width;
        this.height = height;
        this.pixels = pixel;
    }

    public void paintComponent(Graphics g) {
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++) {
                if (pixels[col][row] >= 0 && pixels[col][row] < 256) {
                    g.setColor(new Color(pixels[col][row], pixels[col][row], pixels[col][row]));
                    g.fillRect(col, row, 1, 1);
                }
            }
    }
}
