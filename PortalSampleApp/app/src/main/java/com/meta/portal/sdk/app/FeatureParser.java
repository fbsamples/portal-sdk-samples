package com.meta.portal.sdk.app;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.DrawableRes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FeatureParser {
    private static final String TAG = FeatureParser.class.getSimpleName();

    final List<Feature> mFeatures = new ArrayList<>();
    
    private final AssetManager mAssetManager;
    private final Resources mResources;
    private final String mPackageName;
    
    public FeatureParser(AssetManager assetManager, Resources resources, String packageName) {
        mAssetManager = assetManager;
        mResources = resources;
        mPackageName = packageName;
    }

    public void parseFeatures() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = mAssetManager.open("featuredata.xml");
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

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        int eventType = parser.getEventType();
        Feature feature = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if ("feature".equals(eltName)) {
                        feature = new Feature();
                        mFeatures.add(feature);
                    } else if (feature != null) {
                        if ("screenname".equals(eltName)) {
                            feature.setScreenName(parser.nextText());
                        } else if ("classnametitle".equals(eltName)) {
                            feature.setClassNameTitle(parser.nextText());
                        } else if ("backgroundresourceid".equals(eltName)) {
                            @DrawableRes int resourceID = mResources.getIdentifier(parser.nextText(), "drawable", mPackageName);
                            feature.setBackgroundResourceId(resourceID);
                        } else if ("classname".equals(eltName)) {
                            try {
                                Class featureClass = Class.forName(parser.nextText());
                                feature.setClassName(featureClass);
                            } catch(ClassNotFoundException e) {
                                Log.d(TAG, "No class found for class name");
                            }
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
        
    }
}
