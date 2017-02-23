/**
 * 
 */
package com.datathon.farepricing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datathon.farepricing.dao.AlertsDAO;
import com.datathon.farepricing.model.AlertDetail;
import com.datathon.farepricing.model.AlertInfo;

/**
 * @author Vinod
 *
 */
@CrossOrigin
@RestController
public class AlertController {

	@Autowired
	private AlertsDAO dao;
	
	@CrossOrigin
	@GetMapping("/alerts")
	public List<AlertDetail> getAlerts(){
		return dao.getAlerts();
	}
	
	@CrossOrigin
	@GetMapping("/alertSample")
	public List<AlertInfo> getAlertSample(){
		return dao.getAlertInfo();
	}
	
	@CrossOrigin
	@GetMapping("/alertKafka")
	public List<AlertDetail> getAlertFromKafka(){
		return dao.getAlertKafka();
	}
	
	
}
