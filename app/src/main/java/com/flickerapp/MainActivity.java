package com.flickerapp;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flickerapp.architecture.PhotoFramePresenter;
import com.flickerapp.architecture.PhotoFramePresenterImpl;
import com.flickerapp.architecture.PhotoFrameView;
import com.flickerapp.utility.Constants;
import com.flickerapp.database.FlickerDatabase;
import com.flickerapp.network.PhotoListAsync;
import com.flickerapp.utility.FlickerUtils;
import com.flickerapp.models.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoFrameView, SearchView.OnQueryTextListener {


    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private List<Photo> mPhotoList = new ArrayList<>();
    private PhotoFramePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new PhotoFramePresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.bind(this);
        mPresenter.onViewInitialized(FlickerUtils.isInternetAvailable(getApplicationContext()), FlickerDatabase.getInstance(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setAdapter(List<Photo> photoList) {
        this.mPhotoList = photoList;
        mRecyclerView.setAdapter(new GridAdapter(getApplicationContext(), mPhotoList));
    }

    @Override
    public void showErrorMessage(int error_text) {
        Toast.makeText(getApplicationContext(), getString(error_text), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUpViews() {
        askForPermission();
        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mSearchView = findViewById(R.id.search);
        mProgressBar = findViewById(R.id.progress_bar);
        mSearchView.setOnQueryTextListener(this);

    }

    @Override
    public void setSearchViewVisibility(int viewVisibility) {
        mSearchView.setVisibility(viewVisibility);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mPhotoList.clear();
        mPresenter.onSearchText(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                    , Constants.REQUEST_PERMISSION);
        }
    }
}
