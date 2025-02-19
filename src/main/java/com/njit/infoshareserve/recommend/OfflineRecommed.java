package com.njit.infoshareserve.recommend;

import com.njit.infoshareserve.bean.PredictSetRate;
import com.njit.infoshareserve.bean.Rateset;
import com.njit.infoshareserve.bean.SetSimilar;
import com.njit.infoshareserve.service.PredictSetRateService;
import com.njit.infoshareserve.service.RatesetService;
import com.njit.infoshareserve.service.SetSimilarService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

@Component
public class OfflineRecommed {

    @Autowired
    RatesetService ratesetService;
    @Autowired
    PredictSetRateService predictSetRateService;
    @Autowired
    SetSimilarService setSimilarService;
    @Value("${py-uri}")
    private String pyPath;

    public void LFM_Predict() {
        // 需python脚本的绝对路径，在windows中用"\\"分隔，在Linux中用"/"分隔
//        String pyPath = "E:\\LFM_GradDesc.py";

        /** 获取已有真实评分数据，并转类型 */
        List<Rateset> ratesetList = ratesetService.list();
        JSONArray jsArr = JSONArray.fromObject(ratesetList);
        for (int i = 0; i < jsArr.size(); i++) {
            //去除createtime字段，减小数据量
            ((JSONObject)jsArr.get(i)).remove("createtime");
        }
        String ratesetsStr = jsArr.toString();
        System.out.println(ratesetsStr);
        //转义，要不然python收到的字典key没有双引号，认不出字典
        String newRatesetsStr = ratesetsStr.replaceAll("\"","\\\\\"");

        /** 传入python脚本的参数 */
        String[] args1 = new String[]{"python", pyPath, newRatesetsStr};  //设定命令行

        try {
            // 如果是python脚本中需要用到第三方库，则最好要用Runtime.getRuntime().exec的方法来从Java中调用python
            // 执行Python文件，并传入参数
            Process process = Runtime.getRuntime().exec(args1);
            // 获取Python输出字符串作为输入流被Java读取
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String resStr = in.readLine();
            if (resStr != null) {
                //截断表
                predictSetRateService.truncateTable();
                setSimilarService.truncateTable();

                //切分结果字符串
                String[] split = resStr.split("=item_similar_dict=");

                /** 第一部分为预测用户评分矩阵 */
                JSONObject predRates = JSONObject.fromObject(split[0]);
                Iterator<String> keys = predRates.keys();
                PredictSetRate rate = new PredictSetRate();
                String next, subNext;
                JSONObject subRates;
                Iterator subKeys;
                while(keys.hasNext()) { //userid
                    next = keys.next();
                    rate.setUserid(Integer.parseInt(next));

                    subRates = JSONObject.fromObject(predRates.get(next));
                    subKeys = subRates.keys();
                    while(subKeys.hasNext()) { //setid
                        subNext = subKeys.next().toString();
                        rate.setSetid(Integer.parseInt(subNext));
                        rate.setRate((Double) subRates.get(subNext));
                        // 插入
                        predictSetRateService.save(rate);
                    }
                }

                /** 第二部分为物品相似度，用于实时推荐，存成json比较好 */
                JSONObject sim_json = JSONObject.fromObject(split[1]);
                keys = sim_json.keys();
                String sim_dict;
                SetSimilar setSimilar = new SetSimilar();
                while(keys.hasNext()) {
                    next = keys.next();
                    sim_dict = sim_json.get(next).toString();
                    setSimilar.setSetid(Integer.parseInt(next));
                    setSimilar.setSimilarSets(sim_dict);
                    setSimilarService.save(setSimilar);
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
