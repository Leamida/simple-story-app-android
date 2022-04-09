package com.example.storyapp.di

import com.example.storyapp.BuildConfig
import com.example.storyapp.feature.auth.data.repository.AuthRepository
import com.example.storyapp.feature.auth.data.repository.UserRepository
import com.example.storyapp.feature.auth.data.source.api.AuthApiService
import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferences
import com.example.storyapp.feature.auth.domain.repository.AuthRepositoryService
import com.example.storyapp.feature.auth.domain.repository.UserRepositoryService
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.data.source.api.StoryApiService
import com.example.storyapp.feature.story.domain.repository.StoryRepositoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStoryApi(): StoryApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(StoryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(api: StoryApiService): StoryRepositoryService {
        return StoryRepository(api)
    }


    @Provides
    @Singleton
    fun provideAuthApi(): AuthApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApiService): AuthRepositoryService {
        return AuthRepository(api)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserPreferences): UserRepositoryService {
        return UserRepository(api)
    }
}