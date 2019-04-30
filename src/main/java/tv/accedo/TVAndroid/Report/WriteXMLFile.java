package tv.accedo.TVAndroid.Report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXMLFile extends TestData {

	Xslt_XlsReader xls = null;
	String directory = "";
	int threadCount;
	public  String retry= "false";
	static Logger log = LoggerFactory.getLogger(WriteXMLFile.class);

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, URISyntaxException, IOException {
		WriteXMLFile xmlFile = new WriteXMLFile();
		xmlFile.initializeConfigVariables(args[0],args[1]);
		xmlFile.assignXLS();
	}


	/***********************************************************************************************
	 * Function Description : Get the test cases name to be executed
	 *  date: 14-May-2017
	 * *********************************************************************************************/

	public void assignXLS() {
		directory = System.getProperty("user.dir");
		directory = directory.replace("\\", "\\\\");
		try {
			for (int i = 2; i <= TestData.globalXLS.getRowCount("Sheet1"); i++) {
				if (TestData.globalXLS.getCellData("Sheet1", "Refer_Sheet", i)
						.equalsIgnoreCase("Y")) {
					TestData.xlsPath = directory
							+ "/TestCaseCreation/"
							+ TestData.globalXLS.getCellData("Sheet1",
									"Reference_Sheet_Name", i);
					xls = new Xslt_XlsReader(directory
							+ "/TestCaseCreation/"
							+ TestData.globalXLS.getCellData("Sheet1",
									"Reference_Sheet_Name", i));

					createXML();// Normal Create XML Function call	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/***********************************************************************************************
	 * Function Description : Create a XML File for executing test cases
	 *  date: 14-May-2017
	 * *********************************************************************************************/

	public void createXML() {
		if (xls == null)
			log.info("PLEASE ASSIGN A TEST CASE SHEET IN '''excelSheetRef.xls''' FILE");

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("suite");
			doc.appendChild(rootElement);
			Attr attr = doc.createAttribute("name");

			File f = new File("");
			String name = f.getAbsolutePath();
			String mainName = name.substring(name.lastIndexOf("/") + 1, name.length());

			attr.setValue(mainName);
			rootElement.setAttributeNode(attr);

			Element listeners = doc.createElement("listeners");
			rootElement.appendChild(listeners);
			Element listener1 = doc.createElement("listener");
			Attr list1 = doc.createAttribute("class-name");
			list1.setValue("tv.accedo.TVAndroid.Report.ExtentReporter");
			listener1.setAttributeNode(list1);

			Element listener2 = doc.createElement("listener");
			Attr list2 = doc.createAttribute("class-name");
			list2.setValue("tv.accedo.TVAndroid.Report.TakeScreenshot");
			listener2.setAttributeNode(list2);

			Element listener3 = doc.createElement("listener");
			Attr list3 = doc.createAttribute("class-name");
			list3.setValue("tv.accedo.TVAndroid.Report.TestNGError");
			listener3.setAttributeNode(list3);

			Element listener4 = doc.createElement("listener");
			Attr list4 = doc.createAttribute("class-name");
			list4.setValue("tv.accedo.TVAndroid.Report.AnnotationTransformer");
			listener4.setAttributeNode(list4);

			listeners.appendChild(listener4);
			listeners.appendChild(listener1);
			listeners.appendChild(listener2);
			listeners.appendChild(listener3);

			for (int l = 2; l <= TestData.globalXLS.getRowCount("Sheet1"); l++) {
				if (TestData.globalXLS.getCellData("Sheet1", "Refer_Sheet", l)
						.equalsIgnoreCase("Y")) {
					xls = new Xslt_XlsReader(directory
							+ "/TestCaseCreation/"
							+ TestData.globalXLS.getCellData("Sheet1",
									"Reference_Sheet_Name", l));
				} else {
					continue;
				}

				HashSet<String> DistinctClassNames = new HashSet<String>();// to remove dependency
				for (int i = 2; i <= xls.getRowCount("TestCaseSheet"); i++)
					DistinctClassNames.add(xls.getCellData("TestCaseSheet",
							"PackageName", i) + "."
							+ xls.getCellData("TestCaseSheet", "ClassFileName",	i));

				int testCaseCounter = xls.getRowCount("TestCaseSheet");

				HashSet<Method> methodsList = new HashSet<Method>();
				/*
				 * Used below to prevent duplicate methods from being added to
				 * testng .xml
				 */
				for (String classname : DistinctClassNames) {

					Object className2 =null;
					try {
						className2 = Class.forName(classname)
								.getConstructor().newInstance();
					} catch (ClassNotFoundException e) {
						log.info("Class " + classname + " not found.");
						e.printStackTrace();
						continue;
					}

					/*
					 * First, Class is created from string name found from the
					 * sheet and then Object of that class is created to fetch
					 * all the methods for that particular class
					 */
					Method[] methodsArray = className2.getClass()
							.getDeclaredMethods();
					Element test = doc.createElement("test");
					rootElement.appendChild(test);
					Attr attr1 = doc.createAttribute("name");
					attr1.setValue(classname);
					test.setAttributeNode(attr1);
					Attr attr2 = doc.createAttribute("preserve-order");
					attr2.setValue("true");
					test.setAttributeNode(attr2);

					Element classes = doc.createElement("classes");
					test.appendChild(classes);

					Element class1 = doc.createElement("class");
					classes.appendChild(class1);
					// classname

					Attr attrr = doc.createAttribute("name");
					attrr.setValue(classname);
					class1.setAttributeNode(attrr);

					Element methods = doc.createElement("methods");
					class1.appendChild(methods);

					for (int j = 2; j <= testCaseCounter; j++) {
						String classNameWithinTags = xls.getCellData(
								"TestCaseSheet", "PackageName", j)
								+ "."
								+ xls.getCellData("TestCaseSheet",
										"ClassFileName", j);
						if (classname.equalsIgnoreCase(classNameWithinTags)) {
							if (xls.getCellData("TestCaseSheet",
									"RunAutomation_" + Execution_Type, j)
									.equalsIgnoreCase("Yes")
									|| xls.getCellData("TestCaseSheet",
											"RunAutomation_" + Execution_Type,
											j).equalsIgnoreCase("Y")) {

								String methodNameFromSheet = classNameWithinTags
										+ "."
										+ xls.getCellData("TestCaseSheet",
												"TestCaseID", j) + "(";

								/*
								 * Fetching package . classname . methodname
								 * from TestCaseSheet and added ( to handle
								 * whole match. 1. Reflection API returns
								 * prototype of the method. 2. Whole match taken
								 * to take care of same method name in different
								 * class
								 */
								int methodCounter = 0;
								for (Method mName : methodsArray) {
									methodCounter++;
									/*
									 * To keep track if all the methods returned
									 * by Reflection API are taken care of.
									 */

									if (mName.toString().contains(
											methodNameFromSheet)
											&& !methodsList.contains(mName)) {
										/*
										 * If sheet has proper method name and
										 * method is not already added to
										 * testng.xml (tracked by HashSet) then
										 * proceed else ignore */
										methodsList.add(mName);
										/*
										 * Method name added to HashSet so that
										 * next time it can be checked if method
										 * is already added to testng.xml or not
										 * 
										 */
										Element include = doc
												.createElement("include");
										methods.appendChild(include);
										Attr incAtt = doc
												.createAttribute("name");
										incAtt.setValue(xls.getCellData(
												"TestCaseSheet", "TestCaseID",
												j));
										include.setAttributeNode(incAtt);
										break;
									} else if (methodCounter == methodsArray.length)
										/*
										 * To check if all the methods in
										 * methodsArray are tracked
										 */
									{
										Element include = doc
												.createElement("exclude");
										methods.appendChild(include);
										Attr incAtt = doc
												.createAttribute("name");
										incAtt.setValue(xls.getCellData(
												"TestCaseSheet", "TestCaseID",
												j));
										include.setAttributeNode(incAtt);
										Attr parAtt = doc
												.createAttribute("reason");
										parAtt.setValue("Invalid_Method_Name");
										/*
										 * Reason of exclusion of method is --
										 * Method name mentioned in TestCaseID
										 * Column in TestCaseSheet is not a
										 * valid Method name for class
										 */
										include.setAttributeNode(parAtt);
									}
								}
							} else {
								Element exclude = doc.createElement("exclude");
								methods.appendChild(exclude);
								Attr incAtt = doc.createAttribute("name");
								incAtt.setValue(xls.getCellData(
										"TestCaseSheet", "TestCaseID", j));
								exclude.setAttributeNode(incAtt);
								Attr parAtt = doc.createAttribute("reason");
								parAtt.setValue("Marked as No");
								/*
								 * Reason of exclusion of method is -- Method is
								 * marked as No in TestCaseSheet
								 */
								exclude.setAttributeNode(parAtt);
							}
						}
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File file = new File("testng.xml");
			StreamResult result = new StreamResult(file.getAbsolutePath());
			transformer.transform(source, result);
			log.info("XML File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/***********************************************************************************************
	 * Function Description : Initialize variables used to execute the test cases
	 *  date: 14-May-2017
	 * *********************************************************************************************/
	
	public void initializeConfigVariables(String testorlive, String executiontype) {

		if(System.getenv("nodecount") == null){
			System.out.println("node count from environement variable is null");
			threadCount=5;
		}
		else{
			threadCount = Integer.parseInt(System.getenv("nodecount"));
			log.info("node count from environement variable is "+threadCount);
		}

		if (!testorlive.equalsIgnoreCase("${environment}"))
			TestorLive = testorlive.trim();
		String Execution_Type_temp ;
		if (!executiontype.equalsIgnoreCase("${executiontype}")){
			Execution_Type = executiontype.trim();		

			Execution_Type_temp  =Execution_Type.trim();
			if(Execution_Type_temp.split("-").length==1)
				Execution_Type = Execution_Type_temp;
			else if(Execution_Type_temp.split("-").length==2){
				Execution_Type = Execution_Type_temp.split("-")[0];
				int browserCount = Integer.parseInt(Execution_Type_temp.split("-")[1]);				
				threadCount = browserCount*threadCount;
			}
			else if(Execution_Type_temp.split("-").length>=3){
				Execution_Type = Execution_Type_temp.split("-")[0];
				int browserCount = Integer.parseInt(Execution_Type_temp.split("-")[1]);				
				threadCount = browserCount*threadCount;
				retry = "true";
			}
			else
				log.info("Seems environment you passed is not proper taking default value");

		}

		try {
			File configfile = new File("Config.properties");
			FileWriter fileWritter = new FileWriter(configfile, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.newLine();
			bufferWritter.append("TestorLive=" + TestorLive);
			bufferWritter.newLine();
			bufferWritter.append("Execution_Type=" + Execution_Type);
			bufferWritter.newLine();
			bufferWritter.append("retry=" + retry);
			bufferWritter.close();
			fileWritter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
