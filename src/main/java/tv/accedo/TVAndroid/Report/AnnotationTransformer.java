package tv.accedo.TVAndroid.Report;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.IAnnotationTransformer;


public class AnnotationTransformer implements IAnnotationTransformer {
	
	//@Override
	public void transform(ITestAnnotation testAnnotation, Class clazz, Constructor testConstructor,Method method) {
		testAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
}