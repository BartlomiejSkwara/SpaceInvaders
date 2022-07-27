package sample.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.Main;
import sample.ResourceAndUtility;
import sample.objects.CCLabel;

public class AfterGameScene extends CustomScene{


    int newScore;


    public AfterGameScene(VBox root, String komunikat, int score){
        super(root);

        newScore=score;

        //Kolor tłą i wyśrodkowanie
        root.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        root.setAlignment(Pos.TOP_CENTER);

        CCLabel txt1 = new CCLabel(komunikat, ResourceAndUtility.fontTitle);
        txt1.addMargin(40,0,0,0);

        CCLabel txt2 = new CCLabel("Zdobyłeś " +score+" punktów :>", ResourceAndUtility.fontOther);
        txt2.addMargin(40,40,0,0);

        CCLabel txt3 = new CCLabel("Zapisać twój wynik ?", ResourceAndUtility.fontOther);
        txt3.addMargin(20,40,0,0);

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.TOP_CENTER);

        //Zgoda na zapis wyniku
        CCLabel button1 = new CCLabel("Tak", ResourceAndUtility.fontOther,(e)->{
            root.getChildren().removeAll(buttons,txt1,txt2,txt3);
            this.askForName(root);

        });
        button1.addMargin(0,0,40,0);

        //Brak zgody na zapis wyniku i wczesne przekierowanie do  mozliwosci ponownej gry lub powrotu do menu
        CCLabel button2 = new CCLabel("Nie", ResourceAndUtility.fontOther,(e)->{
            root.getChildren().removeAll(buttons,txt1,txt2,txt3);
            this.restartOrMenu(root);
        });
        button2.addMargin(0,0,0,40);

        buttons.getChildren().addAll(button1,button2);
        root.getChildren().addAll(txt1,txt2,txt3,buttons);

    }


    /**
     * Zmienia pokazywane elementy na takie ktore pozwalają pobrać nick gracza
     */
    void askForName(VBox root){

        CCLabel txt1 =  new CCLabel("Podaj swoją nazwę", ResourceAndUtility.fontOther);
        txt1.addMargin(200,0,0,0);

        TextField textField = new TextField();
        textField.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        textField.setFont(ResourceAndUtility.fontOther);
        textField.setMaxWidth(500);
        textField.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,null,BorderWidths.DEFAULT)));
        textField.setAlignment(Pos.BASELINE_CENTER);
        textField.setStyle("-fx-text-fill: white;");
        //Pole tekstowe ograniczenie długości nazwy
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (textField.getText().length()>15){
                    textField.deleteNextChar();
                }
            }
        });

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.TOP_CENTER);

        //Zatwierdzenie nicku i sprawdzenie czy nie jest pustym ciągiem znaków
        CCLabel button = new CCLabel("Ok",ResourceAndUtility.fontOther,(e)->{
            String name=textField.getText();
            if (name.length()>0){
                ScoreboardScene.addNewScore(newScore,name);
                root.getChildren().removeAll(txt1,textField,buttons);
                restartOrMenu(root);
            }

        });

        buttons.getChildren().add(button);
        root.getChildren().addAll(txt1,textField,buttons);


        ScoreboardScene.scoreboard.updateScoreFile();

    }

    /**
     * Zmienia pokazywane elementy na takie ktore pozwalają spytać gracza czy chce grać ponownie czy wrócić do menu
     */
    void restartOrMenu(VBox root){
        CCLabel txt1 = new CCLabel("Chcesz zagrać ponownie czy wrócić do menu?",ResourceAndUtility.fontOther);
        txt1.addMargin(200,90,0,0);


        HBox buttons = new HBox();
        buttons.setAlignment(Pos.TOP_CENTER);


        CCLabel button1 = new CCLabel("Grać",ResourceAndUtility.fontOther,(f)->Main.setCurrentScene(new GameScene(new Pane())));
        button1.addMargin(0,0,50,0);
        CCLabel button2 = new CCLabel("Wrócić",ResourceAndUtility.fontOther,(f)->Main.setCurrentScene(new MainMenuScene(new VBox(20))));
        button2.addMargin(0,0,0,50);

        buttons.getChildren().addAll(button1,button2);

        root.getChildren().addAll(txt1,buttons);
    }


}
