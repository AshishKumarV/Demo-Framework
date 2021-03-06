package tv.accedo.TVAndroid.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;

import tv.accedo.TVAndroid.Utils.BaseUtil;

@Component
public class S3Helper extends BaseUtil
{
	
	
    String clientRegion = "*** Client region ***";
    String keyName = "*** Key name ***";
    String filePath = "*** Path to file to upload ***";
    
    File file = new File(filePath);
    long contentLength = file.length();
    long partSize = 5 * 1024 * 1024;
    // Set part size to 5 MB. 
    
    @Value("${AWSAccessKeyId}")
	private String AWSAccessKeyId;
    
    @Value("${AWSSecretKey}")
   	private String AWSSecretKey;
    
    @Value("${bucketName}")
   	private String bucketName;
    
    public void upload()
    {

	    try {
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	                                .withRegion(clientRegion)
	                                .withCredentials(new ProfileCredentialsProvider())
	                                .build();
	                   
	        // Create a list of ETag objects. You retrieve ETags for each object part uploaded,
	        // then, after each individual part has been uploaded, pass the list of ETags to 
	        // the request to complete the upload.
	        List<PartETag> partETags = new ArrayList<PartETag>();
	
	        // Initiate the multipart upload.
	        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
	        InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
	
	        // Upload the file parts.
	        long filePosition = 0;
	        for (int i = 1; filePosition < contentLength; i++) {
	            // Because the last part could be less than 5 MB, adjust the part size as needed.
	            partSize = Math.min(partSize, (contentLength - filePosition));
	
	            // Create the request to upload a part.
	            UploadPartRequest uploadRequest = new UploadPartRequest()
	                    .withBucketName(bucketName)
	                    .withKey(keyName)
	                    .withUploadId(initResponse.getUploadId())
	                    .withPartNumber(i)
	                    .withFileOffset(filePosition)
	                    .withFile(file)
	                    .withPartSize(partSize);
	
	            // Upload the part and add the response's ETag to our list.
	            UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
	            partETags.add(uploadResult.getPartETag());
	
	            filePosition += partSize;
	        }
	
	        // Complete the multipart upload.
	        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName,
	                initResponse.getUploadId(), partETags);
	        s3Client.completeMultipartUpload(compRequest);
	    }
	    catch(AmazonServiceException e1) {
	        // The call was transmitted successfully, but Amazon S3 couldn't process 
	        // it, so it returned an error response.
	        e1.printStackTrace();
	    }
	    catch(SdkClientException e) {
	        // Amazon S3 couldn't be contacted for a response, or the client
	        // couldn't parse the response from Amazon S3.
	        e.printStackTrace();
	    }
    }
    
    
	

    public void uploadToS3(String fileName)
    {
    	//AWSCredentials credentials = new BasicAWSCredentials("AKIAJPERBZI6Q5R5RFTQ", "VNFy+fvX65RtIgKPiOGF+Kn09ZqxQCehQ9pyYpZY");
    	AWSCredentials credentials = new BasicAWSCredentials("AKIAIUMDCUGWDS6T555Q", "NYgD87ESq5GnJJnWbmx269TGf7s8j7oKOg3ELbAI");
    	AmazonS3 s3client = new AmazonS3Client(credentials);
    	s3client.putObject(new PutObjectRequest("wynkqa", "reports/"+fileName, 
    			new File(System.getProperty("user.dir")+"/"+fileName)));
//    	AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKey);
//    	AmazonS3 s3client = new AmazonS3Client(credentials);
//    	s3client.putObject(new PutObjectRequest(bucketName, fileName, 
//    											new File(System.getProperty("user.dir")+"/"+fileName)));
    }
    
    public void test()
    {
    	System.out.println(AWSAccessKeyId);
    }
    
 
    
    
}

