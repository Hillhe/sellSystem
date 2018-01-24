package com.learnspringboot.sell.conf;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatMpConf {

    @Autowired
    private WechatAccountConf wechatAccountConf;

    @Bean
    public WxMpService wxMpService(){
       WxMpService wxMpService = new WxMpServiceImpl();
       wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
       return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(wechatAccountConf.getMpAppId());
        wxMpConfigStorage.setSecret(wechatAccountConf.getMpAppsecret());
        return wxMpConfigStorage;
    }
}
