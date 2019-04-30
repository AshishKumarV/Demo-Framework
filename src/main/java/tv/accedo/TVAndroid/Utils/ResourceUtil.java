package tv.accedo.TVAndroid.Utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceUtil {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties(){
	   PropertySourcesPlaceholderConfigurer pspc =
	      new PropertySourcesPlaceholderConfigurer();
	   Resource[] resources = new ClassPathResource[ ]
	      { new ClassPathResource( "testData.properties" ) };
	  pspc.setLocations( resources );
	  pspc.setIgnoreUnresolvablePlaceholders( true );
	  return pspc;
	}

}
