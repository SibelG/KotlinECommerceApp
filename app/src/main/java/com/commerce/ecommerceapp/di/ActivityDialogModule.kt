package com.commerce.ecommerceapp.di

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils.DISPLAY_DIALOG
import com.commerce.ecommerceapp.Utils.LOADING_ANNOTATION
import com.commerce.ecommerceapp.Utils.PERMISSION_ANNOTATION
import com.commerce.ecommerceapp.loadGif
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named


@Module
@InstallIn(ActivityComponent::class)
class ActivityDialogModule {

    @ActivityScoped
    @Provides
    @Named(LOADING_ANNOTATION)
    fun provideLoadingDialog(@ActivityContext context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val loadingImageView = dialog.findViewById<ImageView>(R.id.loadingImageView)
        loadingImageView.loadGif(R.drawable.dialog_icon)
        dialog.create()
        return dialog
    }


}