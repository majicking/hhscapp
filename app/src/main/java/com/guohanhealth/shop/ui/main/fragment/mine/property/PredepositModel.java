package com.guohanhealth.shop.ui.main.fragment.mine.property;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.HealthInfo;
import com.guohanhealth.shop.bean.HealthNumInfo;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PaySignInfo;
import com.guohanhealth.shop.bean.PointInfo;
import com.guohanhealth.shop.bean.PredepositInfo;
import com.guohanhealth.shop.bean.RcdInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.bean.RedPagcketInfo;
import com.guohanhealth.shop.bean.VoucherInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import java.util.List;

import io.reactivex.Observable;

public class PredepositModel extends BaseModel {
    public Observable<MyAssectInfo> getMyAsset(String key) {
        return Api.getDefault().getMyAsset(key).compose(RxHelper.handleResult());
    }

    public Observable<PaySignInfo> recharge(String key, String number) {
        return Api.getDefault().recharge(key, number).compose(RxHelper.handleResult());
    }

    public Observable<RechargeOrderInfo> rechargeOrder(String key, String paysn) {
        return Api.getDefault().rechargeOrder(key, paysn).compose(RxHelper.handleResult());
    }

    public Observable<RcdInfo> predeposit(String key, String op, String curpage) {
        return Api.getDefault().predeposit(key, op, curpage, "10").compose(RxHelper.handleResult());
    }

    public Observable<VoucherInfo> voucherList(String key, String curpage) {
        return Api.getDefault().voucherList(key, curpage, "10").compose(RxHelper.handleResult());
    }

    public Observable<RedPagcketInfo> redpacketList(String key, String curpage) {
        return Api.getDefault().redpacketList(key, curpage, "10").compose(RxHelper.handleResult());
    }

    public Observable<PointInfo> getPointList(String key, String curpage) {
        return Api.getDefault().pointList(key, curpage, "10").compose(RxHelper.handleResult());
    }

    public Observable<HealthInfo> getHealthList(String key, String curpage) {
        return Api.getDefault().healthbeanlog(key, curpage, "10").compose(RxHelper.handleResult());
    }
    public Observable<HealthNumInfo> healthNum(String key) {
        return Api.getDefault().healthNum(key).compose(RxHelper.handleResult());
    }


    public Observable<PredepositInfo> pdcashAdd(String key,
                                                String pdc_amount,
                                                String pdc_bank_name,
                                                String pdc_bank_no,
                                                String pdc_bank_user,
                                                String mobilenum,
                                                String password) {
        return Api.getDefault().pdcashAdd(key, pdc_amount, pdc_bank_name,
                pdc_bank_no, pdc_bank_user, mobilenum, password, "android").compose(RxHelper.handleResult());
    }

    public Observable<PredepositInfo> rechargecardAdd(String key, String rc_sn, String captcha, String codekey) {
        return Api.getDefault().rechargecardAdd(key, rc_sn, captcha, codekey).compose(RxHelper.handleResult());
    }

}
