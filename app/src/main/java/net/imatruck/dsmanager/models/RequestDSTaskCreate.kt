package net.imatruck.dsmanager.models


import java.util.HashMap

import okhttp3.MultipartBody
import okhttp3.RequestBody


object RequestDSTaskCreate {
    private val api = "SYNO.DownloadStation.Task"
    private val version = 3
    private val method = "create"

    fun getCreateWithURIMap(sid: String, uri: String): MutableMap<String, String> {
        val map = HashMap<String, String>()
        map.put("api", api)
        map.put("version", version.toString())
        map.put("method", method)
        map.put("uri", uri)
        map.put("_sid", sid)
        return map
    }

    fun getCreateWithUriDestinationMap(
            sid: String, uri: String, destination: String): Map<String, String> {

        val map = RequestDSTaskCreate.getCreateWithURIMap(sid, uri)
        map.put("destination", destination)
        return map
    }

    val createWithFile: MutableMap<String, RequestBody>
        get() {
            val map = HashMap<String, RequestBody>()
            map.put("api", createPartFromString(api))
            map.put("version", createPartFromString(version.toString()))
            map.put("method", createPartFromString(method))
            return map
        }

    fun getCreateWithFileDestination(destination: String): Map<String, RequestBody> {
        val map = createWithFile
        map.put("destination", createPartFromString(destination))
        return map
    }

    private fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(
                MultipartBody.FORM, string)
    }
}
