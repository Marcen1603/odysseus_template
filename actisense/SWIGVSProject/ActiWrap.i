/* File : ActiWrap.i */
%module(directors="1") ActisenseJava

%include "std_string.i"
%include "various.i"
%include "exception.i"
%include "carrays.i"
%include <windows.i>
%include <typemaps.i>

%typemap(throws, throws="java.lang.RuntimeException") std::exception
{
  jclass excep = jenv->FindClass("java/lang/RuntimeException");
  if (excep)
    jenv->ThrowNew(excep, $1.what());
  return $null;
}
/* %typemap(javabase) std::ios_base::failure "java.lang.Exception"; */

%typemap(in) (void *buffer, long size) { 
  $1 = jenv->GetDirectBufferAddress($input); 
  $2 = (long)(jenv->GetDirectBufferCapacity($input)); 
} 

/* These 3 typemaps tell SWIG what JNI and Java types to use */ 
%typemap(jni) (void *buffer, long size) "jobject" 
%typemap(jtype) (void *buffer, long size) "java.nio.ByteBuffer" 
%typemap(jstype) (void *buffer, long size) "java.nio.ByteBuffer" 
%typemap(javain) (void *buffer, long size) "$javainput" 
%typemap(javaout) (void *buffer, long size) { 
    return $jnicall; 
} 

%typemap(directorin, descriptor="Ljava/nio/ByteBuffer;") (void *buffer, long size) {
$input = (jenv)->NewDirectByteBuffer($1, $2); } 

%typemap(javadirectorin) (void *buffer, long size) "$jniinput"
%typemap(javadirectorout) (void *buffer, long size) "$javacall"

%{
#include "ActiWrap.h"

%}

/* turn on director wrapping Callback */
%feature("director") ActisenseWrapper;

/*typedef unsigned int u32;
typedef unsigned char u8;*/
%include "ActiWrap.h"


 

