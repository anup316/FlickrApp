package com.flickerapp.architecture;

import android.content.Context;

import com.flickerapp.database.FlickerDatabase;

/**
 * Created by GUR47272 on 23-08-2018.
 */

public interface PhotoFramePresenter {

    void onViewInitialized(final boolean isInternetAvailable, final FlickerDatabase database);

    void bind(final PhotoFrameView view);

    void unbind();

    void onSearchText(final String query);

}
