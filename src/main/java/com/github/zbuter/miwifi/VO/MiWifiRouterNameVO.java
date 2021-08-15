package com.github.zbuter.miwifi.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MiWifiRouterNameVO extends MiWifiBaseVO {
    String locale;
    String name;
}
