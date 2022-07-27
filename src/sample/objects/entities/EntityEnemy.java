package sample.objects.entities;

import javafx.scene.image.Image;

import sample.ResourceAndUtility;

public class EntityEnemy extends Entity {


    private int animationState = 0;
    private int alienVariant=1;


    /**
     * Podstawowy konstruktor
     * @param image - spritesheet
     * @param alienVariant - rodzaja spritea kosmity
     * @param animationState - początkowa klatka
     */
    public EntityEnemy(Image image, int alienVariant, int animationState){
        super(image, ResourceAndUtility.createAlienViewport(alienVariant,animationState));

        this.alienVariant=alienVariant;
        this.animationState=animationState;
    }




    public void moveVertical(){
        this.setTranslateY(this.getTranslateY()+5);
    }

    public void moveHorizontal (int distance){
        this.setTranslateX(this.getTranslateX()+distance);
    }

    /**
     * Zmienia klatkę animacji na następną
     */
    public void changeSprite (){
        if (animationState==0){
            this.setViewport(ResourceAndUtility.createAlienViewport(alienVariant,1));
            animationState=1;
        }
        else if (animationState==1) {
            this.setViewport(ResourceAndUtility.createAlienViewport(alienVariant,0));
            animationState=0;
        }
    }



}
