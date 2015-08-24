package bos.whu.familytree.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bos.whu.familytree.R;
import bos.whu.familytree.adapter.HomeListAdapter;
import bos.whu.familytree.model.PersonBean;
import bos.whu.familytree.support.views.DividerDecoration;
import bos.whu.familytree.support.views.RecyclerItemClickListener;
import bos.whu.familytree.ui.activitys.DetailsActivity;
import bos.whu.familytree.ui.fragments.base.BaseFragment;

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    public List<PersonBean> list;
    private RequestQueue mVolleyQueue;
    public static Map<String,PersonBean> mPersonMap;
    public static Map<String,List<PersonBean>> mChildsMap;
    private View mViewHome;
    private HomeListAdapter mAdapter;
    private ProgressDialog mProgress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewHome = inflater.inflate(R.layout.fragment_home, container, false);
        return mViewHome;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = (RecyclerView) mViewHome.findViewById(R.id.recyclerview);
        mPersonMap = new HashMap<String,PersonBean>();
        mChildsMap = new HashMap<String,List<PersonBean>>();
        list = new ArrayList<PersonBean>();
        mAdapter = new HomeListAdapter(getActivity(),list);
        recyclerView.setAdapter(mAdapter);
        initData();

        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        recyclerView.addItemDecoration(headersDecor);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), DetailsActivity.class);
                        intent.putExtra("pid", String.valueOf(list.get(position).getPid()));
                        getActivity().startActivity(intent);
                    }
                }));
    }
    private void initData() {
        showProgress();
        mVolleyQueue = Volley.newRequestQueue(getActivity());
        String url = "http://10.0.2.2:8080/FamilyTreeServer/person";
//        String url = "http://192.168.191.1:8080/FamilyTreeServer/person";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        List<PersonBean> ps = gson.fromJson(response, new TypeToken<List<PersonBean>>(){}.getType());
                        list.clear();
                        list.addAll(ps);
                        for(int i=0;i<list.size();++i) {
                            PersonBean tps = list.get(i);
                            mPersonMap.put(String.valueOf(tps.getPid()), list.get(i));
                            if (tps.getFatherID()!=0) {
                                if (mChildsMap.get(String.valueOf(tps.getFatherID())) == null)
                                    mChildsMap.put(String.valueOf(tps.getFatherID()), new ArrayList<PersonBean>());
                                mChildsMap.get(String.valueOf(tps.getFatherID())).add(tps);
                            }
                        }

                        //不显示妻子
                        Iterator<PersonBean> it = list.iterator();
                        while (it.hasNext()) {
                            PersonBean value = it.next();
                            if (2 == value.getSex()) {
                                it.remove();
                            }
                        }

                        for(int i=0;i<list.size();++i) {
                            PersonBean tps = list.get(i);
                            if (tps.getFatherID()!=0) {
                                tps.setFather(mPersonMap.get(String.valueOf(tps.getFatherID())));
                            }
                            if (tps.getMotherID()!=0) {
                                tps.setMother(mPersonMap.get(String.valueOf(tps.getMotherID())));
                            }
                            if (tps.getWifeID()!=0) {
                                tps.setWife(mPersonMap.get(String.valueOf(tps.getWifeID())));
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        stopProgress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), " 无法连接服务器", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
                stopProgress();
            }
        });
        mVolleyQueue.add(stringRequest);
        mVolleyQueue.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onStop() {
        super.onStop();
        if(mProgress != null)
            mProgress.dismiss();
    }

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }

    private void showProgress() {
        mProgress = ProgressDialog.show(getActivity(), "", "获取网络数据中...");
    }

    private void stopProgress() {
        mProgress.cancel();
    }
}