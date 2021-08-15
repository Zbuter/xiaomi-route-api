package com.github.zbuter.miwifi;

import com.github.zbuter.miwifi.model.MiWifiDevice;
import com.github.zbuter.miwifi.model.MiWifiRouterName;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * xiaomi-route-api
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
public interface MiWifiService {
    public List<MiWifiDevice> onlineDevice();
    public MiWifiRouterName routerName();
    public boolean setRouterName(String name, String locale);
    public ZonedDateTime sysTime();
    public boolean allowWan(String mac);
    public boolean forbidWan(String mac);
    public boolean setSysTime(ZonedDateTime time);
    public boolean allowConnWifi(String mac);
    public boolean forbidConnWifi(String mac);
}
