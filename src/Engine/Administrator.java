package Engine;

import GUI.AdminMainMenu;

/**
 * Created by Or Keren on 06/05/2016.
 */
public class Administrator extends User {


    public Administrator(){
        super(userTypes.ADMINISTRATOR);
    }

    public void openMainMenu(){
        AdminMainMenu m = new AdminMainMenu();
    }
}
