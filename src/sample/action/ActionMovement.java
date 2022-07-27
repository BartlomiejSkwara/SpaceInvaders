package sample.action;

public class ActionMovement extends ActionWrapper{


    public void setDirectionRight(boolean directionRight) {
        this.directionRight = directionRight;
    }

    public boolean isDirectionRight() {
        return directionRight;
    }

    boolean directionRight=true;


    public ActionMovement(int cooldown,boolean directionRight, Action action) {
        super(cooldown, action);
        this.directionRight=directionRight;
    }

    @Override
    public void callAction() {
        super.callAction();
    }
}
