BEGIN		{ printLines="true"; }

/BEGIN/		{ print $0"";
		  print ENVIRON["HOST_LIST"];
		  printLines= "false"
		  }

		{ if (printLines=="true") print $0; }

/END/		{ print $0;
		  printLines= "true";
		}
