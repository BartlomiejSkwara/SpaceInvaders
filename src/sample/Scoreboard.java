package sample;

import java.io.*;
import java.util.ArrayList;

public class Scoreboard implements Serializable {

    //Listy wynikow i osob ktore je mają
    public ArrayList<Integer> topScore;
    public ArrayList<String> scoreHolder;

    public Scoreboard(){

    }

    /**
     * Funkcja odczytuje z pliku wyniki
     */
    public void readFromScoreFile(){
        topScore=new ArrayList<>();
        scoreHolder=new ArrayList<>();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("score.bin"))){
            while ((topScore.add(objectInputStream.readInt()))) {
                scoreHolder.add(objectInputStream.readUTF());
            }

            objectInputStream.close();
        }
        catch (IOException exception){
            //System.out.println("Koniec odczytu");
        }
    }

    /**
     * Funkcja zapisuje do list nowy wynik i w razie potrzeby  sortuje
     */
    public void addNewScore (int score, String name){

        boolean replaceMode = false;
        //Sprawdza czy nowy wynik jest większy od jednego z obecnych
        for (int i = 0; i < topScore.size(); i++) {
            if (score>topScore.get(i)){
                replaceMode=true;
                topScore.add(i,score);
                scoreHolder.add(i,name);
                break;
            }
        }

        //Dodaje nowy wynik na koncu tabeli jezeli jest miejsce
        if (replaceMode==false){
            if (topScore.size()<10){
                topScore.add(score);
                scoreHolder.add(name);
            }

        }else if (topScore.size()>10){  //Usuwa ostatni wynik gdy nowy przebił jeden z obecnych
            topScore.remove(topScore.size()-1);
            scoreHolder.remove(scoreHolder.size()-1);
        }

        updateScoreFile();

    }

    /**
     * Funkcja zapisuje wyniki w pliku binarnym
     */
    public void updateScoreFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("score.bin"))) {
            for (int i  = 0; i<topScore.size(); i++){
                objectOutputStream.writeInt(topScore.get(i));
                objectOutputStream.writeUTF(scoreHolder.get(i));
            }
            objectOutputStream.close();
        }
        catch (IOException exception){
            //System.out.println("Nie moge zapisac danych");
        }
    }
}
