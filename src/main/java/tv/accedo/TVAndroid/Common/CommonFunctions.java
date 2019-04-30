package tv.accedo.TVAndroid.Common;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.grizzly.streams.BufferedInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;


@Component
public class CommonFunctions {
	
	public static final Logger LOGGER =  Logger.getLogger(CommonFunctions.class);
	
	@Value("{$rentedPath}")
	private static String rentedPath;
	
	@Value("{$rentedPath0}")
	private static String rentedPath0;
	
	
	public static HashMap<String,LinkedList<String>> eventExcelMap = new HashMap<String,LinkedList<String>>();	
	public String filePath = System.getProperty("user.dir")+"/src/main/resources/"+"EventStats.xlsx";
	
	
	public void writeFile(String path,String content){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter( new FileWriter(path,false));
			writer.write(content);
		} catch ( IOException e){
            System.out.println("exception in writing to file to "+path+ " "+ e);
		}
		finally {
			try{
				if ( writer != null)
					writer.close( );
			} catch ( IOException e){
				 LOGGER.info("exception in closing file to "+path+ " "+ e);
			}
		}
	}
	
	 public void writeExcel(String sheetName,String difference,int rowNo) throws IOException
	 {
		LOGGER.info("writing into file");

	    FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir")+"/src/main/resources/"+"EventStats.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook (fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(rowNo);
		//for adding difference to next line
		//Cell cell = row.createCell(row.getLastCellNum());
		
		Row row1 = sheet.getRow(0);
		int differenceColNo = 4;
		
		for(int i = 0; i<row1.getLastCellNum(); i++) {
			if(row1.getCell(i).toString().equalsIgnoreCase("Difference")){
				differenceColNo = i;
			}
		}
		
		Cell cell = row.createCell(differenceColNo);
		cell.setCellValue(difference);
		
		fis.close();
		FileOutputStream fos =new FileOutputStream(System.getProperty("user.dir")+"/src/main/resources/"+"EventStats.xlsx");
	    workbook.write(fos);
	    fos.close();
	    
	    LOGGER.info("writing into excel is completed");
	
	 }
	 
	 
	 public void clearAllDifferenceColumn(String sheetName) throws IOException {
		FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir")+"/src/main/resources/"+"EventStats.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook (fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		LOGGER.info("CLEAR ALL DIFF column");
		
		int diff=4;
		Row row  = sheet.getRow(0);
		for(int j = 0; j<row.getLastCellNum(); j++) {
			if(row.getCell(j).toString().equalsIgnoreCase("Difference")){
				diff = j;
			}
		}
		
		for(int i = 1 ;i< sheet.getLastRowNum();i++) {
			Row row1  = sheet.getRow(i);
			Cell cell = row1.createCell(diff);
			cell.setCellValue((String) null);
			cell.setCellType(Cell.CELL_TYPE_BLANK);
			row1.removeCell(cell);
		}	
		
		fis.close();
		FileOutputStream fos =new FileOutputStream(System.getProperty("user.dir")+"/src/main/resources/"+"EventStats.xlsx");
	    workbook.write(fos);
	    fos.close();
	 }
	 
	
	public  void readExcel(String sheetName) {
		FileInputStream fs = null;
		XSSFWorkbook workBook = null;
		BufferedInputStream bs = null;
		LinkedList<String> eventData = null;
		
		try {
			fs = new FileInputStream(filePath);
			 bs = new BufferedInputStream(fs);
			workBook = new XSSFWorkbook(bs);
		} catch (IOException e) {
			LOGGER.info("Exception while getting worksheet : "+ e);	
		}
		
		XSSFSheet sheet= workBook.getSheet(sheetName);
		HashMap<String,Integer> columnMapping = new HashMap<String,Integer>();
		Row row = sheet.getRow(0);
		
		for(int i =0; i<row.getLastCellNum(); i++) {
			System.out.println("E! "+row.getCell(i));
			columnMapping.put(row.getCell(i).toString(),i);
		}
		
		for(int i= 0;i<=sheet.getLastRowNum();i++) {
			eventData = new LinkedList<String>();
			eventData.add(String.valueOf(i));
			eventData.add(sheet.getRow(i).getCell(columnMapping.get("Event")).toString());
			eventData.add(sheet.getRow(i).getCell(columnMapping.get("Mandatory_List")).toString());
			eventExcelMap.put(sheet.getRow(i).getCell(columnMapping.get("Event_Name")).toString(), eventData);
		}	
		try {
			bs.close();
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getEvent(String eventType) {
		return eventExcelMap.get(eventType).get(1);
	}
	
	
	public List<String> getEventMandatoryList(String eventType) {
		List<String> mandatoryFieldList = new ArrayList<String>();
		mandatoryFieldList = Arrays.asList(eventExcelMap.get(eventType).get(2).split(","));
		return mandatoryFieldList;
	}
	
	
	public int getRowNo(String eventType) {
		return Integer.parseInt(eventExcelMap.get(eventType).get(0));
	}
	
	
	public String getRentedPath(String deviceName) {
		switch(deviceName) {
			case "ZY223K2BBZ":
				return rentedPath0;
			default:
				return rentedPath;
		}
	}






}
