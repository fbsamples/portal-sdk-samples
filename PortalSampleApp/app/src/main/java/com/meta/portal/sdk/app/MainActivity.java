package com.meta.portal.sdk.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.base.BaseActivity;
import com.meta.portal.sdk.app.data.Feature;
import com.meta.portal.sdk.app.data.FeatureParser;
import com.meta.portal.sdk.app.ui.FeatureCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements FeatureCardAdapter.FeatureCardAdapterListener {
    
    List<Feature> mFeatures = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FeatureParser featureParser = new FeatureParser(getAssets(), getResources(), getPackageName());
        featureParser.parseFeatures();

        mFeatures = featureParser.getFeatures();

        FeatureCardAdapter featureCardAdapter = new FeatureCardAdapter();
        featureCardAdapter.setData(mFeatures);
        featureCardAdapter.setFeatureCardAdapterListener(this);
        featureCardAdapter.setHeaderFirst(getString(R.string.header_first_title));
        featureCardAdapter.setHeaderSecond(getString(R.string.header_second_title));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(featureCardAdapter);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == 0 || position == 1) {
                            return 2;
                        } else {
                            return 1;
                        }
                    }
                });

        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onListItemClicked(final Feature feature) {
        Intent intent = new Intent(this, feature.getClassName());
        startActivity(intent);
    }

}
