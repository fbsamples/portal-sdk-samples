package com.meta.portal.sdk.app;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.base.BaseActivity;
import com.meta.portal.sdk.app.data.Feature;
import com.meta.portal.sdk.app.data.FeatureParser;
import com.meta.portal.sdk.app.ui.FeatureCardAdapter;
import com.meta.portal.sdk.app.ui.FeatureCardAdapterListener;
import com.meta.portal.sdk.app.ui.FeatureCardAdapterTv;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements FeatureCardAdapterListener {

  List<Feature> mFeatures = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    FeatureParser featureParser =
        new FeatureParser(getAssets(), getResources(), getPackageName(), this);
    featureParser.parseFeatures();

    mFeatures = featureParser.getFeatures();

    RecyclerView recyclerView = findViewById(R.id.recycler_view);

    if (Utils.isTvDevice(this)) {
      FeatureCardAdapterTv featureCardAdapter = new FeatureCardAdapterTv();
      featureCardAdapter.setData(mFeatures);
      featureCardAdapter.setFeatureCardAdapterListener(this);
      recyclerView.setAdapter(featureCardAdapter);
    } else {
      FeatureCardAdapter featureCardAdapter = new FeatureCardAdapter();
      featureCardAdapter.setData(mFeatures);
      featureCardAdapter.setFeatureCardAdapterListener(this);
      featureCardAdapter.setHeaderFirst(getString(R.string.header_first_title));
      featureCardAdapter.setHeaderSecond(getString(R.string.header_second_title));
      recyclerView.setAdapter(featureCardAdapter);
    }

    if (!Utils.isTvDevice(this)) {
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
    } else {
      LinearLayoutManager layoutManager =
          new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

      recyclerView.setLayoutManager(layoutManager);

      SpacesItemDecorator itemDecorator = new SpacesItemDecorator(26);
      recyclerView.addItemDecoration(itemDecorator);
    }
  }

  @Override
  public void onListItemClicked(final Feature feature) {
    Intent intent = new Intent(this, feature.getClassName());
    startActivity(intent);
  }

  public class SpacesItemDecorator extends RecyclerView.ItemDecoration {

    private final int space;

    public SpacesItemDecorator(int spaceInPx) {
      this.space = spaceInPx;
    }

    @Override
    public void getItemOffsets(
        Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      outRect.top = space;
      outRect.bottom = space;
      outRect.left = space;
      outRect.right = space;
    }
  }
}
