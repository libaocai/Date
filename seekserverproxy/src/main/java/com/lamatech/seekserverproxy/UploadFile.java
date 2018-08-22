package com.lamatech.seekserverproxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static com.lamatech.seekserverproxy.ServerProxy.urlencode;

/**
 * 文件上传,下载
 *
 * @author Administrator
 */
public class UploadFile {

    /**
     * 文件上传
     *
     * @param urlStr 服务器路径
     * @param file   文件名称
     */
    public String uploadFile(final String urlStr, final String file) {

        String uploadUrl = urlStr;
        String srcPath =  file;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = "";
            String str = null;
            while((str = br.readLine()) != null){
                result = result + str;
            }

            Log.e("seek", "upload file result is " + result);

            dos.close();
            is.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    /**
     * 文件上传
     *
     * @param urlStr 服务器路径
     * @param file   文件名称
     */
    public String uploadFile(final String urlStr, final String file, Map params) {

        String uploadUrl = urlStr;
        String srcPath =  file;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

            addFormField(params, dos);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            Log.e("seek", srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();


            int responseCode = httpURLConnection.getResponseCode();
            Log.e("seek", "res code is " + responseCode);
            InputStream is;
            if (responseCode == 200) {
                is = httpURLConnection.getInputStream();
            } else {
                is = httpURLConnection.getErrorStream();
            }

            //HttpURLConnection.HTTP_INTERNAL_ERROR;

            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = "";
            String str = null;
            while((str = br.readLine()) != null){
               result = result + str;
            }
//            String result = br.readLine();
//
//            while(br.read() != -1){
//                result += br.readLine();
//                Log.e("seek",result);
//            }
            Log.e("seek",result);
            dos.close();
            is.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR";
    }


    /**
     * 文件上传
     *
     * @param urlStr 服务器路径
     * @param file   文件名称
     */
    public String uploadFile(final String urlStr, final String[] file, Map params) {

        String uploadUrl = urlStr;
        String srcPath =  file[0];
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

            addFormField(params, dos);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file[]\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            for(int i=0; i< file.length; i++){
                srcPath = file[i];
                Log.e("seek", srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
                FileInputStream fis = new FileInputStream(srcPath);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                }
                fis.close();
                dos.writeBytes(end);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                dos.flush();
            }


            int responseCode = httpURLConnection.getResponseCode();
            Log.e("seek", "res code is " + responseCode);
            InputStream is;
            if (responseCode == 200) {
                is = httpURLConnection.getInputStream();
            } else {
                is = httpURLConnection.getErrorStream();
            }

            //HttpURLConnection.HTTP_INTERNAL_ERROR;

            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = "";
            String str = null;
            while((str = br.readLine()) != null){
                result = result + str;
            }

//            String result = br.readLine();
            Log.e("seek",result);
//
//            while(br.read() != -1){
//                result = br.readLine();
//                Log.e("seek",result);
//            }

            dos.close();
            is.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    private void addFormField(Map<String,Object> params, DataOutputStream output) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : params.entrySet()) {
//            try {
                sb.append(twoHyphens + boundary + end);
                sb.append("Content-Disposition: form-data; name=\"" + i.getKey() + "\"" + end);
                sb.append(end);
                sb.append(i.getValue() + end);
                //sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }
        Log.e("seek", "params form data is " + sb.toString());


//        for(Map.Entry<Object, Object> param : params) {
//            sb.append(twoHyphens + boundary + end);
//            sb.append("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + end);
//            sb.append(end);
//            sb.append(param.getValue() + end);
//        }
        try {
            output.writeBytes(sb.toString());// 发送表单字段数据
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL
                    .openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

}
