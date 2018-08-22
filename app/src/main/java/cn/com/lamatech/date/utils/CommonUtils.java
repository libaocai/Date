package cn.com.lamatech.date.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.lamatech.date.R;

public class CommonUtils {


	/**
	 * 验证邮箱格式是否正确
	 * @param address
	 * @return
     */
	public static boolean checkEmail(String address)
	{if (null == address || "".equals(address))
		return false;
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(address);
		return m.matches();
	}

	/**
	 * 验证手机号码格式是否正确
	 * @param phone
	 * @return
     */

	public static boolean checkPhone(String phone){
		Pattern pattern = Pattern.compile("^((19[8-9])|(166)|(14[0-9])|(13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(phone);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}
	/**
	 * Json 转成 Map<>
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> getMapForJson(String jsonStr) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonStr);

			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("ERROR", e.toString());
		}
		return null;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		if (drawable == null)
			return null;
		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		// canvas.setBitmap(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;

	}

	public static byte[] getAudioData(String fileName) {
		InputStream in;
		byte b[] = new byte[1024 * 1024 * 2];
		int len = 0;
		try {
			in = new FileInputStream(fileName);

			int temp = 0;// 所有读取的内容都使用temp接收
			while ((temp = in.read()) != -1) { // 当没有读取完时，继续读取
				b[len] = (byte) temp;
				len++;
				if (len > 1024 * 1024 * 2)
					break;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte data[] = new byte[len];
		System.arraycopy(b, 0, data, 0, len);

		return data;
	}

	public static String getAudioFileName(byte[] audioData) {
		String filedir = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File file = new File(filedir, "record_out.amr");
		try {
			OutputStream os = new FileOutputStream(file);
			os.write(audioData, 0, audioData.length);
			os.flush();
			os.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/record_out.amr";
	}

	public static byte[] getIconData(Bitmap bitmap) {
		if (bitmap == null)
			return null;

		Log.d("diankan", "bitmap 原来大小"+ bitmap.getByteCount()/1024);
	
		//int size = bitmap.getWidth() * bitmap.getHeight() * 10;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	//	Log.d("diankan", bitmap.getByteCount());
		
	
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("diankan", "bitmap 大小"+ bitmap.getByteCount()/1024);
		return out.toByteArray();
	}
	/**
	 * 压缩图片
	 * 
	 * @param bitmap
	 *            源图片
	 * @param width
	 *            想要的宽度
	 * @param height
	 *            想要的高度
	 * @param isAdjust
	 *            是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
	 * @return Bitmap
	 */
	public static Bitmap reduce(Bitmap bitmap, int width, int height,
								boolean isAdjust) {
		// 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
		if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
			return bitmap;
		}
		// 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor,
		// int scale, int roundingMode);
		// scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
		float sx = new BigDecimal(width).divide(
				new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		float sy = new BigDecimal(height).divide(
				new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
			sx = (sx < sy ? sx : sy);
			sy = sx;// 哪个比例小一点，就用哪个比例
		}
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/**
	 * @param bitmap
	 * @param edgeLength
	 * @return 居中裁剪图片
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
	  {
	   if(null == bitmap || edgeLength <= 0)
	   {
	    return  null;
	   }
	                                                                                  
	   Bitmap result = bitmap;
	   int widthOrg = bitmap.getWidth();
	   int heightOrg = bitmap.getHeight();
	                                                                                  
	   if(widthOrg > edgeLength && heightOrg > edgeLength)
	   {
	    //压缩到一个最小长度是edgeLength的bitmap
	    int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
	    int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
	    int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
	    Bitmap scaledBitmap;
	                                                                                   
	          try{
	           scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	          }
	          catch(Exception e){
	           return null;
	          }
	                                                                                        
	       //从图中截取正中间的正方形部分。
	       int xTopLeft = (scaledWidth - edgeLength) / 2;
	       int yTopLeft = (scaledHeight - edgeLength) / 2;
	                                                                                      
	       try{
	        result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
	        scaledBitmap.recycle();
	       }
	       catch(Exception e){
	        return null;
	       }       
	   }
	                                                                                       
	   return result;
	  }
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	  
	    if (height > reqHeight || width > reqWidth) {  
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
	    }  
	        return inSampleSize*2;  
	}  
	public static Bitmap bitmapreduce(String absolutePath) {
		 final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;  
	        BitmapFactory.decodeFile(absolutePath, options);
	  
	        // Calculate inSampleSize  
	    options.inSampleSize = calculateInSampleSize(options, 480, 800);  
	  
	        // Decode bitmap with inSampleSize set  
	    options.inJustDecodeBounds = false;  
	  
	    return BitmapFactory.decodeFile(absolutePath, options);
	}

	/**
	 * 图片缩放的method
	 * 
	 * @param bmp
	 *            原图
	 * @param scale
	 *            图像缩放比例
	 * @return
	 */
	public static Bitmap zoom(Bitmap bmp, float scale) {
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();

		float sx = new BigDecimal(bmpWidth * scale).divide(
				new BigDecimal(bmpWidth), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		float sy = new BigDecimal(bmpHeight * scale).divide(
				new BigDecimal(bmpHeight), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		/* 产生reSize后的Bitmap对象 */
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		return Bitmap
				.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);

	}

	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double distance(double long1, double lat1, double long2,
			double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */

     
    public static String getVerName(Context context) {
        String verName = "";
        try {  
            verName = context.getPackageManager().getPackageInfo(  
                    "cn.com.lamatech.date", 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("seek", e.getMessage());
        }  
        return verName;     
}  

	/**
	 * 获取代码版本号
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context) {
        int verCode = -1;  
        try {  
            verCode = context.getPackageManager().getPackageInfo(  
                    "com.lamatech.seek", 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e("seek", e.getMessage());
        }  
        return verCode;  
    }  

	/**
	 * 获取应用名字
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}
	
	/**
     * 从指定的URL中获取数组
     * @param urlPath
     * @return
     * @throws Exception
     */
    public static String readParse(String urlPath) throws Exception {
               ByteArrayOutputStream outStream = new ByteArrayOutputStream();
               byte[] data = new byte[1024];  
                int len = 0;  
                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inStream = conn.getInputStream();
                while ((len = inStream.read(data)) != -1) {  
                    outStream.write(data, 0, len);  
                }  
                inStream.close();  
                return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
            }

	/**
	 * 四个数字验证码
	 * @return 四位数字验证码
     */
	public static String getCode() {
		int   intCount;

		intCount=(new Random()).nextInt(9999);//

		if(intCount<1000)intCount+=1000;

		return intCount+"";

	}
	//通过路径压缩bitmap
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 1920f;//这里设置高度为800f
		float ww = 1080f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

	public static void saveBitmap(Bitmap bitmap,String bitName) throws IOException
	{
		File file = new File(bitName);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream out;
		try{
			out = new FileOutputStream(file);
			if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
			{
				out.flush();
				out.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//时间戳转字符串
	public static String getStrTime(String timeStamp){
		String timeString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:MM");
		long  l = Long.valueOf(timeStamp);
		timeString = sdf.format(new Date(l));//单位秒
		return timeString;
	}

}
