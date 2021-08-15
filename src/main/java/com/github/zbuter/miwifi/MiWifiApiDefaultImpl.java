package com.github.zbuter.miwifi;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.github.zbuter.miwifi.VO.*;
import com.github.zbuter.miwifi.exceptions.MiWifiException;
import com.github.zbuter.miwifi.exceptions.MiWifiStatusException;
import sun.net.util.IPAddressUtil;

import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * xiaomi-route-api
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
class MiWifiApiDefaultImpl implements MiWifiApi {
    private String adminUrl;
    private String token;
    private String username;
    private String passwd;


    public MiWifiApiDefaultImpl(String url, String username, String passwd) {
        this.adminUrl = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public MiWifiLoginVO login() {
        String reqUrl = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/api/xqsystem/login", Charset.defaultCharset()))
                .addQuery("username", username)
                .addQuery("password", passwd).build();
        String s = this.getResponse(reqUrl);
        MiWifiLoginVO miWifiLoginModel = JSONUtil.toBean(s, MiWifiLoginVO.class);
        token = miWifiLoginModel.getToken();
        return miWifiLoginModel;
    }

    @Override
    public MiWifiDevicelistVO onlineDevice() {
        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/misystem/devicelist", Charset.defaultCharset()))
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiDevicelistVO.class);
    }

    @Override
    public MiWifiRouterNameVO routerName() {
        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/misystem/router_name", Charset.defaultCharset()))
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiRouterNameVO.class);
    }

    @Override
    public MiWifiBaseVO setRouterName(String name, String locale) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("locale", locale);
        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/misystem/set_router_name", Charset.defaultCharset()))
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiBaseVO.class);
    }

    @Override
    public MiWifiTimeVO sysTime() {
        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/misystem/sys_time", Charset.defaultCharset()))
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiTimeVO.class);
    }

    @Override
    public MiWifiBaseVO setSysTime(ZonedDateTime time) {
        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/misystem/set_sys_time", Charset.defaultCharset()))
                .build();
        Map<String, Object> data = new HashMap<>();
        int offset = time.getOffset().getTotalSeconds() / 60 / 60;
        String prefix = offset <= 0 ? "+" : "-";
        data.put("timezone", "CST" + prefix + offset);
//        data.put("index","0");
        data.put("time", time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        String s = this.getResponse(url,data);
        return JSONUtil.toBean(s, MiWifiBaseVO.class);
    }

    @Override
    public MiWifiBaseVO setMacFilter(String mac, boolean wan){

        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/xqsystem/set_mac_filter", Charset.defaultCharset()))
                .addQuery("mac", mac)
                .addQuery("wan", wan ? "1" : "0")

                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiBaseVO.class);
    }



    public MiWifiBaseVO setWifiFilter(boolean enable,boolean blackMode){

        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/xqnetwork/set_wifi_macfilter", Charset.defaultCharset()))
                .addQuery("model",blackMode?"0":"1")
                .addQuery("enable",enable?"1":"0")
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiBaseVO.class);
    }


    @Override
    public MiWifiStatusVO status() {
        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/misystem/status", Charset.defaultCharset()))
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiStatusVO.class);
    }

    @Override
    public MiWifiBaseVO editDevice(String mac, boolean isblack, boolean active) {

        String model = isblack ? "0" : "1";
        String option = active ? "0" : "1";

        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/xqnetwork/edit_device", Charset.defaultCharset()))
                .addQuery("mac", mac)
                .addQuery("model", model)
                .addQuery("option", option)
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiBaseVO.class);
    }

    @Override
    public MiWifiMacFilterInfoVO macFilterInfo(boolean isBlackList) {
        String model = isBlackList ? "0" : "1";

        String url = UrlBuilder.ofHttp(adminUrl)
                .setPath(UrlPath.of("/cgi-bin/luci/;stok=" + token + "/api/xqnetwork/wifi_macfilter_info", Charset.defaultCharset()))
                .addQuery("model", model)
                .build();
        String s = this.getResponse(url);
        return JSONUtil.toBean(s, MiWifiMacFilterInfoVO.class);

    }

    public String getResponse(String url){
       return this.getResponse(url,null);
    }
    public String getResponse(String url,Map<String,Object> data){
        Method method = null;
        if(data == null){
            method = Method.GET;
        }else{
            method = Method.POST;
        }

        HttpRequest request = HttpUtil.createRequest(method, url);
        request.form(data);
        HttpResponse execute = request.execute();
        if (execute.getStatus() != 200) {

            throw new MiWifiStatusException("响应状态码错误："+execute.getStatus()+" 请确保可以正确访问管理地址："+adminUrl);
        }
        return execute.body();
    }
}
