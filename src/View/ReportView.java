package View;

import Modal.Test;
import Modal.Unit;
import Modal.UnitsInventory;


import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ReportView class actually displays reports based on the menu option selected by the user. This class has
 * the functionality to display all units, single unit summary , different menus based on user interaction,
 * units which failed their most recent test and units which passed the most recent test and are ready to be shipped.
 */
public class ReportView {
    private static final String[] HEADERS = {"Model", "Serial", "# Tests", "Ship Date", "Date", "Passed?",
            "Test Comments", "Test Date", "Test Comments"};
    private static final String SHIP_DATE_IF_NULL = "-";

    public String getShipDateAfterAlterIfNull(Unit unit) {
        if (unit.getShipDate() == null) {
            return SHIP_DATE_IF_NULL;
        }
        return unit.getShipDate();
    }

    public void printAllUnitReport(List<Unit> inventoryArray) {

        System.out.println("\nList of Water Purification Units:\n" +
                "*********************************");
        if (inventoryArray.isEmpty()) {
            System.out.println("No units found.");
            return;
        }
        System.out.printf("%10s %17s %12s %12s", HEADERS[0], HEADERS[1], HEADERS[2], HEADERS[3]);
        System.out.println("\n----------  ----------------  -----------  -----------");

        for (Unit unit : inventoryArray) {

            System.out.printf("%10s %17s %12d %12s\n", unit.getModelNumber(), unit.getSerialNumber(),
                    unit.getTotalTestPerformed(), getShipDateAfterAlterIfNull(unit));

        }
    }

    public void printUnitSummary(Unit unit) {
        System.out.println("Unit details:");
        System.out.printf("%10s: %s\n", HEADERS[1], unit.getSerialNumber());
        System.out.printf("%10s: %s\n", HEADERS[0], unit.getModelNumber());
        System.out.printf("%10s: %s\n", HEADERS[3], getShipDateAfterAlterIfNull(unit));
        System.out.println("\nTests\n" +
                "************");
        System.out.printf("%12s  %10s %15s", HEADERS[4], HEADERS[5], HEADERS[6]);
        System.out.println("\n------------  ----------   -------------");
        if (unit.tests == null) {
            return;
        }
        for (Test test : unit.tests) {
            String testResult = "Failed";
            if (test.getIsTestPassed()) {
                testResult = "Passed";
            }
            System.out.printf("%12s %11s   %s\n", test.getDate(), testResult, test.getTestResultComment());
        }

    }

    public void printReportOptionsMenu() {
        System.out.println("******************\n" +
                "* Report Options *\n" +
                "******************");
        System.out.println("1. ALL:           All products.");
        System.out.println("2. DEFECTIVE:     Products that failed their last test.");
        System.out.println("3. READY-TO-SHIP: Products passed tests, not shipped.");
        System.out.println("4. Cancel report request.");

    }

    public void printDefectiveUnitsReportHeading() {
        System.out.println("DEFECTIVE Water Purification Units:\n" +
                "******************************************");
        if (UnitsInventory.getInventoryArraySize() == 0) {
            System.out.println("No units found.");
            return;
        }
        System.out.printf("%10s %17s %12s %13s  %s", HEADERS[0], HEADERS[1], HEADERS[2], HEADERS[7], HEADERS[8]);
        System.out.println("\n----------  ----------------  -----------  ------------  -------------");
    }

    public boolean checkIfMostRecentTestFailed(Unit unit, Test test) {
        if ((test.getDate().equals(unit.getMostRecentTestDateOfAUnit().format(DateTimeFormatter.ISO_LOCAL_DATE))) &&
                !test.getIsTestPassed()) {
            return true;
        }
        return false;
    }

    public void printRecentDefectiveUnitDetails(Unit unit, Test test) {
        if (checkIfMostRecentTestFailed(unit, test)) {
            System.out.printf("%10s %17s %12s %12s %s\n", unit.getModelNumber(), unit.getSerialNumber(),
                    unit.getTotalTestPerformed(), test.getDate(), test.getTestResultComment());
        }
    }

    public void printRecentDefectiveUnits(List<Unit> inventoryArray) {
        for (Unit unit : inventoryArray) {
            if (unit.tests == null) {
                continue;
            }
            for (Test test : unit.tests) {
                printRecentDefectiveUnitDetails(unit, test);
            }
        }
    }

    public void printSuccessAndReadyToShipReportHeading() {
        System.out.println("READ-TO-SHIP Water Purification Units:\n" +
                "********************************************");
        if (UnitsInventory.getInventoryArraySize() == 0) {
            System.out.println("No units found.");
            return;
        }
        System.out.printf("%10s %17s %12s", HEADERS[0], HEADERS[1], HEADERS[7]);
        System.out.println("\n----------  ----------------  -----------");
    }

    public void printSuccessAndReadyToShipUnitsDetails(Unit unit, Test test) {
        if ((test.getDate().equals(unit.getMostRecentTestDateOfAUnit().format(DateTimeFormatter.ISO_LOCAL_DATE))) &&
                test.getIsTestPassed() && getShipDateAfterAlterIfNull(unit).equals("-")) {
            System.out.printf("%10s %17s %12s\n", unit.getModelNumber(), unit.getSerialNumber(), test.getDate());
        }
    }

    public void printSuccessAndReadyToShipUnits(List<Unit> inventoryArray) {
        for (Unit unit : inventoryArray) {
            if (unit.tests == null) {
                continue;
            }
            for (Test test : unit.tests) {
                printSuccessAndReadyToShipUnitsDetails(unit, test);
            }

        }
    }

    public void printReportSortOrderMenu() {
        System.out.println("*************************************\n" +
                "* Select desired report sort order: *\n" +
                "*************************************");
        System.out.println("1. Sort by serial number.");
        System.out.println("2. Sort by model, then serial number.");
        System.out.println("3. Sort by most recent test date.");
        System.out.println("4. Cancel");

    }
}
