package letters_recogniser;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Mohamed
 */
public class Letter {

    private static int MatrixSize = 128;
    private final char _char;
    private final ArrayList<ArrayList<Boolean>> _letterMatrix;

    public static void setMatrixSize(int value) {
        Letter.MatrixSize = value;
    }

    public Letter(String _char) throws IOException {
        this._char = _char.charAt(0);
        this._letterMatrix = new ArrayList(Letter.MatrixSize);
        letterToMatrix(initImage(), this._letterMatrix);
    }

    public char getChar() {
        return this._char;
    }

    private void letterToMatrix(BufferedImage letterImage, ArrayList<ArrayList<Boolean>> letterMatrix) throws IOException {
        int pixelArray[] = new int[Letter.MatrixSize * Letter.MatrixSize];
        letterImage.getSampleModel().getPixels(0, 0, Letter.MatrixSize, Letter.MatrixSize, pixelArray, letterImage.getData().getDataBuffer());
        for (int x = 0; x < Letter.MatrixSize; x++) {
            letterMatrix.add(new ArrayList<>(Letter.MatrixSize));
            for (int y = 0; y < Letter.MatrixSize; y++) {
                letterMatrix.get(x).add(pixelArray[Letter.MatrixSize * x + y] < 90);
            }
        }
    }

    private BufferedImage initImage() {
        BufferedImage letterImage = new BufferedImage(Letter.MatrixSize, Letter.MatrixSize, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D letterImageGraphics = letterImage.createGraphics();
        letterImageGraphics.setColor(Color.white);
        letterImageGraphics.fillRect(0, 0, Letter.MatrixSize, Letter.MatrixSize);
        letterImageGraphics.setColor(Color.black);
        Font font = Font.decode("Arial " + (Letter.MatrixSize));
        letterImageGraphics.setFont(font);
        Rectangle2D charBound = font.getStringBounds("" + this._char, letterImageGraphics.getFontRenderContext());
        letterImageGraphics.drawString(this._char + "",
                (int) ((Letter.MatrixSize - charBound.getWidth()) / 2),
                (int) (Letter.MatrixSize - (Math.abs(Letter.MatrixSize - charBound.getHeight()))));
        return letterImage;
    }

    public int match(BufferedImage letterInQuestion) throws IOException {
        letterInQuestion = prepareImageToMatch(letterInQuestion);
        ArrayList<ArrayList<Boolean>> letterInQuestionMatrix = new ArrayList<>(MatrixSize);
        letterToMatrix(letterInQuestion, letterInQuestionMatrix);
        int count = 0;
        int countPositive = 0,
                totalPositive = 0;
        for (int x = 0; x < Letter.MatrixSize; x++) {
            for (int y = 0; y < Letter.MatrixSize; y++) {
                totalPositive += this._letterMatrix.get(x).get(y) == true ? 1 : 0;
                if (this._letterMatrix.get(x).get(y) == letterInQuestionMatrix.get(x).get(y)) {
                    count++;
                    countPositive += this._letterMatrix.get(x).get(y) == true ? 1 : 0;
                }
            }
        }
        return 100 * countPositive / totalPositive;
    }

    private BufferedImage prepareImageToMatch(BufferedImage letterInQuestion) throws IOException {
        BufferedImage modifiedImage = new BufferedImage(Letter.MatrixSize, Letter.MatrixSize, BufferedImage.TYPE_BYTE_GRAY);
        double xs = Letter.MatrixSize * 1.0 / letterInQuestion.getWidth(),
                ys = Letter.MatrixSize * 1.0 / letterInQuestion.getHeight();
        AffineTransform affineTransform = AffineTransform.getScaleInstance(xs, ys);
        modifiedImage.createGraphics().drawImage(letterInQuestion, affineTransform, null);
        return modifiedImage;
    }
}
