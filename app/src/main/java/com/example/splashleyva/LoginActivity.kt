package com.example.splashleyva

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mysplash.json.MyInfo
import com.example.splashleyva.databinding.ActivityLoginBinding
import com.example.splashleyva.databinding.ActivityMainBinding
import com.example.splashleyva.json.Metodos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var list: List<MyInfo>
    var TAG = "LoginActivity"
    private lateinit var json: String
    private lateinit var usuario: String
    private lateinit var password: String
    val archivo = "archivo.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (ReadFile()) {
            jsonToList(json)
        }

        binding.tvRegistro.setOnClickListener {
            goRegister()
        }

        binding.tvForgotPswd.setOnClickListener {
            goForgotPswd()
        }


    }

    fun signIn(v: View) {
        usuario = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()
        password = Metodos.bytesToHex(Metodos.createSha1(password))
        verificar(usuario, password)

    }

    private fun goRegister() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goForgotPswd(){
        val intent = Intent(this@LoginActivity, ForgotPswd::class.java)
        startActivity(intent)
        finish()
    }

    fun verificar(usr: String, pswd: String) {
        var i = 0
        if (usr == "" || pswd == "") {
            Toast.makeText(applicationContext, "Llena los campos", Toast.LENGTH_LONG).show()
        } else {
            for (myInfo in list) {
                if (myInfo.usuario == usr && myInfo.password == pswd) {
                    val intent = Intent(this@LoginActivity, Menu::class.java)
                    startActivity(intent)
                    i = 1
                }
            }
            if (i == 0) {
                Toast.makeText(
                    applicationContext,
                    "El usuario o contraseña son incorrectos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun ReadFile(): Boolean {
        if (!isFileExist()) {
            return false
        }
        val file = getFile()
        var fileInputStream: FileInputStream? = null
        var bytes: ByteArray? = null
        bytes = ByteArray(file.length().toInt())
        try {
            fileInputStream = FileInputStream(file)
            fileInputStream.read(bytes)
            json = String(bytes)
            Log.d(TAG, json)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }

    private fun isFileExist(): Boolean {
        val file: File = getFile() ?: return false
        return file.isFile && file.exists()
    }

    private fun getFile(): File {
        return File(dataDir, archivo)
    }

    fun jsonToList(json: String) {
        var gson: Gson? = null
        val mensaje: String? = null
        if (json == null || json.length == 0) {
            Toast.makeText(applicationContext, "Error json null or empty", Toast.LENGTH_LONG).show()
            return
        }
        gson = Gson()
        val listType = object : TypeToken<List<MyInfo>>() {}.type
        list = gson.fromJson(json, listType)
        Log.d(TAG, "HOLA BRO")
        if (list == null || list.isEmpty()) {
            Toast.makeText(applicationContext, "Error list is null or empty", Toast.LENGTH_LONG)
                .show()
            return
        }
    }
}