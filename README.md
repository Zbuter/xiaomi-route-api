# xiaomi-route-api
小米路由器api

使用方法：

```java
public class MiWifiTest {
    public static void main(String[] args) {
//         MiWifi api = new MiWifi("http://192.168.31.1","路由器管理密码");
        MiWifi api = new MiWifi("路由器管理密码");
        // 所有在线设备
        List<MiWifiDevice> devices = api.onlineDevice();
        // 禁止此MAC地址连接wifi  无法连接到wifi
        api.forbidConnWifi("0E:9D:6C:E6:96:25");
        //允许连接wifi 
        api.allowConnWifi("0E:9D:6C:E6:96:25");
        // 允许访问wan网
        api.allowWan("0E:9D:6C:E6:96:25");
        // 不允许访问wan网 ， 能连接上wifi但无法访问互联网，但可以连接局域网内设备。
        api.forbidWan("0E:9D:6C:E6:96:25");
    }
}
```
