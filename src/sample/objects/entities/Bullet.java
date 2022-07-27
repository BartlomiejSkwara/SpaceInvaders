package sample.objects.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import sample.ResourceAndUtility;

public class Bullet extends Entity{

    public int getSpeed() {
        return speed;
    }

    //AnimationTimer animationTimer;
    int speed;
    boolean ownedByPlayer;

    public boolean isOwnedByPlayer() {
        return ownedByPlayer;
    }

    /**
     * Konstruktor dla pocisków gracza
     * @param image - spritesheet
     * @param sprite - pozycja i wymiary pocisku na spritesheecie
     * @param xOrigin - pozycja pocisku na osi X
     * @param yOrigin - pozycja pocisku na osi Y
     * @param ownedByPlayer - czy pocisk należy do gracza (wpływa to na kierunek i system kolizji)
     */
    public Bullet(Image image, Rectangle2D sprite, double xOrigin, double yOrigin, boolean ownedByPlayer) {
        super(image, sprite);
        this.ownedByPlayer=ownedByPlayer;
        this.setTranslateX(xOrigin-2 + ResourceAndUtility.SPACE_SHIP.getWidth()/2 );
        this.setTranslateY(yOrigin+1 - ResourceAndUtility.SPACE_SHIP.getHeight());
        speed = (ownedByPlayer?-1:1)*6;
    }

    /**
     * Konstruktor dla pocisków kosmitów
     * @param image - spritesheet
     * @param sprite - pozycja i wymiary pocisku na spritesheecie
     * @param xOrigin - pozycja pocisku na osi X
     * @param yOrigin - pozycja pocisku na osi Y
     * @param ownedByPlayer - czy pocisk należy do gracza (wpływa to na kierunek i system kolizji)
     * @param shooterViewport - viewport strzelającego, pozwala na odnalezienie jego środka i wyśrodkowanie względem niego pozycji początkowej pocisku
     */
    public Bullet(Image image, Rectangle2D sprite,double xOrigin, double yOrigin, boolean ownedByPlayer, Rectangle2D shooterViewport) {
        super(image, sprite);
        this.ownedByPlayer=ownedByPlayer;
        this.setTranslateX(xOrigin-2 + shooterViewport.getWidth()/2 );
        this.setTranslateY(yOrigin+1 + shooterViewport.getHeight());
        speed = (ownedByPlayer?-1:1)*6;

    }

    /**
     * Funkcja przesuwa pocisk w osi y
     */
    public void moveVertical() {
        super.moveVertical(speed);
    }
}
