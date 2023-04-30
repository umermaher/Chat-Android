// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("revoschat");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("revoschat")
//      }
//    }
#include <jni.h>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_therevotech_revoschat_utils_RevosKeys_getMapsApiKey(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("MAP_KEY");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_therevotech_revoschat_utils_RevosKeys_encrypt(JNIEnv *env, jobject thiz, jobject text) {

    return (jstring) "";
}
