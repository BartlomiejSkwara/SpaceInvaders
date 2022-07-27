package sample.objects;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Klasa do szybkiego tworzenia przycisków i elementów tekstowych
 */
public class CCLabel extends Label {


    /**
     * Konstruktor dla elementu tekstowego
     * @param string zapisany tekst
     * @param font czcionka
     */
    public CCLabel (String string, Font font){
        super(string);
        this.setFont(font);
        this.setTextFill(Color.WHITE);
    }

    /**
     * Dodanie marginesu
     * @param marginTop
     * @param marginBottom
     * @param marginRight
     * @param marginLeft
     */
    public void addMargin(int marginTop, int marginBottom, int marginRight, int marginLeft ){
        this.setBorder(new Border(new BorderStroke(null,null,null,new BorderWidths(marginTop,marginRight,marginBottom,marginLeft))));
    }


    /**
     * Konstruktor dla elementu przycisku
     * @param string napis
     * @param font  czcionka
     * @param handler  co się stanie po wciśnięciu
     */
    public CCLabel (String string, Font font,EventHandler<MouseEvent> handler)
    {
        this(string,font);
        this.setOnMouseClicked(handler);

        //Gdy ustawi się kursor na przycisku to zrobi się żółty
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setTextFill(Color.YELLOW);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setTextFill(Color.WHITE);
            }
        });
    }



}
