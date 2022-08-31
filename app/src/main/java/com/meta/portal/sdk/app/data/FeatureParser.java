/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import com.meta.portal.sdk.app.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class FeatureParser {
  private static final String TAG = FeatureParser.class.getSimpleName();

  final List<Feature> mFeatures = new ArrayList<>();

  private final AssetManager mAssetManager;
  private final Resources mResources;
  private final String mPackageName;
  private final Context mContext;

  public FeatureParser(
      AssetManager assetManager, Resources resources, String packageName, Context context) {
    mAssetManager = assetManager;
    mResources = resources;
    mPackageName = packageName;
    mContext = context;
  }

  public void parseFeatures() {
    XmlPullParserFactory parserFactory;
    try {
      parserFactory = XmlPullParserFactory.newInstance();
      XmlPullParser parser = parserFactory.newPullParser();

      InputStream is;
      if (!Utils.isTvDevice(mContext)) {
        is = mAssetManager.open("featuredata.xml");
      } else {
        is = mAssetManager.open("featuredata_tv.xml");
      }

      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(is, null);

      processParsing(parser);

    } catch (XmlPullParserException e) {
      Log.d(TAG, "Couldn't parse file");
    } catch (IOException e) {
      Log.d(TAG, "Couldn't parse file");
    }
  }

  public List<Feature> getFeatures() {
    return mFeatures;
  }

  private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
    int eventType = parser.getEventType();
    Feature feature = null;
    boolean visible = false;

    while (eventType != XmlPullParser.END_DOCUMENT) {
      String eltName = null;

      switch (eventType) {
        case XmlPullParser.START_TAG:
          eltName = parser.getName();
          if ("feature".equals(eltName)) {
            feature = new Feature();
          } else if (feature != null) {
            if ("visible".equals(eltName)) {
              if (parser.nextText().equals("true")) {
                visible = true;
                mFeatures.add(feature);
              } else {
                visible = false;
              }
            }
            if (visible) {
              if ("screenname".equals(eltName)) {
                feature.setScreenName(parser.nextText());
              } else if ("classnametitle".equals(eltName)) {
                feature.setClassNameTitle(parser.nextText());
              } else if ("backgroundresourceid".equals(eltName)) {
                @DrawableRes
                int resourceID =
                    mResources.getIdentifier(parser.nextText(), "drawable", mPackageName);
                feature.setBackgroundResourceId(resourceID);
              } else if ("color".equals(eltName)) {
                @ColorInt int color = Color.parseColor(parser.nextText());
                feature.setBackgroundColor(color);
              } else if ("classname".equals(eltName)) {
                try {
                  Class featureClass = Class.forName(parser.nextText());
                  feature.setClassName(featureClass);
                } catch (ClassNotFoundException e) {
                  Log.d(TAG, "No class found for class name");
                }
              }
            }
          }
          break;
      }
      eventType = parser.next();
    }
  }
}
