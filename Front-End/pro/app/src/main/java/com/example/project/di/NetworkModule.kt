package com.example.project.di

import android.content.Context
import com.example.project.api.AuctionService
import com.example.project.api.BarterService
import com.example.project.api.EventService
import com.example.project.api.LoginService
import com.example.project.api.MainService
import com.example.project.api.MyPageService
import com.example.project.api.MyService
import com.example.project.api.OcrService
import com.example.project.api.ReuseService
import com.example.project.api.SignupService
import com.example.project.api.StoreService
import com.example.project.sharedpreferences.SharedPreferencesUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 토큰이 생기면 밑에 provideRetrofit를 지우고 이거를 사용하면됨.
    // Retrofit 인스턴스에 OkHttpClient를 포함시켜 인터셉터를 추가하는 방식
    // 각 요청에 알아서 추가함.
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferencesUtil: SharedPreferencesUtil): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val token = sharedPreferencesUtil.getUserToken() // 이거 get이름바뀌면 바꾸고
                val request = chain.request().newBuilder()       // 토큰 형식 맞는지 확인하고
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://j9b207.p.ssafy.io/")
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(sharedPreferencesUtil: SharedPreferencesUtil, refreshTokenService: MyService): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val token = sharedPreferencesUtil.getUserToken()
//                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $token")
//                    .build()
//                chain.proceed(request)
//            }
//            .authenticator(object : Authenticator {
//                override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
//                    if (response.code == 403) {
//                        val userIdx = sharedPreferencesUtil.getUserId()
//                        try {
//                            val refreshTokenResponse = refreshTokenService.refreshToken(userIdx).execute()
//                            if (refreshTokenResponse.isSuccessful) {
//                                val newAccessToken = refreshTokenResponse.body()?.accessToken
//                                if(newAccessToken != null) {
//                                    sharedPreferencesUtil.setUserToken(newAccessToken)
//                                    return response.request.newBuilder()
//                                        .header("Authorization", "Bearer $newAccessToken")
//                                        .build()
//                                }
//                            }
//                        } catch (e: Exception) {
//                        }
//                    }else if(
//                        response.code==403
//                    ){
//                        sharedPreferencesUtil.setLoggedInStatus(false)
//                    }
//                    return null
//                }
//            })
//            .build()
//    }
//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://j9b207.p.ssafy.io")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://j9b207.p.ssafy.io/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuctionService(retrofit: Retrofit): AuctionService {
        return retrofit.create(AuctionService::class.java)
    }

    @Provides
    @Singleton
    fun provideBarterService(retrofit: Retrofit): BarterService {
        return retrofit.create(BarterService::class.java)
    }

    @Provides
    @Singleton
    fun provideEventService(retrofit: Retrofit): EventService {
        return retrofit.create(EventService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService {
        return retrofit.create(MainService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyService(retrofit: Retrofit): MyService {
        return retrofit.create(MyService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreService(retrofit: Retrofit): StoreService {
        return retrofit.create(StoreService::class.java)
    }

    @Provides
    @Singleton
    fun provideReuseService(retrofit: Retrofit): ReuseService {
        return retrofit.create(ReuseService::class.java)
    }

    @Provides
    @Singleton
    fun providerSignupService(retrofit: Retrofit): SignupService {
        return retrofit.create(SignupService::class.java)
    }

    @Provides
    @Singleton
    fun providerMyPageService(retrofit: Retrofit) : MyPageService{
        return retrofit.create(MyPageService::class.java)
    }

    @Provides
    @Singleton
    fun provideOcrService(retrofit: Retrofit): OcrService {
        return retrofit.create(OcrService::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesUtil(@ApplicationContext context: Context): SharedPreferencesUtil {
        return SharedPreferencesUtil(context)
    }
}
