package com.example.jahez_task.di

import android.app.Application
import android.content.Context
import com.example.jahez_task.base.App
import com.example.jahez_task.data.authentication.AuthProvider
import com.example.jahez_task.data.authentication.FirebaseAuthentication
import com.example.jahez_task.data.repository.RepositoryImpl
import com.example.jahez_task.data.retrofitservice.JahezApi
import com.example.jahez_task.domain.repository.Repository
import com.example.jahez_task.utils.Constants.BASE_URL
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthProvider(auth: FirebaseAuth): AuthProvider {
        return FirebaseAuthentication(
            auth
        )
    }

    //I Did not provide an OkHttp Client because I Don't have to set headers in this API
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJahezApi(retrofit: Retrofit): JahezApi {
        return retrofit.create(JahezApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(authProvider: AuthProvider, jahezApi: JahezApi): Repository {
        return RepositoryImpl(
            authProvider,
            jahezApi
        )
    }
}