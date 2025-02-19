package com.njit.infoshareserve.utils;

import com.njit.infoshareserve.bean.Momcomment;

import java.util.ArrayList;
import java.util.List;

public class CommentDealUtil {
    /**
     * 处理动态评论数据--普通方法,供上方函数调用
     */
    public static List<Momcomment> dealMomCommentList(List<Momcomment> originList) {
        /** 处理评论数据 */
        List<Momcomment> commentList = new ArrayList<>();
        List<Momcomment> tempSubList;
        Momcomment tempTopComment, tempSubComment;
        Integer fatherId;
        for (int i = 0; i < originList.size(); i++) {
            if (originList.get(i).getCommentlevel() == 1) {
                //是顶级评论，直接加入commentList
                commentList.add(originList.get(i));
            } else {
                //不是顶级评论，则在commentList遍历找它的顶级评论，并加入
                for (int j = 0; j < commentList.size(); j++) {
                    if (commentList.get(j).getCommentid() == originList.get(i).getTopcommentid()) {
                        //用tempComment暂存顶级评论
                        tempTopComment = commentList.get(j);

                        if (tempTopComment.getSubcommentlist() != null)
                            tempSubList = tempTopComment.getSubcommentlist();
                        else
                            tempSubList = new ArrayList<>();

                        //用tempSubComment暂存子级评论
                        tempSubComment = originList.get(i);
                        if (tempSubComment.getCommentlevel() == 2) {
                            //如果是二级评论，直接加入子评论列表
                            tempSubList.add(tempSubComment);
                        } else {
                            //如果是三级 三级以上子评论，通过父评论id找到父评论者的username【而父评论者一定在该顶级评论的子评论列表中】
                            fatherId = tempSubComment.getFatherid();
                            for (int k = 0; k < tempSubList.size(); k++) {
                                if (tempSubList.get(k).getCommentid() == fatherId) {
                                    tempSubComment.setFatherusername(tempSubList.get(k).getUsername());
                                    tempSubComment.setFatheruserid(tempSubList.get(k).getUserid());
                                    break;
                                }
                            }
                            tempSubList.add(tempSubComment);
                        }

                        //顶级评论tempComment更新
                        tempTopComment.setSubcommentlist(tempSubList);
                        //commentList更新
                        commentList.set(j, tempTopComment);
                        break;
                    }
                }
            }
        }
        return commentList;
    }
}
