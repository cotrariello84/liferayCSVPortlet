package com.sorpresario;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

public class SorpresarioImportCSV {

	public static String CSVRead(String path, ActionRequest actionRequest,	ActionResponse response) {

		Set<Entry<String, SorpresarioCSVModel>> set = SorpresarioStoreCSV.getInstance().getSurprises().entrySet();
		Iterator<Entry<String, SorpresarioCSVModel>> i = set.iterator();
		while (i.hasNext()) {
			Entry<String, SorpresarioCSVModel> e = i.next();
			SorpresarioSurpriseImportUtils.ImportJournalArticle(e.getValue(), actionRequest, response);
		}
		return "success";
	}
}
