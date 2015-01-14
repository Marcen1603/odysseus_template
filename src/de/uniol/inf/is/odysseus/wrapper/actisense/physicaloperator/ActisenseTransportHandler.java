package de.uniol.inf.is.odysseus.wrapper.actisense.physicaloperator;


import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.actisense.SWIG.ActisenseCallback;
import de.uniol.inf.is.odysseus.wrapper.actisense.SWIG.ActisenseWrapper;
import de.uniol.inf.is.odysseus.wrapper.actisense.SWIG.n2kMessage;


public class ActisenseTransportHandler extends AbstractPushTransportHandler 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(ActisenseTransportHandler.class);
	private final Object processLock = new Object();

	private String comPort;
	private int baudRate;
	private ActisenseWrapper actisense;
	private ActisenseCallback cb;
	
	
	
	public ActisenseTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public ActisenseTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		comPort = options.get("comport");
		baudRate = options.getInt("baudrate", 115200);
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new ActisenseTransportHandler(protocolHandler, options);
	}


	@Override public String getName() { return "Actisense"; }

	
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			actisense = new ActisenseWrapper(comPort, baudRate);
			actisense.start();
			cb = new ActisenseCallback()
								 	{
										@Override public void run(n2kMessage N2Kmsg)
										{
											onReceive(N2Kmsg);
										}
								 	};
			actisense.setCallback(	cb);
		}
		
		fireOnConnect();
	}
	
	protected void onReceive(n2kMessage n2Kmsg) 
	{
		int len=n2Kmsg.getLength();
		byte[] buffer = new byte[len];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = n2Kmsg.getData(i);
		}
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		//String s = javax.xml.bind.DatatypeConverter.printHexBinary(buffer);
		
		byteBuffer.position(byteBuffer.limit());
		
		fireProcess(byteBuffer);
	}

	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			fireOnDisconnect();
			
			actisense.stop();
			actisense = null;
			
			cb = null;
		}
	}

		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof ActisenseTransportHandler)) {
    		return false;
    	}
    	ActisenseTransportHandler other = (ActisenseTransportHandler)o;
    	if(!this.comPort.equals(other.comPort))
    		return false;
    	
    	return true;
    }

	@Override
	public void processOutOpen() throws IOException {
		throw new IllegalArgumentException("Operator is not a Sink");
		
	}

	@Override
	public void processOutClose() throws IOException {
		throw new IllegalArgumentException("Operator is not a Sink");
		
	}

	@Override
	public void send(byte[] message) throws IOException 
	{
		throw new IllegalArgumentException("Sending Not Supported");
		// TODO Auto-generated method stub
		
	}
}
