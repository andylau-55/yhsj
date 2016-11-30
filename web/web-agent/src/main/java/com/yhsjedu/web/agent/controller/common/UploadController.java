package com.yhsjedu.web.agent.controller.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SystemProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yhsjedu.datacloud.utils.DateUtility;
import com.yhsjedu.datacloud.utils.ResultBean;


@Controller
public class UploadController {
    private final static Log log = LogFactory.getLog(UploadController.class);

    public static String folder = "";
    public static String url_head = "";

    static {
        try {
            Properties properties = new Properties();
            Enumeration<URL> urls = SystemProperties.class.getClassLoader().getResources("config.properties");
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                InputStream input = null;
                try {
                    URLConnection con = url.openConnection();
                    con.setUseCaches(false);
                    input = con.getInputStream();
                    properties.load(input);
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }

            folder = properties.getProperty("upload.folder").trim();
            url_head = properties.getProperty("upload.url").trim();
            if (!url_head.endsWith("/")) {
                url_head += "/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取配置文件config.properties出错！");
        }
    }

    /**
     * 上传图片到临时目录
     * 
     * @param file
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("uploadImgCommon")
    public Object uploadImgCommon(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request,
            HttpServletResponse response) {
        String actionNm = "uploadImgCommon";
        log.info("[" + actionNm + "] start");
        ResultBean retVal = new ResultBean();
        try {

            String typeId = request.getParameter("uploadTypeId");
            String realPath = request.getSession().getServletContext().getRealPath(folder);
            if (!realPath.endsWith("/")) {
                realPath += "/";
            }

            String tmpDir = "tmpImg/" + typeId + "/";

            // 文件暂时上传到临时目录
            String fileName = DateUtility.formatDateToString(DateUtility.getSystemDateTime(),
                    DateUtility.DATE_PATTERN_YMDHMS2S) + "_" + file.getOriginalFilename();
            File targetFile = new File(realPath + tmpDir, fileName);

            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            // 保存
            file.transferTo(targetFile);

            // model.addAttribute("fileUrl", request.getContextPath() + "/apk/"
            // + fileName);

            Map<String, String> result = new HashMap<String, String>();
            result.put("url", url_head + tmpDir + fileName);
            result.put("title", fileName);
            result.put("original", file.getOriginalFilename());

            log.info("上传文件:" + realPath + tmpDir + fileName);

            retVal.setData(result);
        } catch (Exception e) {
            retVal.addSysErr();
        }
        log.info("[" + actionNm + "] end");
        return retVal;
    }

    public static void deleteImg(HttpServletRequest request, String url) {
        String actionNm = "deleteImg";
        log.info("[" + actionNm + "] start");
        String realPath = request.getSession().getServletContext().getRealPath(folder);
        if (!realPath.endsWith("/")) {
            realPath += "/";
        }
        String filePath = url.replace(url_head, "");
        String fileRealPath = realPath + filePath;

        log.info("删除文件:" + fileRealPath);
        File file = new File(fileRealPath);
        if (file.exists()) {
            file.delete();
            // 删除空目录
            file = new File(file.getParent());
            if (file.isDirectory() && file.list().length == 0) {
                file.delete();
            }
        }
        log.info("[" + actionNm + "] end");
    }

    public static String moveImg(HttpServletRequest request, String fromPath, String toPath) throws Exception {
        String actionNm = "moveImg";
        log.info("[" + actionNm + "] start");
        String fileNm = null;
        File file = null;
        InputStream is = null;
        FileOutputStream fs = null;
        try {
            String realPath = request.getSession().getServletContext().getRealPath(folder);
            if (!realPath.endsWith("/")) {
                realPath = realPath + "/";
            }

            String realFromPath = realPath + fromPath.replace(url_head, "");
            String realToPath = realPath + toPath;
            if (!realToPath.endsWith("/")) {
                realToPath += "/";
            }

            file = new File(realFromPath);

            fileNm = file.getName();
            fileNm = fileNm.substring(fileNm.indexOf("_") + 1);

            File targetFolder = new File(realToPath);

            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }

            if (file.exists()) {
                is = new FileInputStream(file); // 读入原文件
                fs = new FileOutputStream(realToPath + fileNm);
                byte[] buffer = new byte[1444];
                int byteread = 0;
                while ((byteread = is.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (fs != null) {
                    fs.close();
                    fs = null;
                }
                // 删除文件
                file.delete();
            }

            log.info("移动文件从:" + realFromPath);
            log.info("移动文件到:" + realToPath + fileNm);
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null)
                is.close();
            if (fs != null)
                fs.close();
        }
        log.info("[" + actionNm + "] end");
        return url_head + toPath + fileNm;

    }

    /**
     * 删除所有文件
     * 
     * @param request
     * @param filePath
     */
    public static void deleteAll(HttpServletRequest request, String filePath) {
        String realPath = request.getSession().getServletContext().getRealPath(folder);
        if (!realPath.endsWith("/")) {
            realPath = realPath + "/";
        }

        realPath = realPath + filePath;
        log.info("删除目录下的所有文件:" + realPath);
        File file = new File(realPath);
        deleteAll(file);
    }

    public static void deleteAll(File file) {

        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
                files[i].delete();
            }

            if (file.exists()) // 如果文件本身就是目录 ，就要删除目录
                file.delete();
        }
    }

    @ResponseBody
    @RequestMapping("/newsUploadImg")
    public Object newsUploadImg(@RequestParam(value = "upfile") MultipartFile file, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        // String fileName = new Date().getTime()+".jpg";
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        // 保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);

        Map<String, String> result = new HashMap<String, String>();

        result.put("state", "SUCCESS");
        result.put("url", request.getContextPath() + "/upload/" + fileName);
        result.put("title", fileName);
        result.put("original", file.getOriginalFilename());

        return result;
    }

}
