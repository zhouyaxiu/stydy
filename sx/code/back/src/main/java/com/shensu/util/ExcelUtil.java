package com.shensu.util;


import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Excel文件读写类
 */
public class ExcelUtil {

    private HSSFWorkbook wb;//工作台对象
    private HSSFSheet sheet;//工作表对象


    /**
     * 读取EXCEL文件
     * filepath　- excel文件的路径
     * filename　- excel文件的名称
     * 返回值：读取是否成功
     */
    public boolean readFromExcel(String filepath, String filename) {
        try {
            FileInputStream fileInputStream = this.getFileInputStreamFromResource(filepath, filename);
            POIFSFileSystem fs = new POIFSFileSystem(fileInputStream);
            //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filepath + "/" + filename + ".xls"));
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     *
     *
     * 读取EXCEL文件
     * filepath　- excel文件的路径
     * filename　- excel文件的名称
     * 返回值：读取是否成功
     */
    public HSSFWorkbook readExcelFile(String filepath) {
        try {
            FileInputStream fileInputStream = this.getFileInputStreamFromResource(filepath);
            POIFSFileSystem fs = new POIFSFileSystem(fileInputStream);
            //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filepath + "/" + filename + ".xls"));
            wb = new HSSFWorkbook(fs);
            //sheet = wb.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    public FileInputStream getFileInputStreamFromResource(String filepath, String filename) throws IOException {
        filepath = "excel" + File.separator + filepath;
        ClassPathResource classPathResource = new ClassPathResource(filepath + File.separator + filename + ".xls");
        InputStream inputStream = classPathResource.getInputStream();
        FileInputStream fis;
        try {
            File tmpFile = File.createTempFile("tmp", ".xls");
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);
            fis = new FileInputStream(tmpFile);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return fis;
    }

    public FileInputStream getFileInputStreamFromResource(String filepath) throws IOException {
        filepath = "excel" + File.separator + filepath;
        ClassPathResource classPathResource = new ClassPathResource(filepath + ".xls");
        InputStream inputStream = classPathResource.getInputStream();
        FileInputStream fis;
        try {
            File tmpFile = File.createTempFile("tmp", ".xls");
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);
            fis = new FileInputStream(tmpFile);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return fis;
    }

    /**
     * 读取EXCEL文件
     * filepath　- excel文件的路径
     * filename　- excel文件的名称
     * 返回值：读取是否成功
     */
    public boolean readFromExcel(Resource resource, String filename) {
        try {
            InputStream inputStream = resource.getInputStream();
            FileInputStream fis = (FileInputStream) inputStream ;
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            IOUtils.closeQuietly(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 写入EXCEL文件
     * filepath　- excel文件的路径
     * filename　- excel文件的名称
     * 返回值：写入是否成功
     */
    public boolean writeToExcel(String filepath, String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath + filename + ".xls");
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 向现有的单元格填充数据
     * rowId : 行号
     * colId : 列号
     * value : 填充的字符串值
     */
    @SuppressWarnings("deprecation")
    public void fillCellData(int rowId, short colId, Object value) {
        if (value == null) {
            value = "";
        }
        HSSFRow row = sheet.getRow(rowId);
        if (row == null) {
            row = createRow(rowId);
        }
        HSSFCell cell = row.getCell(colId);
        if (cell == null) {
            cell = row.createCell(colId);
        }
        //poi在4.1.2中已经自动设置
//        cell.setEncoding(HSSFCell.ENCODING_UTF_16);//设置字符集编码
        cell.setCellValue(value.toString());

    }

    /**
     * 向现有的单元格填充数据
     * rowId : 行号
     * colId : 列号
     * value : 填充的字符串值
     */
    @SuppressWarnings("deprecation")
    public void fillCellData(int rowId, short colId, Object value, HSSFCellStyle cellStyle) {
        if (value == null) {
            value = "";
        }
        HSSFRow row = sheet.getRow(rowId);
        HSSFCell cell = row.getCell(colId);
        if (cell == null) {
            cell = row.createCell(colId);
        }
        cell.setCellStyle(cellStyle);
//        cell.setEncoding(HSSFCell.ENCODING_UTF_16);//设置字符集编码
        cell.setCellValue(value.toString());

    }

    /**
     * 向现有的单元格填充数据
     * rowId : 行号
     * colId : 列号
     * value : 填充的HSSFRichTextString
     */
    public void fillCellData(int rowId, short colId, HSSFRichTextString value) {
        HSSFRow row = sheet.getRow(rowId);

        HSSFCell cell = row.getCell(colId);
        //设置字符集编码
//        cell.setEncoding(HSSFCell.ENCODING_UTF_16);

        cell.setCellValue(value);
    }

    /**
     * 获取单元格数据
     * rowId : 行号
     * colId : 列号
     */
    public String getCellData(int rowId, short colId) {
        HSSFRow row = sheet.getRow(rowId);
        HSSFCell cell = row.getCell(colId);
        return cell.getStringCellValue();
    }

    /**
     * 创建新行
     */
    public HSSFRow createRow(int rowId) {
        return sheet.createRow(rowId);
    }

    /**
     * 创建单元格
     */
    public void createCell(HSSFRow row, short colId) {
        row.createCell(colId);
    }

    /**
     * 创建单元格样式对象
     */
    public HSSFCellStyle createCellStyle() {
        return wb.createCellStyle();
    }

    /**
     * 设置单元格样式对象
     */
    public void setCellStyle(int rowId, short colId, HSSFCellStyle style) {
        HSSFRow row = sheet.getRow(rowId);
        HSSFCell cell = row.getCell(colId);
        cell.setCellStyle(style);
    }

    /**
     * 创建字体对象
     */
    public HSSFFont createFont() {
        return wb.createFont();
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    public HSSFWorkbook getWb() {
        return wb;
    }

    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public boolean writeExcelToResponse(HttpServletResponse response, String filename) {
        try {
            System.out.println(wb);
            OutputStream out = response.getOutputStream();
            response.setContentType("application/x-msdownload");
            //浏览器弹出窗口显示的文件名
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
            response.setContentType("application/msexcel;charset=GBK");
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeExcelToResponse(String path, String filename) {
        try {
            OutputStream out = new FileOutputStream(new File(path + "/" + filename + ".xls"));
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 在某一行的前面增加一行
     * rownum->需要增加行的行标
     * columnsCount->需要增加的行的列数
     * cellStyle->单元格格式
     * height->行高
     */
    public void insertRow(int rownum, int columnsCount, HSSFCellStyle cellStyle, short height) {

        sheet.shiftRows(rownum, sheet.getLastRowNum(), 1);
//		sh.getRow(rownum).getCell((short) 0)
        if (sheet.getRow(rownum) == null) {
            createRow(rownum);
        }
        sheet.getRow(rownum).setHeight(height);
        for (int i = 0; i < columnsCount; i++) {
            createCell(sheet.getRow(rownum), (short) i);
            setCellStyle(rownum, (short) i, cellStyle);
        }

    }

    /**
     * 补齐后面的单元格
     *
     * @param rownum
     * @param cellStyle
     * @param columnsCount
     */
    public void insertColLast(int rownum, HSSFCellStyle cellStyle, int columnsCount) {

        sheet.shiftRows(rownum, sheet.getLastRowNum(), 1);
        if (sheet.getRow(rownum) == null) {
            createRow(rownum);
        }
        for (int i = 0; i < columnsCount; i++) {
            if (sheet.getRow(rownum).getCell(i) == null) {
                createCell(sheet.getRow(rownum), (short) i);
            }
            setCellStyle(rownum, (short) i, cellStyle);
        }

    }

    /**
     * 合并单元格
     */
    public void addMergedRegion(int startRowID, int endRowID, short startColID, short endColID) {
        sheet.addMergedRegion(new CellRangeAddress(startRowID, endRowID, startColID, endColID));
    }

    /**
     * 合并单元格并赋值
     */
    public  int addMergedRegion( int startRowNum, short startCellNum, int endRowNum, short endCellNum, Object value) {
        int i = sheet.addMergedRegion(new CellRangeAddress(startRowNum, endRowNum, startCellNum, endCellNum));
        fillCellData(sheet, startRowNum, startCellNum, value);
        return i;
    }
    /**
     * 填充单元格
     * @param sheet
     * @param rowId
     * @param colId
     * @param value
     * @return HSSFCell
     */
    public static HSSFCell fillCellData(HSSFSheet sheet, int rowId, short colId, Object value) {
        HSSFCell cell = null;
        HSSFRow row = null;
        row = sheet.getRow(rowId);
        if (row == null) {//行不存在
            row = sheet.createRow(rowId);
            cell = row.createCell(colId);
        } else {
            cell = row.getCell(colId);
            if (cell == null)//列不存在
                cell = row.createCell(colId);
        }
        if (value == null) {
            value = "";
        }
        if (value.getClass() == String.class) {
            cell.setCellValue(new HSSFRichTextString(String.valueOf(value)));
        } else if (value.getClass() == Integer.class) {
            cell.setCellValue((Integer) value);
        } else if (value.getClass() == Double.class) {
            cell.setCellValue((Double) value);
        } else if (value.getClass() == Short.class) {
            cell.setCellValue((Short) value);
        } else if (value.getClass() == Long.class) {
            cell.setCellValue((Long) value);
        } else if (value.getClass() == Character.class) {
            cell.setCellValue((Character) value);
        } else if (value.getClass() == Calendar.class) {
            cell.setCellValue((Calendar) value);
        } else if (value.getClass() == Date.class) {
            cell.setCellValue((Date) value);
        } else if (value.getClass() == Boolean.class) {
            cell.setCellValue((Boolean) value);
        }
        return cell;
    }

    /**
     * 获取新建行数(填表人、备注等不计算在内)
     * firstPageCount : 第一页的记录行数
     * totalCount : 总记录行数
     * pageCount : 每页的记录行数
     */
    public int getNewRowCount(int firstPageCount, int totalCount, int pageCount) {
        if (totalCount <= firstPageCount) {
            return 0;
        }

        int pageSize = (totalCount - firstPageCount) % pageCount == 0 ? (totalCount - firstPageCount) / pageCount : (totalCount - firstPageCount) / pageCount + 1;
        return pageSize * pageCount;
    }


    /**
     * 创建新行
     * rownum : 现有的记录行的行号
     * colnumlist : 现有的某行的全部列号
     * newrownum : 新行的行号
     */
    public void createNewRow(int rownum, List<Integer> colnumlist, int newrownum) {
        HSSFRow row = sheet.getRow(rownum);//现有的记录行对象
        HSSFRow newrow = sheet.createRow(newrownum);//创建新行
        newrow.setHeight(row.getHeight());//设置新行的高度
        for (int i = 0; i < colnumlist.size(); i++) {
            int colnum = colnumlist.get(i);
            //System.out.println(colnum);
            HSSFCell newcell = newrow.createCell((short) colnum);//创建新的单元格
            HSSFCell cell = row.getCell((short) colnum);//现有的记录行的单元格
            if (!this.isNull(cell)) {
                HSSFCellStyle cellStyle = cell.getCellStyle();//现有的记录行的单元格样式
                newcell.setCellStyle(cellStyle);//设置新行的单元格样式
            }
        }

    }

    public static boolean isNull(Object o) {
        boolean bReslut = false;
        if (o == null) {
            bReslut = true;
        } else if (o instanceof String) {
            if (((String) o).trim().length() == 0)
                bReslut = true;
        } else
            bReslut = false;

        return bReslut;
    }


    /**
     * 创建全部新行
     * rownum : 现有的记录行的行号
     * colnumlist : 现有的某行的全部列号
     * newRowCount : 新行总数
     */
    public void createNewRowAll(int rownum, List<Integer> colnumlist, int newRowCount) {
        for (int i = 0; i < newRowCount; i++) {
            int newrownum = rownum + i + 1;
            createNewRow(rownum, colnumlist, newrownum);
        }
    }

    /**
     * 删除现有行
     * rownum ：行号
     */
    public void removeOldRow(int rownum) {
        HSSFRow row = sheet.getRow(rownum);//现有的记录行对象
        if(row!=null) {
            sheet.removeRow(row);
        }
    }


    /**
     * 设置顶端标题
     */
//    public void setTopTitle(int sheetIndex, int startRowNum, int endRowNum) {
//        wb.setRepeatingRowsAndColumns(sheetIndex, -1, -1, startRowNum, endRowNum);
//    }

//    /**
//     * 创建表格标题行
//     * startRownum : 原有表格开始行
//     * endRownum : 原有表格结束行
//     * newrownum : 新行的行号
//     */
//    public void copyNewRow(int startRownum, int endRownum, int newrownum) {
//        int j = 0;
//        for (int i = startRownum; i < endRownum + 1; i++) {
//            HSSFRow row = sheet.getRow(i);//现有的记录行对象
//            HSSFRow newrow = sheet.createRow(newrownum + j);//创建新行
//
//            newrow.setHeight(row.getHeight());//设置新行的高度
//            for (short x = 0; x < row.getLastCellNum(); x++) {
//                HSSFCell newcell = newrow.createCell(x);//创建新的单元格
//                HSSFCell cell = row.getCell(x);//现有的记录行的单元格
//                if (!MessageUtil.isNull(cell)) {
//                    newcell.setCellStyle(cell.getCellStyle());//设置新行的单元格样式
//                    if (1 == cell.getCellType(row.getCell(i))) {
//                        newcell.setCellValue(cell.getStringCellValue());
//                    } else if (0 == cell.getCellType()) {
//                        newcell.setCellValue(cell.getNumericCellValue());
//                    }
//                }
//            }
//
//            j++;
//        }
//    }


    /**
     * 设置表尾
     */
    public void setfooter() {
        HSSFFooter footer = sheet.getFooter();
        footer.setCenter("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
    }


    /*
     * 创建新的单元格
     */
    public void createNewCell(int rownum, short colnum, short newcolnum) {
        HSSFRow row = sheet.getRow(rownum);// 现有的记录行对象
        // HSSFRow newrow = sheet.createRow(newrownum);//创建新行
        // newrow.setHeight(row.getHeight());//设置新行的高度


        HSSFCell newcell = row.createCell(newcolnum);// 创建新的单元格
        HSSFCell cell = row.getCell(colnum);// 现有的记录行的单元格
        if (!this.isNull(cell)) {
            HSSFCellStyle cellStyle = cell.getCellStyle();// 现有的记录行的单元格样式
            newcell.setCellStyle(cellStyle);// 设置新行的单元格样式
        }
    }

    /**
     * 验证表头
     */
    public static boolean valigeExcel(HSSFSheet sheet, int row, String[] strings) {
        HSSFRow rowH = sheet.getRow(row);
        if (rowH == null) {
            return false;
        }
        List<Object> rowByRowNum = ExcelUtil.getRowByRowNum(rowH);
        int i = 0;
        for (String colName : strings) {
            if (!colName.equals(rowByRowNum.get(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     *  批量导入需要的方法
     *  读取一个excel文件的一行内容
     * */
    public static List<Object> getRowByRowNum(HSSFRow row) {
        List<Object> list = new ArrayList<Object>();
        if (row != null) {
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                HSSFCell cell = row.getCell(i);
                if (cell != null) {
                    switch (row.getCell(i).getCellType()) {
                        case STRING://字符类型
                            list.add(String.valueOf(cell.getRichStringCellValue()));
                            break;
                        case NUMERIC://数值或日期
                            NumberFormat nf = NumberFormat.getInstance();
                            String s = nf.format(cell.getNumericCellValue());
                            if (s.indexOf(",") >= 0) {
                                s = s.replace(",", "");
                            }
                            list.add(s);
                            break;
                        case FORMULA://公式
                            list.add(cell.getCellFormula());
                            break;
                        case BLANK://空白
                            list.add("");
                            break;
                        case ERROR://错误
                            list.add(cell.getErrorCellValue());
                            break;
                        case BOOLEAN://BOOLEAN
                            list.add(cell.getBooleanCellValue());
                            break;
                        default:
                            break;
                    }
                } else {
                    list.add("");
                }
            }
        }else{



        }
        return list;
    }

}
