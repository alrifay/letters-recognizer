package letters_recogniser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Main class
 *
 * @version 0.0.1
 * @author Mohamed
 */
public class Letters_recogniser {

    public static ArrayList<Letter> letters;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        initLettersArray();
        //image to test :v
        BufferedImage letterInQuestion = ImageIO.read(new File("images/test-letters/test-t.png"));
        int max = 0;
        char winner = '-';
        for (Letter letter : letters) {
            int result = letter.match(letterInQuestion);
            if (result > max) {
                max = result;
                winner = letter.getChar();
            }
            System.out.println(letter.getChar() + " = " + result);
        }
        System.out.println("Winner is (" + winner + ") With result " + max + "% :D");
    }

    private static void initLettersArray() throws IOException {
        letters = new ArrayList<>();
        for (char Char = 'A'; Char <= 'Z'; Char++) {
            letters.add(new Letter("" + Char));
        }
    }
}
