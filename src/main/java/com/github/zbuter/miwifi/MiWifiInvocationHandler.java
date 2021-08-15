package com.github.zbuter.miwifi;

import cn.hutool.core.util.StrUtil;
import com.github.zbuter.miwifi.VO.MiWifiBaseVO;
import com.github.zbuter.miwifi.VO.MiWifiLoginVO;

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
        MiWifiBaseVO vo = (MiWifiBaseVO)method.invoke(api, args);
        if (vo.getCode()==401) {
            MiWifiLoginVO login = api.login();
            if(StrUtil.isBlank(login.getToken())){
                throw new RuntimeException("用户名密码错误");
            }
            return method.invoke(api, args);
        }
        return method.invoke(api,args);
    }
}
