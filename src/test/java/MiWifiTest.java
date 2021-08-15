import com.github.zbuter.miwifi.MiWifi;
import com.github.zbuter.miwifi.MiWifiService;
import com.github.zbuter.miwifi.model.MiWifiDevice;

import java.util.List;

/**
 * xiaomi-route-api
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
public class MiWifiTest {
    public static void main(String[] args) {
        MiWifi miWifi = new MiWifi("123456789.fa");
//        List<MiWifiDevice> devices = miWifi.onlineDevice();
        boolean b = miWifi.allowConnWifi("0E:9D:6C:E6:96:25");
        List<MiWifiDevice> devices = miWifi.onlineDevice();
        miWifi.forbidWan("fas");
        System.out.println(devices);
    }
}
