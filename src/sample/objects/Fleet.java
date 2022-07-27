package sample.objects;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import sample.ResourceAndUtility;
import sample.action.Action;
import sample.action.ActionMovement;
import sample.action.ActionWrapper;
import sample.objects.entities.EntityEnemy;
import sample.scenes.CustomScene;
import sample.scenes.GameScene;

import java.util.ArrayList;
import java.util.Random;


public class Fleet extends javafx.scene.layout.Pane{

    boolean reachedThePlayer;
    int wave;


    public boolean haveReachedThePlayer() {
        return reachedThePlayer;
    }

    /**Konstruktor podstawowy ustawia statki i filtr na zdarzenia**/
    public Fleet(int wave) {
        enemiesRows =new ArrayList<>();
        this.wave=wave;
        setupFleet();
        setupMovement();
        //setupEvents();
        setupShooting();
    }


    /**
     *  Funkcja odpowiada za tworzenie kosmitów
     */
    private void setupFleet(){

        ///Tworzy wszystkie statki i  je wyśrodkowuje
        this.addRow(11,0,0);
        this.addRow(11,1,0);
        this.addRow(11,1,0);
        this.addRow(11,2,0);
        this.addRow(11,2,0);

        fleetLength=fleetLengthPixels/grid_X_Size;
        fleetHeight=fleetHeightPixels/grid_Y_Size;
        //Wysrodkowuje flote
        this.setTranslateY(GameScene.topMargin);
        this.setTranslateX((CustomScene.windowX- fleetLengthPixels)/2);
    }

    /**
     * Funkcja odpowiada za ruch kosmitów
     */
    private void setupMovement(){
        reachedThePlayer=false;

        //Animacja kosmitów
        animateFleet = new ActionWrapper(60, new Action() {
            @Override
            public void performAction() {
                enemiesRows.forEach(entityEnemyRow -> {
                    for (EntityEnemy ship:entityEnemyRow){
                        if (ship!=null)
                            ship.changeSprite();
                    }
                });
            }
        });



        //Inicjalizuje na zero bo na poczatku nie ma straconych kolumn
        lostColumnsLeft=0;
        lostColumnsRight=0;
        lostColumnsBottom=0;

        //Poruszanie floty
        moveFleet = new ActionMovement(60,true, new Action() {
            @Override
            public void performAction() {
                //Przesuń w prawo
                if (moveFleet.isDirectionRight()){
                    if (getTranslateX()+fleetLengthPixels-(lostColumnsRight*grid_X_Size)<CustomScene.windowX){
                        moveHorizontal(10);
                    }
                    else{
                        moveFleet.setDirectionRight(false);
                        moveVertical(grid_Y_Size-10);
                        if (getTranslateY()+fleetHeightPixels-(lostColumnsBottom*grid_Y_Size)>CustomScene.windowY- GameScene.playerZone-50){
                            reachedThePlayer=true;
                            animationTimer.stop();
                        }
                    }
                }
                //Przesuń w lewo
                else {
                    if (getTranslateX()+(lostColumnsLeft*grid_X_Size)>0){
                        moveHorizontal(-10);
                    }
                    else{
                        moveFleet.setDirectionRight(true);
                        moveVertical(grid_Y_Size-10);
                        if (getTranslateY()+fleetHeightPixels-(lostColumnsBottom*grid_Y_Size)>CustomScene.windowY- GameScene.playerZone-50){
                            reachedThePlayer=true;
                            animationTimer.stop();
                        }
                    }
                }

            }
        });


        //Animacja
        animationTimer=new AnimationTimer() {
            @Override
            public void handle(long l) {
                frame++;

                if (frame>=(int) ((animateFleet.cooldown*animationSpeedMultiplier)-wave)){
                    frame=0;
                    moveFleet.callAction();
                    animateFleet.callAction();

                }

            }
        };

        animationTimer.start();

    }

    /**
     * Funkcja używana do testów - nie powinna się pojawić w grze
     */
    /*private void setupEvents(){
        //Usuwaj statki po kliknieciu
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if(mouseEvent.getTarget()instanceof EntityEnemy){
                    removeShip((EntityEnemy) (mouseEvent.getTarget()));
                }
                mouseEvent.consume();
            }
        });
    }*/


    /**
     * Przygotowuje timer do  strzelania i ustala odstęp między strzałami
     */
    private void setupShooting(){
        setCanShoot(true);
        bulletCooldown = new AnimationTimer() {
            @Override
            public void handle(long l) {
                i++;
                if (i>60){
                    setCanShoot(true);
                    i=0;
                    bulletCooldown.stop();
                }
            }
        };
    }



    /**
     * Funkcja korzysta z funkcji intersection w celu sprawdzenia kolizji dla każdego ze staków a następnie w wypadku jej obecności usuwa trafiony statek
     * @param x - pozycja pocisku na osi x
     * @param y - pozycja pocisku na osi y
     * @param width - szerokość pocisku
     * @param height - wysokość pocisku
     * @return  - w wypadku gdy pocisk trafił zwraca true , w przeciwnym wypadu zwraca false
     */
    public boolean didBulletHit(int x, int y, int width, int height){
        for (ArrayList<EntityEnemy> row : enemiesRows) {
            for (EntityEnemy enemy : row){
                if (enemy!= null && ResourceAndUtility.intersection(enemy.getViewport(),(int) (enemy.getTranslateX()+this.getTranslateX()),(int)(enemy.getTranslateY()+this.getTranslateY()),x,y,width,height)==true){

                    removeShip(enemy);

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Funkcja usuwa dany statek
     * @param entityEnemy - statek wybrany do usunięcia
     */
    public void removeShip(EntityEnemy entityEnemy){
        this.getChildren().remove(entityEnemy);

        //Zapisz gdzie znaleziono usuwany statek by ulatwic usuwanie kolumn i rzedow
        int enemyArrayX;
        int enemyArrayY;

        //Szuka statka w arrayliście zmienia go na null i zapisuje pozycje w liście
        for (int i = 0; i<enemiesRows.size(); i++){
            enemyArrayX = enemiesRows.get(i).lastIndexOf(entityEnemy);
            if (enemyArrayX!=-1){
                enemyArrayY=i;
                enemiesRows.get(i).set(enemyArrayX,null);
                CheckEnemyRows(enemyArrayX,enemyArrayY);
                break;
            }
        }

        //Przyspiesza statki i robi eksplozje
        this.animationSpeedMultiplier*=0.949;
        this.addExplosion(entityEnemy.getTranslateX(),entityEnemy.getTranslateY(),entityEnemy.getViewport().getWidth(),entityEnemy.getViewport().getHeight());

    }

    /**
     * Funkcja dodaje eksplozję w określonym miejscu
     * @param xPos pozycja eksplodującego obiektu na osi x
     * @param yPos  pozycja eksplodującego obiektu na osi y
     * @param width szerokość obiektu który eksploduje
     * @param height wysokość obiektu który eksploduje
     */
    private void addExplosion(double xPos,double yPos,double width,double height){
        animationTimer.stop();
        Explosion explosion = new Explosion(ResourceAndUtility.sprite_sheet, ResourceAndUtility.SHIP_EXPLOSION,(int) xPos, (int) yPos,(int) width,(int) height);

        this.getChildren().add(explosion);
        explosion.setExplosionResult(()->{
            getChildren().remove(explosion);
            animationTimer.start();
        });
    }


    //Wielkosc pola dla pojedynczego statku
    int grid_X_Size =60;
    int grid_Y_Size =50;

    public int getGrid_X_Size() {
        return grid_X_Size;
    }

    public int getGrid_Y_Size() {
        return grid_Y_Size;
    }

    int fleetLengthPixels;
    int fleetLength;
    int fleetHeightPixels;
    int fleetHeight;
    ArrayList<ArrayList<EntityEnemy>> enemiesRows;

    public int getFleetHeightPixels() {
        return fleetHeightPixels;
    }
    public int getFleetHeight() {
        return fleetHeight;
    }

    /**Animacja*/
    AnimationTimer animationTimer;
    ActionWrapper animateFleet;
    double animationSpeedMultiplier = 1;
    int frame=0;

    /**Animacja ruch*/
    int lostColumnsLeft;
    int lostColumnsRight;
    int lostColumnsBottom;
    ActionMovement moveFleet;

    public int getLostColumnsBottom() {
        return lostColumnsBottom;
    }

    /**Strzelanie*/
    AnimationTimer bulletCooldown;
    int i = 0;
    boolean canShoot;

    public boolean getCanShoot() {
        return canShoot;
    }
    public AnimationTimer getBulletCooldown() {
        return bulletCooldown;
    }
    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    /**
     * Funkcja poszukuje kosmity zdolnego do strzelania
     * @return zwraca kosmitę który został wybrany do strzelania w wypadku gdy funkcja zwróci wartość null oznacza to że nie ma już żadnego kosmity
     */
    public EntityEnemy findAlienAbleToShoot(){
        this.setCanShoot(false);
        bulletCooldown.start();
        //Losowanie statku biorąc pod uwagę stracone kolumny z lewej i prawej strony
        Random random = new Random();
        int range =fleetLength-lostColumnsRight-lostColumnsLeft;
        if (range<=0){
            return null;
        }
        int column = random.nextInt(range)+lostColumnsLeft;


        //Sprawdzam w którym rzędzie jest kosmita
        int row = 4;
        for (int i = fleetHeight-lostColumnsBottom-1; i>=0; i--){
            if (enemiesRows.get(i).get(column)!=null){
                row=i;
                break;
            }
        }

        //Unikanie kolumn w środku które są puste
        if (enemiesRows.get(row).get(column)==null){
            for (int i = column+1; i<fleetLength-lostColumnsRight; i++){
                for (int j = fleetHeight-lostColumnsBottom-1; j>=0; j--){
                    if (enemiesRows.get(j).get(i)!=null){
                        return enemiesRows.get(j).get(i) ;
                    }
                }
            }
            for (int i = column-1; i>0+lostColumnsLeft; i--){
                for (int j = fleetHeight-lostColumnsBottom-1; j>=0; j--){
                    if (enemiesRows.get(j).get(i)!=null){
                        return enemiesRows.get(j).get(i) ;
                    }
                }
            }
        }

        return enemiesRows.get(row).get(column) ;

    }


    /**
     * Funkcja sprawdza czy odpadła kolumna lub rząd (używana po usunięcu kosmity)
     * @param x - kolumna ostatnio usuniętego kosmity
     * @param y - rząd ostatnio usuniętego kosmity
     */
    private void CheckEnemyRows(int x, int y){
        ///Sprawdzam rzędy od dołu
        if (y==fleetHeight-1-lostColumnsBottom){
            outerloopH:for (int i = enemiesRows.size()-1-lostColumnsBottom; i>-1; i--){
                for (int j = lostColumnsLeft; j<fleetLength-lostColumnsRight; j++){
                    if (enemiesRows.get(i).get(j)!=null){
                        break outerloopH;
                    }
                }
                lostColumnsBottom++;
            }
        }

        ///Sprawdzam kolumny z lewej strony
        if (x==0+lostColumnsLeft){
            outerloopL:for (int i = 0+lostColumnsLeft; i<fleetLength-lostColumnsRight; i++){
                for (int j = fleetHeight-1-lostColumnsBottom; j>-1; j--){
                    if (enemiesRows.get(j).get(i)!=null){
                        break outerloopL;
                    }
                }
                lostColumnsLeft++;
            }
        }

        ///Sprawdzam kolumny z prawej strony
        else if (x==fleetLength-1-lostColumnsRight){
            outerloopP:for (int i = fleetLength-1-lostColumnsRight; i>-1+lostColumnsLeft; i--){
                for (int j = fleetHeight-1-lostColumnsBottom; j>-1; j--){
                    if (enemiesRows.get(j).get(i)!=null){
                        break outerloopP;
                    }
                }
                lostColumnsRight++;
            }
        }


        ///Zatrzymaj timer gdy skończą się statki
        if (lostColumnsBottom==5){
            animationTimer.stop();
        }

    }


    /**
     * Funkcja dodaje nowy rząd kosmitów
     * @param amountOfShips - ilość kosmitów
     * @param alienVariant - wygląd kosmitów (0,1 lub 2)
     * @param animationState - klatka animacji z którą zaczynają kosmici w nowym rzędzie
     */
    private void addRow(int amountOfShips, int alienVariant, int animationState){
        ArrayList<EntityEnemy>tempArray=new ArrayList<>();
        for (int j = 0; j < amountOfShips; j++) {
            int elementPosition = tempArray.size();
            tempArray.add(new EntityEnemy(ResourceAndUtility.sprite_sheet, alienVariant, 0));

            Rectangle2D temp = tempArray.get(elementPosition).getViewport();
            tempArray.get(elementPosition).setTranslateX((j*grid_X_Size)+
                    (int)(grid_X_Size-temp.getWidth())/2);

            tempArray.get(elementPosition).setTranslateY(fleetHeightPixels +
                    (int) (grid_Y_Size-temp.getHeight())/2);
            this.getChildren().add(tempArray.get(elementPosition));
        }
        fleetLengthPixels =(amountOfShips*grid_X_Size> fleetLengthPixels ?amountOfShips*grid_X_Size: fleetLengthPixels);
        fleetHeightPixels +=grid_Y_Size;
        enemiesRows.add(tempArray);
    }



    private void moveHorizontal(int distance){
        this.setTranslateX(this.getTranslateX()+distance);
    }
    private void moveVertical(int distance){
        this.setTranslateY(this.getTranslateY()+distance);
    }

    public AnimationTimer getAnimationTimer() {
        return animationTimer;
    }
}
