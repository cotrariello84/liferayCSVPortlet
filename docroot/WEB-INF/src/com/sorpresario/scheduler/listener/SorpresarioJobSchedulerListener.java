package com.sorpresario.scheduler.listener;

import java.util.Map;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;

public class SorpresarioJobSchedulerListener implements MessageListener {

	@Override
	public void receive(Message message) throws MessageListenerException {
		// TODO Auto-generated method stub
		System.out.println("Sono nel listener.");
		Map<String, Object> map = message.getValues();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		 
		}
	}

}
