package com.eatrho.fundtool.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eatrho.fundtool.Constant;
import com.eatrho.fundtool.utility.HttpUtil;
import com.eatrho.fundtool.utility.NumberUtil;
import com.eatrho.fundtool.utility.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FundService {


    /**
     * 根据基金代码获取该基金持仓信息
     *
     * @param code
     * @return
     */
    public static Map<String, String> getjjccByCode(String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("code", code);
        Calendar cd = Calendar.getInstance();
        String year = cd.get(Calendar.YEAR) + "";
        params.put("year", year);
        String js = HttpUtil.get(Constant.jjccurl, params);
        Map<String, String> jsobjecToMap = jsobjecToMap(js);
        Map<String, String> fundMap = parseHTML(jsobjecToMap.get("content"));
        return fundMap;
    }

    public static void calculateTrendByjjcc(Map<String, String> fundMap) {
        String foundMapStr = fundMap.keySet().toString().replace("[", "").replace("]", "").replace(" ", "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("cmd", foundMapStr);
        params.put("token", Constant.token);
        String result = HttpUtil.get(Constant.gpzdurl, params);
        result = result.replace("(", "").replace(")", "");
        JSONArray ja = JSON.parseArray(result);
        if (ja.size() != fundMap.size()) {
            System.out.println("数据有误");
            return;
        }
        Map<String, String> fundTempMap = new LinkedHashMap<String, String>();
        for (int i = 0; i < ja.size(); i++) {
            String str = ja.getString(i);
            String[] arr = str.split(",");
            String code = arr[1] + arr[0];// 股票代码+沪深标记
            if (!fundMap.containsKey(code)) {
                System.out.println("数据有误，不存在的股票");
                return;
            }
            String zf = arr[4];
            fundTempMap.put(code, zf);
        }
        System.out.println(fundTempMap);//此刻基金持仓股票的涨幅

        Number total = 0;
        for (int i = 0; i < ja.size(); i++) {
            String str = ja.getString(i);
            String[] arr = str.split(",");
            String code = arr[1] + arr[0];// 股票代码+沪深标记
            if (!fundMap.containsKey(code)) {
                System.out.println("数据有误,不存在的股票");
                return;
            }
            String zf = arr[4].replace("%", "");// 跌涨率
            if (zf.equals("-")) {
                continue;
            }
            Number zf_num = NumberUtil.div(Double.parseDouble(zf), 100);// 跌涨率

            double zxz = Double.parseDouble(arr[3]);// 最新值
            Number old = NumberUtil.div(zxz, NumberUtil.sum(1, zf_num));// 原来值

            Number zfz = NumberUtil.mul(old, zf_num);// 跌涨值
            Number gpbl = NumberUtil.div(Double.parseDouble(fundMap.get(code).replace("%", "")), 100);// 持股所占比例
            Number zfz_num = NumberUtil.mul(zfz, gpbl);// 跌涨值*持股所占比例

            total = NumberUtil.sum(total, zfz_num);
        }
        System.out.println(NumberUtil.format(total, "0.00000"));
    }

    private static Map<String, String> jsobjecToMap(String js) {
        Map<String, String> apidata = new HashMap<String, String>();
        int flag = js.indexOf("={");
        if (flag == -1) {
            return apidata;
        }
        String startflag = "content:\"";
        int start = js.indexOf(startflag);
        int end = js.indexOf("\",arryear");
        String content = js.substring(start, end).replace(startflag, "");
        apidata.put("content", content);
        return apidata;
    }

    private static Map<String, String> parseHTML(String html) {
        Map<String, String> foundMap = new LinkedHashMap<String, String>();
        Document doc = Jsoup.parse(html);
        Element gpdmList = doc.getElementById("gpdmList");
        String gpdmlistStr = gpdmList.text();
        if (StringUtil.isEmpty(gpdmlistStr)) {
            return foundMap;
        }
        List<String> gpdmlist = new ArrayList<String>();
        String[] gpdmlistArry = gpdmlistStr.split(",");
        for (String s : gpdmlistArry) {
            if (StringUtil.isNotEmpty(s)) {
                gpdmlist.add(s);
            }
        }
        Elements tables = doc.getElementsByTag("table");
        Element element = tables.get(0);
        Elements trs = element.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (int i = 0; i < trs.size(); i++) {
            Element e = trs.get(i);
            Elements tdList = e.getElementsByTag("td");
            String text = tdList.get(6).text();
            if (StringUtil.isEmpty(text) || text.equals("0.00%")) {
                break;
            }
            foundMap.put(gpdmlist.get(i), text);
        }
        return foundMap;
    }

}
