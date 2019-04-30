package tv.accedo.TVAndroid.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;


public class TestNGAssertionsCustom {

	static TakeScreenshot screen;

	public TestNGAssertionsCustom(){
		screen = new TakeScreenshot();
	}

	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();

	private static void assertTrue(boolean condition) {
		Assert.assertTrue(condition);
	}

	private static void assertTrue(boolean condition, String message) {
		Assert.assertTrue(condition, message);
	}

	private static void assertFalse(boolean condition) {
		Assert.assertFalse(condition);
	}

	private static void assertFalse(boolean condition, String message) {
		Assert.assertFalse(condition, message);
	}

	private static void assertEquals(boolean actual, boolean expected) {
		Assert.assertEquals(actual, expected);
	}

	private static void assertEquals(Object actual, Object expected) {
		Assert.assertEquals(actual, expected);
	}

	private static void assertEquals(Object[] actual, Object[] expected) {
		Assert.assertEquals(actual, expected);
	}

	private static void assertEquals(Object actual, Object expected, String message) {
		Assert.assertEquals(actual, expected, message);
	}

	private static void assertFalse(String message){
		Assert.fail(message);
	}

	public static void verifyFalse(String message){
		try{
			assertFalse(message);
		}catch(Throwable e){
			e.printStackTrace();
			addVerificationFailure(e);
		}
	}



	public static void verifyTrue(boolean condition) {
		try {
			assertTrue(condition);
			System.out.println("condition:::"+condition);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static void verifyTrue(boolean condition, String message) {
		try {
			assertTrue(condition, message);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static void verifyFalse(boolean condition) {
		try {
			assertFalse(condition);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public void verifyFalse(boolean condition, String message) {
		try {
			assertFalse(condition, message);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static void verifyEquals(Object actual, Object expected, String message){
		try {
			assertEquals(actual, expected, message);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static void verifyEquals(boolean actual, boolean expected) {
		try {
			assertEquals(actual, expected);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static void verifyEquals(Object actual, Object expected) {
		try {
			assertEquals(actual, expected);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static void verifyEquals(Object[] actual, Object[] expected) {
		try {
			assertEquals(actual, expected);
		} catch(Throwable e) {
			screen.xScreenShot();
			addVerificationFailure(e);
		}
	}

	public static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
	}

	private static void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}



	public static void Print(String Text) {
		System.out.println(Text);
		Reporter.log(Text);
		String Temp = Text;
		TestData.sMessages = TestData.sMessages + Temp.replaceAll(" ", "_") + "#";
	}



}

