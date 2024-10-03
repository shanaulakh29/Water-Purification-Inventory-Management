import Modal.Unit;
import Modal.UnitsInventory;
import View.Menu;

import java.time.LocalDate;


/**
 * The Main class serves as the entry point of the program, initializing the UnitInventory class located
 * within the Model package that manages and stores all unit data. Once initialized, the inventory is passed
 * to the Menu class, which resides in the View package. Main class then initiates the program execution
 */
public class Main {
    public static void main(String[] args) {

        UnitsInventory unitInv = new UnitsInventory();
        Menu menu = new Menu(unitInv);
        menu.start();
    }
}