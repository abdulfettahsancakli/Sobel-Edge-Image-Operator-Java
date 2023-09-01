abstract class Edge {
    int max = 0;
    int[][] gStore = new int[0][];
    int[][] g = new int[0][];
    public float formula=0;
    int[][] gX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    int[][] gY = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

    void createDoubleEdge(int width, int height, int gXPixels[][], int gYPixels[][]) {
        gStore  = new int[width][height];
        g = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int gradientValue = (int) Math.sqrt(Math.pow(gXPixels[i][j], 2) + Math.pow(gYPixels[i][j], 2));
                if (gradientValue > max) {
                    max = gradientValue;
                }
                gStore [i][j] = gradientValue;
            }
        }
        setG(width, height);
    }


    public void createSingleEdge(int width, int height, int gPixels[][], int gradient[][]) {
        gStore  = new int[width][height];
        g = new int[width][height];
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                if (i != 0 && j != 0) {
                    int p00 = gPixels[i - 1][j - 1];
                    int p01 = gPixels[i][j - 1];
                    int p02 = gPixels[i + 1][j - 1];
                    int p10 = gPixels[i - 1][j];
                    int p11 = gPixels[i][j];
                    int p12 = gPixels[i + 1][j];
                    int p20 = gPixels[i - 1][j + 1];
                    int p21 = gPixels[i][j + 1];
                    int p22 = gPixels[i + 1][j + 1];

                    int gradientY = ((gradient[0][0] * p00) + (gradient[0][1] *p01) + (gradient[0][2] * p02)) + ((gradient[1][0] * p10) + (gradient[1][1] * p11) + (gradient[1][2] * p12))
                            + ((gradient[2][0] * p20) + (gradient[2][1] * p21) + (gradient[2][2] * p22));
                    if (gradientY > max) {
                        max = gradientY;
                    }
                    gStore [i][j] = gradientY;
                }
            }
        }
        setG(width, height);
    }

    private void setG(int width, int height){
        formula=(float)max/(float)255;
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                g[i][j]=(int)Math.abs(gStore [i][j]/formula);
            }
        }
    }

}
