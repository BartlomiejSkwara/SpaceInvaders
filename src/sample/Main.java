package sample;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.scenes.CustomScene;
import sample.scenes.MainMenuScene;
import sample.scenes.ScoreboardScene;


public class Main extends Application {



    public static CustomStage mainStage;
    public static CustomScene currentScene;


    static int bruh=213;

    //Statyczna funkcja stworzona w celu zmieniania obecnej sceny
    public static void setCurrentScene (CustomScene scene){
        currentScene=scene;
        mainStage.setScene(currentScene);
    }


    //Funkcja Wczytuje wszystkie potrzebne pliki
    private void loadResources (){
        //Load font
        ResourceAndUtility.fontTitle=Font.loadFont(getClass().getResourceAsStream("resources/Alien Eclipse.ttf"),70);
        ResourceAndUtility.fontOther=Font.loadFont(getClass().getResourceAsStream("resources/coolvetica rg.otf"),50);
        ResourceAndUtility.fontOtherSmall = Font.loadFont(getClass().getResourceAsStream("resources/coolvetica rg.otf"),40);
        //Load Icon
        ResourceAndUtility.icon = new Image(getClass().getResourceAsStream("resources/icon.png"));
        //Load Image
        ResourceAndUtility.sprite_sheet = new Image(getClass().getResourceAsStream("resources/sprites2.png"));
    }



    @Override
    public void start(Stage s)  {

        //Wczytuje tabele wynikow , czcionki itd
        loadResources();
        ScoreboardScene.scoreboard.readFromScoreFile();

        //Przygotowuje aplikacje do Pokazania menu głównego
        mainStage=new CustomStage(ResourceAndUtility.icon);
        mainStage.setScene(new MainMenuScene(new VBox(20)));
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
