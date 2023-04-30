#include <jni.h>

//
// Created by MacBook Pro on 29/04/2023.
//
#include <stdio.h>
#include <math.h>
#include <string.h>

int x = 0, y = 0, n, t, i, flag;

long int e[1000], d[1000], temp[1000], j, m[1000], en[1000];

int prime(long int);

void encryption_key();

jstring decrypt(JNIEnv *env, int *augEn, int *augTemp, int lengthKey);

long int cd(long int);

void prerequsites() {
    x = 5;
    y = 17;
    n = x * y;
    t = (x - 1) * (y - 1);
    encryption_key();
}


void encryption_key() {
    int k;
    k = 0;
    for (i = 2; i < t; i++) {
        if (t % i == 0)
            continue;
        flag = prime(i);
        if (flag == 1 && i != x && i != y) {
            e[k] = i;
            flag = cd(e[k]);
            if (flag > 0) {
                d[k] = flag;
                k++;
            }
            if (k == 99)
                break;
        }
    }
}

long int cd(long int a) {
    long int k = 1;
    while (1) {
        k = k + t;
        if (k % a == 0)
            return (k / a);
    }
}

int prime(long int pr) {
    int i;
    j = sqrt(pr);
    for (i = 2; i <= j; i++) {
        if (pr % i == 0)
            return 0;
    }
    return 1;
}


jstring decrypt(JNIEnv *env, int *augEn, int *augTemp, int lengthKey) {
    char keyValue[lengthKey+1];
    char keyValueCpy[1000];
    memset(keyValueCpy, 0, 1000);
    prerequsites();

    long int pt, ct, key = d[0], k;
    i = 0;


    while (*augEn != -1) {
        ct = *augTemp;
        k = 1;
        for (j = 0; j < key; j++) {
            k = k * ct;
            k = k % n;
        }
        pt = k + 96;
        m[i] = pt;
        i++;
        augEn++;
        augTemp++;
    }
    m[i] = -1;
    for (i = 0; i < lengthKey; i++) {
        int len = snprintf(keyValue, lengthKey, "%c", m[i]);
        keyValue[len] = 0;
        strcat(keyValueCpy, keyValue);
    }

    return (*env)->NewStringUTF(env, keyValueCpy);

}


JNIEXPORT jstring JNICALL
Java_com_therevotech_revoschat_utils_RevosKeys_encrypt(JNIEnv *env, jobject thiz, jcharArray text) {
    flag = prime(67);
    flag = prime(77);

//    for(i = 0; msg[i] != NULL; i++)

    n = x * y;
    t = (x-1) * (y-1);
    encryption_key();
}