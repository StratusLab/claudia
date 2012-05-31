/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.provisioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

public class SnapshotResourceBase extends Resource {

	public SnapshotResourceBase() {
		super();
	}

	public SnapshotResourceBase(Context context, Request request, Response response) {
		super(context, request, response);
	}

	/**
	 * Helper method to convert the vapp/vee/vm hierarchy into a collection 
	 * @return
	 */
	protected List<String> getVappList(final String vappId, final String veeId, final String vmId) {
		List<String> vappList = Collections.unmodifiableList(
				new ArrayList<String>() {{
					add(vappId);
					add(veeId);
					add(vmId);
				}});
		return vappList;
	}

}
