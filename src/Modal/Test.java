package Modal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Test class is used to provide a record for each test. Each test record contains a date, isTestPassed
 * and testResultComment. This class also have set and get methods for each of these fields.
 */
public class Test {
    private LocalDate date;
    private boolean isTestPassed;
    private String testResultComment;

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }

    public boolean getIsTestPassed() {
        return isTestPassed;
    }

    public void setIsTestPassed(boolean isTestPassed) {
        this.isTestPassed = isTestPassed;
    }

    public String getTestResultComment() {
        return testResultComment;
    }

    public void setTestResultComment(String testResultComment) {
        this.testResultComment = testResultComment;
    }

}
