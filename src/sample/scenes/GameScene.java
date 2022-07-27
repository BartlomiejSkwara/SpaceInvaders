package sample.scenes;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.Main;
import sample.ResourceAndUtility;
import sample.objects.CCLabel;
import sample.objects.Explosion;
import sample.objects.Fleet;
import sample.objects.entities.Bullet;
import sample.objects.entities.Entity;
import sample.objects.entities.EntityEnemy;
import sample.objects.entities.Player;

import java.util.ArrayList;

public class GameScene extends CustomScene{

    public static final int playerZone = 100;
    public static final int topMargin = 50;

    Pane root;

    Fleet fleet;
    Player player;
    boolean isMovingLeft=false;
    boolean isMovingRight=false;
    boolean isShooting=false;

    ArrayList<Bullet> bulletArrayList;
    ArrayList<Bullet> bulletTrashBin;

    //Timer i nr obecnej klatki
    AnimationTimer mainGameTimer;
    int frame;

    //HP
    CCLabel hp;
    Entity portrait;
    Rectangle line;
    //Fala
    CCLabel fala;
    int wave;
    //Score
    CCLabel scoreLabel;
    int score;



    /*void whenPressedEscape(){
        System.out.println("Wyjscie do menu głównego");
    }*/

    /**
     * Funkcja zatrzymuje wszystkie timery
     */
    void stopAllAnimationTimers(){
        fleet.getAnimationTimer().stop();
        mainGameTimer.stop();

    }



    public GameScene(Pane root) {
        super(root);
        this.root=root;

        //Ustaw fale i stwórz gracza wraz z wrogami
        wave=0;
        fleet = new Fleet(wave);
        player  = new Player(ResourceAndUtility.sprite_sheet, ResourceAndUtility.SPACE_SHIP);
        bulletArrayList = new ArrayList<>();

        //Tło i dodawanie elementów do rysowania
        this.root.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        this.root.getChildren().add(fleet);
        this.root.getChildren().add(player);

        //Reszta
        this.setupAnimationTimer();
        this.setupEventHandling();
        this.setupPlayerStats();
    }


    /**
     * Funkcja zajmuje się zdarzeniami
     */
    private void setupEventHandling(){

        //Dodawanie przycisków do listy tych które obecnie działają
        this.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //Poruszanie
                switch (keyEvent.getCode()) {
                    case D:
                    case RIGHT:
                        isMovingRight = true;
                        break;

                    case A:
                    case LEFT:
                        isMovingLeft = true;
                        break;

                    case SPACE:
                        isShooting = true;
                        break;
                    /*case ESCAPE:
                        whenPressedEscape();
                        stopAllAnimationTimers();
                        Main.setCurrentScene(new MainMenuScene(new VBox(20)));
                        System.out.println("BURH");
                        break;
                    case F2:
                        nextWave();
                        break;
                    case F3:
                        stopAllAnimationTimers();
                        Main.setCurrentScene(new AfterGameScene(new VBox(),"Przegrana :<", score));
                        break;*/

                }
            }
        });


        //Usuwanie przycisków z listy działających
        this.setOnKeyReleased(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case D:
                    case RIGHT:
                        isMovingRight = false;
                        break;

                    case A:
                    case LEFT:
                        isMovingLeft = false;
                        break;

                    case SPACE:
                        isShooting = false;
                        break;
                }
            }
        });

    }

    /**
     * Funkcja dodaje elementy interfejsu odpowiedzialne za przedstwianie statystyk gracza
     */
    private void setupPlayerStats(){
        //HP
        line=new Rectangle(0,windowY-playerZone+10,windowX,4);
        line.setFill(Color.WHITE);

        hp=new CCLabel("3x",ResourceAndUtility.fontOther);
        hp.setTranslateX(10);
        hp.setTranslateY(windowY-playerZone+20);

        portrait = new Entity(ResourceAndUtility.sprite_sheet, ResourceAndUtility.SPACE_SHIP);
        portrait.setTranslateX(70);
        portrait.setTranslateY(windowY-playerZone+38);

        //Fala
        fala=new CCLabel("Fala: "+(wave+1),ResourceAndUtility.fontOther);
        fala.setTranslateX(200);
        fala.setTranslateY(windowY-playerZone+20);


        //Score
        score=0;
        scoreLabel=new CCLabel("Wynik: "+score,ResourceAndUtility.fontOther);
        scoreLabel.setTranslateX(windowX-250);
        scoreLabel.setTranslateY(windowY-playerZone+20);

        root.getChildren().addAll(line,hp,portrait,fala,scoreLabel);
    }

    /**
     * Funkcja przygotowuje główny timer
     */
    private void setupAnimationTimer(){
        frame=0;

        mainGameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                frame++;
                if (frame==1){
                    frame=0;
                    timerPlayerStuff();
                    timerEnemyStuff();
                    timerBulletStuff();
                    //Warunek przegranej (wróg doleciał)
                    if (fleet.haveReachedThePlayer()){
                        defeat();
                    }
                }



            }
        };

        mainGameTimer.start();
    }

    /**
     * Funkcja odpowiada za poruszanie gracza - wykorzystywana w głównym timerze
     */
    private void timerPlayerStuff(){
        if (isMovingLeft){
            player.moveLeft();
        }
        if (isMovingRight){
            player.moveRight();
        }
        if (isShooting){
            if (player.getCanShoot()){
                player.shoot();
                bulletArrayList.add(new Bullet(ResourceAndUtility.sprite_sheet, ResourceAndUtility.BULLET,player.getTranslateX(),player.getTranslateY(),true));
                root.getChildren().add(bulletArrayList.get(bulletArrayList.size()-1));
            }
        }
    }

    /**
     *  Funkcja odpowiada za strzelanie wrogów - wykorzystywana w głównym timerze
     */
    private void timerEnemyStuff(){
        //Strzelanie dla przeciwnikow
        if (fleet.getCanShoot()){

            //Szuka wroga który może strzelać
            EntityEnemy entityEnemy = fleet.findAlienAbleToShoot();

            //Wygrana
            if (entityEnemy==null){
                fleet.setCanShoot(false);
                fleet.getBulletCooldown().stop();
                nextWave();
            }
            else {  //Strzał
                bulletArrayList.add(new Bullet(ResourceAndUtility.sprite_sheet, ResourceAndUtility.BULLET, fleet.getTranslateX()+entityEnemy.getTranslateX(),fleet.getTranslateY()+entityEnemy.getTranslateY(),false,entityEnemy.getViewport()));
                root.getChildren().add(bulletArrayList.get(bulletArrayList.size()-1));
            }

        }
    }

    /**
     * Funkcja przygotowuje kolejną falę wrogów
     */
    private void nextWave(){
        frame= 0;
        wave++;
        fala.setText("Fala: "+(wave+1));
        root.getChildren().remove(fleet);
        fleet = new Fleet(wave);
        root.getChildren().add(fleet);
    }


    /**
     *  Funkcja odpowiada za ruch i kolizję pocisków - wykorzystywana w głównym timerze
     */
    private void timerBulletStuff(){
        bulletTrashBin=new ArrayList<>();
        //Wszystko co tyczy ruchu pociskow
        bulletArrayList.forEach(bullet -> {

            bullet.moveVertical(bullet.getSpeed());

            //Gdy pocisk jest za wysoko lub za nisko wywal go z widoku i dodaj do kosza
            if ((bullet.getTranslateY()<topMargin-(bullet.getViewport().getHeight()+10)||
                    bullet.getTranslateY()>CustomScene.windowY-((bullet.getViewport().getHeight()+10+playerZone))) ){
                addExplosion(bullet.getTranslateX(),bullet.getTranslateY(),bullet.getViewport().getWidth(),bullet.getViewport().getHeight());
                removeBullet(bullet);
            }
            //Pocisk kolizja wstępna
            if(bullet.isOwnedByPlayer() == true && bullet.getTranslateY()>fleet.getTranslateY()&& bullet.getTranslateY()<fleet.getTranslateY()+(fleet.getGrid_Y_Size()*(fleet.getFleetHeight()-fleet.getLostColumnsBottom()))) {
                //Pocisk kolizja precyzyjna
                if (fleet.didBulletHit((int) bullet.getTranslateX(),(int) bullet.getTranslateY(),(int) bullet.getViewport().getWidth(),(int) bullet.getViewport().getHeight())){
                    addExplosion(bullet.getTranslateX(),bullet.getTranslateY(),bullet.getViewport().getWidth(),bullet.getViewport().getHeight());
                    removeBullet(bullet);
                    score+=50;
                    scoreLabel.setText("Wynik: "+score);
                }
            }
            //Pocisk kolizja z graczem
             else if (bullet.isOwnedByPlayer()==false){
                if (ResourceAndUtility.intersection(player.getViewport(),(int) player.getTranslateX(),(int) player.getTranslateY(),(int) bullet.getTranslateX(),(int) bullet.getTranslateY(),(int) bullet.getViewport().getWidth(),(int) bullet.getViewport().getHeight())){

                    if (player.loseLife()){
                       defeat();
                    }
                    hp.setText(player.getLives()+"x");
                    addExplosion(bullet.getTranslateX(),bullet.getTranslateY(),bullet.getViewport().getWidth(),bullet.getViewport().getHeight());
                    removeBullet(bullet);
                }
            }

        });

        //Usuwanie z array za pomocą kosza
        if (bulletTrashBin.size()>0){
            for(Bullet bullet:bulletTrashBin){
                bulletArrayList.remove(bullet);
            }
            bulletTrashBin.clear();
        }
    }

    /**
     * Funkcja w wypadku przegranej kracza przekierowuje go do sceny z wynikiem i możliwością jego zapisu
     */
    private void defeat(){

        stopAllAnimationTimers();
        Explosion explosion = new Explosion(ResourceAndUtility.sprite_sheet, ResourceAndUtility.BULLET_EXPLOSION,(int) player.getTranslateX(), (int) player.getTranslateY(),(int) ResourceAndUtility.SPACE_SHIP.getWidth(),(int) ResourceAndUtility.SPACE_SHIP.getHeight());
        this.root.getChildren().remove(player);
        this.root.getChildren().add(explosion);
        explosion.setExplosionResult(()->{
            root.getChildren().remove(explosion);
            Main.setCurrentScene(new AfterGameScene(new VBox(),"Koniec Gry",score));
        });


    }

    /**
     * Funkcja przygotowuje pocisk do usunięcia
     * @param bullet - pocisk który ma być usunięty
     */
    private void removeBullet(Bullet bullet){
        root.getChildren().remove(bullet);
        bulletTrashBin.add(bullet);
    }

    /**
     * Funkcja dodaje eksplozję
     * @param xPos pozycja eksplodującego obiektu na osi x
     * @param yPos pozycja eksplodującego obiektu na osi y
     * @param width szerokosc obiektu ktory wybucha
     * @param height wysokosc obiektu ktory wybucha
     */
    private void addExplosion (double xPos,double yPos,double width,double height){
        Explosion explosion = new Explosion(ResourceAndUtility.sprite_sheet, ResourceAndUtility.BULLET_EXPLOSION,(int) xPos, (int) yPos,(int) width,(int) height);

        this.root.getChildren().add(explosion);
        explosion.setExplosionResult(()->{
            root.getChildren().remove(explosion);
        });
    }


}
