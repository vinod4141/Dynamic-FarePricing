/**
 * 
 */
package com.datathon.pricing.consumer.model;

/**
 * @author Vinod
 *
 */
public class AlertDetail {
	private String alertId;
	private String origin;
	private String destination;
	private String carrier;
	private String pos;
	private String compartment;
	private String fareClass;
	private String fareBasis;
	private String departureDate;
	private String departureTime;
	private String arrivalDate;
	private String arrivalTime;
	private int	stops;
	private String connectionTime;
	private String seatFactor;
	private String forecastedSF;
	private double availableFare;
	private double previousAvailableFare;
	private double differencetoEK;
	private double previousDifference;
	private String fareExists;
	private double proposedFare;
	private String proposeFareExists;
	private String marketShare;
	private String ekBooking;
	private String ekBookingLY;
	private String channel;
	private String currency;
	private String holiday;
	private String suggestedAction;
	private String aircraftType;
	
	
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getCompartment() {
		return compartment;
	}
	public void setCompartment(String compartment) {
		this.compartment = compartment;
	}
	public String getFareClass() {
		return fareClass;
	}
	public void setFareClass(String fareClass) {
		this.fareClass = fareClass;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public double getAvailableFare() {
		return availableFare;
	}
	public void setAvailableFare(double d) {
		this.availableFare = d;
	}
	public double getDifferencetoEK() {
		return differencetoEK;
	}
	public void setDifferencetoEK(double d) {
		this.differencetoEK = d;
	}
	public String getFareExists() {
		return fareExists;
	}
	public void setFareExists(String fareExists) {
		this.fareExists = fareExists;
	}
	public double getProposedFare() {
		return proposedFare;
	}
	public void setProposedFare(float proposedFare) {
		this.proposedFare = proposedFare;
	}
	public String getProposeFareExists() {
		return proposeFareExists;
	}
	public void setProposeFareExists(String proposeFareExists) {
		this.proposeFareExists = proposeFareExists;
	}
	public String getMarketShare() {
		return marketShare;
	}
	public void setMarketShare(String marketShare) {
		this.marketShare = marketShare;
	}
	public String getEkBooking() {
		return ekBooking;
	}
	public void setEkBooking(String ekBooking) {
		this.ekBooking = ekBooking;
	}
	public String getEkBookingLY() {
		return ekBookingLY;
	}
	public void setEkBookingLY(String ekBookingLY) {
		this.ekBookingLY = ekBookingLY;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getFareBasis() {
		return fareBasis;
	}
	public void setFareBasis(String fareBasis) {
		this.fareBasis = fareBasis;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public int getStops() {
		return stops;
	}
	public void setStops(int stops) {
		this.stops = stops;
	}
	public String getConnectionTime() {
		return connectionTime;
	}
	public void setConnectionTime(String connectionTime) {
		this.connectionTime = connectionTime;
	}
	public String getSeatFactor() {
		return seatFactor;
	}
	public void setSeatFactor(String seatFactor) {
		this.seatFactor = seatFactor;
	}
	public String getForecastedSF() {
		return forecastedSF;
	}
	public void setForecastedSF(String forecastedSF) {
		this.forecastedSF = forecastedSF;
	}
	public double getPreviousAvailableFare() {
		return previousAvailableFare;
	}
	public void setPreviousAvailableFare(double d) {
		this.previousAvailableFare = d;
	}
	public double getPreviousDifference() {
		return previousDifference;
	}
	public void setPreviousDifference(float previousDifference) {
		this.previousDifference = previousDifference;
	}
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	public String getSuggestedAction() {
		return suggestedAction;
	}
	public void setSuggestedAction(String suggestedAction) {
		this.suggestedAction = suggestedAction;
	}
	public String getAircraftType() {
		return aircraftType;
	}
	public void setAircraftType(String aircraftType) {
		this.aircraftType = aircraftType;
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("alertId:"+alertId + " ");
		
		sb.append("origin:"+origin + " ");
		sb.append("destination:"+destination + " ");
		sb.append("carrier:"+carrier + " ");
		sb.append("carrier:"+carrier + " ");
		sb.append("departureDate:"+departureDate + " ");
		sb.append("departureTime:"+departureTime + " ");
		return sb.toString();
		
	}
	
}
