package com.zm.redqueen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * This class generates nsted JSOn objects using the special format excel sheets
 * 
 * @author AjanthanSivalingaraj
 *
 */
public class ExcelToNestedJSONObject {
	private static Logger log = Logger.getLogger(ExcelToNestedJSONObject.class.getName());

	enum SHEET_NAME {
		Outlets, Outlet_users, Suppliers, Supplier_Users, Def_Del_Pref, 
		Outlet_Del_Pref, Products, Products_Dual_UOM, Products_Tripple_UOM, 
		Market_List, Market_List_Dual_UOM
	};

	public static void main(String[] args) throws JSONException {
		// ClassLoader classLoader = ExcelToNestedJSONObject.class.getClassLoader();
		String baseFileDir = "C:/Dev/Docs/Migration/Roo's/1.1/";
		File file = new File(baseFileDir + "Phoenix_Data_WO_Formulas_v1.0.xlsx");

		String sheetName = SHEET_NAME.Market_List_Dual_UOM.name();
		log.info(file.getPath());

		try {

			FileInputStream excelFile = new FileInputStream(file);
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheet(sheetName); // sheet name

			Iterator<Row> iterator = datatypeSheet.iterator();
			File fout = new File(baseFileDir + sheetName + ".json"); // json outputfile name
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			JSONArray dataArray = new JSONArray();

			JSONArray keyArray = new JSONArray();
			while (iterator.hasNext()) {
				JSONObject rowData = new JSONObject();

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();

					// Key
					if (currentRow.getRowNum() == 0) {
						keyArray.put(currentCell.getStringCellValue());
					}
					// Data
					else {
						String key = keyArray.getString(currentCell.getColumnIndex());
						String[] keyParts = key.split(Pattern.quote("."));
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							String value = currentCell.getStringCellValue();
							if (value.toLowerCase().equals("false")) {
								recursiveObjCreate(rowData, keyParts, false);
							} else if (value.toLowerCase().equals("true")) {
								recursiveObjCreate(rowData, keyParts, true);
							} else {
								recursiveObjCreate(rowData, keyParts, value);
							}
						} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							Double value = currentCell.getNumericCellValue();
							String.format("%1$,.2f", value);
							recursiveObjCreate(rowData, keyParts, value);
						} else if (currentCell.getCellTypeEnum() == CellType.BOOLEAN) {
							Boolean value = currentCell.getBooleanCellValue();
							recursiveObjCreate(rowData, keyParts, value);
						} else if (currentCell.getCellTypeEnum() == CellType.FORMULA) {
							String value = currentCell.getStringCellValue();
							if (value.toLowerCase().equals("false")) {
								recursiveObjCreate(rowData, keyParts, false);
							} else if (value.toLowerCase().equals("true")) {
								recursiveObjCreate(rowData, keyParts, true);
							} else {
								recursiveObjCreate(rowData, keyParts, value);
							}
						} else {
							recursiveObjCreate(rowData, keyParts, "");
						}
					}
				}
				if (currentRow.getRowNum() != 0) {
					dataArray.put(rowData);
				}
			}

			bw.write(dataArray.toString());
			bw.close();

			System.out.println(dataArray.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void recursiveObjCreate(JSONArray parentArray, String[] keyParts, Object value)
			throws JSONException {
		if (keyParts.length == 1) {
			parentArray.put(Integer.parseInt(keyParts[0]), value);
		} else if (keyParts.length > 1) {
			JSONObject innerObj = new JSONObject();
			if (Integer.parseInt(keyParts[0]) < parentArray.length()) {
				innerObj = parentArray.getJSONObject(Integer.parseInt(keyParts[0]));
			} else {
				parentArray.put(Integer.parseInt(keyParts[0]), innerObj);
			}
			recursiveObjCreate(innerObj, Arrays.copyOfRange(keyParts, 1, keyParts.length), value);
		}
	}

	private static void recursiveObjCreate(JSONObject parentObj, String[] keyParts, Object value) throws JSONException {
		if (keyParts.length == 1) {
			parentObj.put(keyParts[0], value);
		} else if (keyParts.length > 1) {
			try {
				int arrayIndex = Integer.parseInt(keyParts[1]);
				JSONArray innerArray = new JSONArray();
				if (parentObj.has(keyParts[0])) {
					innerArray = parentObj.getJSONArray(keyParts[0]);
				} else {
					parentObj.put(keyParts[0], innerArray);
				}
				recursiveObjCreate(innerArray, Arrays.copyOfRange(keyParts, 1, keyParts.length), value);
			} catch (NumberFormatException ex) {
				JSONObject innerObj = new JSONObject();
				if (parentObj.has(keyParts[0])) {
					innerObj = parentObj.getJSONObject(keyParts[0]);
				} else {
					parentObj.put(keyParts[0], innerObj);
				}
				recursiveObjCreate(innerObj, Arrays.copyOfRange(keyParts, 1, keyParts.length), value);
			}
		}
	}
}
