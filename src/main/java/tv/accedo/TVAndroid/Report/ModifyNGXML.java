package tv.accedo.TVAndroid.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ModifyNGXML {

	static Logger log = LoggerFactory.getLogger(ModifyNGXML.class);
	public static String  getcurrentDirectory(){
		String directory = System.getProperty("user.dir");

		String mydirectory = directory;		
		mydirectory= mydirectory.replaceAll("/", ",");			
		int pathDepth =  mydirectory.split(",").length;
		return mydirectory.split(",")[pathDepth-1];

	}

	private static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			te.printStackTrace();
		}
		return sw.toString();
	}

	private static String getSkiporFail(Node node){
		NamedNodeMap exception_attr = node.getAttributes();
		Node exception_node_attr = exception_attr
				.getNamedItem("class");

		if ("java.lang.AssertionError".equals(exception_node_attr.getNodeValue())) {

			return "Fail";
		} else{
			return "Skip";
		}

	}



	public static void main(String argv[]) {

		try {
			File file = new File("test-output/testng-results.xml");
			log.info(file.getAbsolutePath());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			InputStream inputStream = new FileInputStream(
					file.getAbsolutePath());
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			Document doc = docBuilder.parse(is);
			Element eleme = (Element) (doc.getElementsByTagName("testng-results").item(0));
			log.info("F "+eleme.getAttributes().getNamedItem("failed").getNodeValue());
			log.info("P "+eleme.getAttributes().getNamedItem("passed").getNodeValue());
			log.info("S "+eleme.getAttributes().getNamedItem("skipped").getNodeValue());
			log.info("T "+eleme.getAttributes().getNamedItem("total").getNodeValue());

			String passCntStr = eleme.getAttributes().getNamedItem("passed").getNodeValue();

			int passCnt          =             Integer.parseInt(passCntStr);

			String totalCntStr = eleme.getAttributes().getNamedItem("total").getNodeValue();
			int totalCnt         =             Integer.parseInt(totalCntStr);

			float passPercent =(passCnt*100/totalCnt);
			NodeList testmethods = doc.getElementsByTagName("test-method");

			for (int j = 0; j < testmethods.getLength(); j++) {
				NodeList list1 = testmethods.item(j).getChildNodes();

				String parameters="";
				String fullstacktrace="";
				for (int i = 0; i < list1.getLength(); i++) {

					Node node = list1.item(i);




					if ("params".equals(node.getNodeName())) {   
						parameters =nodeToString(node);
					}else if ("exception".equals(node.getNodeName())) {

						String typeOfFailure = getSkiporFail(node);

						if (typeOfFailure.equalsIgnoreCase("skip")) {
							NamedNodeMap attr = testmethods.item(j)
									.getAttributes();
							Node nodeAttr = attr.getNamedItem("status");
							nodeAttr.setNodeValue("SKIP");
//							nodeAttr.setTextContent("SKIP");
							Element ro = doc.createElement("reporter-output");
							String act = "%3c![CDATA[[Status]:TestFour_SKIPPED_RUNTIME]]%3e";
							String result = java.net.URLDecoder.decode(act,
									"UTF8");
							ro.appendChild(doc.createTextNode(result));
							testmethods.item(j).appendChild(ro);
							break;
						}
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file.getAbsolutePath());
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
}