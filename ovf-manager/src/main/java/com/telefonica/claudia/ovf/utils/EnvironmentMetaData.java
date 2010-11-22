/**
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is available at https://svn.forge.morfeo-project.org/claudia
 *
 * The Initial Developer of the Original Code is Telefónica Investigación y Desarrollo S.A.U.,
 * (http://www.tid.es), Emilio Vargas 6, 28043 Madrid, Spain.
.*
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s): ______________________________________.
 *
 */

package com.telefonica.claudia.ovf.utils;

import java.io.OutputStream;
import java.io.Serializable;

public class EnvironmentMetaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idEnvironment;
	
	private OutputStream environment;

	
	
	public EnvironmentMetaData() {
	}

	public EnvironmentMetaData(String idEnvironment, OutputStream environment) {
		this.idEnvironment = idEnvironment;
		this.environment = environment;
	}

	public String getIdEnvironment() {
		return idEnvironment;
	}

	public void setIdEnvironment(String idEnvironment) {
		this.idEnvironment = idEnvironment;
	}

	public OutputStream getEnvironment() {
		return environment;
	}

	public void setEnvironment(OutputStream environment) {
		this.environment = environment;
	}
	
	
}
