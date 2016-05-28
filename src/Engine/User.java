package Engine;

/**
 * Created by Or Keren on 06/05/2016.
 */
public abstract class User {

    public enum userTypes {
        CUSTOMER,
        TECHNICIAN,
        ADMINISTRATOR
    }
    public userTypes type;
    public User(userTypes type){
        this.type = type;
    }
    public userTypes getUserType(){
        return type;
    }

    public abstract void openMainMenu();
}
