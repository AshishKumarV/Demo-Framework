package tv.accedo.TVAndroid.Report;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import org.zeroturnaround.zip.ZipUtil;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.BaseUtil;
import tv.accedo.TVAndroid.Utils.S3Helper;

public class ExtentReporter implements IReporter {
	private ExtentReports extent;
	private AndroidDriver androidDriver;
	BaseUtil util = new BaseUtil();
	S3Helper s3Helper = new S3Helper();
	DateFormat df = new SimpleDateFormat("dd_MM_yyyy");
	String data = df.format(new Date());
	public static int testcasesCount;
	public static int passedTestcasesCount;
	public static int failedTestcasesCount;
	public static int skippedTestcasesCount;

	private static final Logger LOGGER = Logger.getLogger(ExtentReporter.class);
	static boolean exceptionPresent =  false;
	

	public static void main(String args[]) throws Exception {
		ExtentReporter e = new ExtentReporter();
	}

	public static String getPropertyFromConfig(String property) {
		BaseUtil util = new BaseUtil();
		return util.getPropertyValue("/config.properties", property);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		LOGGER.info("Extent start time" + sdf.format(cal.getTime()));

		extent = new ExtentReports("./finalReport_" + data + "/TestExecutionReport.html", true);
		Map sysInfo = new HashMap();
		String projectName = ExtentReporter.getPropertyFromConfig("testProject");
		String environment = ExtentReporter.getPropertyFromConfig("TestorLive");
		sysInfo.put("Project", projectName);
		sysInfo.put("Environment", environment);
		extent.addSystemInfo(sysInfo);

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {

				ITestContext context = r.getTestContext();
				System.out.println("EXTENT REPORT : " + context.getName());
				LOGGER.info("EXTENT REPORT tc name : " + context.getName() + " context : " + context.toString());
				context.getAllTestMethods()[0].getDescription();
				try {
					buildTestNodes(context.getPassedTests(), LogStatus.PASS, projectName, environment);
					buildTestNodes(context.getFailedTests(), LogStatus.FAIL, projectName, environment);
					buildTestNodes(context.getSkippedTests(), LogStatus.SKIP, projectName, environment);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		LOGGER.info("Extent End time" + sdf.format(cal.getTime()));
		extent.flush();
		extent.close();
		ZipUtil.pack(new File(System.getProperty("user.dir") + "/" + "finalReport_" + data),
				new File(System.getProperty("user.dir") + "/" + "finalReport_" + data + ".zip"));
		// s3Helper.uploadToS3("finalReport_"+data+".zip");
		sendMail();
	}
	
	
	public void setExceptionFlag(boolean flag) {
		System.out.println("Setting Exception flag "+flag);
		exceptionPresent= flag;
	}
	

	private void buildTestNodes(IResultMap tests, LogStatus status, String projectName, String environment)
			throws MalformedURLException {
		ExtentTest test;
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {

				String packageName = result.getMethod().getTestClass().getName();
				String methodName = result.getMethod().getMethodName();
				String className = result.getMethod().getTestClass().getName();

				ExcelExecutionManager xlmanager = new ExcelExecutionManager();
				Date date = new Date();
				Timestamp ts_now = new Timestamp(date.getTime());

				String finalDesc = xlmanager.addDescription(packageName + "." + className);
				String testClassName = result.getMethod().getTestClass().getName();
				String testMethodName = result.getMethod().getMethodName();

				try {
					LOGGER.info("1." + testMethodName);
					int arrlen = testClassName.split("\\.").length;
					LOGGER.info("2." + testClassName.split("\\.")[arrlen - 1]);
					String testProject = this.getPropertyFromConfig("testProject");
					LOGGER.info("testProject = " + testProject);
					LOGGER.info("3." + testProject);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				LOGGER.info(result.getMethod().getTestClass().getName());
				LOGGER.info(result.getMethod().getMethodName());

				test = extent.startTest(testMethodName);
				LOGGER.info("classname is:" + testClassName.split("\\.").length);
				int len = testClassName.split("\\.").length;
				// test.assignCategory(result.getMethod().getTestClass().toString());

				if (len > 0)
					test.assignCategory(testClassName.split("\\.")[len - 1]);
				else
					test.assignCategory(testClassName);

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				Object currentClass = result.getMethod().getInstance();
				androidDriver = ((BaseUtil) currentClass).getDriver();
				// test.log(LogStatus.INFO, util.deviceName(testClassName));

				if (result.getMethod().getDescription() == null) {
				} else {
					String testcase[] = result.getMethod().getDescription().split(",");
					for (int i = 0; i < testcase.length; i++) {
						testcasesCount++;
					}
					extent.addSystemInfo("Test Cases Count", String.valueOf(testcasesCount));

					if (status.equals(LogStatus.PASS)) {
						for (int i = 0; i < testcase.length; i++) {
							passedTestcasesCount++;
						}
						extent.addSystemInfo("Passed Test Cases Count", String.valueOf(passedTestcasesCount));
					}

					if (status.equals(LogStatus.FAIL)) {
						for (int i = 0; i < testcase.length; i++) {
							failedTestcasesCount++;
						}
						extent.addSystemInfo("Failed Test Cases Count", String.valueOf(failedTestcasesCount));
					}

					if (status.equals(LogStatus.SKIP)) {
						for (int i = 0; i < testcase.length; i++) {
							skippedTestcasesCount++;
						}
						extent.addSystemInfo("Skipped Test Cases Count", String.valueOf(skippedTestcasesCount));
					}
					test.log(LogStatus.INFO, "Manual Test Cases", result.getMethod().getDescription());
					File directory1 = new File("./finalReport_"+data);
					DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy");

					String dateName = dateFormat.format(date);
					
					if(exceptionPresent) {
					
						String NewLogFileNamePath = null;
						String NewLogFilePathName = null;
						try {
							NewLogFileNamePath = directory1.getCanonicalPath() + "/logs/" + result.getMethod().getMethodName()+"___" + dateName + "_"+ ".txt";
							NewLogFilePathName = "/logs/" +  result.getMethod().getMethodName()+"___" + dateName + "_"+ ".txt";
							System.out.println("COPYING FILE to "+ NewLogFileNamePath);
							File f = new File(System.getProperty("user.dir")+"/logs/crashLogs.txt");
							FileUtils.copyFile(f, new File(NewLogFileNamePath));
							
							
//							NewLogFileNamePath = directory1.getCanonicalPath() + "/logs/" + result.getMethod().getMethodName()+"___" + dateName + "_"+ ".txt";
//							NewLogFilePathName = "<a href=" + NewLogFileNamePath + " target='_blank'>Click Here</a>";
							
							//NewLogFileNamePath = "<a href=." + NewLogFilePathName + " target='_blank'>Click Here</a>";
						} catch (IOException e) {
							e.printStackTrace();
						}
						test.log(LogStatus.INFO, "LOGS",NewLogFileNamePath);
					}

				}

				if (result.getThrowable() != null) {

					if (result.getThrowable().toString().contains("java.lang.AssertionError")) {
						test.log(LogStatus.INFO, result.toString().split("output=")[1]);
						test.log(LogStatus.INFO, finalDesc);
						test.log(status, result.getThrowable());

					} else {
						test.log(LogStatus.INFO, result.toString().split("output=")[1]);
						test.log(LogStatus.INFO, finalDesc);
						test.log(status, result.getThrowable());
					}
				} else {
					test.log(status, "Test " + status.toString().toLowerCase() + "ed");
					test.log(LogStatus.INFO, finalDesc);
					// test.log(LogStatus.INFO, result.toString());

				}
				extent.endTest(test);
			}

		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public void sendMail() {
		String recipient = "testing.bsb@gmail.com";

		// email ID of Sender.
		String sender = "testing.bsb@gmail.com";

		// using host as localhost
		String host = "127.0.0.1";
		String password = "adgjtesting";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Automation result : " + new Date());
			message.setText("Link for report folder : https://s3.ap-south-1.amazonaws.com/wynkqa/reports/finalReport_"
					+ data
					+ ".zip \n \n LOGS location - https://vpc-wynkmusic-tw6bcbgcld5jmyz7ob4r7aa5lm.ap-south-1.es.amazonaws.com/_plugin/kibana/app/kibana#/discover?_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now%2FM,mode:quick,to:now%2FM))&_a=(columns:!(message,data,deviceId),index:\'54e312a0-7e93-11e8-91ca-efb82a41d20c\',interval:auto,query:(language:lucene,query:\'\'),sort:!(\'@timestamp\',desc))");
			Transport.send(message);
			System.out.println("Mail successfully sent");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	

}
