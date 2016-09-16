package com.sorpresario.scheduler.portlet.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.ParallelDestination;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactoryUtil;
import com.liferay.portal.kernel.scheduler.TriggerType;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse;

public class SchedulerManagerUtil {

	private static Map<String, MessageListener> _listeners = new HashMap<String, MessageListener>();
	private static Map<String, String> _listenersDestination = new HashMap<String, String>();
		
	public static void restartListeners( String listenerClass ) {
		long methodTime = System.currentTimeMillis();
		System.out.println( "restartListeners >> start" );
		System.out.println( "listenerClass : " + listenerClass );
		
		try {
			
			List<SchedulerResponse> schedulerResponses = SchedulerEngineHelperUtil.getScheduledJobs( listenerClass, StorageType.PERSISTED );
			for( SchedulerResponse schedulerResponse : schedulerResponses ) {
				
				System.out.println( "----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+" );
				System.out.println( schedulerResponse.getJobName() );
				System.out.println( schedulerResponse.getGroupName() );
				System.out.println( schedulerResponse.getDescription() );
				System.out.println( schedulerResponse.getDestinationName() );
				System.out.println( schedulerResponse.getStorageType().toString() );
				
				createDestination( schedulerResponse.getDestinationName() );

				MessageListener jl = registerMessageListener( schedulerResponse.getDestinationName(), listenerClass );
		    	_listeners.put( schedulerResponse.getJobName(), jl );		
		    	_listenersDestination.put( schedulerResponse.getJobName(), schedulerResponse.getDestinationName() );		
			}
		}
		catch( Exception e ) {
			e.printStackTrace();
		}

		methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "restartListeners << end in " + methodTime + "ms" );		
	}


	public static void removeMessageListeners( String listenerClass ) {
		long methodTime = System.currentTimeMillis();
		System.out.println( "removeMessageListeners >> start" );		
		
		System.out.println( "listenerClass : " + listenerClass );
		
		try {
			List<SchedulerResponse> schedulerResponses = SchedulerEngineHelperUtil.getScheduledJobs( listenerClass, StorageType.PERSISTED );
			for( SchedulerResponse schedulerResponse : schedulerResponses ) {
				Destination destination = MessageBusUtil.getMessageBus().getDestination( schedulerResponse.getDestinationName() );
				if( destination != null ) {					
					MessageListener listener = _listeners.get( schedulerResponse.getJobName() );
					if( listener != null ) {
						destination.unregister( listener );
						_listeners.remove( schedulerResponse.getJobName() );
						_listenersDestination.remove( schedulerResponse.getJobName() );
					}
				}
			}
		}
		catch( SchedulerException e ) {
			e.printStackTrace();
		}
		
        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "removeMessageListeners << end in " + methodTime + "ms" );		
	}
	
	public static void removeMessageListener( String jobName, String listenerClass ) {
		long methodTime = System.currentTimeMillis();
		System.out.println( "removeMessageListener >> start" );		
		
		System.out.println( "jobName : " + jobName );
		System.out.println( "listenerClass : " + listenerClass );
			Destination destination = MessageBusUtil.getMessageBus().getDestination( _listenersDestination.get( jobName ) );
					if( destination != null ) {
					
						MessageListener listener = _listeners.get( jobName );
						if( listener != null ) {
							destination.unregister( listener );
							_listeners.remove( jobName );
							_listenersDestination.remove( jobName );
						}
					}
					
        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "removeMessageListener << end in " + methodTime + "ms" );		
	}
	
	public static void schedule( String jobName, String listenerClass, String destinationName, String cronText, String payload ) throws Exception{
		long methodTime = System.currentTimeMillis();
		System.out.println( "schedule >> start" );		
		System.out.println( "jobName : " + jobName );
		System.out.println( "listenerClass : " + listenerClass );
		System.out.println( "destinationName : " + destinationName );
		System.out.println( "cronText : " + cronText );
		System.out.println( "payload : " + payload);
		
		/* Destination */
		createDestination( destinationName );
		
		/* Trigger */
		Trigger trigger = buildCronTrigger( jobName, listenerClass, cronText );
		//destinationName = DestinationNames.SCHEDULER_DISPATCH;
		/* Scheduler */
		SchedulerEngineHelperUtil.schedule( trigger, StorageType.PERSISTED, null, destinationName, payload, 0 );
		/* Listener */
		MessageListener jl = registerMessageListener( destinationName, listenerClass );
    	_listeners.put( jobName, jl );
    	_listenersDestination.put( jobName, destinationName );
    	
    	
        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "schedule << end in " + methodTime + "ms" );
	}	
	
	public static void unschedule( String jobName, String listenerClass ) 
		throws SchedulerException 
	{
		long methodTime = System.currentTimeMillis();
		System.out.println( "unschedule >> start" );

		System.out.println( "jobName : " + jobName );
		System.out.println( "listenerClass : " + listenerClass );

		SchedulerResponse scheduler = SchedulerEngineHelperUtil.getScheduledJob( jobName, listenerClass, StorageType.PERSISTED );

		/* Listener */
    	MessageBusUtil.unregisterMessageListener( scheduler.getDestinationName(), _listeners.get( jobName ) );
    	_listeners.remove( jobName );
    	_listenersDestination.remove( jobName );
    	
		/* Scheduler */
    	SchedulerEngineHelperUtil.delete( jobName, listenerClass, StorageType.PERSISTED );

        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "unschedule << end in " + methodTime + "ms" );
	}
		
	private static void createDestination( String destinationName ) {
		long methodTime = System.currentTimeMillis();
		System.out.println( "createDestination >> start" );
		System.out.println( "destinationName : " + destinationName );
//		if(MessageBusUtil.getMessageBus().getDestination(destinationName) == null) {
			ParallelDestination destination = new ParallelDestination();
			destination.setName(destinationName);
			MessageBusUtil.getMessageBus().addDestination( destination );
//		}
		destination.afterPropertiesSet();
        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "createDestination << end in " + methodTime + "ms" );
		
	}

	private static Trigger buildCronTrigger( String jobName, String jobGroup, String cronText ) 
		throws SchedulerException 
	{
		long methodTime = System.currentTimeMillis();
		System.out.println( "createDestination >> start" );

		System.out.println( "jobName : " + jobName );
		System.out.println( "jobGroup : " + jobGroup );
		System.out.println( "cronText : " + cronText );
		
		Date startDate = new Date();
	
		Trigger trigger = TriggerFactoryUtil.buildTrigger( TriggerType.CRON, jobName, jobGroup, startDate, null, cronText );
	
        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "createDestination << end in " + methodTime + "ms" );
        
		return trigger;
	}

	private static MessageListener registerMessageListener( String destinationName, String listenerClass ) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException 
	{
		long methodTime = System.currentTimeMillis();
		System.out.println( "registerMessageListener >> start" );

		System.out.println( "destinationName : " + destinationName );
		System.out.println( "listenerClass : " + listenerClass );
		
		MessageListener jl = (MessageListener) Class.forName( listenerClass ).newInstance();
		
	    MessageBusUtil.registerMessageListener( destinationName, jl );
	    
        methodTime = System.currentTimeMillis() - methodTime;
        System.out.println( "registerMessageListener << end in " + methodTime + "ms" );
        
		return jl;
	}
		
	
}
