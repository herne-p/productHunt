package com.example.producthunt.data.network

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.producthunt.R
import com.example.producthunt.data.db.CurrentDay
import com.example.producthunt.data.db.entity.Post
import com.example.producthunt.data.network.Response.CurrentPostsResponse
import com.example.producthunt.internal.NoConnectivityException
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*

class PostNetworkDataSourceImpl (
    private val apiProductHuntService: ApiProductHuntService
): PostNetworkDataSource {

    private var mSnackbar: Snackbar? = null

    val messageToUser= "You are offline"

    private val _downloadedPost = MutableLiveData<List<Post>>()
    override val downloadedPost: LiveData<List<Post>>
        get() = _downloadedPost

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun fetchPost(day: String) {
        try {
            val fetchPosts = apiProductHuntService
                .getCurrentPosts(CurrentDay.currentDay()/*, page, pageSize*/)
                .await()
            _downloadedPost.postValue(fetchPosts.posts)
            mSnackbar?.dismiss()
        }catch (e : NoConnectivityException){
            Log.e("Connectivity", "No internet Connection" , e)
        }
    }

}


