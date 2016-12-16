/**
 * 
 */
package com.datathon.farepricing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datathon.farepricing.dao.AlertsDAO;
import com.datathon.farepricing.model.AlertDetail;

/**
 * @author Vinod
 *
 */
@RestController
public class AlertController {

	@Autowired
	private AlertsDAO dao;
	
	@GetMapping("/alerts")
	public List<AlertDetail> getAlerts(){
		return dao.getAlerts();
	}
}
