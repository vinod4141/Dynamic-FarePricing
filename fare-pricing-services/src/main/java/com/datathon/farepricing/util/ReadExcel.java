/**
 * 
 */
package com.datathon.farepricing.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.datathon.farepricing.model.AlertDetail;

/**
 * @author Vinod
 *
 */
public class ReadExcel 
{
    public static List<AlertDetail> readExcel() 
    {
    	List<AlertDetail> alerts = new ArrayList<AlertDetail>();
        try
        {
            FileInputStream file = new FileInputStream(new File("D:/vinod/Alerts_Excel.xlsx"));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
           // Iterator<Row> rowIterator = sheet.iterator();
            
            int rows = sheet.getPhysicalNumberOfRows();
            
            
            AlertDetail alert;
            for ( int i =1; i<rows; i++)
            {
            	alert = new AlertDetail();
                Row row = sheet.getRow(i);
                //For each row, iterate through all the columns
               // Iterator<Cell> cellIterator = row.cellIterator();
                
                System.out.println(row.getCell(1).getStringCellValue());
                System.out.println(row.getCell(2).getStringCellValue());
               /* row.getCell(3).getStringCellValue();
                row.getCell(4).getStringCellValue();
                row.getCell(5).getStringCellValue();*/
                
                alert.setAlertId(String.valueOf(row.getCell(0).getNumericCellValue()));
                alert.setOrigin(row.getCell(1).getStringCellValue());
                alert.setDestination(row.getCell(2).getStringCellValue());
                alert.setCarrier(row.getCell(3).getStringCellValue());
                alert.setPos(row.getCell(4).getStringCellValue());
                alert.setCompartment(row.getCell(5).getStringCellValue());
                alert.setFareClass(row.getCell(6).getStringCellValue());
                alert.setFareBasis(row.getCell(7).getStringCellValue());
                alert.setAvailableFare(row.getCell(16).getNumericCellValue());
                alert.setPreviousAvailableFare(row.getCell(17).getNumericCellValue());
                alert.setDifferencetoEK(row.getCell(18).getNumericCellValue());
                
                /*while (cellIterator.hasNext()) 
                {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    cell.
                    switch (cell.getCellType()) 
                    {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "t");
                            break;
                    }
                }*/
                System.out.println("");
                alerts.add(alert);
            }
            workbook.close();
            file.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return alerts;
    }
}
