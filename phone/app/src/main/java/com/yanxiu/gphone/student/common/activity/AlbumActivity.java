package com.yanxiu.gphone.student.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.adapter.AlbumImageAdapter;
import com.yanxiu.gphone.student.common.adapter.AlbumParentNameAdapter;
import com.yanxiu.gphone.student.util.AlbumUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/22 11:49.
 * Function :
 */
public class AlbumActivity extends YanxiuBaseActivity implements View.OnClickListener, AlbumUtils.onFindFinishedListener, AlbumUtils.onFindToGroupFinishedListener, AlbumParentNameAdapter.onItemClickListener, AlbumImageAdapter.onItemClickListener {

    private Context mContext;
    private ImageView mBackView;
    private TextView mTitleView;
    private LinearLayout mTitleTypeAlbumView;
    private TextView mTitleAlbumView;
    private ImageView mArrowView;
    private RecyclerView mAlbumView;
    private AlbumImageAdapter mImageAdapter;
    private List<AlbumParentNameAdapter.ParentImageMessage> mNameList=new ArrayList<>();
    private HashMap<String, List<AlbumUtils.PictureMessage>> mHashMap;
    private AlbumUtils mAlbumUtils=AlbumUtils.getInstence();
    private RecyclerView mAlbumParentNameView;
    private AlbumParentNameAdapter mParentNameAdapter;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,AlbumActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mContext=AlbumActivity.this;
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mTitleTypeAlbumView= (LinearLayout) findViewById(R.id.ll_title_type2);
        mTitleAlbumView= (TextView) findViewById(R.id.tv_titles);
        mArrowView= (ImageView) findViewById(R.id.iv_choose);
        mAlbumView= (RecyclerView) findViewById(R.id.rv_album);
        mAlbumParentNameView= (RecyclerView) findViewById(R.id.rv_album_parent_name);
    }

    private void listener() {
        mBackView.setOnClickListener(AlbumActivity.this);
        mTitleTypeAlbumView.setOnClickListener(AlbumActivity.this);
    }

    private void initData() {
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setVisibility(View.GONE);
        mTitleTypeAlbumView.setVisibility(View.VISIBLE);
        mTitleAlbumView.setText(R.string.camera_film);

        mAlbumView.setLayoutManager(new GridLayoutManager(mContext,3));
        mImageAdapter=new AlbumImageAdapter(mContext);
        mImageAdapter.addItemClickListener(AlbumActivity.this);
        mAlbumView.setAdapter(mImageAdapter);

        mAlbumParentNameView.setLayoutManager(new LinearLayoutManager(mContext));
        mParentNameAdapter=new AlbumParentNameAdapter(mContext);
        mParentNameAdapter.addItemClickListener(AlbumActivity.this);
        mAlbumParentNameView.setAdapter(mParentNameAdapter);

        mAlbumUtils.findAllPicture(AlbumActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                AlbumActivity.this.finish();
                break;
            case R.id.ll_title_type2:
                if (mNameList!=null&&mNameList.size()>0){
                    mParentNameAdapter.setData(mNameList);
                    mAlbumParentNameView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onFinished(List<AlbumUtils.PictureMessage> list) {
        Toast.makeText(mContext,list.size()+"",Toast.LENGTH_SHORT).show();
        mImageAdapter.setData(list);
        mAlbumUtils.findAllPictureToGroup(AlbumActivity.this);
    }

    @Override
    public void onFinished(HashMap<String, List<AlbumUtils.PictureMessage>> hashMap) {
        this.mHashMap=hashMap;
        setNamesToListFromMap(mHashMap,mNameList);
    }

    private void setNamesToListFromMap(HashMap<String, List<AlbumUtils.PictureMessage>> hashMap,List<AlbumParentNameAdapter.ParentImageMessage> list){
        for (Map.Entry<String, List<AlbumUtils.PictureMessage>> entry : hashMap.entrySet()) {
            AlbumParentNameAdapter.ParentImageMessage message=mParentNameAdapter.new ParentImageMessage();
            String key = entry.getKey();
            List<AlbumUtils.PictureMessage> value = entry.getValue();

            message.name=key;
            message.num=String.valueOf(value!=null?value.size():0);
            if (value!=null&&value.size()>0){
                message.path=value.get(0).path;
            }else {
                message.path="";
            }

            list.add(message);
        }
    }

    @Override
    public void onItemClick(View view, AlbumParentNameAdapter.ParentImageMessage message, int position) {
        List<AlbumUtils.PictureMessage> list=mHashMap.get(message.name);
        mImageAdapter.setData(list);
        mAlbumParentNameView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, AlbumUtils.PictureMessage message, int position) {
        Toast.makeText(mContext,message.path,Toast.LENGTH_SHORT).show();
    }
}
