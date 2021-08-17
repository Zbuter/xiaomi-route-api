package com.github.zbuter.miwifi.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * wifi-manager-bot
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-05-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MiWifiMacFilterInfoDO extends MiWifiBaseDO {
    int enable;
    List<MiWifiDeviceDO> flist;
    List<MiWifiDeviceDO> list;
    List<MacFilter> macfilter;

    List<String> weblist;
    int model;

    @Data
    public static class MacFilter{
        String mac;
        String name;
    }

    public boolean currentModeisBlackMode(){
        return model == 0;
    }
}
