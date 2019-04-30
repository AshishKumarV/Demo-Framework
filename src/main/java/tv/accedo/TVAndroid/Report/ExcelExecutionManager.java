package tv.accedo.TVAndroid.Report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelExecutionManager{

	static Logger log = LoggerFactory.getLogger(ExcelExecutionManager.class);
	private static Xslt_XlsReader xls = null;
	private static String directory = "";
	private HashMap<String, LinkedHashSet<String>> classMethodsMapping;
	private HashMap<String, LinkedHashSet<String>> methodTestsummaryMapping;
	private HashMap<String, LinkedHashSet<String>> methodTeststepsMapping;
	private HashMap<String, LinkedHashSet<String>> methodTestexpectedMapping;

	private String executionType="System";


	public ExcelExecutionManager(String executionType) {
		this.executionType = executionType;
	}

	public ExcelExecutionManager() {
		this.executionType = executionType;
	}
	/**
	 * This method will return HashMap of SuiteName and test methods
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,LinkedHashSet<String>> getTestCasesFromExcel() throws Exception
	{
		directory = System.getProperty("user.dir");
		directory = directory.replace("\\", "\\\\");
		HashSet<Xslt_XlsReader> subSheets = capureSubSheets();
		Iterator<Xslt_XlsReader> sheetIterator =subSheets.iterator();
		classMethodsMapping = new HashMap<String, LinkedHashSet<String>>();
		while(sheetIterator.hasNext()){	

			fetchAllMethodForExecution(sheetIterator.next());
		}
		return classMethodsMapping;
	}


	private HashSet<Xslt_XlsReader> capureSubSheets(){
		HashSet<Xslt_XlsReader> subSheets = new HashSet<Xslt_XlsReader>();
		for (int l = 2; l <= TestData.globalXLS.getRowCount("Sheet1"); l++) {
			if (TestData.globalXLS.getCellData("Sheet1", "Refer_Sheet", l).equalsIgnoreCase("Y")) {
				xls = new Xslt_XlsReader(directory+ "\\TestCaseCreation\\"+ TestData.globalXLS.getCellData("Sheet1", "Reference_Sheet_Name", l));	
				subSheets.add(xls);
			} else {
				continue;
			}
		}
		return subSheets;
	}


	private void fetchAllMethodForExecution(Xslt_XlsReader xls){
		int testCaseCounter = xls.getRowCount("TestCaseSheet");
		for(int i=2; i<=testCaseCounter; i++){
			if(xls.getCellData("TestCaseSheet", "RunAutomation_" + executionType, i).equalsIgnoreCase("yes")){
				String className = xls.getCellData("TestCaseSheet", "ClassFileName",i);
				String packageName= xls.getCellData("TestCaseSheet", "PackageName", i).trim();
				className = packageName+"."+className;
				//log.info(className);
				String methodNameWithoutClass= xls.getCellData("TestCaseSheet", "TestCaseID", i).trim();
				if(packageName.equalsIgnoreCase("") || className.equalsIgnoreCase("") || methodNameWithoutClass.equalsIgnoreCase("")){
					log.info("Either Package or Class or Method name is blank");
					continue;
				}
				else{
					if(classMethodsMapping.containsKey(className))
					{
						LinkedHashSet<String> testMethodList = classMethodsMapping.get(className);
						testMethodList.add(methodNameWithoutClass);
						classMethodsMapping.put(className,testMethodList);
					}
					else
					{
						LinkedHashSet<String> testMethodList = new LinkedHashSet<String>();
						testMethodList.add(methodNameWithoutClass);
						classMethodsMapping.put(className,testMethodList);
					}
				}
			}

		}
	}

	public String addDescription(String QualifiedMethodName){
		String[] paths = null;
		String finalDesc="";
		log.info("Qualified name:"+QualifiedMethodName);

		if(TestData.xlsPath.contains(","))
			paths = TestData.xlsPath.split(",");

		for(int j=0;j<paths.length;j++){
			if(!paths[j].equals("")){
				finalDesc = addDescriptionLoop(paths[j], QualifiedMethodName);
				if(finalDesc.trim().length()>1){
					log.info("finalDesc1:"+finalDesc);
					return finalDesc;
				}
			}
		}
		log.info("finalDescLast:"+finalDesc);
		return finalDesc; 
	}


	public String addDescriptionLoop(String path, String QualifiedMethodName){
		Xslt_XlsReader xls = new Xslt_XlsReader(path);

		String briefDesc ="";
		String steps = "";
		String expected = "";

		for(int i=2;i<=xls.getRowCount("TestCaseSheet"); i++){

			String ExpectedQualifiedMethodName = xls.getCellData("TestCaseSheet", "PackageName", i) + "." + xls.getCellData("TestCaseSheet", "ClassFileName", i) + "." + xls.getCellData("TestCaseSheet", "TestCaseID", i);

			if(QualifiedMethodName.equalsIgnoreCase(ExpectedQualifiedMethodName)){
				
				log.info("reached match");

				/*Reporter.log("[Environment]: " + TestData.TestorLive + " ");
				Reporter.log("[Execution_Type]: " + TestData.Execution_Type + " ");
				Reporter.log("[Browser_Type]: " + TestData.Driver_Type + " ");*/				

				if(!xls.getCellData("TestCaseSheet", "TestCase", i).equals(""))
					briefDesc = "[BriefDesc]: "+xls.getCellData("TestCaseSheet", "TestCase", i);
				else
					briefDesc = "[BriefDesc]: Description is not provided";

				if(!xls.getCellData("TestCaseSheet", "TestSteps", i).equals(""))
					steps = "[Steps]: "+xls.getCellData("TestCaseSheet", "TestSteps", i);
				else
					steps = "[Steps]: Test Steps are not provided";

				if(!xls.getCellData("TestCaseSheet", "ExpectedResults", i).equals(""))
					expected = "[Expected]: "+xls.getCellData("TestCaseSheet", "ExpectedResults", i);
				else
					expected = "[Expected]: Expected Result is not provided";
				return  briefDesc+"\n"+steps+"\n"+expected;


			}

		}
		return  briefDesc+"\n"+steps+"\n"+expected;
	}
}