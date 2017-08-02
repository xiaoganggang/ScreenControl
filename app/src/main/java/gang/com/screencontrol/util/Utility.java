package gang.com.screencontrol.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Created by xiaogangzai on 2017/7/19.
 * http://blog.csdn.net/uikoo9/article/details/27980869
 */

public class Utility {
    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * byte数组转String
     *
     * @param buf
     * @return
     * @throws Exception
     */
    public static String byteToString(byte[] buf) throws Exception {
        int i = 0;
        String result = "";
        for (byte item : buf) {
            if (item != 0) {
                i++;
            }
        }

        InputStream bs1 = byteToInput(buf);
        DataInputStream in1 = new DataInputStream(bs1);
        byte[] str = new byte[i];
        try {
            in1.read(str);
            result = new String(str, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * byte[] 转buf
     *
     * @param buf
     * @return
     */
    public static final InputStream byteToInput(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }
}
