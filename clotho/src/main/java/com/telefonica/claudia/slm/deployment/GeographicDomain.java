/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.deployment;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class GeographicDomain {
    
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
}