package com.linghua.wenda.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.linghua.wenda.model.baidu.BaiduResponse;
import com.linghua.wenda.model.gaode.GaodeResponse;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class postionUtil {

    private static final String BAIDUAPI_AK = "LP7SlVTovnP74rCD2vgS1hKDeiqY4ERa";
    private static final String GAODEAPI_AK = "LP7SlVTovnP74rCD2vgS1hKDeiqY4ERa";

    /**
     * 读取
     * @param rd
     * @return
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * 创建链接
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private static String readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String json = readAll(rd);
            return json;
        } finally {
            if (is!=null){
                is.close();
            }
        }
    }

    /**
     * 百度获取城市信息
     * @param ip
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static BaiduResponse getBaiduResonse(String ip) throws JSONException, IOException{
        //这里调用百度的ip定位api服务 详见 http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
        String json = readJsonFromUrl("http://api.map.baidu.com/location/ip?ip="+ip+"&ak="+BAIDUAPI_AK+"&coor=bd09ll");
        BaiduResponse response = JSON.parseObject(json,BaiduResponse.class);
        return response;
    }

    /**
     * 高德获取城市信息
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static GaodeResponse gaodeGetCityCode(String ip) throws JSONException, IOException {
        //高德key
        String key="";
        String json = readJsonFromUrl("http://restapi.amap.com/v3/ip?ip="+ip+"&key="+key+"");
        GaodeResponse response = JSON.parseObject(json,GaodeResponse.class);
        return response;
    }

    public static void main(String[] args) throws Exception{
        String ip = "112.64.196.94";
        BaiduResponse baiduResonse = getBaiduResonse(ip);
        GaodeResponse gaodeResponse = gaodeGetCityCode(ip);

        System.out.println(baiduResonse);
        System.out.println(gaodeResponse);
    }
}
