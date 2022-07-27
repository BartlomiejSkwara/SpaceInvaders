package sample.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.Main;
import sample.objects.CCLabel;
import sample.ResourceAndUtility;
import sample.objects.entities.Entity;

public class MainMenuScene extends CustomScene{

    public MainMenuScene(VBox root) {

        super(root);

        //Wyśrodkowanie i kolor tła
        root.setAlignment(Pos.TOP_CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));

        ///Napis Space Invaders :)
        Label titleScreen = new Label("Space Invaders");
        titleScreen.setBorder(new Border(new BorderStroke(null,null,null,new BorderWidths(40,0,80,0))));
        titleScreen.setFont(ResourceAndUtility.fontTitle);
        titleScreen.setTextFill(Color.WHITE);

        ///Dodaj przyciski
        CCLabel txt1 = new CCLabel("Graj",ResourceAndUtility.fontOther,(e)-> Main.setCurrentScene(new GameScene((new Pane()))));
        CCLabel txt2 = new CCLabel("Wyniki",ResourceAndUtility.fontOther,(e)-> Main.setCurrentScene(new ScoreboardScene(new VBox(10))));
        CCLabel txt3 = new CCLabel("Wyjdź",ResourceAndUtility.fontOther,(e)->Main.mainStage.close());

        CCLabel txt4 = new CCLabel("Autor: Bartłomiej Skwara",ResourceAndUtility.fontOtherSmall);
        txt4.addMargin(200,0,0,0);

        ///Maluteńki kosmita :3
        Entity sprite1 = new Entity(ResourceAndUtility.sprite_sheet, ResourceAndUtility.createAlienViewport(0,0));


        root.getChildren().addAll(titleScreen,txt1,txt2,txt3,sprite1,txt4);

    }
}
