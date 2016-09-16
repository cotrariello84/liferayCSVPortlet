package com.sorpresario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.ImageModel;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.webserver.WebServerServletTokenUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;
import com.liferay.portlet.journal.model.JournalArticle;

public class SorpresarioSurpriseContentEdit {
	private static Log _log = LogFactoryUtil.getLog(SorpresarioSurpriseContentEdit.class);

	public static JournalArticle contentEdit(JournalArticle article, SorpresarioCSVModel f, String[] qrcode) {
		Document document = null;
		try {
			document = SAXReaderUtil.read(new StringReader(article.getContent()));
			Element root = document.getRootElement();
			Node ThumbnailNode = document.selectSingleNode("/root/dynamic-element[@name='Thumbnail']/dynamic-content");
			Node HighFotoritoccoNode = document.selectSingleNode("/root/dynamic-element[@name='High_Fotoritocco']/dynamic-content");
			Node LowFotoritoccoNode = document.selectSingleNode("/root/dynamic-element[@name='Low_Fotoritocco_JPG']/dynamic-content");
			Node QrCodeNode = document.selectSingleNode("/root/dynamic-element[@name='QR_Code']/dynamic-content");
			List<Node> QrCodeNodes = document.selectNodes("/root/dynamic-element[@name='QR_Code']/dynamic-content");
			//cerco tutti i nodi qrcode ciclo se esistono non faccio nulla altrimenti inserisco nuovo nodo qrcode
			if (ThumbnailNode != null) {
				String tmpValue = ThumbnailNode.getText();
				if(tmpValue.equals(f.getDoc_thumbnail()) || f.getDoc_thumbnail().isEmpty()){
					//La foto è rimasta uguale non faccio nulla					
				}else{
					String path = "C:\\Users\\c.pagliaro\\Downloads\\article.png";		
					File pathToFile = new File(path);
					long imageID = CounterLocalServiceUtil.increment();
					ImageModel image = ImageLocalServiceUtil.createImage(imageID);
					ImageLocalServiceUtil.updateImage(imageID, pathToFile);
					String token = WebServerServletTokenUtil.getToken(image.getImageId());
					String  identifier = "/image/journal/article?img_id=" + image.getImageId() + "&t=" + token;
					ThumbnailNode.setText(identifier);
				}
			}
			if (HighFotoritoccoNode != null) {
				//questa immagine viene prima ricercata nella documentlibrary nel caso non fosse presente deve essere creata
				String path = "1";
				long groupId = article.getGroupId();
				long folderId = 0;
				DLFileEntry dlfile = null;
				try{
					//cerco l'immagine nella documentlibrary
					dlfile = DLFileEntryLocalServiceUtil.getFileEntryByName(groupId, folderId, path);
				}
				catch(Exception e){
					long userId = article.getUserId(); 
					try {
						File file = new File(f.getDoc_thumbnail());
						long repositoryId = article.getGroupId();
						String sourceFileName = file.getName();
						String mimeType;					
						mimeType = Files.probeContentType(file.toPath());
						String title = file.getName();
						String description = file.getName();
						String changeLog = null;
						long fileEntryTypeId = 0;
						Map<String, Fields> fieldsMap = null;
						InputStream is = new FileInputStream(file);
						long size = file.length();
						ServiceContext serviceContext = null;
						// immagine non esistente nella documentlibrary la creo
						dlfile = DLFileEntryLocalServiceUtil.addFileEntry(userId, groupId, repositoryId, folderId, sourceFileName, mimeType, 
								title, description, changeLog, fileEntryTypeId, fieldsMap, file, is, size, serviceContext);						
					} catch (IOException e1) {
						_log.error(e1);
					}
				}
				if(dlfile != null){
					String token = WebServerServletTokenUtil.getToken(dlfile.getFileEntryId());
					String identifier = "/documents/"+dlfile.getGroupId()+"/"+dlfile.getFolderId()+"/"+dlfile.getName()+"/"+dlfile.getUuid()+"?t="+token+"";
					HighFotoritoccoNode.setText(identifier);
				}
			}
			if (LowFotoritoccoNode != null) {
				String tmpValue = LowFotoritoccoNode.getText();
				if(tmpValue.equals(f.getDoc_thumbnail()) || f.getDoc_thumbnail().isEmpty()){
					//La foto è rimasta uguale non faccio nulla							
				}else{
					String path = "C:\\Users\\c.pagliaro\\Downloads\\article.png";		
					File pathToFile = new File(path);
					long imageID = CounterLocalServiceUtil.increment();
					ImageModel image = ImageLocalServiceUtil.createImage(imageID);
					ImageLocalServiceUtil.updateImage(imageID, pathToFile);
					String token = WebServerServletTokenUtil.getToken(image.getImageId());
					String  identifier = "/image/journal/article?img_id=" + image.getImageId() + "&t=" + token;
					LowFotoritoccoNode.setText(identifier);}
			}
			Node lastNode = null;
			int indexNode = 0;
			//se esiste prendo l'ultimo qrcodenode
			if(QrCodeNodes.size()>0){
				lastNode = QrCodeNodes.get(QrCodeNodes.size()-1);
				indexNode = Integer.parseInt(lastNode.getParent().attributeValue("index"));
			}
				
			//scorro i qrcode del csv
			String[] qrc = SorpresarioUtils.removeDuplicates(qrcode);
			for(String qc : qrc){
				//scorro i qrcode associati al ja già a sistema. se lo trovo non faccio nulla, se non lo trovo nella lista a sistema lo inserisco.
				int c = 0;
				for(Node n : QrCodeNodes){
					if(!qc.equalsIgnoreCase(n.getText())){
						c++;
					}else{
						c = 0;
						break;
					}
				}
				//qrcode non presente e non è il primo qrcode lo aggiungo nuovo
				if(c>0 && indexNode>0){
					indexNode++;
					 Element e = SAXReaderUtil.createElement("dynamic-element");	
					 e.addAttribute("name", "QR_Code");
					 e.addAttribute("index", indexNode+"");
					 e.addAttribute("type", "text");
					 e.addAttribute("index-type", "keyword");
					 Element ee  = SAXReaderUtil.createElement("dynamic-content");
					 ee.addAttribute("language-id", "en_US");
					 ee.setText(qc);
					 e.add(ee);
					 root.add(e);
				}else{
					if (QrCodeNode != null) {
						indexNode++;
						QrCodeNode.setText(f.getQr_code());
					}
				}	
			}

		} catch (DocumentException e) {
			_log.error(e);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		article.setContent(document.asXML());
		return article;
	}
	
}
