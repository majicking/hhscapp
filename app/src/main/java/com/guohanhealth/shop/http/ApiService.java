package com.guohanhealth.shop.http;


import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.bean.AddressManagerInfo;
import com.guohanhealth.shop.bean.Adv_list;
import com.guohanhealth.shop.bean.BaseInfo;
import com.guohanhealth.shop.bean.BrandListInfo;
import com.guohanhealth.shop.bean.CartInfo;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.bean.EvalInfo;
import com.guohanhealth.shop.bean.GoodsClassChildInfo;
import com.guohanhealth.shop.bean.GoodsClassInfo;
import com.guohanhealth.shop.bean.GoodsCollectInfo;
import com.guohanhealth.shop.bean.GoodsDetailedInfo;
import com.guohanhealth.shop.bean.GoodsListInfo;
import com.guohanhealth.shop.bean.Goods_hair_info;
import com.guohanhealth.shop.bean.HealthInfo;
import com.guohanhealth.shop.bean.HealthNumInfo;
import com.guohanhealth.shop.bean.HistoryInfo;
import com.guohanhealth.shop.bean.ImgCodeKey;
import com.guohanhealth.shop.bean.LoginBean;
import com.guohanhealth.shop.bean.LogisticsInfo;
import com.guohanhealth.shop.bean.MineInfo;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.OrderInfo;
import com.guohanhealth.shop.bean.PaySignInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.PointInfo;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.bean.PredepositInfo;
import com.guohanhealth.shop.bean.RcdInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.bean.RedPagcketInfo;
import com.guohanhealth.shop.bean.RefundlistInfo;
import com.guohanhealth.shop.bean.ReturnlistInfo;
import com.guohanhealth.shop.bean.SMSCode;
import com.guohanhealth.shop.bean.SearchInfo;
import com.guohanhealth.shop.bean.SearchWordsInfo;
import com.guohanhealth.shop.bean.SelectedInfo;
import com.guohanhealth.shop.bean.Step2Info;
import com.guohanhealth.shop.bean.StoreCollectInfo;
import com.guohanhealth.shop.bean.UpDataAddressInfo;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.bean.VoucherInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author:  andy.xwt
 * Date:    2017/12/20 10:28
 * Description:
 */


public interface ApiService {


    ///////////////////////////////////////////////////////////////////////////
    // 用户相关
    ///////////////////////////////////////////////////////////////////////////
    String LOGIN = Constants.INDEX + "act=login";
    String LOGININFO = Constants.INDEX + "act=member_chat&op=get_info";
    String REGISTER = Constants.INDEX + "act=login&op=register";
    String REGISTERMOBILE = Constants.INDEX + "act=connect&op=sms_register";
    String IMGKEYCODE = Constants.INDEX + "act=seccode&op=makecodekey";
    String SMSKEYCODE = Constants.INDEX + "act=connect&op=get_sms_captcha";
    String MINEINFO = Constants.INDEX + "act=member_index";
    String GOODSCLASS = Constants.INDEX + "act=goods_class";
    String BRAND = Constants.INDEX + "act=brand&op=recommend_list";
    String GOODSCLASSCHILD = Constants.INDEX + "act=goods_class&op=get_child_all";
    String HOMEINDEX = Constants.INDEX + "act=index&op=index";
    String SEARCHDATALIST = Constants.INDEX + "act=index&op=search_key_list";
    String SEARCHDATAWORDS = Constants.INDEX + "act=index&op=search_hot_info";
    String GOODSLIST = Constants.INDEX + "act=goods&op=goods_list";
    String SEARCH_ADV = Constants.INDEX + "act=index&op=search_adv";
    String GOODS_DETAIL = Constants.INDEX + "act=goods&op=goods_detail";
    String AREA_LIST = Constants.INDEX + "act=area&op=area_list";
    String AREAINFO = Constants.INDEX + "act=goods&op=calc";
    String CART_COUNT = Constants.INDEX + "act=member_cart&op=cart_count";
    String BUY_STEP1 = Constants.INDEX + "act=member_buy&op=buy_step1";
    String VBUY_STEP1 = Constants.INDEX + "act=member_vr_buy&op=buy_step1";
    String GOODS_BODY = Constants.INDEX + "act=goods&op=goods_body";
    String GOODS_EVALUATE = Constants.INDEX + "act=goods&op=goods_evaluate";
    String CHANGE_ADDRESS = Constants.INDEX + "act=member_buy&op=change_address";
    String BUY_STEP2 = Constants.INDEX + "act=member_buy&op=buy_step2";
    String PAYMENT_LIST = Constants.INDEX + "act=member_payment&op=payment_list";
    String ALIPAYURL = Constants.INDEX + "act=member_payment&op=alipay_native_pay";
    String ALIPAYURLV = Constants.INDEX + "act=member_payment&op=alipay_native_vr_pay";
    String WXPAYURL = Constants.INDEX + "act=member_payment&op=wx_app_pay3";
    String WXPAYURLV = Constants.INDEX + "act=member_payment&op=wx_app_vr_pay3";
    String CART_LIST = Constants.INDEX + "act=member_cart&op=cart_list";
    String CART_DEL = Constants.INDEX + "act=member_cart&op=cart_del";
    String CART_EDIT_QUANTITY = Constants.INDEX + "act=member_cart&op=cart_edit_quantity";
    String CART_ADD = Constants.INDEX + "act=member_cart&op=cart_add";
    String ORDER_LIST = Constants.INDEX + "act=member_order&op=order_list";
    String ORDER_LISTV = Constants.INDEX + "act=member_vr_order&op=order_list";
    String ORDER_OPERATION = Constants.INDEX + "act=member_order";
    String SEARCH_DELIVER = Constants.INDEX + "act=member_order&op=search_deliver";
    String MY_ASSET = Constants.INDEX + "act=member_index&op=my_asset";
    String RECHARGE = Constants.INDEX + "act=recharge";
    String RECHARGE_ORDER = Constants.INDEX + "act=recharge&op=recharge_order";
    String ALIPAYMENT = Constants.INDEX + "act=member_payment_recharge&op=alipay_native_pay&payment_code=alipay_native&pay_sn=";
    String WEIXINMENT = Constants.INDEX + "act=member_payment_recharge&op=wx_app_pay3&payment_code=wxpay&pay_sn=";
    String PREDEPOSIT = Constants.INDEX + "act=member_fund";
    String PDCASHADD = Constants.INDEX + "act=recharge&op=pd_cash_add";
    String PREDEPOIT = Constants.INDEX + "act=member_index&op=my_asset";
    String RECHARGECARD_ADD = Constants.INDEX + "act=member_fund&op=rechargecard_add";
    String VOUCHER_LIST = Constants.INDEX + "act=member_voucher&op=voucher_list";
    String VOUCHER_PWEX = Constants.INDEX + "act=member_voucher&op=voucher_pwex";
    String REDPACKET_LIST = Constants.INDEX + "act=member_redpacket&op=redpacket_list";
    String RP_PWEX = Constants.INDEX + "act=member_redpacket&op=rp_pwex";
    String POINTSLO = Constants.INDEX + "act=member_points&op=pointslog";
    String HEALTHBEANLOG = Constants.INDEX + "act=healthbean&op=HealthBeanLog";
    String GETHEALTHBEANVALUE = Constants.INDEX + "act=healthbean&op=GetHealthbeanValue";
    String ADDRESS_LIST = Constants.INDEX + "act=member_address&op=address_list";
    String ADDRESS_DEL = Constants.INDEX + "act=member_address&op=address_del";
    String ADDRESS_EDIT = Constants.INDEX + "act=member_address&op=address_edit";
    String ADDRESS_ADD = Constants.INDEX + "act=member_address&op=address_add";
    String GET_USER_LIST = Constants.INDEX + "act=member_chat&op=get_user_list";
    String SEND_MSG = Constants.INDEX + "act=member_chat&op=send_msg";
    String FAVORITES_LIST = Constants.INDEX + "act=member_favorites&op=favorites_list";
    String MEMBER_FAVORITES_STORE = Constants.INDEX + "act=member_favorites_store&op=favorites_list";
    String GOODSFAVORITES_DEL = Constants.INDEX + "act=member_favorites&op=favorites_del";
    String STOREFAVORITES_DEL = Constants.INDEX + "act=member_favorites_store&op=favorites_del";
    String BROWSE_LIST = Constants.INDEX + "act=member_goodsbrowse&op=browse_list";
    String BROWSE_CLEARALL = Constants.INDEX + "act=member_goodsbrowse&op=browse_clearall";
    String SPECIAL = Constants.INDEX + "act=index&op=special";
    String GET_RETURN_LIST = Constants.INDEX + "act=member_return&op=get_return_list";
    String GET_REFUND_LIST = Constants.INDEX + "act=member_refund&op=get_refund_list";
    String GET_REFUND_INFO = Constants.INDEX + "act=member_refund&op=get_refund_info";
    String ORDER_INFO = Constants.INDEX + "act=member_order&op=order_info";
    String MEMBER_EVALUATE = Constants.INDEX + "act=member_evaluate&op=index";
    String FILE_UPLOAD = Constants.INDEX + "act=sns_album&op=file_upload";
    String SAVE = Constants.INDEX + "act=member_evaluate&op=save";
    String SAVE_AGAIN = Constants.INDEX + "act=member_evaluate&op=save_again";
    String RECOMMEND_QR = Constants.INDEX + "act=qr&op=create&key=";
    String REFUND_FORM = Constants.INDEX + "act=member_refund&op=refund_form&key=";
    String REFUND_POST = Constants.INDEX + "act=member_refund&op=refund_post";
    String UPLOAD_PIC = Constants.INDEX + "act=member_refund&op=upload_pic";
    String MEMBER_EVALUATE_AGAIN = Constants.INDEX + "act=member_evaluate&op=again";
    String SIGN=Constants.INDEX +  "act=member_signin&op=checksignin";
    String SIGNIN_LIST=Constants.INDEX +  "act=member_signin&op=signin_list";


    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST(LOGIN)
    Observable<Result<LoginBean>> login(@Field("username") String userName,
                                        @Field("password") String passWord,
                                        @Field("client") String client);

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST(LOGININFO)
    Observable<Result<UserInfo>> getUserInfo(@Field("key") String key,
                                             @Field("u_id") String u_id,
                                             @Field("t") String t);

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST(REGISTER)
    Observable<Result<LoginBean>> register(@Field("username") String username,
                                           @Field("password") String password,
                                           @Field("password_confirm") String password_confirm,
                                           @Field("email") String email,
                                           @Field("client") String client,
                                           @Field("referral_code") String code);

    /**
     * 手机用户注册
     */
    @FormUrlEncoded
    @POST(REGISTERMOBILE)
    Observable<Result<LoginBean>> registerMobile(@Field("phone") String phone,
                                                 @Field("username") String username,
                                                 @Field("password") String password,
                                                 @Field("captcha") String captcha,
                                                 @Field("client") String client,
                                                 @Field("referral_code") String code);

    /**
     * 获取短信动态&phone={mobile}&sec_key={codeKey}&sec_val={sec_val}
     */
    @GET(SMSKEYCODE + "&type=1")
    Observable<Result<SMSCode>> getSMSCode(@Query("phone") String mobile,
                                           @Query("sec_key") String codeKey,
                                           @Query("sec_val") String sec_val);


    /**
     * 加载图片验证码
     */
    @GET(IMGKEYCODE)
    Observable<Result<ImgCodeKey>> getImgCodeKey(@Query("random") String random);

    /**
     * 初始化加载我的信息
     */
    @FormUrlEncoded
    @POST(MINEINFO)
    Observable<Result<MineInfo>> getMineInfo(@Field("key") String key);

    /**
     * 获取商品大类
     */
    @GET(GOODSCLASS)
    Observable<Result<GoodsClassInfo>> getGoodsClass();

    /**
     * 获取商品推荐
     */
    @GET(BRAND)
    Observable<Result<BrandListInfo>> getBrandList();

    /**
     * 根据id返回相应商品种类数据
     */
    @GET(GOODSCLASSCHILD)
    Observable<Result<GoodsClassChildInfo>> getGoodsChild(@Query("gc_id") String gc_id);


    /**
     * 获取热门搜索数据列表
     */
    @GET(SEARCHDATALIST)
    Observable<Result<SearchInfo>> getSearchDataList();

    /**
     * 获取热门搜索数据
     */
    @GET(SEARCHDATAWORDS)
    Observable<Result<SearchWordsInfo>> getSearchDataWords();

    /**
     * 获取商品列表
     */
    @GET(GOODSLIST)
    Observable<Result<GoodsListInfo>> getGoodsList(
            @Query("curpage") String curpage,
            @Query("page") String page,
            @Query("keyword") String keyword,
            @Query("key") String key,
            @Query("order") String order,
            @Query("price_from") String price_from,
            @Query("price_to") String price_to,
            @Query("area_id") String area_id,
            @Query("gift") String gift,
            @Query("groupbuy") String groupbuy,
            @Query("xianshi") String xianshi,
            @Query("virtual") String virtual,
            @Query("own_shop") String own_shop,
            @Query("ci") String ci,
            @Query("gc_id") String gc_id,
            @Query("b_id") String b_id
    );


    /**
     * 获取地区和服务
     */
    @GET(SEARCH_ADV)
    Observable<Result<SelectedInfo>> getSelectedInfo();

    /**
     * 获取地区地址
     */
    @GET(AREA_LIST)
    Observable<Result<Adv_list>> getAreaList(@Query("area_id") String area_id);

    /**
     * 获取地区地址返回信息
     */
    @GET(AREAINFO)
    Observable<Result<Goods_hair_info>> getAreaInfo(@Query("goods_id") String goods_id, @Query("area_id") String area_id);


    /**
     * 获取商品详细
     */
    @GET(GOODS_DETAIL)
    Observable<Result<GoodsDetailedInfo>> getGoodsDetails(@Query("goods_id") String goods_id, @Query("key") String key);

    /**
     * 获取购物车数量
     */
    @FormUrlEncoded
    @POST(CART_COUNT)
    Observable<Result<CartNumberInfo>> getCardNumber(@Field("key") String key);

    /**
     * 购买实体商品
     */
    @FormUrlEncoded
    @POST(BUY_STEP1)
    Observable<Result<BaseInfo>> buyStep1(@Field("key") String key, @Field("cart_id") String cart_id, @Field("ifcart") String ifcart);

    /**
     * 购买虚拟商品
     */
    @FormUrlEncoded
    @POST(VBUY_STEP1)
    Observable<Result<BaseInfo>> buyStep1V(@Field("key") String key, @Field("goods_id") String goods_id, @Field("quantity") String goodsNumber);

    /**
     * 获取评论
     */
    @GET(GOODS_EVALUATE)
    Observable<Result<EvalInfo>> goodsEvaluate(@Query("goods_id") String goods_id,
                                               @Query("curpage") String curpage,
                                               @Query("type") String type,
                                               @Query("page") String page);


    /**
     * @param key
     * @param city_id
     * @param area_id
     * @param freight_hash
     */
    @FormUrlEncoded
    @POST(CHANGE_ADDRESS)
    Observable<Result<UpDataAddressInfo>> changedAdress(@Field("key") String key,
                                                        @Field("city_id") String city_id,
                                                        @Field("area_id") String area_id,
                                                        @Field("freight_hash") String freight_hash);

    /**
     * 购买商品提交订单
     *
     * @param key
     * @param cart_id
     * @param ifcart
     * @param address_id
     * @param vat_hash
     * @param offpay_hash
     * @param offpay_hash_batch
     * @param pay_name
     * @param invoice_id
     * @param pd_pay
     * @param rcb_pay
     * @param healthbean_pay
     * @param password
     * @param client
     */
    @FormUrlEncoded
    @POST(BUY_STEP2)
    Observable<Result<Step2Info>> buyStep2(@Field("key") String key,
                                           @Field("cart_id") String cart_id,
                                           @Field("ifcart") String ifcart,
                                           @Field("address_id") String address_id,
                                           @Field("vat_hash") String vat_hash,
                                           @Field("offpay_hash") String offpay_hash,
                                           @Field("offpay_hash_batch") String offpay_hash_batch,
                                           @Field("pay_name") String pay_name,
                                           @Field("invoice_id") String invoice_id,
                                           @Field("pd_pay") String pd_pay,
                                           @Field("rcb_pay") String rcb_pay,
                                           @Field("healthbean_pay") String healthbean_pay,
                                           @Field("password") String password,
                                           @Field("client") String client);

    /**
     * 购买商品提交订单
     *
     * @param key
     * @param goods_id
     * @param quantity
     * @param buyer_phone
     * @param pd_pay
     * @param rcb_pay
     * @param password
     * @param healthbean_pay
     * @param client;
     */
    @FormUrlEncoded
    @POST(BUY_STEP2)
    Observable<Result<Step2Info>> buyStep2V(@Field("key") String key,
                                            @Field("goods_id") String goods_id,
                                            @Field("quantity") String quantity,
                                            @Field("buyer_phone") String buyer_phone,
                                            @Field("pd_pay") String pd_pay,
                                            @Field("rcb_pay") String rcb_pay,
                                            @Field("healthbean_pay") String healthbean_pay,
                                            @Field("password") String password,
                                            @Field("client") String client);


    /**
     * 获取支付方式列表
     *
     * @param key
     */
    @FormUrlEncoded
    @POST(PAYMENT_LIST)
    Observable<Result<PayWayInfo>> getPaymentList(@Field("key") String key);


    /**
     * 获取购物车列表
     *
     * @param key
     */
    @FormUrlEncoded
    @POST(CART_LIST)
    Observable<Result<CartInfo>> getCartList(@Field("key") String key);


    /**
     * 删除购物车商品
     *
     * @param key
     * @param cart_id
     */
    @FormUrlEncoded
    @POST(CART_DEL)
    Observable<Result<Object>> delCart(@Field("key") String key, @Field("cart_id") String cart_id);

    /**
     * 修改购物车商品数量
     *
     * @param key
     * @param cart_id
     * @param quantity
     */
    @FormUrlEncoded
    @POST(CART_EDIT_QUANTITY)
    Observable<Result<BaseInfo>> upCartNumber(@Field("key") String key, @Field("cart_id") String cart_id, @Field("quantity") String quantity);

    /**
     * 添加到购物车
     *
     * @param key
     * @param goods_id 商品id
     * @param quantity 购买数量
     */
    @FormUrlEncoded
    @POST(CART_ADD)
    Observable<Result<Object>> addCart(@Field("key") String key, @Field("goods_id") String goods_id, @Field("quantity") String quantity);

    /**
     * 实物订单
     */
    @FormUrlEncoded
    @POST(ORDER_LIST + "&page=10")
    Observable<Result<OrderInfo>> getOrderList(@Field("key") String key,
                                               @Field("state_type") String state_type,
                                               @Field("curpage") String curpage,
                                               @Field("order_key") String order_key);

    /**
     * 虚拟订单
     */
    @FormUrlEncoded
    @POST(ORDER_LISTV + "&page=10")
    Observable<Result<OrderInfo>> getOrderListV(@Field("key") String key,
                                                @Field("state_type") String state_type,
                                                @Field("curpage") String curpage,
                                                @Field("order_key") String order_key);


    /**
     * 删除订单/取消订单/确认订单/order_receive order_delete order_cancel
     */
    @FormUrlEncoded
    @POST(ORDER_OPERATION)
    Observable<Result> orderOperation(@Query("op") String op, @Field("key") String key, @Field("order_id") String order_id);


    /**
     * 物流查询
     */
    @FormUrlEncoded
    @POST(SEARCH_DELIVER)
    Observable<Result<LogisticsInfo>> searchLogistics(@Field("key") String key, @Field("order_id") String order_id);

    /**
     * 财产查询
     */
    @FormUrlEncoded
    @POST(MY_ASSET)
    Observable<Result<MyAssectInfo>> getMyAsset(@Field("key") String key);

    /**
     * 充值
     */
    @FormUrlEncoded
    @POST(RECHARGE)
    Observable<Result<PaySignInfo>> recharge(@Field("key") String key, @Field("pdr_amount") String pdr_amount);

    /**
     * 充值订单
     */
    @FormUrlEncoded
    @POST(RECHARGE_ORDER)
    Observable<Result<RechargeOrderInfo>> rechargeOrder(@Field("key") String key, @Field("paysn") String paysn);

    /**
     * 预存款  &op=predepositlog &op=pdrechargelist &op=pdcashlist MEMBER_FUND
     */
    @FormUrlEncoded
    @POST(PREDEPOSIT)
    Observable<Result<RcdInfo>> predeposit(@Field("key") String key,
                                           @Query("op") String op,
                                           @Query("curpage") String curpage,
                                           @Query("page") String page);

    /**
     * 获取预存款数据
     */
    @GET(PREDEPOIT)
    Observable<Result<PreInfo>> getPre(@Query("key") String key, @Query("fields") String fields);

    /**
     * 预存款提现
     */
    @FormUrlEncoded
    @POST(PDCASHADD)
    Observable<Result<PredepositInfo>> pdcashAdd(@Field("key") String key,
                                                 @Field("pdc_amount") String pdc_amount,
                                                 @Field("pdc_bank_name") String pdc_bank_name,
                                                 @Field("pdc_bank_no") String pdc_bank_no,
                                                 @Field("pdc_bank_user") String pdc_bank_user,
                                                 @Field("mobilenum") String mobilenum,
                                                 @Field("password") String password,
                                                 @Field("client") String client);

    /**
     * 充值卡充值
     */
    @FormUrlEncoded
    @POST(RECHARGECARD_ADD)
    Observable<Result<PredepositInfo>> rechargecardAdd(@Field("key") String key,
                                                       @Field("rc_sn") String rc_sn,
                                                       @Field("captcha") String captcha,
                                                       @Field("codekey") String codekey);

    /**
     * 代金卷列表
     */
    @GET(VOUCHER_LIST)
    Observable<Result<VoucherInfo>> voucherList(@Query("key") String key,
                                                @Query("curpage") String curpage,
                                                @Query("page") String page);

    /**
     * 红包列表
     */
    @GET(REDPACKET_LIST)
    Observable<Result<RedPagcketInfo>> redpacketList(@Query("key") String key,
                                                     @Query("curpage") String curpage,
                                                     @Query("page") String page);

    /**
     * 积分列表
     */
    @GET(POINTSLO)
    Observable<Result<PointInfo>> pointList(@Query("key") String key,
                                            @Query("curpage") String curpage,
                                            @Query("page") String page);

    /**
     * 健康豆列表
     */
    @GET(HEALTHBEANLOG)
    Observable<Result<HealthInfo>> healthbeanlog(@Query("key") String key,
                                                 @Query("curpage") String curpage,
                                                 @Query("page") String page);

    /**
     * 收藏商品列表
     */
    @GET(FAVORITES_LIST)
    Observable<Result<GoodsCollectInfo>> goodsCollectList(@Query("key") String key,
                                                          @Query("curpage") String curpage,
                                                          @Query("page") String page);

    /**
     * 收藏店铺列表
     */
    @GET(MEMBER_FAVORITES_STORE)
    Observable<Result<StoreCollectInfo>> storeCollectList(@Query("key") String key,
                                                          @Query("curpage") String curpage,
                                                          @Query("page") String page);

    /**
     * 历史纪录列表
     */
    @GET(BROWSE_LIST)
    Observable<Result<HistoryInfo>> historyList(@Query("key") String key,
                                                @Query("curpage") String curpage,
                                                @Query("page") String page);

    /**
     * 退货列表
     */
    @GET(GET_RETURN_LIST)
    Observable<Result<ReturnlistInfo>> geReturnList(@Query("key") String key,
                                                    @Query("curpage") String curpage,
                                                    @Query("page") String page);

    /**
     * 退款列表
     */
    @GET(GET_REFUND_LIST)
    Observable<Result<RefundlistInfo>> geRefundList(@Query("key") String key,
                                                    @Query("curpage") String curpage,
                                                    @Query("page") String page);

    /**
     * 健康豆值
     */
    @GET(GETHEALTHBEANVALUE)
    Observable<Result<HealthNumInfo>> healthNum(@Query("key") String key);

    /**
     * 获取收货地址
     */
    @FormUrlEncoded
    @POST(ADDRESS_LIST)
    Observable<Result<AddressManagerInfo>> getAddressList(@Field("key") String key);


//    /**
//     * 直接进行下单请求
//     *
//     * @param type            支付方式：1-微信 2-支付宝 3-现金 4-其他
//     * @param orderId         收银id
//     * @param collectedAmount 实收金额
//     * @param authCode        支付码(走现金的时候，不传）
//     */
//    @FormUrlEncoded
//    @POST("finance/revenue/payRev")
//    Observable<Result<FinanceRevenueInfo>> doOrder(@Field("type") int type,
//                                                   @Field("id") String orderId,
//                                                   @Field("collectedAmount") double collectedAmount,
//                                                   @Field("authCode") String authCode);
//
//    /**
//     * 添加快速收银待收款记录
//     *
//     * @param type             支付方式：1-微信 2-支付宝 3-现金 4-其他
//     * @param receivalbeAmount 应收金额(不能为空且大于零)
//     */
//    @FormUrlEncoded
//    @POST("finance/revenue/addFastRev")
//    Observable<Result<FinanceRevenueInfo>> addQuickWaitGather(@Field("type") int type,
//                                                              @Field("receivalbeAmount") double receivalbeAmount);
//
//    /**
//     * 添加点单收银待收款记录
//     *
//     * @param type    支付方式：1-微信 2-支付宝 3-现金 4-其他
//     * @param orderId 订单id
//     */
//    @FormUrlEncoded
//    @POST("finance/revenue/addOrderRev")
//    Observable<Result<FinanceRevenueInfo>> addOrderWaitGather(@Field("type") int type,
//                                                              @Field("orderId") String orderId);
//
//    /**
//     * 撤销订单
//     */
//    @FormUrlEncoded
//    @POST("finance/revenue/reverseOrder/{orderId}")
//    Observable<Result<FinanceRevenueInfo>> reverseOrder(@Field("orderId") String orderId);
//
//    /**
//     * 订单分页查询
//     *
//     * @param appId   应用ID
//     * @param userId  当前用户ID
//     * @param status  1-待付款 2-待提货 3-交易完成 4-订单取消 5-退款审核 6-待退款(审核通过)7-退款失败(审核未通过) 8-退款成功 9-system退款失败(待退款) 10-取消订单(申请退款)
//     * @param keyword 关键字，模糊匹配“订单号，收货人姓名，收货电话”
//     */
//    @FormUrlEncoded
//    @POST("order/findPageList4App/{appId}/{openId}")
//    Observable<Result<List<OrderInfo>>> queryOrder(@Path("appId") String appId,
//                                                   @Path("openId") String userId,
//                                                   @Field("status") String status,
//                                                   @Field("keyword") String keyword,
//                                                   @Field("pageIndex") int pageIndex,
//                                                   @Field("pageSize") int pageSize);
//
//    /**
//     * 收银记录
//     *
//     * @param status  收银状态，默认为全部状态(1-待收款 2-已收款 3-已取消 4-待退款 5-已退款),多状态请用英文逗号(,)分隔
//     * @param keyword 关键字，例如：交易流水、交易号、商品名称、商品规格
//     */
//    @FormUrlEncoded
//    @POST("finance/revenue/findPageList")
//    Observable<Result<ListFinanceRevenueInfo>> queryChargeRecord(@Field("status") String status,
//                                                                 @Field("keyword") String keyword,
//                                                                 @Field("pageIndex") int pageIndex,
//                                                                 @Field("pageSize") int pageSize);
//
//
//    /**
//     * 收银记录
//     *
//     * @param status  收银状态，默认为全部状态(1-待收款 2-已收款 3-已取消 4-待退款 5-已退款),多状态请用英文逗号(,)分隔
//     * @param keyword 关键字，例如：交易流水、交易号、商品名称、商品规格
//     */
//    @FormUrlEncoded
//    @POST("finance/revenue/findPageList")
//    Observable<Result<ListFinanceRevenueInfo>> queryRecordByKeyword(@Field("status") String status,
//                                                                    @Field("keyword") String keyword);
//
//    /**
//     * 查询记录详情
//     */
//    @POST("finance/revenue/find/{id}")
//    Observable<Result<RecordInfo>> getRecordDetail(@Path("id") String id);
//
//
//    /**
//     * 退款申请
//     *
//     * @param orderId       订单ID
//     * @param refundAmount  退款金额，必须大于零
//     * @param description   退款备注
//     * @param orderDetailId 订单明细ID，多个时请使用英文逗号(,)分隔
//     * @param quantity      退货数量，多个时请使用英文逗号(,)分隔
//     */
//    @POST("order/rejected/rejectedGoods4Pos")
//    @FormUrlEncoded
//    Observable<Result<RecordInfo>> rejectGoods(@Field("orderId") String orderId,
//                                               @Field("refundAmount") String refundAmount,
//                                               @Field("description") String description,
//                                               @Field("orderDetailId") String orderDetailId,
//                                               @Field("quantity") String quantity);
//
//    /**
//     * 修改退款备注
//     *
//     * @param refundId    退款id
//     * @param description 退款描述
//     */
//    @POST("order/rejected/updateDescription/{id}")
//    @FormUrlEncoded
//    Observable<Result> modifyRefundNote(@Path("id") String refundId, @Field("description") String description);
//
//    ///////////////////////////////////////////////////////////////////////////
//    // 商品相关
//    ///////////////////////////////////////////////////////////////////////////
//
//    /**
//     * 获取商品分类信息
//     */
//    @POST("goods/type/findPageList4App/{appId}")
//    Observable<Result<GoodsType>> getGoodsType(@Path("appId") String appId);
//
//    /**
//     * 获取商品信息
//     *
//     * @param appId      应用ID
//     * @param typeId     商品分类ID
//     * @param pageIndex  当前页码，默认不分页
//     * @param pageSize   每页显示的记录数，默认不分页
//     * @param sortFields 自定义排序字段集(sortFields)，排序字段和排序方式(ASC,DESC)使用空格分开，多字段排序使用英文逗号(,)分隔 ，可根据商品名称(name)、商品分类名称(typeName)、商品价格(price)、商品排序号(orderNo)字段排序，
//     */
//    @FormUrlEncoded
//    @POST("goods/info/findBoPageList/{appId}")
//    Observable<Result<GoodsInfo>> getGoodsInfo(@Path("appId") String appId, @Field("typeId") String typeId,
//                                               @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize,
//                                               @Field("sortFields") String sortFields);
//
//    /**
//     * 获取商品分类信息
//     *
//     * @param typeId    商品分类id
//     * @param status    发布状态 1 已发布，2 未发布
//     * @param pageIndex 当前页码，默认不分页
//     * @param pageSize  每页显示的记录数，默认不分页
//     */
//    @FormUrlEncoded
//    @POST("goods/info/findPageList")
//    Observable<Result<GoodsTypeStateInfo>> getGoodsClassifyInfo(@Field("typeId") String typeId,
//                                                                @Field("status") int status,
//                                                                @Field("pageIndex") int pageIndex,
//                                                                @Field("pageSize") int pageSize);
//
//    /***
//     * 获取商品详情
//     * @param id
//     * @return
//     */
//    @POST("goods/info/find/{id}")
//    Observable<Result<GoodsInfo.GoodsInfoWrapper>> getShopDetails(@Path("id") String id);
//
//    /***
//     * 删除商品
//     * @param id
//     * @return
//     */
//    @POST("goods/info/del/{id}")
//    Observable<Result<Boolean>> deleShop(@Path("id") String id);
//
//
//    /**
//     * 获取商品轮播图
//     */
//    @POST("goods/picture/find/{id}")
//    Observable<Result<String>> shopPicture(@Path("id") String id);
//
//
//    /**
//     * 新建商品分类
//     *
//     * @param name 分类名称
//     */
//    @FormUrlEncoded
//    @POST("goods/type/add")
//    Observable<Result<GoodsInfo.GoodsInfoWrapper.GoodsTypeRelationship>> addShopType(@Field("name") String name);
//
//    /**
//     * 新建商品草稿，用于商品的保存，状态为未发布
//     *
//     * @param goodsInfo 商品对象
//     */
//    @POST("goods/info/addDraft")
//    Observable<Result> addGoodsDraft(@Body GoodsInfo.GoodsInfoWrapper goodsInfo);
//
//
//    /***
//     * 保存商品
//     * @param goodsInfo
//     */
//    @POST("goods/info/updateDraft")
//    Observable<Result<GoodsInfo.GoodsInfoWrapper>> shopSave(@Body GoodsInfo.GoodsInfoWrapper goodsInfo);
//
//
//    /***
//     * 修改商品并发布商品
//     * @param
//     * @return
//     */
//    @POST("goods/info/updateNormal")
//    Observable<Result<GoodsInfo.GoodsInfoWrapper>> shopIusse(@Body GoodsInfo.GoodsInfoWrapper goodsInfo);
//
//    /**
//     * 直接发布商品
//     *
//     * @param goodsInfo 商品对象
//     */
//    @POST("goods/info/addNormal")
//    Observable<Result> publishGoods(@Body GoodsInfo.GoodsInfoWrapper goodsInfo);
//
//
//    ///////////////////////////////////////////////////////////////////////////
//    // 文件相关
//    ///////////////////////////////////////////////////////////////////////////
//
//    /**
//     * 文件上传
//     */
//    @Multipart
//    @POST("space/uploadImgBatch")
//    Observable<Result<List<UploadFile>>> uploadImage(@Part MultipartBody.Part[] file);


}
