package net.kiwox.manager.dst.wrappers;

import net.kiwox.manager.dst.enums.TestResultGrade;

public class EntelTestResultDetailDataWrapper {

    private String code;
    private String detail;
    private String description;
    private float time;
    private String medition;
    private TestResultGrade result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getMedition() {
        return medition;
    }

    public void setMedition(String medition) {
        this.medition = medition;
    }

    public TestResultGrade getResult() {
        return result;
    }

    public void setResult(TestResultGrade result) {
        this.result = result;
    }
}
