package com.guohanhealth.shop.bean;

import java.util.List;

public class EvalInfo extends BaseInfo {
    public List<Goods_eval_list> goods_eval_list;

    public static class Goods_eval_list {
        public String geval_scores;
        public String geval_content;
        public String geval_addtime;
        public String geval_frommemberid;
        public String geval_frommembername;
        public String geval_explain;
        public String geval_content_again;
        public String geval_addtime_again;
        public String geval_explain_again;
        public String member_avatar;
        public String geval_addtime_date;
        public List<String> geval_image_240;
        public List<String> geval_image_1024;
        public List<String> geval_image_again_240;
        public List<String> geval_image_again_1024;
        public String geval_addtime_again_date;

        @Override
        public String toString() {
            return "Goods_eval_list{" +
                    "geval_scores='" + geval_scores + '\'' +
                    ", geval_content='" + geval_content + '\'' +
                    ", geval_addtime='" + geval_addtime + '\'' +
                    ", geval_frommemberid='" + geval_frommemberid + '\'' +
                    ", geval_frommembername='" + geval_frommembername + '\'' +
                    ", geval_explain='" + geval_explain + '\'' +
                    ", geval_content_again='" + geval_content_again + '\'' +
                    ", geval_addtime_again='" + geval_addtime_again + '\'' +
                    ", geval_explain_again='" + geval_explain_again + '\'' +
                    ", member_avatar='" + member_avatar + '\'' +
                    ", geval_addtime_date='" + geval_addtime_date + '\'' +
                    ", geval_image_240=" + geval_image_240 +
                    ", geval_image_1024=" + geval_image_1024 +
                    ", geval_image_again_240=" + geval_image_again_240 +
                    ", geval_image_again_1024=" + geval_image_again_1024 +
                    ", geval_addtime_again_date='" + geval_addtime_again_date + '\'' +
                    '}';
        }
    }
}
