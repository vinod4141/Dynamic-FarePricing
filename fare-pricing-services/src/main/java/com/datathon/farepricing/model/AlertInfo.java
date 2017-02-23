/**
 * 
 */
package com.datathon.farepricing.model;

/**
 * @author Vinod
 *
 */
public class AlertInfo {
	
	private int alertId;
	private String alertName;
	private String alertDescription;
	
	public int getAlertId() {
		return alertId; 
	}
	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}
	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public String getAlertDescription() {
		return alertDescription;
	}
	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

}
