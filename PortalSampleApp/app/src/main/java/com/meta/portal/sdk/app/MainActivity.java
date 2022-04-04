package com.meta.portal.sdk.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements FeatureCardAdapter.FeatureCardAdapterListener {
    
    final List<Feature> mFeatures = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFeatures.add(createFeature("MockActivity1", "Test MockActivity1", MockActivity1.class));
        mFeatures.add(createFeature("MockActivity2", "Test MockActivity2", MockActivity2.class));
        mFeatures.add(createFeature("MockActivity3", "Test MockActivity3", MockActivity3.class));
        mFeatures.add(createFeature("MockActivity4", "Test MockActivity4", MockActivity4.class));
        mFeatures.add(createFeature("MockActivity5", "Test MockActivity5", MockActivity5.class));
        mFeatures.add(createFeature("MockActivity6", "Test MockActivity6", MockActivity6.class));
        
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

    private Feature createFeature(final String screenName, final String demoModeTitle, final Class<?> className) {
        return new Feature(screenName, demoModeTitle, className);
    }

    @Override
    public void onListItemClicked(final Feature feature) {
        Intent intent = new Intent(this, feature.getClassName());
        startActivity(intent);
    }

}
