package com.njit.infoshareserve;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.mapper.*;
import com.njit.infoshareserve.recommend.OfflineRecommed;
import com.njit.infoshareserve.recommend.OnlineRecommend;
import com.njit.infoshareserve.recommend.StatisticalRecommend;
import com.njit.infoshareserve.service.*;
import com.njit.infoshareserve.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class InfoShareServeApplicationTests {

    @Autowired
    OfflineRecommed offlineRecommed;
    @Autowired
    OnlineRecommend onlineRecommend;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    PredictSetRateService predictSetRateService;
    @Autowired
    SetSimilarService setSimilarService;
    @Autowired
    MailUtil mailUtil;

    @Test
    //训练模型
    void test2() {
        offlineRecommed.LFM_Predict();
    }

    @Test
    void test4() throws Exception {
        mailUtil.sendSimpleMail("w774467672@163.com", "这里是标题2", "这里是内容2");
    }
    @Test
    void test3() {
        /** 传入python脚本的参数 */
        String[] args1 = new String[]{"python", "D:\\linshi\\test.py"};  //设定命令行

        try {
            // 如果是python脚本中需要用到第三方库，则最好要用Runtime.getRuntime().exec的方法来从Java中调用python
            // 执行Python文件，并传入参数
            Process process = Runtime.getRuntime().exec(args1);
            // 获取Python输出字符串作为输入流被Java读取
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String resStr = in.readLine();
            if (resStr != null) {
                System.out.println(resStr);
                String[] split = resStr.split("==item_similar_dict==");
                for (int i = 0; i < split.length; i++) {
                    System.out.println(split[i]);
                }
            }

            in.close();
            process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
