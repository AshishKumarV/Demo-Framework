package tv.accedo.TVAndroid.Report;

import tv.accedo.TVAndroid.Report.Xslt_XlsReader;


public class TestData{

	public static String TestCaseId = "";
	public static String sMessages="";
	public static String TestorLive = "Live";
	public static String Execution_Type = "System";
	public static String System_Type;
	public static String Device_Name="";
	String javaClass;
	String executiontype;
	
	public static Xslt_XlsReader globalXLS= new Xslt_XlsReader("./TestCaseCreation/excelSheetRef.xls");
	public static String xlsPath= xlsPath();
	
	private static String xlsPath(){
		String directory= System.getProperty("user.dir");
		directory = directory.replace("\\", "\\\\");
		String path="";
		for(int i=2;i<=globalXLS.getRowCount("Sheet1");i++){
			if(globalXLS.getCellData("Sheet1", "Refer_Sheet", i).equalsIgnoreCase("Y")){
				path= directory + "/TestCaseCreation/" + globalXLS.getCellData("Sheet1", "Reference_Sheet_Name", i) + "," + path;
			}
		}
		return path;
	}
}