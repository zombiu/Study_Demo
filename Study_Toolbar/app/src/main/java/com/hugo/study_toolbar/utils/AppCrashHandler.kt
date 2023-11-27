package com.hugo.study_toolbar.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread

object AppCrashHandler : Thread.UncaughtExceptionHandler {
    private const val TAG = "AppCrashHandler"
    var defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    var infoMap: HashMap<String, String> = HashMap()

    private var context: Context? = null
    var logPath: String? = null

    fun init(context: Context) {
        this.context = context
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        logPath = getPath()
        collectBaseInfo()
    }


    override fun uncaughtException(t: Thread, e: Throwable?) {
        Log.e(TAG, "thread name ${t.name} throw error ${e?.message}")
        if (e == null) {
            defaultUncaughtExceptionHandler?.uncaughtException(t, e)
        } else {
            thread {
                saveErrorInfo(e)
            }
        }
    }

    private fun collectBaseInfo() {
        //获取包信息
        val packageManager = context?.packageManager
        packageManager?.let {
            try {
                val packageInfo =
                    it.getPackageInfo(context?.packageName ?: "", PackageManager.GET_ACTIVITIES)
                val versionName = packageInfo.versionName
                val versionCode = packageInfo.versionCode
                infoMap["versionName"] = versionName
                infoMap["versionCode"] = versionCode.toString()
            } catch (e: Exception) {

            }
        }
    }

    private fun saveErrorInfo(e: Throwable) {
        val formattedTime: String = formatTime("yyyy-MM-dd-HH:mm:ss.SSS")
        val stringBuffer = StringBuffer(formattedTime)
        infoMap.forEach { (key, value) ->
            stringBuffer.append("$key == $value")
        }

        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        //获取到堆栈信息
        e.printStackTrace(printWriter)
        printWriter.close()
        //转换异常信息
        val errorStackInfo = stringWriter.toString()
        stringBuffer.append(errorStackInfo)

        try {
            var file = File(logPath + File.separator + getLogName())
            LogUtils.e("-->>", file.absolutePath)
            file.parentFile.mkdirs()
            // 创建一个空文件,如果父文件夹或者祖先文件夹不存在，就会抛出异常
            file.createNewFile()
            val fw = BufferedWriter(FileWriter(file.absoluteFile, true))
            fw.write(stringBuffer.toString())
            fw.flush()
            fw.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Log.e(TAG, "time:$formattedTime")
        Log.e(TAG, "error -- ${stringBuffer.toString()}")
    }

    private fun getLogName(): String {
//        val formattedTime: String = formatTime("yyyy-MM-dd-HH:mm:ss.SSS")
        var formattedTime = System.currentTimeMillis()
        return "$formattedTime.log"
    }

    private fun formatTime(format: String): String {
        val currentTime = Date() // 获取当前时间
        val dateFormat = SimpleDateFormat(format)
        val formattedTime: String = dateFormat.format(currentTime)
        return formattedTime
    }

    private fun getPath(): String {
//        var externalCacheDirs = ContextCompat.getExternalCacheDirs(context!!)[0]
        var s =
            context!!.externalCacheDir!!.absolutePath + File.separator + "log" + File.separator + "crash"
        var file = File(s)
        file.mkdirs()
        return file.absolutePath
    }
}