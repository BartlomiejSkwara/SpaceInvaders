package sample.objects;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import sample.action.Action;
import sample.objects.entities.Entity;

public class Explosion extends Entity {
    public AnimationTimer timeTillExplosionEnd;
    int frame;


    /**
     * Konstruktor eksplozji
     * @param image
     * @param sprite
     */
    public Explosion(Image image, Rectangle2D sprite) {
        super(image, sprite);
        frame=0;

    }

    /**
     * Ten konstruktor wyśrodkowuje eksplzje na innym obiekcie
     * @param image spritesheet
     * @param sprite pozycja sprita na spritesheecie
     * @param targetX miejsce na ekranie oś x
     * @param targetY miejsce na ekranie oś x
     * @param targetWidth  szerokość eksplodującego obiektu
     * @param targetHeight wysokość eksplodującego obiektu
     */
    public Explosion(Image image, Rectangle2D sprite, int targetX, int targetY,int targetWidth ,int targetHeight){
        this(image,sprite);
        this.setTranslateX(targetX+targetWidth/2-this.getViewport().getWidth()/2);
        this.setTranslateY(targetY+targetHeight/2-this.getViewport().getHeight()/2);

    }


    /**
     * Funkcja ustwia co stanie się pod koniec eksplozji
     * @param action to co się stanie
     */
    public void setExplosionResult(Action action){
        timeTillExplosionEnd=new AnimationTimer() {
            @Override
            public void handle(long l) {
                frame++;
                if (frame==15){
                    timeTillExplosionEnd.stop();
                    action.performAction();
                }
            }

        };
        timeTillExplosionEnd.start();
    }


}
