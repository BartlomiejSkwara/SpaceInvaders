package sample.objects.entities;


import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import sample.scenes.CustomScene;
import sample.scenes.GameScene;

public class Player extends Entity{

    AnimationTimer bulletCooldown;
    int i = 0;
    boolean canShoot;


    public Player(Image image, Rectangle2D sprite) {
        super(image, sprite);
        this.lives=3;
        this.setTranslateX((CustomScene.windowX-this.getViewport().getWidth())/2);
        this.setTranslateY(CustomScene.windowY-this.getViewport().getHeight()- GameScene.playerZone);
        canShoot=true;

        bulletCooldown = new AnimationTimer() {
            @Override
            public void handle(long l) {
                i++;
                if (i>40){
                    setCanShoot(true);
                    i=0;
                    bulletCooldown.stop();
                }
            }
        };

    }
    public void moveLeft(){
        if (this.getTranslateX()>4){
            moveHorizontal(-4);
        }
      }
    public void moveRight(){
        if (this.getTranslateX()<1024){
            moveHorizontal(+4);
        }
    }

    public boolean getCanShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void shoot(){
        this.setCanShoot(false);
        bulletCooldown.start();
    }

    private int lives;

    public int getLives() {
        return lives;
    }
    public boolean loseLife(){
        lives-=1;
        if(lives<0){
            return true;
        }
        return false;
    }
}
