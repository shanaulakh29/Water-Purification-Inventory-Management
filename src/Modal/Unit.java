package Modal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit class contains fields specific to a single unit like its model, serialNumber, dateShipped and
 * list of tests performed.This class has methods to get and set these fields and also the functionality of
 * validating the serialNumber, adding new test, getting the most recent test date and
 * getTotalTestsPerformed on a unit.
 */
public class Unit {
    private String model;
    private String serialNumber;
    private LocalDate dateShipped;
    public List<Test> tests;
    private static final int MIN_POSSIBLE_SERIAL_DIGITS = 3;
    private static final int MAX_POSSIBLE_SERIAL_DIGITS = 15;

    public String getModelNumber() {
        return model;
    }

    public void setModelNumber(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getShipDate() {
        if (dateShipped == null) {
            return null;
        }
        return dateShipped.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void setShipDate() {

        this.dateShipped = LocalDate.now();

    }

    public int getTotalTestPerformed() {
        if (tests == null) {
            return 0;
        }
        return tests.size();
    }

    public void addTest(boolean testResult, String testComment) {
        if (tests == null) {
            tests = new ArrayList<>();
        }
        Test test = new Test();
        test.setIsTestPassed(testResult);
        test.setTestResultComment(testComment);
        LocalDate now = LocalDate.now();
        test.setDate(now);
        tests.add(test);
    }

    public LocalDate getMostRecentTestDateOfAUnit() {
        if (tests==null || tests.isEmpty()) {
            return LocalDate.MAX;
        }
        LocalDate mostRecentTestDate = LocalDate.parse(tests.get(0).getDate());
        for (Test test : tests) {
            LocalDate testDate = LocalDate.parse(test.getDate());
            if (testDate.isAfter(mostRecentTestDate)) {
                mostRecentTestDate = testDate;
            }
        }
        return mostRecentTestDate;
    }


    public static String getCheckSum(long serialNumber) {
        long checkSumValue = serialNumber % 100;
        if (checkSumValue <= 9) {
            return "0" + checkSumValue;
        }
        return String.valueOf(checkSumValue);

    }

    //Got idea about from chatgpt to get each digit and add it at a time.
    public static boolean serialNumberIsValid(String serialNumberUserInput) {
        try {
            if (serialNumberUserInput.length() < MIN_POSSIBLE_SERIAL_DIGITS ||
                    serialNumberUserInput.length() > MAX_POSSIBLE_SERIAL_DIGITS) {
                throw new IllegalArgumentException("Invalid Serial Number Length");
            }
            if (UnitsInventory.getSpecificUnit(serialNumberUserInput) != null) {
                throw new IllegalArgumentException("Serial Number already exists");
            }
            long serialNumber = Long.parseLong(serialNumberUserInput);
            String checkSum = getCheckSum(serialNumber);
            long serialNumAfterLast2DigitsRemoved = serialNumber / 100;
            long sum = 0;
            while (serialNumAfterLast2DigitsRemoved != 0) {
                long lastDigit = serialNumAfterLast2DigitsRemoved % 10;
                sum += lastDigit;
                serialNumAfterLast2DigitsRemoved = serialNumAfterLast2DigitsRemoved / 10;
            }
            String modOfSum = String.valueOf(sum % 100);
            if (sum % 100 <= 9) {
                modOfSum = "0" + modOfSum;
            }
            if (modOfSum.equals(checkSum)) {
                return true;
            } else {
                throw new IllegalArgumentException("CheckSum didn't match");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }
}
