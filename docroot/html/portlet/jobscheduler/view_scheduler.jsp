<%@ include file="../init.jsp" %>

<%
String jsonGroupId = pp.getValue( ""+groupId, "" );
JSONObject jsonObjectGroupId = JSONFactoryUtil.createJSONObject( jsonGroupId );

boolean enable = jsonObjectGroupId.getBoolean( "enable", false );

int startTimeHourPart = jsonObjectGroupId.getInt( "startTimeHourPart", 12 );
int startTimeMinutesPart = jsonObjectGroupId.getInt( "startTimeMinutesPart", 0 );

String every = jsonObjectGroupId.getString( "every", "day" );

Calendar c = Calendar.getInstance();
String date = c.get( Calendar.DATE ) + "/" + ( c.get( Calendar.MONTH ) + 1 ) + "/" + c.get( Calendar.YEAR );
String dayDate = jsonObjectGroupId.getString( "dayDate", date );

String dailyChoise = jsonObjectGroupId.getString( "dailyChoise", "first-choise" );
int dailyChoiseFirstDays = jsonObjectGroupId.getInt( "dailyChoiseFirstDays", 1 );

boolean weeklyMonday = jsonObjectGroupId.getBoolean( "weeklyMonday", true );
boolean weeklyTuesday = jsonObjectGroupId.getBoolean( "weeklyTuesday", true );
boolean weeklyWednesday = jsonObjectGroupId.getBoolean( "weeklyWednesday", true );
boolean weeklyThursday = jsonObjectGroupId.getBoolean( "weeklyThursday", true );
boolean weeklyFriday = jsonObjectGroupId.getBoolean( "weeklyFriday", true );
boolean weeklySaturday = jsonObjectGroupId.getBoolean( "weeklySaturday", true );
boolean weeklySunday = jsonObjectGroupId.getBoolean( "weeklySunday", true );

int monthlyDayOfMonth = jsonObjectGroupId.getInt( "monthlyDayOfMonth", 1 );
%>

<aui:layout>
	
	<aui:row>
		
		<aui:column columnWidth="20">
			<aui:input type="checkbox" name="enable" id="enable" label="enabled" checked="<%= enable %>" />
		</aui:column>
		
		<aui:column columnWidth="60">

			<aui:select name="startTimeHourPart" id="startTimeHourPart" label="start-time" inlineLabel="true" inlineField="true" 
				disabled='<%= !enable %>' style="width: 56px;"
			>
				<%for( int i=0; i<=23; i++ ) { %>
					<aui:option value="<%= i %>" label='<%= (i<=9 ? "0"+i : i ) %>' selected='<%= (i==startTimeHourPart ? true : false ) %>' />
				<%}%>

			</aui:select>

			<aui:select name="startTimeMinutesPart" id="startTimeMinutesPart" label=" : " inlineLabel="true" inlineField="true" 
				disabled='<%= !enable %>' style="width: 56px;"
			>
				<%for( int i=0; i<=59; i+=1 ) { %>
					<aui:option value="<%= i %>" label='<%= (i<=9 ? "0"+i : i ) %>' selected='<%= (i==startTimeMinutesPart ? true : false ) %>' />
				<%}%>
			</aui:select>
		</aui:column>
		
		<aui:column columnWidth="20">
					
		</aui:column>
		
		
	</aui:row>
	
	<hr/>
	
	<aui:row>
		
		<aui:column columnWidth="20">

			<aui:input name="every" id="day" type="radio" label="day" 
				value="day"
				checked='<%= "day".equals( every ) %>' 
				disabled='<%= !enable %>' 
				/>

		</aui:column>
		<aui:column columnWidth="60">
			<aui:input type="text" name="dayDate" id="dayDate" cssClass="date-picker" placeholder="dd/mm/yyyy" label="" 
				value="<%= dayDate %>"
				disabled='<%= !( enable && "day".equals( every ) ) %>'  />
			<script>
			YUI().use( 'aui-datepicker', function(Y) {
				new Y.DatePicker( {
					trigger: 'input.date-picker',
					mask: '%d/%m/%Y',
					popover: { zIndex: 99 },
					panes: 1
				});
			});
			</script>
		</aui:column>
		<aui:column columnWidth="20">&nbsp;</aui:column>
	</aui:row>
	
	<hr/>

	<aui:row>

		<aui:column columnWidth="20">

			<aui:input name="every" id="everyDaily" type="radio" label="daily" 
				value="daily"
				checked='<%= "daily".equals( every ) %>'
				disabled='<%= !enable %>' 
			/>

		</aui:column>

		<aui:column columnWidth="60">

			<aui:row>

				<aui:input name="dailyChoise" id="dailyChoiseFirst" 
					type="radio" inlineField="true"
					label="every" 
					value="first-choise"
					checked='<%= "first-choise".equals( dailyChoise ) %>'
					disabled='<%= !( "daily".equals( every ) ) %>' 
				/>

				<aui:input name="dailyChoiseFirstDays" id="dailyChoiseFirstDays" 
					type="text" inlineField="true"
					label="" inlineLabel="true" 
					value="<%= dailyChoiseFirstDays %>" 
					style="width: 40px;"
					disabled='<%= !( "daily".equals( every ) && "first-choise".equals( dailyChoise ) ) %>' 
				/>

				<liferay-ui:message key="day(s)" />

			</aui:row>

			<aui:row>

				<aui:input name="dailyChoise" id="dailyChoiseSecond" 
					type="radio" inlineField="true"
					label="every-week-day" 
					value="second-choise"
					checked='<%= "second-choise".equals( dailyChoise ) %>'
					disabled='<%= !( "daily".equals( every ) ) %>' 
				/>

			</aui:row>

		</aui:column>

		<aui:column columnWidth="20">&nbsp;</aui:column>

	</aui:row>

	<hr/>



	<aui:row>

		<aui:column columnWidth="20">

			<aui:input name="every" id="everyWeekly" type="radio" label="weekly" 
				value="weekly"
				checked='<%= "weekly".equals( every ) %>'
				disabled='<%= !enable %>' 
			/>

		</aui:column>

		<aui:column columnWidth="60">

			<aui:input type="checkbox" name="weeklyMonday" id="weeklyMonday" 
					label="monday" value="<%= weeklyMonday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

			<aui:input type="checkbox" name="weeklyTuesday" id="weeklyTuesday" 
					label="tuesday" value="<%= weeklyTuesday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

			<aui:input type="checkbox" name="weeklyWednesday" id="weeklyWednesday" 
					label="wednesday" value="<%= weeklyWednesday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

			<aui:input type="checkbox" name="weeklyThursday" id="weeklyThursday" 
					label="thursday" value="<%= weeklyThursday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

			<aui:input type="checkbox" name="weeklyFriday" id="weeklyFriday" 
					label="friday" value="<%= weeklyFriday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

			<aui:input type="checkbox" name="weeklySaturday" id="weeklySaturday" 
					label="saturday" value="<%= weeklySaturday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

			<aui:input type="checkbox" name="weeklySunday" id="weeklySunday" 
					label="sunday" value="<%= weeklySunday %>" disabled='<%= !( "weekly".equals( every ) ) %>' />

		</aui:column>

		<aui:column columnWidth="20">&nbsp;</aui:column>
		
	</aui:row>
	
	<hr/>
	
	
	
	<aui:row>
		
		<aui:column columnWidth="20">
			
			<aui:input name="every" id="everyMonthly" type="radio" label="monthly" 
				value="monthly"
				checked='<%= "monthly".equals( every ) %>' 
				disabled='<%= !enable %>' 
			/>
			
		</aui:column>
		
		<aui:column columnWidth="60">
			
			<aui:select name="monthlyDayOfMonth" id="monthlyDayOfMonth" label="day-of-month" inlineLabel="true" inlineField="true" 
				disabled='<%= !( "monthly".equals( every ) ) %>' style="width: 56px;"
			>
				<%for( int i=1; i<=31; i++ ) { %>
					<aui:option value="<%= i %>" label='<%= (i<=9 ? "0"+i : i ) %>' selected='<%= (i==monthlyDayOfMonth ? true : false ) %>' />
				<%}%>
			</aui:select>			
			
		</aui:column>
		
		<aui:column columnWidth="20">&nbsp;</aui:column>
		
	</aui:row>
	
</aui:layout>







<aui:script use="aui-base, aui-node">

A.one( '#<portlet:namespace />enableCheckbox' ).on( 'click', function() {
	disableSchedulerPanel( !this.attr('checked') );
} );


A.one( '#<portlet:namespace />day' ).on( 'click', function() {

	disableDailyPanel( this.attr('checked') );
	resetDailyPanel();
	
	disableWeeklyPanel( this.attr('checked') );
	resetDailyPanel();
	
	disableMonthlyPanel( this.attr('checked') );
	resetMonthlyPanel();
	
	disableDayPanel( !this.attr('checked') );
} );

A.one( '#<portlet:namespace />everyDaily' ).on( 'click', function() {

	disableDayPanel( this.attr('checked') );
	resetDayPanel();
	
	disableWeeklyPanel( this.attr('checked') );
	resetWeeklyPanel();
	
	disableMonthlyPanel( this.attr('checked') );
	resetMonthlyPanel();
	
	disableDailyPanel( !this.attr('checked') );
} );



A.one( '#<portlet:namespace />everyWeekly' ).on( 'click', function() {

	disableDayPanel( this.attr('checked') );
	resetDayPanel();
	
	disableDailyPanel( this.attr('checked') );
	resetDailyPanel();
	
	disableMonthlyPanel( this.attr('checked') );
	resetMonthlyPanel();
	
	disableWeeklyPanel( !this.attr('checked') );
} );

A.one( '#<portlet:namespace />everyMonthly' ).on( 'click', function() {
	
	disableDayPanel( this.attr('checked') );
	resetDayPanel();
	
	disableDailyPanel( this.attr('checked') );
	resetDailyPanel();
	
	disableWeeklyPanel( this.attr('checked') );
	resetWeeklyPanel();
	
	disableMonthlyPanel( !this.attr('checked') );
} );



function disableSchedulerPanel( value ) {
	A.one( '#<portlet:namespace />startTimeHourPart' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />startTimeMinutesPart' ).attr( 'disabled', value );


	A.one( '#<portlet:namespace />day' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />everyDaily' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />everyWeekly' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />everyMonthly' ).attr( 'disabled', value );
	
	if( value ) {

		disableDailyPanel( value );
		disableWeeklyPanel( value );
		disableMonthlyPanel( value );
	}
	disableDayPanel( value );
	
	A.one( '#<portlet:namespace />startTimeHourPart' ).val( '12' );
	A.one( '#<portlet:namespace />startTimeMinutesPart' ).val( '00' );
	A.one( '#<portlet:namespace />day' ).attr( 'checked', true );
}


function disableDayPanel( value ) {
	A.one( '#<portlet:namespace />startTimeHourPart' ).attr( 'disabled', false );
	A.one( '#<portlet:namespace />startTimeMinutesPart' ).attr( 'disabled', false );
	
	A.one( '#<portlet:namespace />dayDate' ).attr( 'disabled', value );
}
function resetDayPanel( value ) {
	A.one( '#<portlet:namespace />dayDate' ).attr( 'value', '' );
}



function disableDailyPanel( value ) {
	A.one( '#<portlet:namespace />startTimeHourPart' ).attr( 'disabled', false );
	A.one( '#<portlet:namespace />startTimeMinutesPart' ).attr( 'disabled', false );
	
	A.one( '#<portlet:namespace />dailyChoiseFirst' ).attr( 'disabled', value );		
	A.one( '#<portlet:namespace />dailyChoiseFirstDays' ).attr( 'disabled', value );		
	A.one( '#<portlet:namespace />dailyChoiseSecond' ).attr( 'disabled', value );		
}
function resetDailyPanel( value ) {
	A.one( '#<portlet:namespace />dailyChoiseFirst' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />dailyChoiseFirstDays' ).attr( 'value', '1' );	
	A.one( '#<portlet:namespace />dailyChoiseSecond' ).attr( 'checked', false );
}



function disableWeeklyPanel( value ) {
	A.one( '#<portlet:namespace />startTimeHourPart' ).attr( 'disabled', false );
	A.one( '#<portlet:namespace />startTimeMinutesPart' ).attr( 'disabled', false );

	A.one( '#<portlet:namespace />weeklyMondayCheckbox' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />weeklyTuesdayCheckbox' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />weeklyWednesdayCheckbox' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />weeklyThursdayCheckbox' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />weeklyFridayCheckbox' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />weeklySaturdayCheckbox' ).attr( 'disabled', value );
	A.one( '#<portlet:namespace />weeklySundayCheckbox' ).attr( 'disabled', value );
}
function resetWeeklyPanel( value ) {
	A.one( '#<portlet:namespace />weeklyMondayCheckbox' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />weeklyTuesdayCheckbox' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />weeklyWednesdayCheckbox' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />weeklyThursdayCheckbox' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />weeklyFridayCheckbox' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />weeklySaturdayCheckbox' ).attr( 'checked', true );
	A.one( '#<portlet:namespace />weeklySundayCheckbox' ).attr( 'checked', true );
}



function disableMonthlyPanel( value ) {
	A.one( '#<portlet:namespace />startTimeHourPart' ).attr( 'disabled', false );
	A.one( '#<portlet:namespace />startTimeMinutesPart' ).attr( 'disabled', false );
	
	A.one( '#<portlet:namespace />monthlyDayOfMonth' ).attr( 'disabled', value );
}
function resetMonthlyPanel( value ) {
	A.one( '#<portlet:namespace />monthlyDayOfMonth' ).attr( 'value', '1' );
}
</aui:script>
