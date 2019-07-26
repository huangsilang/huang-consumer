package com.example.huang.huangconsumer.repository;

import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginRepository {

    public  List<ConsumerPojo> readExcel(String filePath) throws Exception {
        // 检查
        File file = new File(filePath);
        // 获取workbook对象
        Workbook workbook = null;
        List<ConsumerPojo> list = new ArrayList<>();
        try {
            workbook = this.getWorkbook(filePath);
            // 读文件 一个sheet一个sheet地读取
            int sheetNum = workbook.getNumberOfSheets() ;
            for (int numSheet = 0; numSheet < sheetNum; numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();
                // 读取首行 即,表头
                Row firstRow = sheet.getRow(firstRowIndex);
                // 读取数据行
                for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                    Row currentRow = sheet.getRow(rowIndex);// 当前行
                    Cell cell = currentRow.getCell(0);// 当前单元格
                    cell.setCellType(CellType.STRING);
                    Cell cell1 = currentRow.getCell(1);// 当前单元格
                    Cell cell2 = currentRow.getCell(2);// 当前单元格
                    Cell cell3 = currentRow.getCell(3);// 当前单元格
                    Cell cell4 = currentRow.getCell(4);// 当前单元格
                    Cell cell5 = currentRow.getCell(5);// 当前单元格
                    Cell cell6 = currentRow.getCell(6);// 当前单元格
                    Cell cell7 = currentRow.getCell(7);// 当前单元格
                    String id = cell.getStringCellValue();
                    String name = cell1.getStringCellValue();// 当前单元格的值
                    String product = cell2.getStringCellValue();// 当前单元格的值
                    String howmuch = cell3.getStringCellValue();// 当前单元格的值
                    Date date = cell4.getDateCellValue();// 当前单元格的值
                    String type = cell5.getStringCellValue();// 当前单元格的值
                    String isMust = cell6.getStringCellValue();// 当前单元格的值
                    String remark = cell7.getStringCellValue();// 当前单元格的值
                    BigDecimal bg = new BigDecimal(howmuch);
                    ConsumerPojo s = new ConsumerPojo();
                    s.setId(id);
                    s.setName(name);
                    s.setProduct(product);
                    s.setHowMuch(bg);
                    s.setDate(date);
                    s.setTimeLong(date.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    s.setTime(sdf.format(date));
                    s.setType(type);
                    s.setIsMust("0".equals(isMust)?"是":"不是");
                    s.setRemark(remark);
                    list.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private  Workbook getWorkbook(String filePath) throws IOException {
        Workbook workbook = null;
        InputStream is = new FileInputStream(filePath);
        if (filePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(is);
        } else if (filePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }
}
