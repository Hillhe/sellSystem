package com.learnspringboot.sell.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping(value = "/auth")
    public void auth(@RequestParam("code") String code, @RequestParam("state") String state){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx18fd4f031d4d5e6c&secret=ef279276932dc805825bf48bd7340b93&code="+code+"&grant_type=authorization_code";
        log.info("进入auth code = {}, state = {}", code, state);
        RestTemplate restTemplate = new RestTemplate();
        String resp = restTemplate.getForObject(url, String.class);
        log.info("resp = {}", resp);
    }
}
