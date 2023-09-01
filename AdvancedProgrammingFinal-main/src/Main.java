import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static int index = 0;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the converted image as txt:");
        String fileName = scan.nextLine();
        //Kullanıcıdan image proccessing işlemlerinin uygulanması için bir txt belgesi istedik.
        //Bu dönüştürme işlemini daha önceden jpegtotext clasından elde etmiştik.
        class ReadFile {
            int width;  //Okunacak dosyanın genişliği
            int height; //Okuncacak dosyanın yüksekliği
            int[][][] pixels = null; //Okuncak dosyanın içerisindeki pixellerin değerlerinin tutulacağı yer dizi

            public void readImage(String fileName) {
                Scanner inFile = null;

                try {
                    inFile = new Scanner(new File(fileName));
                    int fileType = inFile.nextInt();
                    width = inFile.nextInt();  //Dosya içeriğinin genişliğini aldık.
                    height = inFile.nextInt(); //Dosya içeriğinin yüksekliğini aldık.
                    inFile.nextInt();
                    System.out.printf("type: %d, width: %d, height:%d\n",
                            fileType, width, height);
                    pixels = new int[width][height][3]; //pixels değerlerini tutan değerleri belirledik.
                    //Pngden elde edilmesi gereken değerleri tanımladık.
                    for (int col = 0; col < height; col++)
                        for (int row = 0; row < width; row++) {
                            for (int rgb = 0; rgb < 3; rgb++) {
                                pixels[row][col][rgb] = inFile.nextInt();//Satır sütun okuyarak pixels arrayinin değerlerini içine atadık.
                            }
                        }

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        ReadFile read = new ReadFile(); //ReadFile sınıfndan bir read objesi olusturduk
        read.readImage(fileName); // Oluşturduğumuz obje ile ReadFile sınıfındaki readImage metodunu kullanıcıdan aldığımız dosya ismini gönderdik.

        int width = read.width; // Okuduğumuz dosyadan elde ettiğimiz genişlik değerini width değişkenine atadık.
        int height = read.height; //Okuduğumuz dosyadan elde ettiğimiz yükseklik değerini height değişkenine atadık.
        int[][][] Imagepixels = read.pixels; //Okuduğumuz dosyadan elde ettiğimiz r g b değerlerini Imagepixels arrayinde eşitledik.



        class ConvertImages {
            // Bu classda original resmimizi grayscale ve binary metodlarını uygulayarak değiştirdik.
            public int[][] pixels = null;
            public void convertGrayScale(int width, int height, int[][][] graypixels) { // Metodun uygulanması için width,height ve 3 boyutlu bir dizi parametrelerini olusturduk
                pixels = new int[width][height];
                for (int col = 0; col < width; col++) {
                    for (int row = 0; row < height; row++) {
                        pixels[col][row] = (int)((0.30* graypixels[col][row][0]) + (0.59 *graypixels[col][row][1]) + (0.11 * graypixels[col][row][2]));
                        //Metoda gönderdiğimiz 3 boyutlu dizinin pixel değerlerini grayscale methodunu uygularak değiştirdik.
                    }
                }
            }

        }


        ConvertImages grayscale = new ConvertImages();
        grayscale.convertGrayScale(width,height,Imagepixels);
        int [][] GrayScalePxl = grayscale.pixels;
        // ConvertImages sınıfından grayscale objesi oluşturduk.
        //Oluşturduğumuz objeden o sınıfa ait convertGrayscale methoduna yükseklik,genişlik ve renkli resim dizimizi gönderdik.
        //Bu methoddan oluşan değeri 2 boyutlu pixelsForGrayScale değerine eşitledik.

        VerticalEdge verticalEdge = new VerticalEdge();
        verticalEdge.create(width, height, GrayScalePxl);
        int[][] gX = verticalEdge.g;

        HorizontalEdge horizontalEdge = new HorizontalEdge();
        horizontalEdge.create(width, height, GrayScalePxl);
        int[][] gY = horizontalEdge.g;

        GEdge GEdge = new GEdge();
        GEdge.create(width, height, gX, gY);
        int[][] g = GEdge.g;

        Byte2Panel originalImg = new Byte2Panel(width, height, GrayScalePxl);

        final int[] xVals = new int[2];
        final int[] yVals = new int[2];
        JTextField textField = new JTextField();
        JButton resetButton = new JButton("Reset");
        textField.setColumns(20);

        originalImg.add(textField);
        originalImg.add(resetButton);

        final JFrame frame = new JFrame("Abdulfettah Sancaklı");
        frame.setSize(920, 270);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(1, 1));

        Byte2Panel xEdge = new Byte2Panel(width, height, gX);
        Byte2Panel yEdge = new Byte2Panel(width, height, gY);
        Byte2Panel XYEdge = new Byte2Panel(width, height, g);
        Byte2Panel histogram = new Byte2Panel(width, height, new int[width][height]);
        Byte2Panel click = new Byte2Panel(width, height, new int[width][height]);

        JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP);
        tp.add("Original", originalImg);
        tp.add("X Edge", xEdge);
        tp.add("Y Edge", yEdge);
        tp.add("XY Edge", XYEdge);
        tp.add("Histogram", histogram);
        tp.add("Click", click);
        frame.getContentPane().add(tp);

        originalImg.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (index < 2) {
                    if (me.getX() >= GrayScalePxl.length || me.getY() >= GrayScalePxl[0].length) {
                        JOptionPane.showMessageDialog(null, "How could you miss that image :D ", "Come on!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        xVals[index] = me.getX();
                        yVals[index] = me.getY();
                    }
                }
            }

            public void mouseReleased(MouseEvent me) {
                if (me.getX() < GrayScalePxl.length && me.getY() < GrayScalePxl[0].length) {
                    //Update the text field
                    if (index < 2) {
                        String msg = "Click-" + (index + 1) + ":" + xVals[index] + "," + yVals[index] + "  ";
                        textField.setText(textField.getText() + msg);
                    }
                    index++;

                    //If coordinates selected, then get sub image pixels and print on the tab-6
                    if (index == 2) {
                        int[][] tab6Pixels = makeTabPixel(xVals, yVals, GrayScalePxl);
                        tp.removeTabAt(5);
                        tp.addTab("click", new Byte2Panel(tab6Pixels.length, tab6Pixels[0].length, tab6Pixels));
                    }
                }
            }
        });

        //Reset button task to clear selected coordinates
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xVals[0] = 0;
                xVals[1] = 0;
                yVals[0] = 0;
                yVals[1] = 0;
                index = 0;
                textField.setText("");
            }
        });
    }

    private static int[][] makeTabPixel(int[] xVals, int[] yVals, int[][] GrayScalePxl) {

        //Setup actual coordinates for sub image pixels
        int x1 = Math.min(xVals[0], xVals[1]);
        int x2 = Math.max(xVals[0], xVals[1]);
        int y1 = Math.min(yVals[0], yVals[1]);
        int y2 = Math.max(yVals[0], yVals[1]);

        int xLength = (x2 - x1);
        int yLength = (y2 - y1);

        //Init doubled array for subImage
        int[][] doubledSubPixels = new int[xLength * 2][yLength * 2];

        for (int i = x1, xIndex = 0; i < x2; i++, xIndex += 2) {
            for (int j = y1, yIndex = 0; j < y2; j++, yIndex += 2) {
                doubledSubPixels[xIndex][yIndex] = GrayScalePxl[i][j];
                doubledSubPixels[xIndex + 1][yIndex] = GrayScalePxl[i][j];
                doubledSubPixels[xIndex][yIndex + 1] = GrayScalePxl[i][j];
                doubledSubPixels[xIndex + 1][yIndex + 1] = GrayScalePxl[i][j];
            }
        }
        return doubledSubPixels;
    }
}
