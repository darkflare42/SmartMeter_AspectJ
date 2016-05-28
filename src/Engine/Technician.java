package Engine;

import GUI.TechnicianMainMenu;

/**
 * Created by Or Keren on 06/05/2016.
 */
public class Technician extends User {

    public Technician(){
        super(userTypes.TECHNICIAN);
    }

    @Override
    public void openMainMenu(){
        TechnicianMainMenu m = new TechnicianMainMenu();
    }
}
