/**
 *
 * 导出 excel（带水印） 公共方法
 * @param header v 为头，k 为对应 data 数据的 name
 * 例:
    const header = [
      {
        k: 'region',
        v: '所属区域',
      }
    ]
 * @param data 要导出的数据
    例:
    const data = [
      {
        region:'广州'
      },
      {
        region:'深圳'
      }
    ]
 * @param filename 导出的 excel 文件名称
 * @param sheetName 指定第一个工作表的名称
 * @param staff 水印名称
 * @param params 导出多个工作表
 * 格式为:
 * [{header:[],data:[],sheetName:''}]
 * @returns
 */
    import {
        saveAs
    } from 'file-saver';
    // const ExcelJS = require('exceljs');
    import ExcelJS from "exceljs"
    /* eslint-disable */
    export const createWsSheet = (
        header,
        columns,
        dataList,
        staff,
        filename,
    ) => {
        if (!((header && dataList) && (header.length && dataList.length))) {
            // Message.error('导出失败');
            return;
        }
        const EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    
        // 创建工作簿
        const workbook = new ExcelJS.Workbook();
        workbook.created = new Date()
        workbook.modified = new Date()
        // 获取水印
        const base64 = setWatermark(staff);
        const imageId1 = workbook.addImage({
            base64,
            extension: 'png'
        });
        // 创建带有红色标签颜色的工作表
        let worksheet = workbook.addWorksheet('Sheet1', { //新建工作表
            views: [{
                state: 'frozen',
                xSplit: 0,
                ySplit: 2
            }], // 冻结视图：xSplit：冻结多少列
    
        });
        worksheet.properties.defaultColWidth = 14
        worksheet.columns = columns
        worksheet.columns.forEach(function(column) {
            var dataMax = 0;
            column.eachCell({
                includeEmpty: true
            }, function(cell) {
                dataMax = cell.value ? cell.value.toString().length : 0;
                if (dataMax <= (column.header.length + 2)) {
                    if (column.width > dataMax) {
                        //retain its default width
                    } else {
                        column.width = column.header.length + 3;
                    }
                } else {
                    column.width = dataMax + 3;
                    column.header.length = dataMax + 3;
                }
                dataMax = 0;
            })
    
        });
        // 添加背景图片
        worksheet.addBackgroundImage(imageId1);
        worksheet.getRow(1).values = [`${filename}(${staff})`];
        worksheet.mergeCells(1, 1, 1, columns.length) //第1行  第1列  合并到第1行的第n列
    
        // worksheet.getRow(1).height = 40
        worksheet.getRow(1).eachCell({
            includeEmpty: true
        }, (cell, colNumber) => {
            worksheet.getRow(1).getCell(colNumber).fill = {
                type: 'pattern',
                pattern: 'solid',
                // fgColor: { argb: 'ffd3d3d3' },
                // bgColor: { argb: 'FF0000FF' },
    
            }
            worksheet.getRow(1).getCell(colNumber).font = {
                bold: true,
            }
            worksheet.getRow(1).getCell(colNumber).border = {
                top: {
                    style: 'thin'
                },
                left: {
                    style: 'thin'
                },
                bottom: {
                    style: 'thin'
                },
                right: {
                    style: 'thin'
                },
            }
        })
        // 添加数据
        worksheet.getRow(2).values = []
        worksheet.getRow(2).values = header
    
        // 表头样式
    
        worksheet.getRow(2).eachCell({
            includeEmpty: true
        }, (cell, colNumber) => {
            worksheet.getRow(2).getCell(colNumber).fill = {
                type: 'pattern',
                pattern: 'solid',
                // fgColor: { argb: 'ffd3d3d3' },
                // bgColor: { argb: 'FF0000FF' },
    
            }
            worksheet.getRow(2).getCell(colNumber).font = {
                bold: true,
            }
            worksheet.getRow(2).getCell(colNumber).border = {
                top: {
                    style: 'thin'
                },
                left: {
                    style: 'thin'
                },
                bottom: {
                    style: 'thin'
                },
                right: {
                    style: 'thin'
                },
            }
    
        })
    
        worksheet.addRows(dataList)
        // 自定义样式
        worksheet.eachRow({
            includeEmpty: true
        }, (row, rowNumber) => {
            // if (rowNumber > 2) {
            //   worksheet.getRow(rowNumber).height = 28.6
            // }
            worksheet.getRow(rowNumber).eachCell({
                includeEmpty: true
            }, (cell, colNumber) => {
                // 文字居中
                worksheet.getRow(rowNumber).getCell(colNumber).alignment = {
                    vertical: 'middle',
                    horizontal: 'center',
                    wrapText: true // 设置自动换行
                }
                //边框样式
                worksheet.getRow(rowNumber).getCell(colNumber).border = {
                    top: {
                        style: 'thin'
                    },
                    left: {
                        style: 'thin'
                    },
                    bottom: {
                        style: 'thin'
                    },
                    right: {
                        style: 'thin'
                    },
                }
    
            })
        })
        worksheet.columns.forEach((column) => {
            column.width = 20;
        });
        workbook.xlsx.writeBuffer().then((buffer) => {
            const blob = new Blob([buffer], {
                type: EXCEL_TYPE
            })
            saveAs(blob, `${filename}.xlsx`)
        })
    
    
        // Message.success('导出成功');
    };
    /**
     * 判断该数据是否为空
     * @param data 要判断的数据
     * @returns 布尔值:true/false
     */
    const isNull = (data) => {
        return !!data;
    }
    
    /**
     * 绘画水印
     * @param str 要做出水印的文字
     * @returns Base64
     */
    const setWatermark = (str) => {
        let id = '1.23452384164.123412416';
    
        if (document.getElementById(id) !== null) {
            document.body.removeChild(document.getElementById(id));
        }
        // 创建一个画布
        let can = document.createElement('canvas');
        // 设置画布的长宽
        can.width = 300;
        can.height = 110;
        let cans = can.getContext('2d');
        // 旋转角度
        cans.rotate(-25 * Math.PI / 180);
        // 设置字体大小
        cans.font = "300 24px Microsoft JhengHei";
        // 设置填充绘画的颜色、渐变或者模式
        cans.fillStyle = "rgba(130, 142, 162, 0.5)";
        // 设置文本内容的当前对齐方式
        cans.textAlign = 'center';
        // 设置在绘制文本时使用的当前文本基线
        cans.textBaseline = 'Middle';
        cans.fillText(str, 220, 180);
    
        const dataURL = can.toDataURL('image/png');
        return dataURL;
    }