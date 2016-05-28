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
    public static int count=0;
    public int m_id;
    public userTypes type;
    public User(userTypes type){
        this.type = type;
        this.m_id=count;
        count++;
    }
    public User(userTypes type, int id){
        this.type=type;
        this.m_id=id;
    }
    public userTypes getUserType(){
        return type;
    }
    public int getID(){
        return m_id;
    }

    public abstract void openMainMenu();
}
