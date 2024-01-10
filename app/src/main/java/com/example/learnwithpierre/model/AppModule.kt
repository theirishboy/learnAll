package com.example.learnwithpierre.model

import android.app.Application
import android.content.Context
import com.example.learnwithpierre.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    @Named("signInRequest")
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                // 1.
                .GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // 2.
                .setServerClientId(app.getString(R.string.web_client_id))
                // 3.
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        // 4.
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named("signUpRequest")
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                // 1.
                .GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // 2.
                .setServerClientId(app.getString(R.string.web_client_id))
                // 5.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()


    @Provides
    @Singleton
    @Named("dataProvider")
    fun provideDataProvider(): DataProvider {
        return DataProvider()
    }
}