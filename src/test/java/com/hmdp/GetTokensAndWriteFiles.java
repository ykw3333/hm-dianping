package com.hmdp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

@SpringBootTest
public class GetTokensAndWriteFiles {

    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void getTokensAndWriteFiles() throws IOException {
        List<User> users = userService.lambdaQuery().last("limit 1000").list();
        FileWriter fr = null;
        try {
            fr = new FileWriter("D:\\3_JavaSoftWare\\apache-jmeter-5.6.3\\bin\\Redis_HMDP\\tokens.txt");//文件位置
            for (User user : users) {
                // 7.保存用户信息到 Redis 中
                // 7.1。随机生成 token 作为登录令牌
                String token = UUID.randomUUID().toString(true);
                // 7.2.将 user 对象 转化为 hashmap 存储
                UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
                Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                        CopyOptions.create()
                                .setIgnoreNullValue(true)
                                .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
                // 7.3.存储
                String tokenKey = LOGIN_USER_KEY + token;
                stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
                // 7.4.设置 token 有效期
                stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

                //将Redis中token写入文件中
                fr.append(token);
                fr.append("\n");
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally{
            fr.close();
        }
    }
}

