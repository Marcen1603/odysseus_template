package de.uniol.inf.is.odysseus.wrapper.actisense.physicaloperator;


import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.actisense.SWIG.ActisenseWrapper;

public class ActisenseTransportHandler extends AbstractPushTransportHandler 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(ActisenseTransportHandler.class);

	private String comPort;
	private int baudRate;
	private ActisenseWrapper actisense;
	
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
		actisense = new ActisenseWrapper(comPort, baudRate)
				 	{
						@Override public void onMessage(ByteBuffer buffer)
						{
							ByteBuffer copy = ByteBuffer.allocate(buffer.capacity());
							copy.put(buffer);
								
							fireProcess(copy);
						}
				 	};

		actisense.start();
		
		fireOnConnect();
	}

	@Override public void processInClose() throws IOException 
	{
		fireOnDisconnect();
			
		actisense.stop();
		actisense = null;
	}
		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof ActisenseTransportHandler))
    		return false;
    	
    	ActisenseTransportHandler other = (ActisenseTransportHandler)o;
    	if (!comPort.equals(other.comPort))	return false;
    	if (baudRate != other.baudRate)		return false;
    	
    	return true;
    }

	@Override
	public void processOutOpen() throws IOException 
	{
		throw new IllegalArgumentException("Operator is not a Sink");		
	}

	@Override
	public void processOutClose() throws IOException 
	{
		throw new IllegalArgumentException("Operator is not a Sink");		
	}

	@Override
	public void send(byte[] message) throws IOException 
	{
		throw new IllegalArgumentException("Sending Not Supported");
	}
}
