package com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.comment;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.EvalInfo;

import java.util.List;

public interface CommentView extends BaseView {
    void getData(List<EvalInfo.Goods_eval_list> list);
}
