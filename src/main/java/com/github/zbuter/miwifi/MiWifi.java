package com.github.zbuter.miwifi;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.github.zbuter.miwifi.VO.*;
import com.github.zbuter.miwifi.model.MiWifiDevice;
import com.github.zbuter.miwifi.model.MiWifiMacFilter;
import com.github.zbuter.miwifi.model.MiWifiRouterName;
import com.github.zbuter.miwifi.model.MiWifiRouterStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * xiaomi-route-api
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
public class MiWifi implements MiWifiService {

    private final MiWifiApi api;

    public MiWifi(String url, String username, String passwd) {
        this(new MiWifiApiDefaultImpl(url, username, passwd));
    }

    public MiWifi(String url, String passwd) {
        this(url, "admin", passwd);
    }

    public MiWifi(String passwd) {
        this("http://192.168.31.1", passwd);
    }

    public MiWifi(MiWifiApi api) {
        this.api = ProxyUtil.newProxyInstance(new MiWifiInvocationHandler(api), MiWifiApi.class);
    }


    /**
     * 获取所有在线设备
     *
     * @return 在线设备集合
     */
    @Override
    public List<MiWifiDevice> onlineDevice() {
        MiWifiDevicelistVO miWifiDevicelistVO = api.onlineDevice();

        ZonedDateTime sysTime = this.sysTime();

        List<MiWifiDevice> devices = new ArrayList<>();
        for (MiWifiDeviceVO deviceVO : miWifiDevicelistVO.getList()) {
            MiWifiDevice device = new MiWifiDevice();
            MiWifiDeviceVO.IP ip = deviceVO.getIp().get(0);
            device.setIp(ip.getIp());
            device.setMac(deviceVO.getMac());
            device.setRemarkName(deviceVO.getName());
            device.setName(deviceVO.getOname());
            device.setOnline(deviceVO.getOnline() == 1);
            device.setAP(deviceVO.getIsap() != 0);
            device.setConnType(MiWifiDevice.ConnType.mapType(deviceVO.getType()));
            device.setDownloadSpeed(Integer.parseInt(ip.getDownspeed()));
            device.setOnlineSecond(Integer.parseInt(ip.getOnline()));
            device.setUploadSpeed(Integer.parseInt(ip.getUpspeed()));
            DateTime upTime = DateUtil.offsetSecond(Date.from(sysTime.toInstant()), -device.getOnlineSecond());
            device.setUpTime(LocalDateTime.ofInstant(upTime.toInstant(), sysTime.getZone()));
            device.setAllowAdmin(deviceVO.getAuthority().getAdmin()==1);
            device.setAllowLan(deviceVO.getAuthority().getLan()==1);
            device.setAllowPriDisk(deviceVO.getAuthority().getPridisk()==1);
            device.setAllowWan(deviceVO.getAuthority().getWan()==1);
            devices.add(device);
        }
        return devices;
    }

    /**
     * 路由器名称和所在位置
     */
    @Override
    public MiWifiRouterName routerName() {
        MiWifiRouterNameVO miWifiRouterNameVO = api.routerName();
        MiWifiRouterName rn = new MiWifiRouterName();
        rn.setLocale(miWifiRouterNameVO.getLocale());
        rn.setName(miWifiRouterNameVO.getName());
        return rn;
    }

    /**
     * 设置路由器名称和所在位置
     *
     * @param name   名称
     * @param locale 位置
     * @return 是否设置成功
     */
    @Override
    public boolean setRouterName(String name, String locale) {
        // {"locale":"Home","name":"Xiaomi_A3D7","code":0}
        MiWifiBaseVO miWifiBaseVO = api.setRouterName(name, locale);
        return miWifiBaseVO.getCode() == 0;
    }

    /**
     * 获取路由器系统时间
     *
     * @return 系统时间
     */
    public ZonedDateTime sysTime() {
        MiWifiTimeVO timeVO = api.sysTime();
        MiWifiTimeVO.Time time = timeVO.getTime();
        return ZonedDateTime.of(time.getYear(), time.getMonth(), time.getMonth(), time.getHour(), time.getMin(), time.getSec(), 0, ZoneId.of(time.getTimezone().replace("CST-", "UTC+").replace("CST+", "UTC-")));
    }

    public boolean setSysTime(ZonedDateTime time) {
        MiWifiBaseVO miWifiBaseVO = api.setSysTime(time);
        return miWifiBaseVO.getCode() == 0;
    }

    public MiWifiRouterStatus status() {
        MiWifiStatusVO statusVO = api.status();
        MiWifiRouterStatus status = new MiWifiRouterStatus();

        status.setTemperature(statusVO.getTemperature());
        status.setOnlineCount(statusVO.getCount().getOnline());
        status.setAllCount(statusVO.getCount().getAll());

        status.setCpuCore(statusVO.getCpu().getCore());
        status.setCpuHz(statusVO.getCpu().getHz());
        status.setCpuLoad(statusVO.getCpu().getLoad());

        ZonedDateTime sysTime = sysTime();
        DateTime upTime = DateUtil.offsetSecond(Date.from(sysTime.toInstant()), -Double.valueOf(statusVO.getUpTime()).intValue());
        status.setUpTime(LocalDateTime.ofInstant(upTime.toInstant(), sysTime.getZone()));

        status.setChannel(statusVO.getHardware().getChannel());
        status.setPlatform(statusVO.getHardware().getPlatform());
        status.setMac(statusVO.getHardware().getMac());
        status.setSn(statusVO.getHardware().getSn());
        status.setVersion(statusVO.getHardware().getVersion());

        status.setMemHz(statusVO.getMem().getHz());
        status.setMemSize(statusVO.getMem().getTotal());
        status.setMemType(statusVO.getMem().getType());
        status.setMemUsage(statusVO.getMem().getUsage());

        status.setWanName(statusVO.getWan().getDevname());
        status.setDownloadSize(Long.parseLong(statusVO.getWan().getDownload()));
        status.setUploadSize(Long.parseLong(statusVO.getWan().getUpload()));
        status.setDownloadSpeed(Integer.parseInt(statusVO.getWan().getDownspeed()));
        status.setUploadSpeed(Integer.parseInt(statusVO.getWan().getUpspeed()));

        status.setMaxDownloadSpeed(Integer.parseInt(statusVO.getWan().getMaxdownloadspeed()));
        status.setMaxUploadSpeed(Integer.parseInt(statusVO.getWan().getMaxuploadspeed()));

        List<MiWifiRouterStatus.ConnDevice> devices = new ArrayList<>();
        for (MiWifiStatusVO.Device deviceVO : statusVO.getDev()) {
            MiWifiRouterStatus.ConnDevice device = new MiWifiRouterStatus.ConnDevice();
            device.setName(deviceVO.getDevname());
            device.setDownloadSize(Long.parseLong(deviceVO.getDownload()));
            device.setUploadSize(Long.parseLong(deviceVO.getUpload()));
            device.setDownSpeed(Integer.parseInt(deviceVO.getDownspeed()));
            device.setUploadSpeed(Integer.parseInt(deviceVO.getUpspeed()));
            device.setMac(deviceVO.getMac());
            device.setMaxDownloadSpeed(Integer.parseInt(deviceVO.getMaxdownloadspeed()));
            device.setMaxUploadSpeed(Integer.parseInt(deviceVO.getMaxuploadspeed()));
            device.setOnlineSecond(Integer.parseInt(deviceVO.getOnline()));
            devices.add(device);
        }
        status.setDeviceList(devices);
        return status;
    }

    public boolean allowWan(String mac){
        MiWifiBaseVO vo = api.setMacFilter(mac, true);
        return vo.getCode()==0;
    }
    public boolean forbidWan(String mac){
        MiWifiBaseVO vo = api.setMacFilter(mac, false);
        return vo.getCode()==0;
    }


    public boolean allowConnWifi(String mac){
        MiWifiBaseVO vo = api.editDevice(mac, true, false);
        return vo.getCode()==0;
    }
    public boolean forbidConnWifi(String mac){
        MiWifiBaseVO vo = api.editDevice(mac, true, true);
        return vo.getCode()==0;
    }
    public List<MiWifiMacFilter> blackList(){
        MiWifiMacFilterInfoVO info = api.macFilterInfo(true);
        return getMiWifiMacFilters(info);
    }
    public List<MiWifiMacFilter> whiteList(){
        MiWifiMacFilterInfoVO info = api.macFilterInfo(false);
        return getMiWifiMacFilters(info);
    }

    public boolean isBlackControlMode(){
        MiWifiMacFilterInfoVO info = api.macFilterInfo(false);
        return info.getModel()==0;
    }

    public boolean enableWifiControl(boolean blackmode){
        MiWifiBaseVO vo = api.setWifiFilter(true, blackmode);
        return vo.getCode()==0;
    }
    public boolean disableWifiControl(){
        MiWifiBaseVO vo = api.setWifiFilter(false, false);
        return vo.getCode()==0;
    }

    private List<MiWifiMacFilter> getMiWifiMacFilters(MiWifiMacFilterInfoVO info) {
        List<MiWifiMacFilterInfoVO.MacFilter> macfilter = info.getMacfilter();
        List<MiWifiMacFilter> list = new ArrayList<>();
        for (MiWifiMacFilterInfoVO.MacFilter filter : macfilter) {
            MiWifiMacFilter i = new MiWifiMacFilter();
            i.setMac(filter.getMac());
            i.setName(filter.getName());
            list.add(i);
        }
        return list;
    }

    public static void main(String[] args) {

        MiWifi miwifi = new MiWifi("123456789.fa");
        MiWifiRouterStatus status = miwifi.status();
        System.out.println(status);
    }
}
