package com.sorpresario.scheduler.portlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.sorpresario.scheduler.listener.SorpresarioJobSchedulerListener;
import com.sorpresario.scheduler.portlet.manager.SchedulerManagerUtil;

public final class JobsSchedulerPortlet extends MVCPortlet {
	
	public JobsSchedulerPortlet() {
		super();
	}

	@Override
	public void init() throws PortletException {
		long methodTime = System.currentTimeMillis();
		System.out.println("init JobsSchedulerPortlet >> start");

		super.init();
		SchedulerManagerUtil.restartListeners(SorpresarioJobSchedulerListener.class.getName());

		methodTime = System.currentTimeMillis() - methodTime;
		System.out.println("init JobsSchedulerPortlet << end in " + methodTime + "ms");
	}

	@Override
	public void destroy() {
		long methodTime = System.currentTimeMillis();
		System.out.println("destroy JobsSchedulerPortlet >> start");

		super.destroy();

		SchedulerManagerUtil.removeMessageListeners(SorpresarioJobSchedulerListener.class.getName());

		methodTime = System.currentTimeMillis() - methodTime;
		System.out.println("destroy JobsSchedulerPortlet << end in " + methodTime + "ms");
	}

	@Override
	public void doView(RenderRequest renderRequest,	RenderResponse renderResponse) throws IOException, PortletException {
		super.doView(renderRequest, renderResponse);
	}

	@Override
	public void processAction(ActionRequest actionRequest,	ActionResponse actionResponse) throws IOException, PortletException {
		String action = actionRequest.getParameter("javax.portlet.action");
		System.out.println("JobsSchedulerPortlet-processAction >> action : " + action);
		if (action == null) {

			String groupId = SorpresarioJobSchedulerListener.class.getName();

			boolean enable = ParamUtil.getBoolean(actionRequest, "enable",
					false);

			int startTimeHourPart = ParamUtil.getInteger(actionRequest,	"startTimeHourPart", 12);
			int startTimeMinutesPart = ParamUtil.getInteger(actionRequest,	"startTimeMinutesPart", 0);

			String every = ParamUtil.getString(actionRequest, "every", "day");

			/* Day Params */
			Calendar c = Calendar.getInstance();
			String date = c.get(Calendar.DATE) + "/"+ (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
			String dayDate = ParamUtil.getString(actionRequest, "dayDate", date);

			/* Daily Params */
			String dailyChoise = ParamUtil.getString(actionRequest,	"dailyChoise", "first-choise");
			String dailyChoiseFirstDays = ParamUtil.getString(actionRequest,"dailyChoiseFirstDays", "1");

			/* Weekly Params */
			boolean weeklyMonday = ParamUtil.getBoolean(actionRequest,	"weeklyMonday", true);
			boolean weeklyTuesday = ParamUtil.getBoolean(actionRequest,	"weeklyTuesday", true);
			boolean weeklyWednesday = ParamUtil.getBoolean(actionRequest,"weeklyWednesday", true);
			boolean weeklyThursday = ParamUtil.getBoolean(actionRequest,"weeklyThursday", true);
			boolean weeklyFriday = ParamUtil.getBoolean(actionRequest,"weeklyFriday", true);
			boolean weeklySaturday = ParamUtil.getBoolean(actionRequest,"weeklySaturday", true);
			boolean weeklySunday = ParamUtil.getBoolean(actionRequest,	"weeklySunday", true);

			/* Monthly Params */
			String monthlyDayOfMonth = ParamUtil.getString(actionRequest,"monthlyDayOfMonth", "1");

			String cronExpression = "";
			PortletPreferences pp = actionRequest.getPreferences();

			try {

				/* Scheduler */

				JSONObject jsonObjectStored = JSONFactoryUtil.createJSONObject(pp.getValue("" + groupId, ""));

				boolean isAlreadyEnabled = jsonObjectStored.getBoolean("enable", false);
				if (isAlreadyEnabled != enable) {

					if (enable) {

						JSONObject jsonGroupId = JSONFactoryUtil.createJSONObject();

						jsonGroupId.put("enable", String.valueOf(enable));
						jsonGroupId.put("startTimeHourPart",String.valueOf(startTimeHourPart));
						jsonGroupId.put("startTimeMinutesPart",	String.valueOf(startTimeMinutesPart));

						jsonGroupId.put("every", every);

						/* Day preferences */
						jsonGroupId.put("dayDate", dayDate);

						/* Daily preferences */
						jsonGroupId.put("dailyChoise", dailyChoise);
						jsonGroupId.put("dailyChoiseFirstDays",	dailyChoiseFirstDays);

						/* Weekly preferences */
						jsonGroupId.put("weeklyMonday",String.valueOf(weeklyMonday));
						jsonGroupId.put("weeklyTuesday",String.valueOf(weeklyTuesday));
						jsonGroupId.put("weeklyWednesday",	String.valueOf(weeklyWednesday));
						jsonGroupId.put("weeklyThursday",String.valueOf(weeklyThursday));
						jsonGroupId.put("weeklyFriday",String.valueOf(weeklyFriday));
						jsonGroupId.put("weeklySaturday",String.valueOf(weeklySaturday));
						jsonGroupId.put("weeklySunday",	String.valueOf(weeklySunday));

						/* Monthly preferences */
						jsonGroupId.put("monthlyDayOfMonth", monthlyDayOfMonth);

						pp.setValue("" + groupId, jsonGroupId.toString());

						if ("day".equalsIgnoreCase(every)) {
							Calendar cronDate = CalendarFactoryUtil.getCalendar();
							try {
								cronDate.setTimeInMillis(new SimpleDateFormat("d/MM/yyyy HH:mm").parse(dayDate + " " + startTimeHourPart + ":"	+ startTimeMinutesPart).getTime());

								cronExpression = "0" + " "
										+ cronDate.get(Calendar.MINUTE) + " "
										+ cronDate.get(Calendar.HOUR_OF_DAY)
										+ " " + cronDate.get(Calendar.DATE)
										+ " "
										+ (cronDate.get(Calendar.MONTH) + 1)
										+ " ? " + cronDate.get(Calendar.YEAR);

							} catch (ParseException e) {
								e.printStackTrace();
							}

						} else if ("daily".equalsIgnoreCase(every)) {

							System.out.println("every : " + every);
							System.out.println("everyDailyChoise : " + dailyChoise);
							System.out.println("everyDailyChoiseFirstDays : "+ dailyChoiseFirstDays);

							if ("first-choise".equalsIgnoreCase(dailyChoise)) {
								cronExpression = "0" + " "
										+ startTimeMinutesPart + " "
										+ startTimeHourPart + " 1/"
										+ dailyChoiseFirstDays + " *" + " ?"
										+ " *";
							} else if ("second-choise".equalsIgnoreCase(dailyChoise)) {
								System.out.println("everyDailyChoise : " + dailyChoise);

								cronExpression = "0" + " "
										+ startTimeMinutesPart + " "
										+ startTimeHourPart + " ?" + " *"
										+ " MON-FRI" + " *";
							}
						} else if ("weekly".equalsIgnoreCase(every)) {

							String everyWeekly = "";
							String separatore = "";
							if (weeklyMonday) {
								everyWeekly += separatore + "MON";
								separatore = ",";
							}
							if (weeklyTuesday) {
								everyWeekly += separatore + "TUE";
								separatore = ",";
							}
							if (weeklyWednesday) {
								everyWeekly += separatore + "WED";
								separatore = ",";
							}
							if (weeklyThursday) {
								everyWeekly += separatore + "THU";
								separatore = ",";
							}
							if (weeklyFriday) {
								everyWeekly += separatore + "FRI";
								separatore = ",";
							}
							if (weeklySaturday) {
								everyWeekly += separatore + "SAT";
								separatore = ",";
							}
							if (weeklySunday) {
								everyWeekly += separatore + "SUN";
								separatore = ",";
							}

							cronExpression = "0" + " " + startTimeMinutesPart+ " " + startTimeHourPart + " ?" + " *"+ " " + everyWeekly + " *";
						} else if ("monthly".equalsIgnoreCase(every)) {
							cronExpression = "0" + " " + startTimeMinutesPart+ " " + startTimeHourPart + " "+ monthlyDayOfMonth + " 1/1" + " ? " + " *";

						}

						System.out.println("processAction - cronExpression : "	+ cronExpression);

						ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

						String portletId = (String) actionRequest.getAttribute(WebKeys.PORTLET_ID);

						long userId = (Long) actionRequest.getAttribute(WebKeys.USER_ID);

						long plid = themeDisplay.getLayout().getPlid();

						JSONObject payload = JSONFactoryUtil.createJSONObject();
						payload.put("plid", plid);
						payload.put("portletId", portletId);
						payload.put("groupId", groupId);
						payload.put("userId", userId);

						SchedulerManagerUtil.schedule(String.valueOf(groupId),	SorpresarioJobSchedulerListener.class.getName(),	"liferay/sorpresario", cronExpression,payload.toString());
					
						System.out.println("Job on groupId " + groupId	+ " scheduled !!! ");
					} else {
						pp.reset(groupId);

						try {
							SchedulerManagerUtil.unschedule("" + groupId,	SorpresarioJobSchedulerListener.class.getName());
							System.out.println("Job on groupId " + groupId	+ " unscheduled !!! ");
						} catch (Exception e1) {
						}
					}
				}

				pp.store();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
