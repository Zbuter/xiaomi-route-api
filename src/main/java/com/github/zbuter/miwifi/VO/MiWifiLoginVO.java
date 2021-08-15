package com.github.zbuter.miwifi.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MiWifiLoginVO extends MiWifiBaseVO {
    String url;
    String token;
}
