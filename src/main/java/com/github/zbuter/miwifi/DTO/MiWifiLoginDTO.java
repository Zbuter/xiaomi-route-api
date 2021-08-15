package com.github.zbuter.miwifi.DTO;

import lombok.Data;

/**
 * xiaomi-route-api
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
@Data
public class MiWifiLoginDTO {
    private String adminUrl = "http://192.168.31.1";
    private String username = "admin";
    private String passwd;
}
