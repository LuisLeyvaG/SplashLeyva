package com.example.splashleyva.des

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

class MyDesUtil {
    private var stringKeyBase64: String? = null
    private var cipherCifrar: Cipher? = null
    private var cipherDesCifrar: Cipher? = null
    private var secretKey: SecretKey? = null

    constructor() {}
    constructor(stringKeyBase64: String?) {
        this.stringKeyBase64 = stringKeyBase64
    }

    fun addStringKeyBase64(stringKeyBase64: String?): MyDesUtil {
        this.stringKeyBase64 = stringKeyBase64
        return this
    }

    fun loadCipher(modo: MODO): Boolean {
        var cipher: Cipher? = null
        try {
            cipher = Cipher.getInstance(PROVIDER)
            if (cipher == null) {
                return false
            }
            if (secretKey == null) {
                if (stringKeyBase64 != null && stringKeyBase64!!.length > 0) {
                    if (!loadSecreteKeyByStringKey()) {
                        return false
                    }
                } else {
                    if (!loadSecreteKey()) {
                        return false
                    }
                }
            }
            cipher.init(modo.modo, secretKey)
            if (MODO.CIFRAR == modo) {
                cipherCifrar = cipher
            } else {
                cipherDesCifrar = cipher
            }
            return cipher != null
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return false
    }

    fun cifrar(textoEnClaro: String?): String? {
        var bytes: ByteArray? = null
        var bytesCifrados: ByteArray? = null
        var bytesBase64: ByteArray? = null
        try {
            if (textoEnClaro == null || textoEnClaro.length == 0) {
                return null
            }
            if (cipherCifrar == null) {
                if (!loadCipher(MODO.CIFRAR)) {
                    return null
                }
            }
            bytes = textoEnClaro.toByteArray(StandardCharsets.UTF_8)
            bytesCifrados = cipherCifrar!!.doFinal(bytes)
            bytesBase64 = Base64.encode(bytesCifrados, Base64.DEFAULT)
            return String(bytesBase64)
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return null
    }

    fun desCifrar(textoCifradoBase64: String?): String? {
        var bytes: ByteArray? = null
        var bytesBase64: ByteArray? = null
        try {
            if (textoCifradoBase64 == null || textoCifradoBase64.length == 0) {
                return null
            }
            if (cipherDesCifrar == null) {
                if (!loadCipher(MODO.DESCIFRAR)) {
                    return null
                }
            }
            bytesBase64 = Base64.decode(textoCifradoBase64.toByteArray(), Base64.DEFAULT)
            bytes = cipherDesCifrar!!.doFinal(bytesBase64)
            return String(bytes, StandardCharsets.UTF_8)
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return null
    }

    fun loadSecreteKey(): Boolean {
        try {
            secretKey = KeyGenerator.getInstance(ALGORITHM).generateKey()
            return secretKey != null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return false
    }

    fun loadSecreteKeyByStringKey(): Boolean {
        var bytes: ByteArray? = null
        bytes = Base64.decode(stringKeyBase64, Base64.DEFAULT)
        secretKey = SecretKeySpec(bytes, 0, bytes.size, ALGORITHM)
        return secretKey != null
    }

    fun toStringSecreteKey(): String? {
        return if (secretKey == null) {
            null
        } else Base64.encodeToString(secretKey!!.encoded, Base64.DEFAULT)
    }

    enum class MODO(val modo: Int) {
        CIFRAR(Cipher.ENCRYPT_MODE), DESCIFRAR(Cipher.DECRYPT_MODE);

    }

    companion object {
        var PROVIDER = "DESede/ECB/PKCS7Padding"
        var ALGORITHM = "DESede"
    }
}