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
package com.telefonica.claudia.smi.util;

import java.util.ArrayList;
import java.util.Iterator;

public class Util {
	
	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
	
	public static boolean areNumbers(ArrayList<String> al) {
		boolean areNumbers = true;
		Iterator<String> it = al.iterator();
		while (it.hasNext()){
			if (!isNumber(it.next())) areNumbers = false;
		}
		return areNumbers;
	}
	
	public static String getUpHref(String href){
		StringBuffer sb = new StringBuffer(href);
		int n = sb.lastIndexOf("?");
		if (n>0) sb.delete(sb.lastIndexOf("?"),sb.length());
		sb.delete(sb.lastIndexOf("/"),sb.length());
		return sb.toString();
	}
	
	public static String removeParameters(String href) {
		StringBuilder sb = new StringBuilder(href);
		int n = sb.lastIndexOf("?");
		if (n>0) {
			sb.delete(n, sb.length());
		}
		return sb.toString();
	}
	
	public static long convertTimeInterval(String timeInterval) {
		String quantity = timeInterval.substring(0, timeInterval.length() - 1);
		String units = timeInterval.substring(timeInterval.length() - 1);
		
		long result = 0;
		if (isNumber(quantity)) {
			result = Integer.parseInt(quantity);
		}
		
		if ("s".equals(units)) {
			result *= 1;
		} else if ("m".equals(units)) {
			result *= 60;
		} else if ("h".equals(units)) {
			result *= 3600;
		} else if ("d".equals(units)) {
			result *= 3600 * 24;
		} else {
			result *= 0;
		}
		
		return result;
	}

}


