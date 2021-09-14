#include <string.h>
#include <jni.h>

JNIEXPORT jstring JNICALL Java_fm.magiclantern.app_MainActivity_stringFromJNI(JNIEnv* env, jobject thiz) {
	int i = 1 + 2;
	char *string = "Hello. I am C code running on an Android app.";

    return (*env)->NewStringUTF(env, string);
}

JNIEXPORT jintArray JNICALL Java_fm.magiclantern.app_MainActivity_arrayTest(JNIEnv* env, jobject thiz){
	jobjectArray array;
	int size = 5;
	array = (*env)->NewObjectArray(
		env,
		size,
		(*env)->FindClass(env, "java/lang/String"),
		(*env)->NewStringUTF(env, "")
	 );

	if (array == NULL) {
		return NULL;
	}

	char *testArray[5] = {
		"2",
		"1",
		"4",
		"5",
		"6"
	};

	for (int i = 0; i < size; i++) {
		(*env)->SetObjectArrayElement(
			env,
			array,
			i,
			(*env)->NewStringUTF(env, testArray[i])
		);
	}

	//(*env)->SetIntArrayRegion(env, result, 0, size, fill);
	return array;
}
