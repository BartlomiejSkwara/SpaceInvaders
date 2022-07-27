package sample.action;

public class ActionWrapper {

    public int cooldown;
    Action action;




    public ActionWrapper(int cooldown, Action action){
        this.cooldown=cooldown;
        this.action=action;
    }

    public void callAction(){
        action.performAction();
    }

}
