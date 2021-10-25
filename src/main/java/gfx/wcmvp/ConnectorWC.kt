package gfx.wcmvp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.walletconnect.Session
import org.walletconnect.impls.*
import org.walletconnect.nullOnThrow
import java.io.File
import java.util.*

class ConnectorWC {
    private lateinit var client: OkHttpClient
    private lateinit var moshi: Moshi
    private lateinit var storage: WCSessionStore
    lateinit var config: Session.Config
    lateinit var session: Session

    init {
        initMoshi()
        initClient()
        initSessionStorage()
    }


    private fun initClient() {
        client = OkHttpClient.Builder().build()
    }

    private fun initMoshi() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }


    private fun initSessionStorage() {
        storage = FileWCSessionStore(File("./", "session_store.json").apply { createNewFile() }, moshi)
    }

    fun resetSession() {
        nullOnThrow { session }?.clearCallbacks()
        val key = getRandomString(64)
        config = Session.Config(UUID.randomUUID().toString(), "https://e.bridge.walletconnect.org", key)
        session = SessionWC(config,
            MoshiPayloadAdapter(moshi),
            storage,
            OkHttpTransport.Builder(client, moshi),
            Session.PeerMeta("https://etherlands.com", "CraftConnect", "Minecraft WC",listOf("https://i.ibb.co/2gKnfcj/Etherelands-Social-Logo.png"))
        )
        session.addCallback(CallbackWC())
        session.offer()
    }
    fun getRandomString(length: Int) : String {
        val allowedChars = ('a'..'f') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}
