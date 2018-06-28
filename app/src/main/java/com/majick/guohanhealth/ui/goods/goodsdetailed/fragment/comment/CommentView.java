package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.comment;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.EvalInfo;

import java.util.List;

public interface CommentView extends BaseView {
    void getData(List<EvalInfo.Goods_eval_list> list);
}
