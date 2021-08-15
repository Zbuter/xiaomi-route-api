package com.github.zbuter.miwifi.VO;

import lombok.Data;

import java.util.List;

@Data
public class MiWifiDeviceVO {
    String name;
    String mac;
    int online;
    Statistics statistics;
    Authority authority;
    String icon;
    List<IP> ip;
    int isap;
    String oname;
    String parent;
    int push;
    int times;
    int type;

    @Data
    public static class IP {
        int active;
        String downspeed;
        String ip;
        String online;
        String upspeed;
    }

    @Data
    public static class Authority {
        int admin;
        int lan;
        int pridisk;
        int wan;

    }

    @Data
    public static class Statistics {
        String downspeed;
        String online;
        String upspeed;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof MiWifiDeviceVO) {
            MiWifiDeviceVO device = (MiWifiDeviceVO) o;
            return this.mac.equals(device.mac);
        }
        return super.equals(o);
    }

}
