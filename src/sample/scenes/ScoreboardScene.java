package sample.scenes;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sample.Main;
import sample.ResourceAndUtility;
import sample.Scoreboard;
import sample.objects.CCLabel;

public class ScoreboardScene extends CustomScene{

    public static Scoreboard scoreboard=new Scoreboard();
    VBox vBox;


    public ScoreboardScene(VBox vBox) {
        super(vBox);
        this.vBox=vBox;

        //Kolorek i centrowanko
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));


        CCLabel header = new CCLabel("Tabela Wynikow", ResourceAndUtility.fontTitle);
        header.addMargin(25,0,0,0);
        vBox.getChildren().add(header);

        //Wypisywanie wyników
        int i = 0;
        //Wypisz wyniki
        while (i<scoreboard.topScore.size()){
            CCLabel txt = new CCLabel((i+1)+". "+scoreboard.scoreHolder.get(i)+": "+scoreboard.topScore.get(i), ResourceAndUtility.fontOtherSmall);
            vBox.getChildren().add(txt);
            i++;
        }
        //A tutaj puste miejsca
        while (i<10){
            CCLabel txt = new CCLabel((i+1)+". Brak: "+0, ResourceAndUtility.fontOtherSmall);
            vBox.getChildren().add(txt);
            i++;
        }

        //Cofanko
        CCLabel txt2 = new CCLabel("Wróć", ResourceAndUtility.fontOther, (e)-> Main.setCurrentScene(new MainMenuScene(new VBox(20))));
        vBox.getChildren().add(txt2);

    }

    /**
     * Funkcja dodaje nowy wynik do tabeli
     * @param newScore punkty zdobyte przez gracza
     * @param newName nazwa gracza
     */
    static public void addNewScore (int newScore, String newName){
        scoreboard.addNewScore(newScore,newName);
    }


}
