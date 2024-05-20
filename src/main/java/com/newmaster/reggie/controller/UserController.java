package com.newmaster.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newmaster.reggie.common.Result;
import com.newmaster.reggie.entity.User;
import com.newmaster.reggie.service.UserService;
import com.newmaster.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码请求
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成4位随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为:{}", code);

            //调用阿里云短信服务API来完成发送短信任务
            //SMSUtils.sendMessage("", "", phone, code);

            //需要将生成的验证码保存到session
            // session.setAttribute(phone, code);

            //1.将随机生成的验证码缓存到Redis中,并设置有效期为5分钟(原先的session默认有效期是30分钟)
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return Result.success("手机验证码发送成功!");
        }
        return Result.error("短信发送失败");
    }

    /**
     * 登录校验
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从session中获取保存的验证码
        // Object codeInSession = session.getAttribute(phone);

        //从Redis中获取保存的验证码
        Object codeInRedis = redisTemplate.opsForValue().get(phone);

        //进行验证码的比对(页面提交的验证码与session中保存的验证码进行比对)
        if (codeInRedis != null && codeInRedis.equals(code)) {
            //如果比对成功====>登陆成功
            //判断当前手机号对应用户是否为新用户,如果是新用户就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断为:新用户====>自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            //2.如果用户登录成功,则将缓存的验证码删除
            redisTemplate.delete(phone);
            return Result.success(user);
        }

        return Result.error("登录失败");
    }
}
