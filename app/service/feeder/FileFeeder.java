package service.feeder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import models.Component;
import models.ComponentTrademark;
import models.ComponentType;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class FileFeeder {

	public static void process(File food) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook( new FileInputStream(food));
			HSSFSheet sheet = workbook.getSheetAt(0);
			for (Iterator rowIterator = sheet.iterator(); rowIterator.hasNext();) {
				Row row = (Row) rowIterator.next();
				process(row);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void process(Row row) {
		if (row.getRowNum() > 0 ) {

			String model = StringUtils.EMPTY;
			String type = StringUtils.EMPTY;
			String partNumber = StringUtils.EMPTY;
			String trademark = StringUtils.EMPTY;
			for (Iterator cellIterator = row.iterator(); cellIterator.hasNext();) {
				Cell cell = (Cell)cellIterator.next();
				switch (cell.getColumnIndex()) {
				case 3 :
					partNumber = getValueOfCell(cell).trim();
					break;
				case 4:
					trademark = getValueOfCell(cell).trim();
					break;
				case 5:
					model =  StringUtils.upperCase(getValueOfCell(cell)).trim();
					break;
				case 7:
					type =  getValueOfCell(cell).trim();
				default:
					break;
				}
			}
			
			Component component = new Component();
			component.model = model;
			component.partNumber = partNumber;
			
			List<ComponentTrademark> componentTrademarks = ComponentTrademark.find("byDescription", trademark).fetch();
			ComponentTrademark componentTrademark;
			if (componentTrademarks == null || componentTrademarks.isEmpty() ) {
				componentTrademark = new ComponentTrademark();
				componentTrademark.description = trademark;
			} else {
				componentTrademark = componentTrademarks.get(0);
			}
			component.trademark = componentTrademark;
			List<ComponentType> componentTypes = ComponentType.find("byDescription", type).fetch();
			ComponentType componentType;
			if(componentTypes == null || componentTypes.isEmpty()) {
				componentType = new ComponentType();
				componentType.description = type;
			} else {
				componentType = componentTypes.get(0);
			}
			component.type = componentType;
			
			
			List<Component> components = Component.find("select c from Component c," +
					"								   ComponentTrademark tr, ComponentType ty " +
					"						where c.trademark = tr and c.type = ty and tr.description = ? and" +
					" 							  c.model = ? and ty.description = ?", trademark, model, type).fetch();
			
			if (components == null || components.isEmpty()) {
				component.save();
			}
		}
		
	}

	private static String getValueOfCell(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return StringUtils.EMPTY;
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		default:
			return StringUtils.EMPTY;
		}
	}

}