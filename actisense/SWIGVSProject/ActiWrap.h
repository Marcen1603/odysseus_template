#ifndef ACTI_WRAP_H
#define ACTI_WRAP_H

#include <exception>
#include "NMEA2000_Datatypes.h"

class ActisenseWrapper
{
public:
	ActisenseWrapper(std::string comPort, int baudRate) throw(std::exception);
	virtual ~ActisenseWrapper() {};

	void start();
	void stop();

	virtual void onMessage(void *buffer, long size);
	
private:
	static void __stdcall RxCallback (void* p);
	void onReceive();

	std::string comPort;
	int baudRate;
	s32  Handle;

	s32 ApiError;
	s32 portNumber;
};

#endif