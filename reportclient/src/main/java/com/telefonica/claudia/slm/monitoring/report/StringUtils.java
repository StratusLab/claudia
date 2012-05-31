package com.telefonica.claudia.slm.monitoring.report;

public class StringUtils {
    private static final char QUOTE = '"';
    private static final char DELIMITER = ',';
       
    public static String toCSV(String[] valueArray) {
    	StringBuilder sb = new StringBuilder();
       
        for (int i=0; i<valueArray.length; i++) {
            String value = valueArray[i] == null ? "" : valueArray[i];
           
            sb.append(QUOTE);
           
            for (int j=0; j<value.length(); j++) {
                char c = value.charAt(j);
                switch(c) {
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case QUOTE:
                        sb.append("\\" + QUOTE);
                        break;
                    default:
                        sb.append(c);
                }
            }
            sb.append(QUOTE);
           
            if (i < valueArray.length -1 ) { // is not the last one
                sb.append(DELIMITER);
            }
        }
        return sb.toString();
    }
} 
