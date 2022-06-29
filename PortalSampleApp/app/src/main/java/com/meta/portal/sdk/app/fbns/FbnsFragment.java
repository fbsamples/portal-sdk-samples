package com.meta.portal.sdk.app.fbns;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.Utils;
import com.meta.portal.sdk.app.data.FbnsData;

public class FbnsFragment extends Fragment implements FbnsDataCardAdapterListener, Callback {
    
    private FeatureCardAdapterFbns mFeatureCardAdapter;
    
    private FeatureCardAdapterFbnsTv mFeatureCardAdapterTv;

    private FbnsHelper mFbnsHelper;

    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFbnsHelper = new FbnsHelper(getActivity());
        mFbnsHelper.setCallback(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fbns, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.recycler_view);

        if (!Utils.isTvDevice(getActivity())) {
            mFeatureCardAdapter = new FeatureCardAdapterFbns();
            mFeatureCardAdapter.setData(mFbnsHelper.getFbnsData());
            mFeatureCardAdapter.setFbnsDataAdapterListener(this);
            mRecyclerView.setAdapter(mFeatureCardAdapter);
        } else {
            mFeatureCardAdapterTv = new FeatureCardAdapterFbnsTv();
            mFeatureCardAdapterTv.setData(mFbnsHelper.getFbnsData());
            mFeatureCardAdapterTv.setFbnsDataAdapterListener(this);
            mRecyclerView.setAdapter(mFeatureCardAdapterTv);
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        SpacesItemDecorator itemDecorator = new SpacesItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);

    }

    @Override
    public void onListItemClicked(final FbnsData fbnsData) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FbnsAlertDialog);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.customview, null, false);
        Button buttonOk = dialogView.findViewById(R.id.button_ok);
        ImageView buttonClose = dialogView.findViewById(R.id.button_close);
        TextView step = dialogView.findViewById(R.id.step);
        step.setText("Step " + String.valueOf(fbnsData.getStep() + 1) + " of 7");
        builder.setView(dialogView, 0, 0, 0, 0);
        final AlertDialog alertDialog = builder.create();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mFbnsHelper.setFinished(fbnsData.getStep(), true);
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        if (!Utils.isTvDevice(getActivity())) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = 868;
            lp.height = 735;
            alertDialog.show();
            alertDialog.getWindow().setAttributes(lp);
        } else {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = 1085;
            lp.height = 919;
            alertDialog.show();
            alertDialog.getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onFbnsDataChanged() {
        if (!Utils.isTvDevice(getActivity())) {
            mFeatureCardAdapter.setData(mFbnsHelper.getFbnsData());
            mFeatureCardAdapter.notifyDataSetChanged();
        } else {
            mFeatureCardAdapterTv.setData(mFbnsHelper.getFbnsData());
            mFeatureCardAdapterTv.notifyDataSetChanged();
        }
    }

    protected void setFeatureInfoShowing(boolean showing) {

    }
    
    public static FbnsFragment newInstance() {
        FbnsFragment fbnsFragment = new FbnsFragment();
        return fbnsFragment;
    }

    public class SpacesItemDecorator extends RecyclerView.ItemDecoration {

        private final int space;

        public SpacesItemDecorator(int spaceInPx) {
            this.space = spaceInPx;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.top = space;
            outRect.bottom = space;
            outRect.left = space;
            outRect.right = space;
        }
    }

}
