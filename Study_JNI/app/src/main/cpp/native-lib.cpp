#include <jni.h>
#include <string>
#include  <android/log.h>

#define TAG "TEST_TAG"

// 定义info信息

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
// 定义debug信息
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

// 定义error信息
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

/**
 * 静态注册：按照JNI规范书写函数名：java_类路径_方法名（路径用下划线分隔）
动态注册：JNI_OnLoad中指定Java Native函数与C函数的对应关系
 */
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_study_1jni_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
// extern "C" 将这部分代码按c语言的进行编译，而不是C++的。由于C++支持函数重载，因此编译器编译函数的过程中会将函数的参数类型也加到编译后的代码中，而不仅仅是函数名；
// 而C语言并不支持函数重载，因此编译C语言代码的函数时不会带上函数的参数类型，一般只包括函数名
// 避免因为使用C++编译器 造成找不到该方法
extern "C"
// JNIEXPORT 方法的可见性 是否可以被外部调用 类似于JAVA里面的public
JNIEXPORT jstring JNICALL
Java_com_example_study_1jni_MainActivity_nativeGetName(JNIEnv *env, jobject thiz, jobject jni_demo) {
    // 获取java对象的class 类类型
    jclass jmodelClass = (env)->GetObjectClass(jni_demo);

    if (jmodelClass == 0) {
        return nullptr;
    }
    //获取jni_demo的 id字段的值
    jfieldID jfieldId = env->GetFieldID(jmodelClass, "id", "J");
    long id = (env)->GetLongField(jni_demo, jfieldId);
    LOGI("-->>打印的id=%d", id);
    //获取jni_demo的 name字段的值
    jfieldID nameField = env->GetFieldID(jmodelClass, "name", "Ljava/lang/String;");
    // jstring 不能%s打印！！！
    jstring jname = (jstring) (env)->GetObjectField(jni_demo, nameField);

    const char *name = (env)->GetStringUTFChars(jname, 0);
    // 【jni】利用NewStringUTF，char*转jstring，
//    jstring param_string = env->NewStringUTF(name);
    LOGE("打印的name=%s", name);

    jmethodID getNameMethod = (env)->GetMethodID(jmodelClass, "getName", "()Ljava/lang/String;");
    if (getNameMethod == 0) {
        return nullptr;
    }

    //调用java层的方法 返回值为 string
//    这里强转为jstring
    jstring getNameStr = static_cast<jstring>((env)->CallObjectMethod(jni_demo, getNameMethod));

    return getNameStr;
}


