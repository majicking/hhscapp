package com.guohanhealth.shop.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * JSON数据解析
 *
 * @date 2016-5-11
 */
public class JSONParser {

    /**
     * Json转对象
     *
     * @param <T>
     * @param jsonStr
     * @param cls
     * @return
     */
    public static <T> T JSON2Object(String jsonStr, Class<T> cls) {
        try {
            // 不为空，并且服务器没有异常
            if (StringUtil.isNoEmpty(jsonStr)) {
                if (jsonStr.indexOf("exception") == -1) {
                    return JSON.parseObject(jsonStr, cls);
                } else {
                    Logger.e("服务器异常：" + jsonStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json 转 list集合
     *
     * @param <T>
     * @param jsonStr
     * @param cls
     * @return
     */
    public static <T> List<T> JSON2Array(String jsonStr, Class<T> cls) {
        try {
            // 不为空，并且服务器没有异常
            if (StringUtil.isNoEmpty(jsonStr)) {
                if (jsonStr.indexOf("exception") == -1) {
                    return JSON.parseArray(jsonStr, cls);
                } else {
                    Logger.e("服务器异常：" + jsonStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param jsonStr 获取的字段
     * @param key     JSON数据串
     * @return
     */
    public static String getStringFromJsonString(String key, String jsonStr) {
        JSONObject dataJson;
        String success = "";
        try {
            if (!StringUtil.isNoEmpty(jsonStr)) {
                return success;
            }
            try {
                dataJson = new JSONObject(jsonToFormat(jsonStr));
                success = dataJson.optString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }


    public static String jsonToFormat(String jsonStr) {
        if (jsonStr != null && jsonStr.startsWith("\ufeff")) {
            jsonStr = jsonStr.substring(1);
        }
        return jsonStr;
    }

    /**
     * @param obj 要转成JSON数据串的对象
     * @return
     */
    public static String objToJson(Object obj) {
        String success = "";
        try {
            if (obj != null) {
                success = JSON.toJSONString(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;

    }

    /**
     * @param list list转成JSON数据串
     * @return
     */
    public static String listToJson(List<?> list) {
        String success = "";
        try {
            JSONArray json = new JSONArray();
            json.addAll(list);
            success = json.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;

    }

    /**
     * @param result list转成JSON数据串
     * @return
     */
    public static List<String> getJsonKeys(String result) {
        List<String> success = new ArrayList<String>();
        try {
            if (!StringUtil.isNoEmpty(result)) {
                return success;
            }
            JSONObject json = new JSONObject(result);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                success.add(keys.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;

    }


    public static Object getJsonField(JSONObject root, String path) {
        Pattern array = Pattern.compile("(.*)\\[(\\d+)\\]");
        Object result = root;
        for (String part : path.split("\\.")) {
            Matcher matcher = array.matcher(part);
            if (matcher.matches()) {
                String arrayName = matcher.group(1);
                if (!arrayName.isEmpty()) {
                    try {
                        result = ((JSONObject) result).get(arrayName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                int index = Integer.parseInt(matcher.group(2));
                if (result != null) {
                    result = ((JSONArray) result).get(index);
                }
            } else {
                try {
                    result = ((JSONObject) result).get(part);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (result == null) {
                break;
            }
        }
        return result;
    }

    private static JSONObject listTojsoJsonObject(List<?> list) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("DataBean", list);
        } catch (JSONException e) {
            // The JSONException is thrown by the JSON.org classes when things
            // are amiss.
            e.printStackTrace();
        }
        return jsonObj;
    }
}
