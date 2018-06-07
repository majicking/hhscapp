package com.majick.guohanhealth.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.majick.guohanhealth.R;

import java.util.ArrayList;
import java.util.List;

public class TestRecycleViewAdapter extends Activity {

    private RecyclerView recycler;
    private BaseRecyclerAdapter mAdapter;
    private List<String> stringList = new ArrayList<String>();


    private MultiItemCommonAdapter adapter;
    private int typeOne = 1;
    private int typeTwo = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//            recycler = findViewById(R.id.recycler);

        /**
         * 普通的列表形式
         */
//        mAdapter = new BaseRecyclerAdapter<String>(this, R.layout.rl_item_view, stringList) {
//            @Override
//            public void convert(BaseViewHolder holder, String s) {
//                holder.setText(R.id.test, s);
//            }
//        };
//
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recycler.setLayoutManager(manager);
//        recycler.setAdapter(mAdapter);
//
//
//        for (int i = 0; i < 50; i++) {
//            stringList.add("测试-->" + i);
//        }
//        mAdapter.notifyDataSetChanged();


//            /**
//             * 不同item布局的形式
//             */
//            BaseRecyclerAdapter.ConmonItemType conmonItemType = new BaseRecyclerAdapter.ConmonItemType<String>() {
//
//                @Override
//                public int getLayoutId(int itemType) {
//                    if (itemType == typeOne) {
//                        return R.layout.item_one;
//                    } else {
//                        return R.layout.item_two;
//                    }
//                }
//
//                @Override
//                public int getItemViewType(int position, String s) {
//                    if (position == 0) {
//                        return typeOne;
//                    } else {
//                        return typeTwo;
//                    }
//                }
//            };
//
//
//            for (int i = 0; i < 2; i++) {
//                stringList.add("测试-->" + i);
//            }
//
//
//            adapter = new MultiItemCommonAdapter<String>(this, stringList, conmonItemType) {
//                @Override
//                public void convert(BaseViewHolder holder, String s) {
//                    if (holder.getItemViewType() == 0) {
//                        //Todo
//
//                    } else {
//                        //Todo
//                    }
//                }
//            };
//            LinearLayoutManager manager = new LinearLayoutManager(this);
//            recycler.setLayoutManager(manager);
//            recycler.setAdapter(adapter);
//        }
    }

}
