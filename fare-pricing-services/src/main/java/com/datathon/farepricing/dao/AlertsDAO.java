/**
 * 
 */
package com.datathon.farepricing.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.datathon.farepricing.model.AlertDetail;
import com.datathon.farepricing.model.AlertInfo;
import com.datathon.farepricing.util.GeneralUtil;
import com.datathon.farepricing.util.ReadExcel;

/**
 * @author Vinod
 *
 */
@Component
public class AlertsDAO {

	public List<AlertDetail> getAlerts(){
		
		//AlertDetail alert = new AlertDetail();
		
		List<AlertDetail> alerts = ReadExcel.readExcel();
		/*alert.setAlertId("1");
		alert.setOrigin("DXB");
		alert.setDestination("LHR");
		alert.setAvailableFare(3000);
		
		alerts.add(alert);*/
		
		return alerts;
	}

	public List<AlertInfo> getAlertInfo() {
		
		List<AlertInfo> alertInfos = new ArrayList<AlertInfo>();
		
		AlertInfo alert;
		
		for(int i=1; i<=15; i++){
			alert = new AlertInfo();
			alert.setAlertId(i);
			alert.setAlertName("Test " + i);
			alert.setAlertDescription("TestData");
			alertInfos.add(alert);
		}
		return alertInfos;
	}

	public List<AlertDetail> getAlertKafka() {
		
		return GeneralUtil.getAlertFromKafka();
	}
}
