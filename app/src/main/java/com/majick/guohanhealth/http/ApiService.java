package com.majick.guohanhealth.http;


import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.bean.BrandListInfo;
import com.majick.guohanhealth.bean.GoodsClassChildInfo;
import com.majick.guohanhealth.bean.GoodsClassInfo;
import com.majick.guohanhealth.bean.GoodsDetailedInfo;
import com.majick.guohanhealth.bean.GoodsListInfo;
import com.majick.guohanhealth.bean.ImgCodeKey;
import com.majick.guohanhealth.bean.LoginBean;
import com.majick.guohanhealth.bean.MineInfo;
import com.majick.guohanhealth.bean.SMSCode;
import com.majick.guohanhealth.bean.SearchInfo;
import com.majick.guohanhealth.bean.SearchWordsInfo;
import com.majick.guohanhealth.bean.SelectedInfo;
import com.majick.guohanhealth.bean.UserInfo;

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
    Observable<Result<ImgCodeKey>> getImgCodeKey();

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
            @Query("ci") String ci
    );


    /**
     * 获取地区和服务
     */
    @GET(SEARCH_ADV)
    Observable<Result<SelectedInfo>> getSelectedInfo();


    /**
     * 获取商品详细
     */
    @GET(GOODS_DETAIL)
    Observable<Result<GoodsDetailedInfo>> getGoodsDetails(@Query("goods_id") String goods_id, @Query("key") String key);


//    /**
//     * 重置密码(需要短信验证码)
//     */
//    @FormUrlEncoded
//    @POST("user/resetPwd")
//    Observable<Result> resetPassword(@Field("username") String userName, @Field("passwd") String passWord, @Field("code") String authCode);
//
//    /**
//     * 修改密码
//     *
//     * @param passwdOld 原密码(32位小写MD5加密)
//     * @param passwd    新密码(32位小写MD5加密)
//     */
//    @FormUrlEncoded
//    @POST("user/updatePasswd")
//    Observable<Result> modifyPassWord(@Field("passwdOld") String passwdOld, @Field("passwd") String passwd);
//

//
//    /**
//     * 更新用户信息
//     *
//     * @param nickName 用户昵称
//     * @param icon     用户头像(图片ID)
//     */
//    @FormUrlEncoded
//    @POST("user/updateDynamic")
//    Observable<Result<UserInfo>> updateUserInfo(@Field("nickName") String nickName, @Field("icon") String icon);
//
//    ///////////////////////////////////////////////////////////////////////////
//    // 点单相关
//    ///////////////////////////////////////////////////////////////////////////
//
//    /**
//     * 获取最近点单商品信息
//     */
//    @FormUrlEncoded
//    @POST("goods/info/findLastedPageList")
//    Observable<Result<GoodsInfo>> getRecentlyOrderGoodsInfo(@Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize);
//
//    /**
//     * pos收银
//     *
//     * @param appId    应用ID
//     * @param userId   当前用户ID
//     * @param goodsId  商品ID
//     * @param paramId  商品参数ID
//     * @param quantity 购买数量
//     */
//    @FormUrlEncoded
//    @POST("order/add4Pos/{appId}/{openId}")
//    Observable<Result<OrderInfo>> posOrder(@Path("appId") String appId, @Path("openId") String userId,
//                                           @Field("goodsId") String goodsId, @Field("paramId") String paramId,
//                                           @Field("quantity") int quantity);
//
//    /**
//     * 批量下单校验
//     */
//    @FormUrlEncoded
//    @POST("order/check4GoodsBatch/{appId}/{openId}")
//    Observable<Result<List<CheckGoodsInfo>>> orderCheck(@Path("appId") String appId, @Path("openId") String userId,
//                                                        @Field("goodsId") List<String> goodsIds, @Field("paramId") List<String> paramIds,
//                                                        @Field("quantity") List<String> quantities);
//
//    /**
//     * 批量下单
//     */
//    @FormUrlEncoded
//    @POST("order/add4GoodsBatch/{appId}/{openId}")
//    Observable<Result<OrderInfo>> placeOrder(@Path("appId") String appId, @Path("openId") String userId,
//                                             @Field("goodsId") List<String> goodsIds, @Field("paramId") List<String> paramIds,
//                                             @Field("quantity") List<String> quantities);
//
//    /**
//     * 取消订单
//     */
//    @POST("finance/revenue/reverseOrder/{orderId}")
//    Observable<Result> cancelOrder(@Path("orderId") String orderId);
//
//    /**
//     * 删除订单
//     */
//    @POST("finance/revenue/del/{id}")
//    Observable<Result> deleteOrder(@Path("id") String orderId);
//
//
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
