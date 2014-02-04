package service.feeder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import models.Component;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void process(Row row) {
//		if (row.getRowNum() > 0 ) {
//			Component component = new Component();
//			for (Iterator cellIterator = row.iterator(); cellIterator.hasNext();) {
//				Cell cell = (Cell)cellIterator.next();
//				switch (cell.getColumnIndex()) {
//				case 3 :
//					component.nroDeParte = getValueOfCell(cell);
//					break;
//				case 4:
//					component.marca =  getValueOfCell(cell);
//					break;
//				case 5:
//					component.modelo =  getValueOfCell(cell);
//					break;
//				case 6:
//					component.submodelo =  getValueOfCell(cell);
//					break;
//				case 7:
//					component.tipo =  getValueOfCell(cell);
//				default:
//					break;
//				}
//			}
//			MorphiaQuery q = Component.createQuery();
//			
//			String marca = component.marca != null ? component.marca :StringUtils.EMPTY;
//			String modelo = component.modelo != null ? component.modelo :StringUtils.EMPTY;
//			String submodelo = component.submodelo != null ? component.submodelo :StringUtils.EMPTY;
//			String tipo = component.tipo != null ? component.tipo :StringUtils.EMPTY;
//			String nroDeParte = component.nroDeParte != null ? component.nroDeParte :StringUtils.EMPTY;
//			
//			q.and(q.criteria("marca").contains(marca), q.criteria("modelo").contains(modelo),
//					q.criteria("submodelo").contains(submodelo ), q.criteria("tipo").contains(tipo),
//					q.criteria("nroDeParte").contains(nroDeParte));
//			List<Model> components = q.asList();
//			if (components == null || components.isEmpty()) {
//				component.save();
//			}
//		}
		
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