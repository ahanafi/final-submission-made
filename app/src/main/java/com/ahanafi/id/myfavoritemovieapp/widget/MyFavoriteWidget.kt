package com.ahanafi.id.myfavoritemovieapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.ahanafi.id.myfavoritemovieapp.R

class MyFavoriteWidget : AppWidgetProvider() {

    companion object{
        private const val TOAST_ACTION = "com.ahanafi.id.myfavoritemovieapp.TOAST_ACTION"
        const val EXTRA_MOVIE = "com.ahanafi.id.myafavoritemovieapp.EXTRA_MOVIE"
        const val EXTRA_ITEM = "com.ahanafi.id.myfavoritemovieapp.EXTRA_ITEM"

        const val UPDATE_WIDGET = "com.ahanafi.id.myafavoriteapp.UDPATE_WIDGET"

        fun sendUpdateFavoriteMovie(context: Context) {
            val intent = Intent(context, MyFavoriteWidget::class.java)
            intent.action = UPDATE_WIDGET
            context.sendBroadcast(intent)
        }

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, FavoriteWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.my_favorite_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, MyFavoriteWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            when (intent.action) {
                TOAST_ACTION -> {
                    val title = intent.getStringExtra(EXTRA_ITEM)
                    Toast.makeText(context, "Touched view $title", Toast.LENGTH_SHORT).show()
                }
                UPDATE_WIDGET -> {
                    val componentName = context?.let {
                        ComponentName(it, MyFavoriteWidget::class.java)
                    }

                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)

                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}