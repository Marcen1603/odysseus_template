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


%{
#include "ActiWrap.h"

%}

/* turn on director wrapping Callback */
%feature("director") ActisenseCallback;

typedef unsigned int u32;
typedef unsigned char u8;
%include "ActiWrap.h"


 

