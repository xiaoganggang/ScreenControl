package gang.com.screencontrol.util;

/**
 * Created by xiaogangzai on 2017/4/2.
 */

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * json与javabean对象之间相互转换的封装类
 */
public class JsonUitl {

    private static Gson mGson = new Gson();

    /**
     * 将javabean对象转换成json字符串
     * 2017.4.20
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return mGson.toJson(obj);
    }

    /**
     * json字符串转成javabean对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        return mGson.fromJson(str, type);
    }


    /**
     * 将json字符串转化成实体对象
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject(String json, Class classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 把json 字符串转化成list
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

}