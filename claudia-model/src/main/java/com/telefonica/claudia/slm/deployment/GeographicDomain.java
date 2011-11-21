/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.deployment;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
public class GeographicDomain implements PersistentObject {
    
	public static class Location {
		String latitude;
		String longitude;
		String units;
		long distance;
		
		public Location(String latitude, String longitude, String units, long distance) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.units = units;
			this.distance = distance;
		}
		
		@Override
		public int hashCode() {
			return latitude.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (!(obj instanceof Location)) return false;
			
			Location loc = (Location) obj;
			
			if (latitude!=null && !latitude.equals(loc.latitude)) return false;
			if (longitude!=null && !longitude.equals(loc.longitude)) return false;
			if (units!=null && !units.equals(loc.units)) return false;
			if (distance!=loc.distance) return false;
		
			return true;
		}
	}
	
	@Id
	@GeneratedValue
	public long internalId;
	
    private int id;
    private static int idCounter= 1;
    
    @CollectionOfElements
    private Map<String, Boolean> timezones = new HashMap<String, Boolean>();
    
    @CollectionOfElements
    private Map<String, Boolean> areas = new HashMap<String, Boolean>();
    
    @CollectionOfElements
    private Map<String, Boolean> countries = new HashMap<String, Boolean>();
    
    @Transient
    private Map<Location, Boolean> locations = new HashMap<Location, Boolean>();
    
    public GeographicDomain() {
    	this.id = idCounter ++;
    }
    
    public int getId() {
        return id;
    }
    
    public void addTimezone(String zone, boolean valid) {
    	timezones.put(zone, valid);
    }
    
    public void addAreas(String area, boolean valid) {
    	areas.put(area, valid);
    }
    
    public void addCountries(String country, boolean valid) {
    	countries.put(country, valid);
    }
    
    public void addLocations(Location loc, boolean valid) {
    	locations.put(loc, valid);
    }
    
    public Map<String, Boolean> getTimezones() {
    	return timezones;
    }
    
    public Map<String, Boolean> getAreas() {
    	return areas;
    }
    
    public Map<String, Boolean> getCountries() {
    	return countries;
    }
     
    public Map<Location, Boolean> getLocations() {
    	return locations;
    }
    
	public long getObjectId() {
		return internalId;
	}
}