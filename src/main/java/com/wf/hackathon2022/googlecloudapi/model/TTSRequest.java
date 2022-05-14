package com.wf.hackathon2022.googlecloudapi.model;

/**
 * 
 * @author satpra
 * Created for Google DFCX SDK.
 *
 */
public class TTSRequest {
	public String empId;
	public String countryCode;
	public String textStr;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTextStr() {
		return textStr;
	}
	public void setTextStr(String textStr) {
		this.textStr = textStr;
	}
	
	
	
}