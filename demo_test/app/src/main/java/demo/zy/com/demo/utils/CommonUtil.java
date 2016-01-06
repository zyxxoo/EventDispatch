
package demo.zy.com.demo.utils;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.Editable;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import demo.zy.com.demo.MyApplication;


/**
 * 常用工具类
 * 
 * @author HH
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";



    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



    /**
     * 根据时间戳 得到 时间
     * @param time
     * @return
     */

    public static String getTime(String time){

        long t = Integer.parseInt(time);

        return getTime(t);
    };

    public static String getTime(long time){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(time * 1000L));

        return date;
    }

    /**
     * 半角转全角
     * @param input
     * @return
     */
    public static String ToSBC(String input) {
        if (isEmpty(input)) return "";

        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static void getScreenRect(Context ctx_, Rect outrect_) {
        Display screenSize = ((WindowManager) ctx_
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        outrect_.set(0, 0, screenSize.getWidth(), screenSize.getHeight());
    }

    public static String stringToUnicode(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255)
                str += "\\u" + Integer.toHexString(ch);
            else
                str += "\\" + Integer.toHexString(ch);
        }
        return str;
    }

    public static int dip2px(float dpValue) {
        return dip2px(null, dpValue);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                MyApplication.mDisplayMetrics);

        // return (int) TypedValue.applyDimension(
        // TypedValue.COMPLEX_UNIT_DIP,
        // dpValue,
        // context.getResources().getDisplayMetrics());
        // final float scale =
        // context.getResources().getDisplayMetrics().density;
        // return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object object) {
        if (object == null)
            return true;
        if (object instanceof Uri)
            return (((Uri) object).toString()).length() == 0;
        if (object instanceof CharSequence)
            return ((CharSequence) object).length() == 0;
        if (object instanceof Collection)
            return ((Collection) object).isEmpty();
        if (object instanceof Map)
            return ((Map) object).isEmpty();
        if (object.getClass().isArray())
            return java.lang.reflect.Array.getLength(object) == 0;
        return false;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static void copyWithoutOutputClosing(InputStream in, OutputStream out)
            throws IOException
    {
        try {
            byte[] buffer = new byte[512];
            int count = -1;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
    }

    /**
     * 清除缓存目录
     * @param dir
     * @param curTime
     * @return
     */
 public static int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        String a = child.toString();
                        int index = a.lastIndexOf("/");
                        a = a.substring(index + 1);
                        if (!"volley".endsWith(a) && child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return deletedFiles;
    }

    // 获取SD卡剩余空间
    public static long getRemainSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    // 判断文件是否存在
    public static boolean isFileExist(String path) {
        File file = new File(Environment.getExternalStorageDirectory() + path);
        return file.exists();
    }

    // 创建路径
    public static synchronized void createPath(File file) {
        file.mkdirs();
    }


    // 删除Editable中指定位置的元素，可能为字符，也可能为ImageSpan
    public static Editable deleteElement(Editable editText, int position) {
        ImageSpan[] imageSpans = editText
                .getSpans(0, position, ImageSpan.class);
        if (imageSpans.length > 0) {
            ImageSpan lastImageSpan = null;
            int end = 0;
            for (int i = imageSpans.length - 1; i >= 0; i--) {
                lastImageSpan = imageSpans[i];
                end = editText.getSpanEnd(lastImageSpan);
                if (end == position) {
                    break;
                }
            }
            if (end == position) {
                int start = editText.getSpanStart(lastImageSpan);
                editText.delete(start, end);
            } else {
                editText.delete(position - 1, position);
            }
        } else {
            editText.delete(position - 1, position);
        }
        return editText;
    }

    /**
     * 获取CPU核心数
     * 
     * @return
     */
    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }



    /** 是否超过给定的时间 */
    public static boolean isOverTheTime(int hour) {
        if (hour < 0 || hour > 24) {
            throw new RuntimeException("请给出正确的小时时间");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= hour) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     * 
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
            final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 俩字符串相加
     * @param s1
     * @param s2
     * @return
     */
    public static String getResult_add(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return new java.text.DecimalFormat("#0.00").format(b1.add(b2).doubleValue());
    }


    /**
     * 俩字符串相乘
     * @param s1
     * @param s2
     * @return
     */
    public static String getResult_mul(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return new java.text.DecimalFormat("#0.00").format(b1.multiply(b2).doubleValue());
    }

    /**
     * 俩字符串相减
     * @param s1
     * @param s2
     * @return
     */
    public static String getResult_sub(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return new java.text.DecimalFormat("#0.00").format(b1.subtract(b2).doubleValue());
    }

    /**
     * 俩字符串相除
     * @param s1
     * @param s2
     * @return
     */
    public static String getResult_div(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return new java.text.DecimalFormat("#0.00").format(b1.divide(b2).doubleValue());
    }



//
//    public static List<Image> getImage(List<Image> images, String cover) {
//
//        List<Image> ret = null;
//        if (CommonUtil.isEmpty(images) && CommonUtil.isEmpty(cover)) return ret;
//
//        Image image = null;
//        if (!CommonUtil.isEmpty(cover)){
//            image = new Image();
//            image.uuid = cover;
//            if (CommonUtil.isEmpty(images)){
//
//                ret = new ArrayList<Image>();
//                ret.add(image);
//                return ret;
//            }
//            if (!images.contains(image)) {
//
//                images.add(0, image);
//            }else{
//
//                images.remove(image);
//                images.add(0, image);
//            }
//
//            List<Image> list = new LinkedList<Image>();
//        }
//
//
//        return images;
//    }
}
