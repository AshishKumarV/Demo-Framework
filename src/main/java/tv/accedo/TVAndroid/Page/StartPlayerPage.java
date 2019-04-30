package tv.accedo.TVAndroid.Page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.BaseUtil;

public class StartPlayerPage extends BaseUtil{
	
	@FindBy(id = "actionBtnD")
    public WebElement register;
    
    @FindBy(id = "you_receive_pin")
    public WebElement mobOTP;
    
    @FindBy (id = "edit_phone_number")
    public WebElement enterMobNo;
    
    @FindBy (id = "btn_next")
    public WebElement continueBtn;
    
    @FindBy (id = "you_receive_pin")
    public WebElement receiveOPT;
    
    @FindBy (id = "edit_pin_code")
    public WebElement enterOTP;
    
    @FindBy (id = "verify_btn")
    public WebElement verifyBtn;
    
    @FindBy(id="confirmBtn")
	public WebElement confirmBtn;
    
    @FindBy(id="playerloading")
	public WebElement loading;
    
    @FindBy(id="progressLoader")
	public WebElement loader;
    
    int i,j,k=0;
    List<Long> startTime = new ArrayList<>();
    List<Long> endTime = new ArrayList<>();
    
    CommonPages common = new CommonPages(androidDriver);
    
    public void playerLoading() {
    	long strTime= System.currentTimeMillis();
        long i5Minutes = 5 * 60 * 1000;
        
        while(System.currentTimeMillis() < (strTime + i5Minutes)) {
//        while (androidDriver.findElement(By.id("ivShareTextChannel")).isDisplayed()) {
        	if (common.isWebElementDisplayed(loading)) {
        		System.out.println("time of player start: "+System.currentTimeMillis() );
        		startTime.add(System.currentTimeMillis());
        		
        		try {
        			while(common.isWebElementDisplayed(loading)) {
            			System.out.println("Player is starting...");
            		}
        		}
        		catch (Exception e) {
        			System.out.println("time of player end: "+System.currentTimeMillis() );
        			endTime.add(System.currentTimeMillis());
				}
        		
        		
        		
        		
        	}else if(common.isWebElementDisplayed(loader)) {
        		System.out.println("time of player laoder start: "+System.currentTimeMillis() );
        		startTime.add(System.currentTimeMillis());
        		
        		try {
        			while(common.isWebElementDisplayed(loader)) {
            			System.out.println("Player is in loading state...");
            		}
        		}
        		catch (Exception e) {
        			System.out.println("time of player loader end: "+System.currentTimeMillis() );
        			endTime.add(System.currentTimeMillis());
				}
   
        		
        	}
        }
        for (long i :startTime) {
        	System.out.print("Loading Starts on following time :" +i +",");
        }
        
        for (long j : endTime) {
        	System.out.print("Loading Ends on following time : "+ j + ",");
        }
    }
    
    public void launchLiveTV()throws InterruptedException {
    	register.click();
    	Thread.sleep(2000);
    	enterMobNo.sendKeys("1234123412");
    	continueBtn.click();
    	enterOTP.sendKeys("1234");
    	Thread.sleep(2000);
    	verifyBtn.click();
    	confirmBtn.click();
    	androidDriver.navigate().back();
    	
    	
    }
}
