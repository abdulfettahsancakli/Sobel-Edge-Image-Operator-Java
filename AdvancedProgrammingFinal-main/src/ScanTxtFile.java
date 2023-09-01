import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScanTxtFile {
    public int width;
    public int height;
    public int[][] pixels = null;
    public void scanFile(String fileName) {
        try {
            Scanner inFile = new Scanner(new File(fileName));
            int fileType = inFile.nextInt();
            width = inFile.nextInt();
            height = inFile.nextInt();
            System.out.printf("type: %d, width: %d, height:%d\n",
                    fileType, width, height);
            pixels = new int[width][height];
            for (int row = 0; row < height; row++)
                for (int col = 0; col < width; col++)
                    pixels[col][row] = inFile.nextShort();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
