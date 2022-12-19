package com.commerce.ecommerceapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
class ActivityDialogModule {

    /*@ActivityScoped
    @Provides
    @Named(LOADING_ANNOTATION)
    fun provideLoadingDialog(@ActivityContext context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val loadingImageView = dialog.findViewById<ImageView>(R.id.loadingImageView)
        loadingImageView.loadGif(R.drawable.carrot_loader)
        dialog.create()
        return dialog
    }


    @ActivityScoped
    @Provides
    @Named(PERMISSION_ANNOTATION)
    fun provideExplainPermissionDialog(@ActivityContext context: Context): Dialog {
        val dialog = Dialog(context)
        with(dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.explain_permission_dialog_layout)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val openPermissionSettings = findViewById<Button>(R.id.openPermissionSettings)
            val closeDialog = findViewById<ImageView>(R.id.closeDialog)
            closeDialog.setOnClickListener { hide() }
            openPermissionSettings.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            create()
        }
        return dialog
    }

    @ActivityScoped
    @Provides
    @Named(DISPLAY_DIALOG)
    fun provideDisplayAlert(
        @ActivityContext context: Context
    ) = AlertDialog.Builder(context).let { builder ->
                builder.setPositiveButton(context.getString(R.string.ok), null)
                builder.create()
            }*/


}