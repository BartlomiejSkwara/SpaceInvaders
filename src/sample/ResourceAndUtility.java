package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
public class ResourceAndUtility {

    public static Image icon;
    public static Image sprite_sheet;

    public static Font fontOtherSmall;
    public static Font fontTitle;
    public static Font fontOther;

    public static final Rectangle2D SPACE_SHIP = new Rectangle2D(12, 196, 52, 32);
    public static final Rectangle2D SHIP_EXPLOSION = new Rectangle2D(224, 4, 52, 32);
    public static final Rectangle2D BULLET_EXPLOSION = new Rectangle2D(232, 196, 32, 32);
    public static final Rectangle2D BULLET = new Rectangle2D(208, 84, 4, 28);


    /**
     * Funkcja odnajduje na spriteshecie kosmitę
     * @param variant - typ kosmity (0,1 lub 2)
     * @param frame - początkowa klatka animacji (0 lub 1)
     * @return - zwraca pozycję na spritesheecie  i wymiary kosmity
     */
    static public Rectangle2D createAlienViewport(int variant, int frame) {
        int height = 0;
        switch (variant) {
            case 0:
                height = 32;
                break;
            case 1:
                height = 44;
                break;
            case 2:
                height = 48;
                break;
        }
        return new Rectangle2D(20 + 68 * variant, 4 + 41 * frame, height, 32);
    }

    /**
     * Funkcja sprawdza czy dwa obiekty ze sobą kolidują
     * @param viewport - viewport statku z którego można wyciągnąć szerokość i wysokość obiektu nr 1
     * @param shipX - pozycja statku na osi x
     * @param shipY - pozycja statku na osi y
     * @param x - pozycja pocisku na osi x
     * @param y - pozycja pocisku na osi y
     * @param width - szerokość pocisku
     * @param height - wysokość pocisku
     * @return gdy kolizja występuje zwraca true , w przeciwny wypadku zwrócona wartość to false
     */
    static public boolean intersection (Rectangle2D viewport,int shipX, int shipY, int x, int y, int width, int height){
        if (((x>shipX&&(x<shipX+viewport.getWidth()))||((x<shipX)&&(x+width>shipX)))   &&
                (((y>shipY)&&(y<shipY+viewport.getHeight()))||((y<shipY)&&(y+height>shipY)))) {

            return true;
        }
        return false;
    }
}
