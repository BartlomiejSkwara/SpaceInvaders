package sample.objects.entities;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Entity extends ImageView {



    public Entity (Image image, Rectangle2D sprite ){
        super(image);
        this.setViewport(sprite);

    }

    public void moveHorizontal(int x){
        this.setTranslateX(this.getTranslateX()+x);
    }

    public void moveVertical(int y){
        this.setTranslateY(this.getTranslateY()+y);
    }

}
