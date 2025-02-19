package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.ResultData;
import com.njit.infoshareserve.bean.Watch;
import com.njit.infoshareserve.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    WatchService watchService;

    /**
     * 获取历史记录
     * @param userId
     */
    @GetMapping("/getHistory/{userId}")
    public ResultData<List<Watch>> getHistory(@PathVariable("userId") Integer userId) {
        List<Watch> setHistory = watchService.getSetHistory(userId);
        return ResultData.success(setHistory);
    }

    /**
     * 删除一条历史
     * @param map  userid，setid
     */
    @PostMapping("/deleteHistory")
    public ResultData<String> deleteHistory(@RequestBody Map<String,Integer> map) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",map.get("userid"));
        queryWrapper.eq("setId",map.get("setid"));
        Boolean success = watchService.remove(queryWrapper);
        if(success)
            return ResultData.success("删除成功");
        return ResultData.fail(500,"删除失败");
    }

    /**
     * 删除用户所有历史记录
     * @param map userid
     * @return
     */
    @PostMapping("/deleteAllHistory")
    public ResultData<String> deleteAllHistory(@RequestBody Map<String,Integer> map){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",map.get("userid"));
        Boolean success = watchService.remove(queryWrapper);
        if(success)
            return ResultData.success("删除成功");
        return ResultData.fail(500,"删除失败");
    }
}
