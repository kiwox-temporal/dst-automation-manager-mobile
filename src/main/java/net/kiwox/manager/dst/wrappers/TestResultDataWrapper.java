package net.kiwox.manager.dst.wrappers;

import net.kiwox.manager.dst.enums.TestResultGrade;

public class TestResultDataWrapper {
	
	private String testName;
	private float value;
	private String medition;
	private TestResultGrade result;
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	public String getMedition() {
		return medition;
	}
	public void setMedition(String medition) {
		this.medition = medition;
	}
	
	public TestResultGrade getResultGrade() {
		return result;
	}
	public void setResultGrade(TestResultGrade result) {
		this.result = result;
	}
	
}
