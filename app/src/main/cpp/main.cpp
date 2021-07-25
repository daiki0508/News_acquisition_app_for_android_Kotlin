#include <jni.h>
#include <cstring>
#include <cstring>
#include <android/log.h>
#include <dlfcn.h>
#include <unistd.h>

#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/ptrace.h>
#include <sys/wait.h>
#include <pthread.h>
//
// Created by oocha on 2021/07/07.
//

struct VT_JdwpAdbState {
    unsigned long x;
    unsigned long y;
    void * JdwpSocketState_destructor;
    void * _JdwpSocketState_destructor;
    void * Accept;
    void * showmanyc;
    void * ShutDown;
    void * ProcessIncoming;
};

extern "C"
JNIEXPORT void JNICALL
Java_com_websarva_wings_android_newsapp_1kotlin_SecurityCheckClass_crashOnInit(JNIEnv *env,
                                                                               jobject thiz) {
    void* lib = dlopen("libart.so", RTLD_NOW);

    if (lib == NULL) {
        dlerror();
    }else{

        struct VT_JdwpAdbState *vtable = ( struct VT_JdwpAdbState *)dlsym(lib, "_ZTVN3art4JDWP12JdwpAdbStateE");

        if (vtable == 0) {
        }else {

            // Let the fun begin!

            unsigned long pagesize = sysconf(_SC_PAGE_SIZE);
            unsigned long page = (unsigned long)vtable & ~(pagesize-1);

            mprotect((void *)page, pagesize, PROT_READ | PROT_WRITE);

            vtable->ProcessIncoming = vtable->ShutDown;

            // Reset permissions & flush cache

            mprotect((void *)page, pagesize, PROT_READ);

        }
    }
}

static int child_pid;

void *monitor_pid() {

    int status;

    waitpid(child_pid, &status, 0);

    /* Child status should never change. */

    _exit(0); // Commit seppuku

}

void anti_debug() {

    child_pid = fork();

    if (child_pid == 0)
    {
        int ppid = getppid();
        int status;

        if (ptrace(PTRACE_ATTACH, ppid, NULL, NULL) == 0)
        {
            waitpid(ppid, &status, 0);

            ptrace(PTRACE_CONT, ppid, NULL, NULL);

            while (waitpid(ppid, &status, 0)) {

                if (WIFSTOPPED(status)) {
                    ptrace(PTRACE_CONT, ppid, NULL, NULL);
                } else {
                    // Process has exited
                    _exit(0);
                }
            }
        }

    } else {
        pthread_t t;

        /* Start the monitoring thread */
        pthread_create(&t, NULL, reinterpret_cast<void *(*)(void *)>(monitor_pid), (void *)NULL);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_websarva_wings_android_newsapp_1kotlin_SecurityCheckClass_antidebug(JNIEnv *env,
                                                                             jobject thiz) {
    anti_debug();
}