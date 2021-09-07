package com.bank.web.controller;


import com.bank.api.user.service.IUserService;
import com.bank.common.redis.RedisUtil;
import com.bank.common.verificationcode.GenerateVCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户
 *
 * @author huzhen
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    @Resource
    RedisUtil redisUtil;

    @RequestMapping("test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        try {

            int width = 200;

            int height = 69;


            //生成对应宽高的初始图片
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            String randomText = GenerateVCode.drawRandomText(width, height, verifyImg);

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

