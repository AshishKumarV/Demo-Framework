package tv.accedo.TVAndroid.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

public class PropertyInjectorListener implements IAlterSuiteListener {
    @Override
    public void alter(List<XmlSuite> suites) {
        XmlSuite suite = suites.get(0);
        System.out.println("suite file"+suite.getFileName());
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(System.getProperty("user.dir")+"/src/main/resources/configuration.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<Object, Object> each : properties.entrySet()) {
            params.put(each.getKey().toString(), each.getValue().toString());
           

        }
        suite.setParameters(params);
    }
}