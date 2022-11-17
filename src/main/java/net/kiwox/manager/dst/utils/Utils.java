package net.kiwox.manager.dst.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.domain.TestScale;
import net.kiwox.manager.dst.enums.TestResultGrade;

public class Utils {

    private Utils() {
    }

    public static String[] splitCommandLine(String line) {
        List<String> command = new LinkedList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(line);
        while (m.find()) {
            if (m.group(1) != null) {
                command.add(m.group(1));
            } else {
                command.add(m.group(2));
            }
        }
        return command.toArray(new String[0]);
    }

    public static TestResultGrade joinGrades(List<TestResultGrade> grades) {
        TestResultGrade finalGrade = null;
        for (TestResultGrade grade : grades) {
            if (finalGrade == null || (grade != null && finalGrade.compareTo(grade) < 0)) {
                finalGrade = grade;
            }
        }
        return finalGrade == null ? TestResultGrade.FAILED : finalGrade;
    }


    public static TestResultGrade getGrade(float value, List<TestScale> testScales) {
        return getGrade(value, testScales, false);
    }

    public static TestResultGrade getGrade(float value, List<TestScale> testScales, boolean error) {
        if (error && value == 0) {
            return TestResultGrade.FAILED;
        }

        for (TestScale scale : testScales) {
            Integer from = scale.getFrom();
            Integer to = scale.getToEqual();
            if ((from == null || from < value) && (to == null || value <= to)) {
                return scale.getType();
            }
        }
        return null;
    }

    public static <T> boolean isNull(T value) {
        return value == null;
    }

    public static String getTestExecutionCode(TestResult testResult) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return String.format("%s%s", testResult.getTest().getExecution_code(), df.format(testResult.getCreatedOn()));
    }
}
