package com.demo.controller;/**
 * Created by Administrator on 2017/12/17.
 */

import com.demo.utils.ExportExcel;
import com.demo.utils.ImportExcelUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;


/**
 * @author 廖永生
 * @create 2017-12-17 19:55
 * @decription
 */
@Controller
public class ExcelController {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @RequestMapping("/uploadExcel")
    public String uploadExcel(){
        return "uploadExcel";
    }
    @RequestMapping("/exportExcel")
    @ResponseBody
    public Object exportExcel(){
        ExportExcel exportExcel;
        return "ok";
    }
    /**
     * 描述：通过传统方式form表单提交方式导入excel文件
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/importExcel")
    @ResponseBody
    public  Object  importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        log.debug("通过传统方式form表单提交方式导入excel文件！");

        InputStream in =null;
        List<List<Object>> listob = null;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),
                            new String[]{"虚拟账户号","商户存管交易帐号","用户类型","开户日期","真实姓名","身份证号","手机号","认证状态"});
        in.close();

        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
        int j=0;
        for(List row :listob){
            int i=0;
            System.out.println("第"+j+"行：");
            j++;
            for(Object obj:row){

                System.out.print("i="+obj.toString()+",");
                i++;
            }

        }
        //-------------------
        return listob;
    }

    /**
     * 描述：通过 jquery.form.js 插件提供的ajax方式上传文件
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="ajaxUpload")
    public  void  ajaxUploadExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        System.out.println("通过 jquery.form.js 提供的ajax方式上传文件！");

        InputStream in =null;
        List<List<Object>> listob = null;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }

        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),null);

        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出



    }
}
