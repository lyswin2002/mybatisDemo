package com.demo.utils;/**
 * Created by Administrator on 2017/12/17.
 */

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 廖永生
 * @create 2017-12-17 19:26
 * @decription
 */
public class ImportExcelUtil {
    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * @param in,fileName
     * @return
     * @throws IOException
     */
    public  List<Map<String,Object>> getBankListByExcel(InputStream in, String fileName,String[] title) throws Exception{

        List<Map<String,Object>> list = null;

        //创建Excel工作薄
        Workbook work = this.getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄失败！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            list =getSheetData(sheet,title);
            if (list!=null) break;
        }
        work.close();
        return list;
    }

    public List<Map<String,Object>> getSheetData(Sheet sheet,String[] title){
        if(sheet==null) return null;
        List<Map<String,Object>> sheetData =new ArrayList<Map<String, Object>>();
        int firstCell=0;
        int lastCell=0;
        Row row;
        Cell cell;
        //遍历当前sheet中的所有行
        for (int j = sheet.getFirstRowNum(); j <=sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if(row==null) continue;
            if (row.getFirstCellNum()==j){
                firstCell =row.getFirstCellNum();
                lastCell = row.getLastCellNum();
                if (checkTitleCell(row,title)==false) {
                    break;
                    //throw new Exception("导入文件字段不匹配，请按导入模板格式");
                }
                continue;
            }
            //遍历所有的列
            Map<String,Object> li = new HashMap();
            for (int y = firstCell; y < lastCell; y++) {
                cell = row.getCell(y);
                li.put(title[y-firstCell],this.getCellValue(cell));
            }
            sheetData.add(li);
        }
        return sheetData;
    }
    /**
     * 检查标题字段是否匹配
     * @param row
     * @param title
     * @return
     */
    private boolean checkTitleCell(Row row,String[] title){
       if (title==null) return true;
       boolean isOk=true;
       int firstCell =row.getFirstCellNum();
       int lastCell = row.getLastCellNum();
       for (int y = firstCell; y < lastCell; y++) {
            Cell cell = row.getCell(y);
            if (((String)this.getCellValue(cell)).equalsIgnoreCase(title[y-firstCell])){
                continue;
            }else{
                isOk=false;break;
            }
       }
       return isOk;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public  Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字
        if (cell!=null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:

                    if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                        value = df.format(cell.getNumericCellValue());
                    } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                        value = sdf.format(cell.getDateCellValue());
                    } else {
                        value = df2.format(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                default:
                    break;
            }
        }else{
            value="";
        }
        return value;
    }


}
