package com.github.zbuter.miwifi;

import cn.hutool.core.util.StrUtil;
import com.github.zbuter.miwifi.DO.MiWifiBaseDO;
import com.github.zbuter.miwifi.DO.MiWifiLoginDO;
import com.github.zbuter.miwifi.exceptions.MiWifiException;
import com.github.zbuter.miwifi.exceptions.MiWifiLoginException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * xiaomi-route-api
 * <br />
 *
 * @author: Jiashun Zhang
 * @since: 2021-08-14
 */
public class MiWifiInvocationHandler implements InvocationHandler {
    private MiWifiApi api;
    public MiWifiInvocationHandler(MiWifiApi api){
        this.api = api;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MiWifiBaseDO vo = (MiWifiBaseDO)method.invoke(api, args);
        if (vo.getCode()==401) {
            MiWifiLoginDO login = api.login();
            if(StrUtil.isBlank(login.getToken())){
                throw new MiWifiLoginException("用户名密码错误");
            }
            return method.invoke(api, args);
        }
        return method.invoke(api,args);
    }
}
