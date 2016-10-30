package com.github.yangweigbh.volleyx.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.github.yangweigbh.volleyx.VolleyX;
import com.github.yangweigbh.volleyx.demo.util.MusicListDiffCallback;
import com.github.yangweigbh.volleyx.demo.R;
import com.github.yangweigbh.volleyx.demo.domain.Music;
import com.github.yangweigbh.volleyx.demo.mapper.JsonMapper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class StringRequestFragment extends Fragment {
    private static final String TAG = "StringRequestFragment";
    private static final String URL = "https://api.douban.com/v2/music/search?tag=pop&count=20";

    RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private JsonMapper mapper;
    private ItemRecyclerViewAdapter mAdapter;

    public StringRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StringRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StringRequestFragment newInstance() {
        StringRequestFragment fragment = new StringRequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_string_request, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
        mAdapter = new ItemRecyclerViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        refreshItems();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mapper = new JsonMapper();
    }
    void refreshItems() {
        final StringRequest request = new StringRequest(URL, null, null);
        VolleyX.from(request).subscribeOn(Schedulers.io())
                .map(new Func1<String, List<Music>>() {
                    @Override
                    public List<Music> call(String s) {
                        return mapper.transformJsonToMusicCollection(s);
                    }
                }).map(new Func1<List<Music>, Pair<DiffUtil.DiffResult, List<Music>>>() {
                    @Override
                    public Pair<DiffUtil.DiffResult, List<Music>> call(List<Music> musics) {
                        return Pair.create(DiffUtil.calculateDiff(new MusicListDiffCallback(mAdapter.getDatas(), musics)), musics);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<DiffUtil.DiffResult, List<Music>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError " + Log.getStackTraceString(e));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(Pair<DiffUtil.DiffResult, List<Music>> result) {
                        Log.d(TAG, "onNext");
                        mAdapter.setDatas(result.second);
                        result.first.dispatchUpdatesTo(mAdapter);
                    }
                });
    }

    public static class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {
        public static final String KEY_TITLE = "TITLE";
        public static final String KEY_IMAGE = "IMAGE";

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private List<Music> mDatas = new ArrayList<>();

        public ItemRecyclerViewAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        public void setDatas(List<Music> newDatas) {
            mDatas.clear();
            mDatas.addAll(newDatas);
        }

        public List<Music> getDatas() {
            return mDatas;
        }

        @Override
        public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemRecyclerViewAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemRecyclerViewAdapter.ViewHolder holder, int position, List<Object> payloads) {
            if(payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);
            } else{
                Bundle o = (Bundle) payloads.get(0);
                for (String key : o.keySet()) {
                    if(key.equals(KEY_TITLE)){
                        holder.mTitleView.setText(o.getString(KEY_TITLE));
                    }else if(key.equals(KEY_IMAGE)){
                        Picasso.with(mContext).load((String) o.get(KEY_IMAGE)).into(holder.mImageView);
                    }
                }
            }
        }

        @Override
        public void onBindViewHolder(ItemRecyclerViewAdapter.ViewHolder holder, int position) {
            Picasso.with(mContext).load(mDatas.get(position).getImage()).into(holder.mImageView);
            holder.mTitleView.setText(mDatas.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTitleView;
            ImageView mImageView;

            ViewHolder(View view) {
                super(view);
                mImageView = (ImageView) view.findViewById(R.id.imageView);
                mTitleView = (TextView) view.findViewById(R.id.textView);
            }
        }
    }
}
