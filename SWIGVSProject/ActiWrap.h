#ifndef ACTI_WRAP_H
#define ACTI_WRAP_H

#include <exception>
#include "NMEA2000_Datatypes.h"
#include <iostream>

class n2kMessage
{
private:
	const signed char  *msg;
	int length;
//	signed char array[N2K_MAXLEN_TP];
public:
	explicit n2kMessage(const signed char  *msg, int length):msg(msg),length(length){
//		memcpy(&array[0],&msg,sizeof(msg));
	}

	int getLength() { return length;} //15; }
	signed char getData(int index) 
	{ 
		return msg[index];

//		return *((s8*)&msg.Data[index]); 
	}
};


class ActisenseCallback {
public:
	virtual ~ActisenseCallback(){}
	virtual void run(n2kMessage N2Kmsg) 
	{
		std::cout << "Standard Callback"<<std::endl;
	}

};



class ActisenseWrapper
{

public:
	ActisenseWrapper(std::string comPort, int baudRate) throw(std::exception);
	~ActisenseWrapper(){ delCallback(); };

	void	start();
	void	stop();
	void delCallback() { delete JavaCallback; JavaCallback = 0; }
	void setCallback(ActisenseCallback *cb) { delCallback(); JavaCallback = cb; }
	
private:
		ActisenseCallback* JavaCallback;
		static void __stdcall RxCallback (void* p);
		void onReceive();

		std::string comPort;
		int baudRate;
		s32  Handle;

		s32 ApiError;
		s32 portNumber;

};

#endif