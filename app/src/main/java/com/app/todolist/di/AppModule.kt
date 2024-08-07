package com.app.todolist.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.todolist.data.preferences.DefaultPreferences
import com.app.todolist.domain.preferences.Preferences
import com.app.todolist.utils.AppDispatchers
import com.app.todolist.utils.Constants.SHARED_PREFERENCE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDispatchers(): AppDispatchers {
        return AppDispatchers(
            default = Dispatchers.Default,
            main = Dispatchers.Main,
            io = Dispatchers.IO
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app:Application):SharedPreferences{
        return app.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences):Preferences{
        return DefaultPreferences(sharedPreferences)
    }

}