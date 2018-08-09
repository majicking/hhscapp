package com.guohanhealth.shop.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.LogisticsInfo;
import com.guohanhealth.shop.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogisticsActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.logistics_name)
    TextView logisticsName;
    @BindView(R.id.logistics_number)
    TextView logisticsNumber;
    @BindView(R.id.logistics_view_crcycle)
    RecyclerView logisticsViewCrcycle;
    private LogisticsInfo mInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_logistics;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "物流详情");
        mInfo = (LogisticsInfo) getIntent().getSerializableExtra(Constants.DATA);
        try {
            logisticsName.setText(Utils.getString(mInfo.express_name));
            logisticsNumber.setText(Utils.getString(mInfo.shipping_code));
            logisticsViewCrcycle.setLayoutManager(new LinearLayoutManager(mContext));
            logisticsViewCrcycle.setAdapter(new BaseRecyclerAdapter<String, com.chad.library.adapter.base.BaseViewHolder>(mContext, R.layout.express_item, mInfo.deliver_info) {
                @Override
                public void convert(BaseViewHolder holder, String o) {
                    TextView time = holder.getView(R.id.time);
                    TextView textView = holder.getView(R.id.content);
                    String[] result = o.split("&nbsp;&nbsp;");
                    if (result.length > 1) {
                        time.setText(result[0]);
                        textView.setText(result[1]);
                    }

                    if (holder.getPosition() == mInfo.deliver_info.size() - 1) {
                        holder.getView(R.id.down).setVisibility(View.INVISIBLE);
                    } else if (holder.getPosition() == 0) {
                        holder.getView(R.id.up).setVisibility(View.INVISIBLE);
                        textView.setTextColor(getResources().getColor(R.color.green));

                    } else {
                        textView.setTextColor(getResources().getColor(R.color.djk_textcolor444));
                        holder.getView(R.id.up).setVisibility(View.VISIBLE);
                        holder.getView(R.id.down).setVisibility(View.VISIBLE);
                    }


                }
            });

            
        } catch (NullPointerException r) {
            showToast(r.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
