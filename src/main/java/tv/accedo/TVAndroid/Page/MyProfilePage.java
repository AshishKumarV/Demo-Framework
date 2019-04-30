package tv.accedo.TVAndroid.Page;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.BaseUtil;


public class MyProfilePage extends BaseUtil
{

	@FindBy(id="et_phone")
	public WebElement phone;
	
	@FindBy(id="et_name")
	public WebElement name;
	
	@FindBy(id="et_name")
	public WebElement email;
	
	@FindBy(id="tv_dialog_message")
	public WebElement dialogBox;
	
	
	@FindBy(id="iv_close_dialog")
	public WebElement close;
	
	@FindBy(id="btn_dialog_2")
	public WebElement registerOK;
	
	@FindBy(id="tv_dialog_title")
	public WebElement regPopperTitle;

	
	List<WebElement> popUpListElement ;
	

	public MyProfilePage(AndroidDriver androidDriver)
	{
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}
	
	public  boolean verifyAllSectionIsPresent()
	{
		boolean flag = false;
		if(phone.isDisplayed() && name.isDisplayed() && email.isDisplayed())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getRegisterPopperTitle() {
		return regPopperTitle.getText();
	}
	
	public void closeRegisterPopper() {
		registerOK.click();
	}
}
