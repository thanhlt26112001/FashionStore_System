package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Product;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private CategoriesService categoriesService;

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_DESCRIPTION = 2;
    private static final int COLUMN_CATEGORY_ID = 3;
    private static final int COLUMN_IMAGE = 4;
    private static final int COLUMN_PRICE = 5;
    private static final int COLUMN_QUANTITY = 6;
    private static final int COLUMN_STATUS = 7;
    private static final int COLUMN_CREATED_AT = 8;
    private static final int COLUMN_UPDATED_AT = 9;
    private static final int COLUMN_SOLD = 10;


    public void createList(Product product, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(product.getId());
        cell = row.createCell(1);
        cell.setCellValue(product.getName());
        cell = row.createCell(2);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getDescription());
        }
        cell = row.createCell(3);
        cell.setCellValue(product.getCategory().getId());
        cell = row.createCell(4);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getImage());
        }
        cell = row.createCell(5);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getPrice().toString());
        }
        cell = row.createCell(6);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getQuantity());
        }
        cell = row.createCell(7);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getStatus());
        }
        cell = row.createCell(8);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getCreatedAt().toString());
        }
        cell = row.createCell(9);
        if (product.getDescription() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(product.getUpdatedAt().toString());
        }
        cell = row.createCell(10);
        cell.setCellValue(product.getCount());
    }

    public void createHeader(Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue("Product Id");
        cell = row.createCell(1);
        cell.setCellValue("Product name");
        cell = row.createCell(2);
        cell.setCellValue("Description");
        cell = row.createCell(3);
        cell.setCellValue("Category");
        cell = row.createCell(4);
        cell.setCellValue("Image");
        cell = row.createCell(5);
        cell.setCellValue("Price");
        cell = row.createCell(6);
        cell.setCellValue("Quantity");
        cell = row.createCell(7);
        cell.setCellValue("Status");
        cell = row.createCell(8);
        cell.setCellValue("Created At");
        cell = row.createCell(9);
        cell.setCellValue("Updated At");
        cell = row.createCell(10);
        cell.setCellValue("Sold");
    }


    public List<Product> readExcel(MultipartFile excelFile) throws IOException {

        List<Product> lisProducts = new ArrayList<>();
        // Get file
        InputStream inputStream = excelFile.getInputStream();
        // Get workbook
        Workbook workbook = getWorkbook(inputStream, excelFile.getOriginalFilename());
        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);
        // Get all rows
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() == 0) {
                // Ignore header
                continue;
            }
            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            // Read cells and set value
            Product product = new Product();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_ID:
                        product.setId(new BigDecimal((double) cellValue).intValue());
                        break;
                    case COLUMN_NAME:
                        product.setName((String) getCellValue(cell));
                        break;
                    case COLUMN_DESCRIPTION:
                        product.setDescription((String) getCellValue(cell));
                        break;
                    case COLUMN_CATEGORY_ID:
                        product.setCategory(categoriesService.getById(new BigDecimal((double) cellValue).intValue()));
                        break;
                    case COLUMN_IMAGE:
                        product.setImage((String) getCellValue(cell));
                        break;
                    case COLUMN_QUANTITY:
                        product.setQuantity(new BigDecimal((double) cellValue).intValue());
                        break;
                    case COLUMN_PRICE:
                        product.setPrice(new BigDecimal((String) cellValue));
                        break;
                    case COLUMN_STATUS:
                        product.setStatus(new BigDecimal((double) cellValue).intValue());
                        break;
                    case COLUMN_CREATED_AT:
                        product.setCreatedAt(Timestamp.valueOf((String) getCellValue(cell)));
                        break;
                    case COLUMN_UPDATED_AT:
                        product.setUpdatedAt(Timestamp.valueOf((String) getCellValue(cell)));
                        break;
                    case COLUMN_SOLD:
                        product.setCount(new BigDecimal((double) cellValue).intValue());
                        break;
                    default:
                        break;
                }
            }
            lisProducts.add(product);
        }
        inputStream.close();
        return lisProducts;
    }

    // Get Workbook
    public static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }
    // Get cell value
    public Object getCellValue(Cell cell) {
        int cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }
}
