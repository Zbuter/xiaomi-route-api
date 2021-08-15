package com.github.zbuter.miwifi;

import com.github.zbuter.miwifi.DTO.MiWifiLoginDTO;
import com.github.zbuter.miwifi.VO.*;

import java.time.ZonedDateTime;

/**
 * MiWifiApi
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
interface MiWifiApi {
    public MiWifiLoginVO login();
    public MiWifiDevicelistVO onlineDevice();
    public MiWifiRouterNameVO routerName();
    public MiWifiBaseVO setRouterName(String name, String locale);
    public MiWifiTimeVO sysTime();
    public MiWifiBaseVO setSysTime(ZonedDateTime time);
    public MiWifiBaseVO setMacFilter(String mac, boolean wan);
    public MiWifiBaseVO setWifiFilter(boolean enable,boolean blackMode);
    public MiWifiStatusVO status();
    public MiWifiBaseVO editDevice(String mac, boolean isblack, boolean active);
    public MiWifiMacFilterInfoVO macFilterInfo(boolean isBlackList);
}
