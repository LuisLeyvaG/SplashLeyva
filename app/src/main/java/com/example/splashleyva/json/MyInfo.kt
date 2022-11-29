package com.example.mysplash.json

import com.example.splashleyva.json.MyData
import java.io.Serializable

class MyInfo( public var usuario: String, public var password: String,  public var email: String, public var contras: List<MyData> = ArrayList<MyData>()) : Serializable {
}