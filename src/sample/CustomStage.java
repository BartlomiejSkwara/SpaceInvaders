package sample;


import javafx.scene.image.Image;
import javafx.stage.Stage;


public class CustomStage extends Stage {
    public CustomStage(Image icon){
        this.setTitle("Space Invaders");
        this.setResizable(false);
        this.getIcons().add(icon);

    }
}
