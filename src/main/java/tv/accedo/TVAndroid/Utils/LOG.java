package tv.accedo.TVAndroid.Utils;

import org.apache.commons.logging.Log;

public enum LOG 

{
		THREAD_START(0,"Starting the thread to capture  DEVICE LOG "),
	    THREAD_STOP(0,"Starting the thread to capture  DEVICE LOG "),
	    THREAD_INTERUPPTED(1,"Thread is in interuppted state"),
	    EXECUTION_STARTED(0,"Started Execution of TC "),
	    EXECUTION_COMPLETED(0,"Completed Execution of TC "),
	    ASSERT_FAIL(2,"ASSERTION FAILED"),
	    NOT_EXISTS(1,"doesn't exist on the app"),
	    COMMAND_EXEC(0,"Executing command : "),
	    EVENT_LOGS(0,"Result fetched from event logs : ");
	   
			
		  private final int code;
		  private final String description;

		  private LOG(int code, String description) {
		    this.code = code;
		    this.description = description;
		  }
		

		  public String getDescription() {
		     return description;
		  }

		  public int getCode() {
		     return code;
		  }

		  @Override
		  public String toString() {
		    return code + ": " + description;
		  }
	

}
