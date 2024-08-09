package id.passage.android

import android.app.Activity
import android.webkit.WebSettings
import id.passage.android.utils.ResourceUtils.Companion.getRequiredResourceFromApp
import okhttp3.OkHttpClient

class Passage(
    activity: Activity,
    appId: String,
) {
    // region Private VARIABLES
    var passageApp: PassageApp
    var passagePasskey: PassagePasskey
    var passageMagicLink: PassageMagicLink
    var passageHosted: PassageHosted
    var passageOneTimePasscode: PassageOneTimePasscode
    var passageSocial: PassageSocial
    var passageCurrentUser: PassageCurrentUser
    var tokenStore: PassageTokenStore
    var passageClient: OkHttpClient

    // region CONSTANTS AND SINGLETON VARIABLES
    internal companion object {
        internal const val TAG = "Passage"
        internal var BASE_PATH = "https://auth.passage.id/v1"
        internal lateinit var appId: String
        internal lateinit var authOrigin: String
    }

    // region INITIALIZATION

    init {
        authOrigin = getRequiredResourceFromApp(activity, "passage_auth_origin")
        val userAgent = WebSettings.getDefaultUserAgent(activity) ?: "Android"
        passageClient =
            OkHttpClient
                .Builder()
                .addNetworkInterceptor { chain ->
                    chain.proceed(
                        chain
                            .request()
                            .newBuilder()
                            .header("User-Agent", userAgent)
                            .build(),
                    )
                }.build()
        Companion.appId = appId
        tokenStore = PassageTokenStore(activity)
        passageApp = PassageApp(passageClient)
        passagePasskey = PassagePasskey(passageClient, activity, tokenStore)
        passageOneTimePasscode = PassageOneTimePasscode(passageClient, tokenStore)
        passageHosted = PassageHosted(activity, tokenStore)
        passageSocial = PassageSocial(passageClient, activity, tokenStore)
        passageMagicLink = PassageMagicLink(passageClient, tokenStore)
        passageCurrentUser = PassageCurrentUser(tokenStore, activity)
    }

    public fun overrideBasePath(newPath: String) {
        BASE_PATH = newPath
    }
}
