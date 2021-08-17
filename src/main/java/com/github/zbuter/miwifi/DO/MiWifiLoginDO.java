package com.github.zbuter.miwifi.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MiWifiLoginDO extends MiWifiBaseDO {
    String url;
    String token;
}
