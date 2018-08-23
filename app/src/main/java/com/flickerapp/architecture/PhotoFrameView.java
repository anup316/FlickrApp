package com.flickerapp.architecture;

import com.flickerapp.models.Photo;

import java.util.List;

/**
 * Created by GUR47272 on 23-08-2018.
 */

public interface PhotoFrameView {

    void showLoading();

    void hideLoading();

    void setAdapter(List<Photo> photoList);

    void showErrorMessage(int error_text);

    void setUpViews();

    void setSearchViewVisibility(int viewVisiblity);

}
