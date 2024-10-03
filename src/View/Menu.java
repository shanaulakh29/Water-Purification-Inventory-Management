package View;

import Modal.Unit;
import Modal.UnitsInventory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu class interacts and guide the user about how to interact with the Water Purification Inventory Management
 * system. This class is basically responsible for displaying menu to the user and get inputs based on the
 * instructions provided. This class also handles the edge cases where user provides unexpected values to the program
 * and inturn provides the opportunity to the user to input again. Menu class allows user to display reports,
 * add a new unit, test a unit, ship a unit and sort reports based on the menu selected.
 */
public class Menu {
    ReportView reportView = new ReportView();
    List<String> menuArray = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    UnitsInventory unitsInventory;
    private static final int MENU_ITEM_2 = 2;
    private static final int MENU_ITEM_3 = 3;
    private static final int MENU_ITEM_4 = 4;
    private static final int MENU_ITEM_5 = 5;
    private static boolean DEFAULT_SORT_ORDER_BY_SERIAL = true;
    private static boolean SORT_BY_MODEL_AND_SERIAL = false;
    private static boolean SORT_BY_MOST_RECENT_TESTDATE = false;

    public Menu(UnitsInventory unitsInventory) {
        this.unitsInventory = unitsInventory;
    }

    public void printProgramTitle() {
        System.out.println("***************************************\n" +
                "Water Purification Inventory Management \n" +
                "by Gurshan Singh Aulakh\n" +
                "Student ID : 301608359\n" +
                "***************************************");
    }

    public void printMenuTitle() {

        System.out.println("\n*************\n" +
                "* Main Menu *\n" +
                "*************");
    }

    public void addMenuItems() {
        menuArray.add("Read JSON input file");
        menuArray.add("Display info on a unit");
        menuArray.add("Create new unit");
        menuArray.add("Test a unit");
        menuArray.add("Ship a unit");
        menuArray.add("Print report");
        menuArray.add("Set report sort order");
        menuArray.add("Exit");
    }

    public void displayUserPrompt() {
        System.out.print("> ");
    }

    public void displayMenu() {
        printMenuTitle();
        for (int i = 0; i < menuArray.size(); i++) {
            System.out.println(i + 1 + "." + menuArray.get(i) + ".");
        }
        displayUserPrompt();
    }

    public String getSerialNumberInput() {
        System.out.print("Enter the serial number (0 for list, -1 for cancel): ");
        return scanner.nextLine();
    }

    public void checkAndEnableDefaultSortOrder() {
        if (DEFAULT_SORT_ORDER_BY_SERIAL) {
            unitsInventory.sortBySerialNumber();
        }
    }

    public void checkAndEnableSortByModelAndSerial() {
        if (SORT_BY_MODEL_AND_SERIAL) {
            unitsInventory.sortByModelAndSerialNumber();
        }
    }

    public void checkAndEnableSortByMostRecentTestDate() {
        if (SORT_BY_MOST_RECENT_TESTDATE) {
            unitsInventory.sortByMostRecentTestDate();
        }
    }

    public void checkAndEnableTheSelectedSortOrder() {
        checkAndEnableDefaultSortOrder();
        checkAndEnableSortByModelAndSerial();
        checkAndEnableSortByMostRecentTestDate();
    }

    public void printCheckSumErrorMessage() {
        System.out.println("Unable to add the product.");
        System.out.println("     'Serial Number Error: Checksum does not match.'");
        System.out.print("Please try again.\nSerial number: ");
    }

    public void guideUserAboutInstructionsOfSerialNumber() {
        System.out.println("Invalid Serial Number. " +
                "Please enter a valid serial Number that follows these guidelines.");
        System.out.println("1) Serial Number must be greater than 3 and less than or equal to 15 digits.");
        System.out.println("2) The last two digits of serial Number must be the checkSum computed by " +
                "adding all the previous digits and mod by 100.");
        System.out.println("3) Serial Number must be unique.");
        System.out.print("Serial Number: ");
    }

    public void printMessageIfUserEntersInvalidLengthSerialNumber() {
        System.out.println("Invalid Serial Number. " +
                "Please enter a valid serial Number that consist of atleast 3 and " +
                "less than or equal to 15 digits.");
        System.out.print("Serial Number: ");
    }

    public void printMessageIfUserEnterSerialNumberAlreadyExists() {
        System.out.println("Invalid Serial Number. " +
                "Serial Number you provided already exists in the system." +
                " Please provide a different Serial Number.");
        System.out.print("Serial Number: ");
    }

    public String validateSerialNumber(String serialNumberUserInput) {
        while (true) {
            try {
                if (serialNumberUserInput.isEmpty()) {
                    return serialNumberUserInput;
                }
                if (Unit.serialNumberIsValid(serialNumberUserInput)) {
                    return serialNumberUserInput;
                }
            } catch (NumberFormatException e) {
                guideUserAboutInstructionsOfSerialNumber();
                serialNumberUserInput = scanner.nextLine();
            } catch (IllegalArgumentException e) {
                if (e.getMessage().equals("Invalid Serial Number Length")) {
                    printMessageIfUserEntersInvalidLengthSerialNumber();
                    serialNumberUserInput = scanner.nextLine();
                } else if (e.getMessage().equals("Serial Number already exists")) {
                    printMessageIfUserEnterSerialNumberAlreadyExists();
                    serialNumberUserInput = scanner.nextLine();
                } else {
                    printCheckSumErrorMessage();
                    serialNumberUserInput = scanner.nextLine();
                }
            }
        }
    }


    public String getValidSerialNumberInputFromUser(String serialNumberUserInput, int currentMenuItemNumber) {
        while (true) {
            if (serialNumberUserInput.equals("-1") &&
                    ((currentMenuItemNumber == MENU_ITEM_2) ||
                            (currentMenuItemNumber == MENU_ITEM_4) || (currentMenuItemNumber == MENU_ITEM_5))) {
                break;
            } else if (serialNumberUserInput.isEmpty() && (currentMenuItemNumber == MENU_ITEM_3)) {
                break;
            } else if (serialNumberUserInput.equals("0") &&
                    ((currentMenuItemNumber == MENU_ITEM_2) ||
                            (currentMenuItemNumber == MENU_ITEM_4) || currentMenuItemNumber == MENU_ITEM_5)) {
                checkAndEnableDefaultSortOrder();
                reportView.printAllUnitReport(unitsInventory.getinventoryArrayReference());
                serialNumberUserInput = getSerialNumberInput();
            } else if ((currentMenuItemNumber == MENU_ITEM_3)) {
                return validateSerialNumber(serialNumberUserInput);
            } else if (((currentMenuItemNumber == MENU_ITEM_4) || (currentMenuItemNumber == MENU_ITEM_2) ||
                    (currentMenuItemNumber == MENU_ITEM_5)) &&
                    (UnitsInventory.getSpecificUnit(serialNumberUserInput) == null)) {
                System.out.println("No unit found matching serial '" + serialNumberUserInput + "'");
                System.out.print("Enter the serial number (0 for list, -1 for cancel): ");
                serialNumberUserInput = scanner.nextLine();
            } else {
                break;
            }
        }
        return serialNumberUserInput;
    }

    public void testSingleUnit(String serialNumberUserInput, int currentMenuItemNumber) {
        String validSerialNumber = getValidSerialNumberInputFromUser(serialNumberUserInput, currentMenuItemNumber);
        if (validSerialNumber.equals("-1")) {
            return;
        }
        Unit unit = UnitsInventory.getSpecificUnit(validSerialNumber);
        System.out.print("Pass? (Y/n): ");
        String testResult = scanner.nextLine();
        testResult = testResult.toLowerCase();
        while (true) {
            if (testResult.equals("y") || testResult.isEmpty() || testResult.equals("n")) {
                break;
            } else {
                System.out.print("Error: Please enter [Y]es or [N]o: ");
                testResult = scanner.nextLine();
                testResult = testResult.toLowerCase();
            }
        }
        System.out.print("Comment: ");
        String testComment = scanner.nextLine();

        if (testResult.equals("y") || testResult.isEmpty()) {
            unit.addTest(true, testComment);
        } else {
            unit.addTest(false, testComment);
        }
        System.out.println("Test recorded.");

    }

    public void printMessageIfInventoryArrayIsEmpty() {
        System.out.println("No units defined.");
        System.out.println("Please create a unit and then re-try this option.");
    }

    public void validateFileAddress(String userInputFileName) {
        while (true) {
            try {
                FileReader reader = new FileReader(userInputFileName);
                unitsInventory.fetchFromJson(reader, userInputFileName);
                System.out.println("Read " + unitsInventory.getArraySize() + " products from JSON file '" +
                        userInputFileName + "'");
                return;
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please enter a valid file address.");
                displayUserPrompt();
                userInputFileName = scanner.nextLine();
            }

        }
    }

    public void modifySortFieldsAsPerUserInput(int userInputSortOrder) {
        if (userInputSortOrder == 1) {
            DEFAULT_SORT_ORDER_BY_SERIAL = true;
            SORT_BY_MODEL_AND_SERIAL = false;
            SORT_BY_MOST_RECENT_TESTDATE = false;
        } else if (userInputSortOrder == 2) {
            DEFAULT_SORT_ORDER_BY_SERIAL = false;
            SORT_BY_MODEL_AND_SERIAL = true;
            SORT_BY_MOST_RECENT_TESTDATE = false;
        } else {
            DEFAULT_SORT_ORDER_BY_SERIAL = false;
            SORT_BY_MODEL_AND_SERIAL = false;
            SORT_BY_MOST_RECENT_TESTDATE = true;
        }
    }

    public void readFromJSONFile() {
        scanner.nextLine();
        if (unitsInventory.getArraySize() != 0) {
            unitsInventory.emptyInventoryArray();
        }
        System.out.println("Enter the path to the input JSON file; blank to cancel\n" +
                "WARNING: This will replace all current data with data from the file.");

        displayUserPrompt();
        String userInputFileName = scanner.nextLine();
        validateFileAddress(userInputFileName);
    }

    public void displayInfoOnAUnit() {
        scanner.nextLine();
        int currentMenuItemNumber = 2;
        if (UnitsInventory.getInventoryArraySize() == 0) {
            printMessageIfInventoryArrayIsEmpty();
            return;
        }
        String serialNumberUserInput = getSerialNumberInput();
        String validSerialNumber = getValidSerialNumberInputFromUser(serialNumberUserInput, currentMenuItemNumber);
        if (validSerialNumber.equals("-1")) {
            return;
        }
        Unit unit = UnitsInventory.getSpecificUnit(validSerialNumber);
        reportView.printUnitSummary(unit);
    }

    public void createNewUnit() {
        scanner.nextLine();
        int currentMenuItemNumber = 3;
        System.out.println("Enter product info; blank line to quit.");
        System.out.print("Model: ");
        String modelUserInput = scanner.nextLine();
        if (modelUserInput.isEmpty()) {
            return;
        }
        System.out.print("Serial number: ");
        String serialNumberUserInput = scanner.nextLine();
        String validSerialNumber = getValidSerialNumberInputFromUser(serialNumberUserInput, currentMenuItemNumber);
        if (validSerialNumber.isEmpty()) {
            return;
        }
        unitsInventory.addNewUnit(modelUserInput, validSerialNumber);
        checkAndEnableTheSelectedSortOrder();
    }

    public void testSingleUnit() {
        scanner.nextLine();
        int currentMenuItemNumber = 4;
        String serialNumberUserInput = getSerialNumberInput();
        testSingleUnit(serialNumberUserInput, currentMenuItemNumber);
        checkAndEnableTheSelectedSortOrder();
    }

    public void shipSingleUnit() {
        scanner.nextLine();
        int currentMenuItemNumber = 5;
        String serialNumberUserInput = getSerialNumberInput();
        serialNumberUserInput = getValidSerialNumberInputFromUser(serialNumberUserInput, currentMenuItemNumber);
        if (UnitsInventory.getSpecificUnit(serialNumberUserInput) == null) {
            return;
        }
        Unit unit = UnitsInventory.getSpecificUnit(serialNumberUserInput);
        unit.setShipDate();
        checkAndEnableTheSelectedSortOrder();
        System.out.println("Unit successfully shipped.");
    }

    public void printReport() {
        scanner.nextLine();
        reportView.printReportOptionsMenu();
        displayUserPrompt();
        int getUserInputForReport = scanner.nextInt();
        scanner.nextLine();
        while (true) {
            if (getUserInputForReport == 1) {
                checkAndEnableTheSelectedSortOrder();
                reportView.printAllUnitReport(unitsInventory.getinventoryArrayReference());
                break;
            } else if (getUserInputForReport == 2) {
                checkAndEnableTheSelectedSortOrder();
                reportView.printDefectiveUnitsReportHeading();
                reportView.printRecentDefectiveUnits(unitsInventory.getinventoryArrayReference());
                break;
            } else if (getUserInputForReport == 3) {
                checkAndEnableTheSelectedSortOrder();
                reportView.printSuccessAndReadyToShipReportHeading();
                reportView.printSuccessAndReadyToShipUnits(unitsInventory.getinventoryArrayReference());
                break;
            } else if (getUserInputForReport == 4) {
                break;
            } else {
                System.out.println("Error: Please enter a selection between 1 and 4");
                displayUserPrompt();
                getUserInputForReport = scanner.nextInt();
                scanner.nextLine();
            }
        }
    }

    public void setReportSortOrder() {
        scanner.nextLine();
        reportView.printReportSortOrderMenu();
        displayUserPrompt();
        String userInputSortOrderinString = scanner.nextLine();
        int userInputSortOrder = Integer.parseInt(userInputSortOrderinString);
        while (true) {
            if (userInputSortOrder == 1) {
                modifySortFieldsAsPerUserInput(userInputSortOrder);
                unitsInventory.sortBySerialNumber();
                return;
            } else if (userInputSortOrder == 2) {
                modifySortFieldsAsPerUserInput(userInputSortOrder);
                unitsInventory.sortByModelAndSerialNumber();
                return;
            } else if (userInputSortOrder == 3) {
                modifySortFieldsAsPerUserInput(userInputSortOrder);
                unitsInventory.sortByMostRecentTestDate();
                return;
            } else if (userInputSortOrder == 4) {
                return;
            } else {
                System.out.println("Error: Please enter a selection between 1 and 4");
                displayUserPrompt();
                userInputSortOrderinString = scanner.nextLine();
                userInputSortOrder = Integer.parseInt(userInputSortOrderinString);
            }
        }
    }

    public void start() {
        printProgramTitle();
        addMenuItems();
        displayMenu();
        while (true) {
            int copyOfUserInput = 0;
            try {
                int userInput = scanner.nextInt();
                copyOfUserInput = userInput;
                switch (userInput) {
                    case 1 -> readFromJSONFile();
                    case 2 -> displayInfoOnAUnit();
                    case 3 -> createNewUnit();
                    case 4 -> testSingleUnit();
                    case 5 -> shipSingleUnit();
                    case 6 -> printReport();
                    case 7 -> setReportSortOrder();
                    case 8 -> {
                        return;
                    }
                    default -> System.out.print("Error: Please enter a selection between 1 and 8: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Error: Please enter a selection between 1 and 8: ");
                scanner.nextLine();
            }
            if (copyOfUserInput >= 1 && copyOfUserInput <= 8) {
                displayMenu();
            }
        }
    }
}
//users/shan/desktop/cmpt213ass2/src/sample.json
