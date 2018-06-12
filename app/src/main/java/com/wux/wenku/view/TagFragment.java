package com.wux.wenku.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wux.wenku.BaseFragment;
import com.wux.wenku.R;
import com.wux.wenku.activity.TypeShowActivity;
import com.wux.wenku.app.AppConfig;
import com.wux.wenku.model.Tags;
import com.wux.wenku.parse.ParseTags;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TagFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagFragment extends BaseFragment implements AppConfig.OnHandlerCallBack {

    private TagFlowLayout tags_en;
    private TagFlowLayout tags_type;
    private List<Tags> en_list = new ArrayList<>();
    private List<Tags> type_list = new ArrayList<>();
    private final String _Url = "http://www.wenku8.com/modules/article/articlelist.php";
    private Activity mActivity;
    private View rootView = null;

    private OnFragmentInteractionListener mListener;
    private RefreshLayout refreshLayout;

    public TagFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TagFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TagFragment newInstance() {
        TagFragment fragment = new TagFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tag, container, false);
            initView(rootView);
//            loadTags();
        }
        return rootView;
    }

    @Override
    public void reflush() {
        refreshLayout.autoRefresh();
    }

    private void initView(View view) {
        tags_en = (TagFlowLayout) view.findViewById(R.id.tags_en);
        tags_type = (TagFlowLayout) view.findViewById(R.id.tags_type);
        tags_en.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(mActivity, TypeShowActivity.class);
                intent.putExtra("url", en_list.get(position).getTagUrl());
                intent.putExtra("title", en_list.get(position).getTitle());
                getActivity().startActivity(intent);
                return false;
            }
        });
        tags_type.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(mActivity, TypeShowActivity.class);
                intent.putExtra("url", type_list.get(position).getTagUrl());
                intent.putExtra("title", type_list.get(position).getTitle());
                getActivity().startActivity(intent);
                return false;
            }
        });
        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                loadTags();
            }
        });
    }

    private void initTags() {
        tags_en.setAdapter(new TagAdapter<Tags>(en_list) {

            @Override
            public View getView(FlowLayout parent, int position, Tags listBean) {

                TextView mTags = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_tags_item, parent, false);
                mTags.setText(listBean.getTitle());
//                mTags.setOnClickListener(
//                        v -> TotalStationSearchActivity.launch(getActivity(), listBean.getKeyword()));

                return mTags;
            }
        });
        tags_type.setAdapter(new TagAdapter<Tags>(type_list) {

            @Override
            public View getView(FlowLayout parent, int position, Tags listBean) {

                TextView mTags = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_tags_item, parent, false);
                mTags.setText(listBean.getTitle());
//                mTags.setOnClickListener(
//                        v -> TotalStationSearchActivity.launch(getActivity(), listBean.getKeyword()));

                return mTags;
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void loadTags() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    en_list = ParseTags.parseEnglish(_Url);
                    type_list = ParseTags.parseType(_Url);
                    AppConfig.sendMessage(1, TagFragment.this, 1, 0, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppConfig.sendMessage(0, e.getMessage());
                } finally {
                    refreshLayout.finishRefresh();
                }
            }
        }).start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void handlercallback(int arg1, int arg2, Bundle data) {
        switch (arg1) {
            case 1:
                initTags();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
