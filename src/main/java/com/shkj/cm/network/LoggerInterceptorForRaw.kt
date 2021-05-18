package com.shkj.cm.network

import com.alibaba.fastjson.util.IOUtils
import com.alibaba.fastjson.util.IOUtils.UTF8
import com.orhanobut.logger.Logger
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException


class LoggerInterceptorForRaw : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val orgRequest = chain!!.request()
        val response = chain.proceed(orgRequest)
        if (orgRequest.body() is FormBody) {
            if (orgRequest.method() == "POST") {
                val sb = StringBuilder()
                val body1 = orgRequest.body() as FormBody
                for (i in 0 until body1.size()) {
                    sb.append(body1.encodedName(i) + "=" + body1.encodedValue(i) + ",")
                }
                sb.delete(sb.length - 1, sb.length)
                //打印post请求的信息
                Logger.d(
                    "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                            + "\n" + "headers:" + orgRequest.headers().toMultimap()
                            + "\n" + "post请求体:{" + sb.toString() + "}"
                )
            } else {
                //打印get请求的信息
                Logger.d(
                    "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                            + "\n" + "headers:" + orgRequest.headers().toMultimap()
                )
            }
        } else {
            val sb = bodyToString(orgRequest)
            //打印post请求的信息
            Logger.d(
                "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                        + "\n" + "headers:" + orgRequest.headers().toMultimap()
                        + "\n" + "post请求体:" + sb.toString()
            )
        }
        //返回json
        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()
        var charset = IOUtils.UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(IOUtils.UTF8)
            } catch (e: UnsupportedCharsetException) {
                return response
            }

        }
        if (contentLength != 0L) {
            //打印返回json
            //json日志使用鼠标中键进行选中
            Logger.json(buffer.clone().readString(charset))
        }
        return response
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy: Request = request.newBuilder().build()
            val body: RequestBody = copy.body() ?: return ""
            val buffer = Buffer()
            body.writeTo(buffer)
            val mediaType: MediaType? = body.contentType()
            val charset: Charset? = getCharset(mediaType)
            var str: String = buffer.readString(charset)
            str = URLDecoder.decode(str, "UTF-8")
            if (mediaType != null) {
                when (mediaType.subtype()) {
                    "x-www-form-urlencoded" -> {
                        val stringBuilder = StringBuilder()
                        val split = str.split("&".toRegex()).toTypedArray()
                        for (s in split) {
                            stringBuilder.append("\t\t\t")
                            stringBuilder.append(s)
                            stringBuilder.append("\n")
                        }
                        str = stringBuilder.toString()
                    }
                    "json" -> str = formatJson(str) ?: "JSON 没有"
                    else -> {
                    }
                }
            } else {
                str = "\t\t\t" + str
            }
            str
        } catch (e: Exception) {
            "error"
        }
    }

    private fun getCharset(contentType: MediaType?): Charset? {
        var charset = if (contentType != null) contentType.charset(UTF8) else UTF8
        if (charset == null) charset = UTF8
        return charset
    }

    @Throws(JSONException::class)
    private fun formatJson(body: String): String? {
        var body = body
        val stringBuilder = StringBuilder()
        val split: Array<String>
        if (body.startsWith("{")) {
            val jsonObject = JSONObject(body)
            body = jsonObject.toString(2)
        } else if (body.startsWith("[")) {
            val jsonArray = JSONArray(body)
            body = jsonArray.toString(2)
        }
        split = body.split("\n".toRegex()).toTypedArray()
        val `is`: Boolean = split.size <= 20
        for (s in split) {
            if (`is`) {
                stringBuilder.append("\t\t\t")
                stringBuilder.append(s)
            } else {
                stringBuilder.append(java.lang.String.format("\"\\t\\t\\t at %s \"", s))
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}