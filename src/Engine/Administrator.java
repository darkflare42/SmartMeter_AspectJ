package Engine;

import GUI.AdminMainMenu;

/**
 * Created by Or Keren on 06/05/2016.
 */
public class Administrator extends User {

    @Override
    public void openMainMenu(){
        AdminMainMenu m = new AdminMainMenu();
    }
}
