package com.hugo.study_toolbar.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoryUtils {
    private static final String TAG = "MemoryUtils";

//    RSS和PSS 的区别：
//
//RSS - Resident Set Size 实际使用物理内存（包含共享库占用的内存）
//PSS - Proportional Set Size 实际使用的物理内存（比例分配共享库占用的内存）假如有3个进程使用同一个共享库，那么每个进程的PSS就包括了1/3大小的共享库内存。通常我们使用PSS大小来作为内存性能指标。
//————————————————
//
//                            版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//原文链接：https://blog.csdn.net/xiaobaaidaba123/article/details/123299035

//    获取应用整体虚拟内存使用情况
    public static long getProcessRealMemory() {
//        获取虚拟内存信息 adb shell cat /proc/18558/status
        String memFilePath = "/proc/" + android.os.Process.myPid() + "/status";
        LogUtils.e(TAG, memFilePath);
        BufferedReader bufferedReader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(memFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d(TAG, " read line : " + line);
                if (!TextUtils.isEmpty(line) && line.contains("VmRSS")) {
                    String rss = line.split(":")[1].trim().split(" ")[0];
                    return Integer.parseInt(rss) * 1024;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;

    }

    //    传入当前进程的 pid 获取到当前进程的总内存占用情况，其中不仅包括了虚拟机的内存占用情况，还包括原生层和其它内存占用
//    优点：获取的是PSS
//
//缺点：安卓P以上限制频率，需要隔约5分钟（不同手机间隔不同）才能获取到新的值。而且获取的 PSS 不包括 Graphics。
    public static void getAppPss() {
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        final Debug.MemoryInfo[] memInfo = activityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
        final int totalPss = memInfo[0].getTotalPss();
        LogUtils.e("pss", GsonUtils.toJson(memInfo));
    }

// dumpsys是android系统里面的一个可执行文件  是一个分析android设备问题、查看运行状态、使用情况等十分有效的工具
//    adb shell dumpsys meminfo --local 19915  获取档期App整体物理内存和详细分类的物理内存数据
//    adb shell dumpsys meminfo <PID> <包名 com.hugo.study_toolbar>   adb shell dumpsys meminfo com.hugo.study_toolbar

// App Summary中的数据 与Profiler 显示的内存数据 一致

//    Applications Memory Usage (in Kilobytes):
//    Uptime: 13036594 Realtime: 13036594
//
//            ** MEMINFO in pid 19915 [com.hugo.study_toolbar] **
//    Pss  Private  Private  SwapPss     Heap     Heap     Heap
//    Total    Dirty    Clean    Dirty     Size    Alloc     Free
//                ------   ------   ------   ------   ------   ------   ------
//    Native Heap    23412    23316        0      121        0        0        0
//    Dalvik Heap     3058     3024        8       29        0        0        0
//    Dalvik Other     1397     1384        8        1
//    Stack       68       68        0        0
//    Ashmem        2        0        0        0
//    Gfx dev     4944     4944        0        0
//    Other dev       20        0       20        0
//            .so mmap     3654       92      640        7
//            .jar mmap     2973        0     1468        0
//            .apk mmap      190        0       60        0
//            .ttf mmap      144        0       44        0
//            .dex mmap    11026    10944        4        0
//            .oat mmap      113        0        0        0
//            .art mmap     6145     5580      148       25
//    Other mmap      370      132        4        0
//    Unknown     1106     1068       16       19
//    TOTAL    58824    50552     2420      202        0        0        0
//
//    App Summary
//    Pss(KB)
//                        ------
//    Java Heap:     8752
//    Native Heap:    23316
//    Code:    13252
//    Stack:       68
//    Graphics:     4944
//    Private Other:     2640
//    System:     5852
//
//    TOTAL:    58824       TOTAL SWAP PSS:      202



/*-------------------------------------------------------------------------------------------------------*/

//    线上内存数据获取 应用 Java内存使用情况 不包含 native内存
    public static void getRuntimeMemory() {
//        通过Runtime接口获取运行时内存（JVM消耗，不包括Native）
        long maxMem = Runtime.getRuntime().maxMemory(); //当前虚拟机可用的最大内存
        long totalMem = Runtime.getRuntime().totalMemory(); //当前虚拟机已分配的内存
        long freeMem = Runtime.getRuntime().freeMemory(); //当前虚拟机已分配内存中未使用的部分
        long currentMemUsage = totalMem - freeMem; // 当前虚拟机内存占用量
    }

//    线上 获取应用Java、Native、Graphics 物理内存使用情况
    public static void getDebugMemState() {

        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String javaHeap = memoryInfo.getMemoryStat("summary.java-heap");
            String nativeHeap = memoryInfo.getMemoryStat("summary.native-heap");
            String code = memoryInfo.getMemoryStat("summary.code");
            String stack = memoryInfo.getMemoryStat("summary.stack");
            String graphics = memoryInfo.getMemoryStat("summary.graphics");
            String privateOther = memoryInfo.getMemoryStat("summary.private-other");
            String system = memoryInfo.getMemoryStat("summary.system");
            String swap = memoryInfo.getMemoryStat("summary.total-swap");

            LogUtils.e(GsonUtils.toJson(memoryInfo));
        }
    }

    //通过 ActivityManager 获取到的 MemoryInfo 是整个手机的内存信息
    public static void getMobileMemory() {
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();//存放内存信息的对象
        activityManager.getMemoryInfo(memInfo);//传入参数，将获得数据保存在memInfo对象中
        long availMem = memInfo.availMem / 1000000;//可用内存
        boolean isLowMem = memInfo.lowMemory;//是否达到最低内存
        long threshold = memInfo.threshold / 1000000;//临界值，达到这个值，进程就要被杀死
        long totalMem = memInfo.totalMem / 1000000;//总内存
    }

}
