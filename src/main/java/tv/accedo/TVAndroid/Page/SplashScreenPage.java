package tv.accedo.TVAndroid.Page;

import org.mortbay.log.Log;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import tv.accedo.TVAndroid.Utils.BaseUtil;

import io.appium.java_client.android.AndroidDriver;

public class SplashScreenPage extends BaseUtil {

	private AndroidDriver<WebElement> androidDriver;

	public SplashScreenPage(AndroidDriver androidDriver)
	{
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}

	@FindBy(id="tv_continue")
	public WebElement continueButton;

	@FindBy(id="com.android.packageinstaller:id/permission_allow_button")
	public WebElement permissionAllowButton;

	@FindBy(id="com.android.packageinstaller:id/permission_deny_button")
	public WebElement permissionDenyButton;

	@FindBy(id="com.android.packageinstaller:id/permission_message")
	public WebElement permissionMessage;

	@FindBy(id="com.android.packageinstaller:id/current_page_text")
	public WebElement permissionDialogBoxNumber;

	@FindBy(id="iv_close")	
	public WebElement closeButton;

	@FindBy(id="tv_line1")
	public WebElement splashTitle;

	@FindBy(id="permission_title")
	public WebElement permissionTitle;

	@FindBy(id="tv_read_phone_state")
	public WebElement phoneCallsPermission;

	@FindBy(id="tv_external_storage")
	public WebElement mediaPermission;	

	@FindBy(id="tv_privacy_agree")
	public WebElement termsOfUsePrivacyPolicyButton;

	@FindBy(id="contentWrapper")
	public WebElement termsOfUsePrivacyPolicyPage;

	@FindBy(id="action_close")
	public WebElement closeButtonOnWebView;

	@FindBy(id="tv_permission_error")
	public WebElement permissionErrorMessage;

	public void clickContinueButton(){
		clickOnWebElement(androidDriver, continueButton);
	}

	public boolean isVisibleContinueButton(){
		try{
			WaitForElement(androidDriver, continueButton);
			return true;
		}
		catch(NoSuchElementException | org.openqa.selenium.TimeoutException e){
			return false;
		}
	}
	
	public void clickPermissionAllowButton(){
		clickOnWebElement(androidDriver, permissionAllowButton);
	}

	public void clickPermissionDenyButton(){
		clickOnWebElement(androidDriver, permissionDenyButton);
	}

	public String getTextPermissionMessage(){
		return this.permissionMessage.getText();
	}

	public String getTextPermissionDialogBoxNumber(){
		return this.permissionDialogBoxNumber.getText();
	}

	public String getTextSplashTitle(){
		return this.splashTitle.getText();
	}

	public String getTextPermissionTitle(){
		return this.permissionTitle.getText();
	}

	public String getTextPhoneCallsPermission(){
		return this.phoneCallsPermission.getText();
	}

	public String getTextMediaPermission(){
		return this.mediaPermission.getText();
	}

	public void launchAndDirectlyGoToHomePage(String deviceId){
//		clickOnContinueButtonOnStartUp();
		if(getDeviceOsVersion(deviceId)>22){
			this.clickPermissionAllowButton();
			this.clickPermissionAllowButton();
			this.clickPermissionAllowButton();
		}
	
	}

	public void clickOnContinueButtonOnStartUp(){
		clickOnWebElement(androidDriver,continueButton);
	}

	public void closePopUp(){
		try{
			clickOnWebElement(androidDriver,closeButton);
		}catch(TimeoutException e){
			Log.info("pop up not shown");
		}
	}

	public void clickTermsOfUse(){
		scrollToElementByVisibleTextAndClick(androidDriver,"Terms of Use");
	}

	public void clickPrivacyPolicy(){
		scrollToElementByVisibleTextAndClick(androidDriver,"Privacy Policy");
	}

	public Boolean isVisible_TermsOfUsePrivacyPolicyPage(){
		return termsOfUsePrivacyPolicyPage.isDisplayed();
	}

	public void clickCloseButtonOnWebView(){
		clickOnWebElement(androidDriver,closeButtonOnWebView);
	}

	public String getTextPermissionErrorMessage(){
		return this.permissionErrorMessage.getText();
	}



}
