package net.kiwox.manager.dst.wrappers;

import net.kiwox.manager.dst.enums.TestResultGrade;

import java.util.ArrayList;
import java.util.List;
import static net.kiwox.manager.dst.utils.Utils.*;

public class EntelTestResultDataWrapper {
	
	private String code;
	private String message;
	private float time;
	private String medition;
	private TestResultGrade result;

	private List<EntelTestResultDetailDataWrapper> details;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public List<EntelTestResultDetailDataWrapper> getDetails() {
		return details;
	}

	public void setDetails(List<EntelTestResultDetailDataWrapper> details) {
		this.details = details;
	}

	public void addItemDetail(EntelTestResultDetailDataWrapper item){
		if (isNull(this.details)){
			this.details = new ArrayList<>();
		}
		this.details.add(item);
	}
}
