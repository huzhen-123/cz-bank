package com.bank.web.controller;

import com.bank.common.IP.IPUtils;
import com.bank.common.String.StrExChange;
import com.bank.common.redis.RedisConstants;
import com.bank.common.redis.RedisUtil;
import com.bank.common.verificationcode.GenerateVCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 验证码
 *
 * @author huzhen
 * @since 2021-08-10
 */
@Controller
@RequestMapping("/verify_code")
public class VerifyCodeController {

    @Resource
    private RedisUtil redisUtil;

    @RequestMapping("/get")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            int width = 200;
            int height = 69;
            //生成对应宽高的初始图片
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            String randomText = GenerateVCode.drawRandomText(width, height, verifyImg);
            //将验证码存入redis用于验证,一分钟销毁
            String ip = IPUtils.getIpAddr(request);
            redisUtil.set(ip, randomText, 60);
            //验证码错误次数，初始为0
            String errorCountKey = ip + ":" + StrExChange.exChange(randomText);//ip+转大写的验证码 作为key
            redisUtil.set(errorCountKey, "0", 60);
            request.getSession().setAttribute("verifyCode", randomText);
            response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
            OutputStream os = response.getOutputStream(); //获取文件输出流

            ImageIO.write(verifyImg, "png", os);//输出图片流
            os.flush();
            os.close();//关闭流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
