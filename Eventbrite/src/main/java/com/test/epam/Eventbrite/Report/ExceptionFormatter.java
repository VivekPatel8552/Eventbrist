package com.test.epam.Eventbrite.Report;

public class ExceptionFormatter {

	  public int FULL_STACK = -1;
	  
    /**
     * Converts a string to html format by replacing newline symbols 
     * to &lt;br&gt; and space symbols to &lt;&amp;nbsp;&gt;.
     *
     * @param s input string.
     * @return output string in html format.
     */
    public String toHtml(String s) {
    	
    	if(s!=null) {
        StringBuilder sb = new StringBuilder(s.length() * 2);
        for (int i = 0;  i < s.length();  i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '\n':
                    sb.append("<br>");
                    break;
                case ' ':
                    sb.append("&nbsp;");
                    break;
                default:
                    sb.append(ch);
                    break;
            }
        }
    	
        return sb.toString();
    	}
    	else return null;
    }
    

    /**
     * Formats exception with full stack trace without custom message and without thread name.
     * 
     * @param t exception
     * @return formatted exception
     */
    public String format(Throwable t) {
    	if(t!=null) {
    		ExceptionFormatter exceptionFormatter = new ExceptionFormatter();
        return format(t, exceptionFormatter.FULL_STACK);
    	}
    	else return null;
    }


    /**
     * Formats exception without custom message and without thread name.
     * 
     * @param t exception
     * @param nStackDepth trace stack depth:
     *    {@link #FULL_STACK} - to format with full trace stack 
     *     0 - to format with "exception class + message"
     * @return formatted exception
     */
    public String format(Throwable t, int nStackDepth) {
        StringBuilder sb = new StringBuilder();
        int n = 1;
        for (Throwable e = t;  e != null;  e = e.getCause()) {
            if (n > 1) {
                sb.append('\n');
            }
            sb.append('[');
            sb.append(n);
            sb.append("] ");
            sb.append(e.getClass().getName());
            sb.append(": ");
            String sMsg = e.getMessage();
            if (sMsg != null) {
                if (sMsg.endsWith("\n")) {
                    sMsg = sMsg.substring(0, sMsg.length() - 1);
                }
                sb.append(sMsg);
            }
            StackTraceElement[] a_stack = e.getStackTrace();
            for (int i = 0;  i < a_stack.length;  i++) {
                if (nStackDepth >= 0  &&  i >= nStackDepth) {
                    break;
                }
                sb.append("\n    at ");
                sb.append(a_stack[i].toString());
            }
            n++;
        }
        return sb.toString(); 
    }
}
