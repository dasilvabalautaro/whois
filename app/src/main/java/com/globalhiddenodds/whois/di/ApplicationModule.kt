package com.globalhiddenodds.whois.di

import android.content.Context
import androidx.room.Room
import com.globalhiddenodds.whois.App
import com.globalhiddenodds.whois.BuildConfig
import com.globalhiddenodds.whois.model.database.AppDatabase
import com.globalhiddenodds.whois.model.exception.NoNetworkException
import com.globalhiddenodds.whois.model.persistent.caching.Constants
import com.globalhiddenodds.whois.model.persistent.network.apply.LoadRequestPerson
import com.globalhiddenodds.whois.model.persistent.network.interfaces.WhoIsWebServices
import com.globalhiddenodds.whois.presentation.plataform.NetworkHandler
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
class ApplicationModule(private val app: App) {
    private val databaseName = "whoIs_db"
    private val domainWhoIs = "https://sheltered-brook-69638.herokuapp.com/"
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = app

    //    Provide to all application operations Rest
    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(domainWhoIs)
            .client(getUnsafeOkHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //    Interceptor of Head Rest out certificate
    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                builder.addInterceptor(loggingInterceptor)
            }
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    @Provides
    @Singleton
    fun provideWebService(networkHandler: NetworkHandler): WhoIsWebServices {
        val baseUrl = Constants.urlBase

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        val okHttpClientBuilder =   getUnsafeOkHttpClient()
        okHttpClientBuilder.addInterceptor { chain ->
            val connected = networkHandler.isConnected
            if (connected) {
                return@addInterceptor chain.proceed(chain.request())

            } else {
                throw NoNetworkException()
            }
        }

        return builder.client(okHttpClientBuilder.build())
            .build()
            .create<WhoIsWebServices>(WhoIsWebServices::class.java)
    }

    //    Provide access the database
    @Provides fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context,
            AppDatabase::class.java,
            databaseName).allowMainThreadQueries().build()

    //    Provide access the table User
    @Provides
    fun provideFaceDao(database: AppDatabase) = database.faceDataDao()

    // Manage operations REST

    @Provides
    @Singleton
    fun provideLoadRequestPerson(dataSource: LoadRequestPerson.SendRequest):
            LoadRequestPerson = dataSource
}