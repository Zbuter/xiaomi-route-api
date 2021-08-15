package com.github.zbuter.miwifi.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MiWifiDevicelistVO extends MiWifiBaseVO {
    String mac;
    List<MiWifiDeviceVO> list;
}
