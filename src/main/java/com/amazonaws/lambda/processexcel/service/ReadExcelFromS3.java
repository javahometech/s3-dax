package com.amazonaws.lambda.processexcel.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ReadExcelFromS3 {

	public static Map<String, String> processsExcelRead(InputStream excelStream) {
		HSSFWorkbook workbook;
		Map<String, String> data = new HashMap<String, String>();

		try {
			workbook = new HSSFWorkbook(excelStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			while(iterator.hasNext()) {
				Row row = iterator.next();
				String key = row.getCell(0).getStringCellValue();
				String value = row.getCell(1).getStringCellValue();
				data.put(key, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
