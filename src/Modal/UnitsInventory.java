package Modal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import View.ReportView;
import com.google.gson.*;

/**
 * UnitsSortBySerialNumber implements the compare method of Comparator interface and provides the functionality
 * to compare units based on their serialNumber.
 */
class UnitsSortBySerialNumber implements Comparator<Unit> {

    @Override
    public int compare(Unit unit1, Unit unit2) {
        BigInteger unit1SerialNumber = new BigInteger(unit1.getSerialNumber());
        BigInteger unit2SerialNumber = new BigInteger(unit2.getSerialNumber());
        return unit1SerialNumber.compareTo(unit2SerialNumber);

    }
}

/**
 * UnitsSortByModelAndSerialNumber implements the compare method of Comparator interface and provides the functionality
 * * to first compare units based on their ModelNumber(lexicographically) and then by SerialNumber(numerically).
 */
class UnitsSortByModelAndSerialNumber implements Comparator<Unit> {

    @Override
    public int compare(Unit unit1, Unit unit2) {
        BigInteger unit1SerialNumber = new BigInteger(unit1.getSerialNumber());
        BigInteger unit2SerialNumber = new BigInteger(unit2.getSerialNumber());
        String unit1ModelNumber = unit1.getModelNumber();
        String unit2ModelNumber = unit2.getModelNumber();
        if (unit1ModelNumber.compareTo(unit2ModelNumber) == 0) {
            return unit1SerialNumber.compareTo(unit2SerialNumber);
        }
        return unit1ModelNumber.compareTo(unit2ModelNumber);
    }
}

/**
 * UnitsSortByMostRecentTestDate implements the compare method of Comparator interface and provides the functionality
 * to compare units based on their most recent test date.
 */
class UnitsSortByMostRecentTestDate implements Comparator<Unit> {

    @Override
    public int compare(Unit unit1, Unit unit2) {
        if (unit1.getMostRecentTestDateOfAUnit().isAfter(unit2.getMostRecentTestDateOfAUnit())) {
            return 1;
        } else if (unit1.getMostRecentTestDateOfAUnit().equals(unit2.getMostRecentTestDateOfAUnit())) {
            return 0;
        } else {
            return -1;
        }

    }
}

/**
 * UnitsInventory class contains inventoryArray field which stores the list of units. This class provides the
 * option to get data from the json file and store it in the local unitsInventory array. Moreover, user can also
 * add new units in the list and also sort the inventoryArray based on serialNumber, model and serial number and
 * by most recent test performed on units.
 */
public class UnitsInventory {
    private static List<Unit> inventoryArray = new ArrayList<>();

    public List<Unit> getinventoryArrayReference() {
        return inventoryArray;
    }

    public int getArraySize() {
        return inventoryArray.size();
    }

    public void emptyInventoryArray() {
        inventoryArray.clear();
    }

    public static Unit getSpecificUnit(String serialNumberUserInput) {

        for (Unit unit : inventoryArray) {
            if (unit.getSerialNumber().equals(serialNumberUserInput)) {
                return unit;
            }
        }
        return null;
    }

    public void addNewUnit(String modelUserInput, String serialNumberUserInput) {
        Unit newUnit = new Unit();
        newUnit.setSerialNumber(serialNumberUserInput);
        newUnit.setModelNumber(modelUserInput);
        inventoryArray.add(newUnit);
    }

    public static int getInventoryArraySize() {
        return inventoryArray.size();
    }

    public void fetchFromJson(FileReader reader, String userInputFileName) {
        //got idea about how to use Gson library with LocalDate class from the dr. Brian Fraser posted video for this
        //assignment.

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        Unit[] jsonArray = gson.fromJson(reader, Unit[].class);
        //Got idea from chatgpt as regular arrays are not part of java collection framework
        //so first convert the regular array to list and use List.addAll(List anotherList) to add another list in it.
        inventoryArray.addAll(Arrays.asList(jsonArray));
    }

    public void sortBySerialNumber() {
        java.util.Collections.sort(inventoryArray, new UnitsSortBySerialNumber());
    }

    public void sortByModelAndSerialNumber() {
        java.util.Collections.sort(inventoryArray, new UnitsSortByModelAndSerialNumber());
    }

    public void sortByMostRecentTestDate() {
        java.util.Collections.sort(inventoryArray, new UnitsSortByMostRecentTestDate());
    }

}
