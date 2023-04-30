package com.therevotech.revoschat.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.gms.location.LocationServices
import com.therevotech.revoschat.apis.RcApi
import com.therevotech.revoschat.data.repositories.auth.AuthRepository
import com.therevotech.revoschat.data.repositories.auth.AuthRepositoryImpl
import com.therevotech.revoschat.data.repositories.chatservice.ChatSocketService
import com.therevotech.revoschat.data.repositories.chatservice.ChatSocketServiceImpl
import com.therevotech.revoschat.data.repositories.chatservice.MessageService
import com.therevotech.revoschat.data.repositories.chatservice.MessageServiceImpl
import com.therevotech.revoschat.data.repositories.general.GeneralRepository
import com.therevotech.revoschat.data.repositories.general.GeneralRepositoryImpl
import com.therevotech.revoschat.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): RcApi{
        val logging= HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient().newBuilder().addInterceptor(logging)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideMessageService(client: HttpClient, preferences: SharedPreferences)
    : MessageService = MessageServiceImpl(client = client, prefs = preferences)

    @Provides
    @Singleton
    fun provideChatSocketService(client: HttpClient, preferences: SharedPreferences)
            : ChatSocketService = ChatSocketServiceImpl(client = client,preferences)

//    @Provides
//    @Singleton
//    fun provideSharedPref(app: Application): SharedPreferences =
//        app.getSharedPreferences("prefs",MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideEncryptedSharedPref(app: Application): SharedPreferences {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        return EncryptedSharedPreferences.create(
            "secure_prefs",
            mainKeyAlias,
            app.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(app: Application, api: RcApi, preferences: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(app, api, preferences)
    }

    @Provides
    @Singleton
    fun provideGeneralRepository(app: Application, api: RcApi, preferences: SharedPreferences): GeneralRepository {
        return GeneralRepositoryImpl(
            api,
            preferences,
            app,
            LocationServices.getFusedLocationProviderClient(app)
        )
    }
}