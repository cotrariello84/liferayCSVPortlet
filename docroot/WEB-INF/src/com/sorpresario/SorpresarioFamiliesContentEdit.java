package com.sorpresario;

import java.io.StringReader;
import java.util.List;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portlet.journal.model.JournalArticle;

public class SorpresarioFamiliesContentEdit {
	private static Log _log = LogFactoryUtil.getLog(SorpresarioFamiliesContentEdit.class);

	public static JournalArticle contentEdit(JournalArticle article, SorpresarioCSVModel f, String[] qrcode) {
		Document document = null;
		try {
			document = SAXReaderUtil.read(new StringReader(article.getContent()));
			Element root = document.getRootElement();
			Node ThumbnailNode = document.selectSingleNode("/root/dynamic-element[@name='Thumbnail']/dynamic-content");
			Node LeafletImageNode = document.selectSingleNode("/root/dynamic-element[@name='Leaflet_Image']/dynamic-content");
			List<Node> LeafletImageNodes = document.selectNodes("/root/dynamic-element[@name='Leaflet_Image']/dynamic-content");
			Node LeafletImageHighNode = document.selectSingleNode("/root/dynamic-element[@name='Leaflet_Image_high']/dynamic-content");
			Node HigResolutionLeafletNode = document.selectSingleNode("/root/dynamic-element[@name='High_Resolution_Leaflet']/dynamic-content");

			if (ThumbnailNode != null) {
				ThumbnailNode.setText("path thumbnail");
			}
			if (LeafletImageNode != null) {
				LeafletImageNode.setText("");
			}
			if (LeafletImageHighNode != null) {
				LeafletImageHighNode.setText("");
			}
			if (HigResolutionLeafletNode != null) {
				HigResolutionLeafletNode.setText("path leaflethighresolution");
			}
			Node lastNode = null;
			int indexNode = 0;
			//se esiste prendo l'ultimo qrcodenode
			if(LeafletImageNodes.size()>0){
				lastNode = LeafletImageNodes.get(LeafletImageNodes.size()-1);
				indexNode = Integer.parseInt(lastNode.getParent().attributeValue("index"));
			}
				
			//scorro i qrcode del csv
			String[] qrc = SorpresarioUtils.removeDuplicates(qrcode);
			for(String qc : qrc){
				//scorro i qrcode associati al ja già a sistema. se lo trovo non faccio nulla, se non lo trovo nella lista a sistema lo inserisco.
				int c = 0;
				for(Node n : LeafletImageNodes){
					if(!qc.equalsIgnoreCase(n.getText())){
						c++;
					}else{
						c = 0;
						break;
					}
				}
				if(c>0 && indexNode>0){
					indexNode++;
					 Element leafletImage = SAXReaderUtil.createElement("dynamic-element");	
					 leafletImage.addAttribute("name", "Leaflet_Image");
					 leafletImage.addAttribute("index", indexNode+"");
					 leafletImage.addAttribute("type", "image");
					 leafletImage.addAttribute("index-type", "");
					 leafletImage.setText("path immagine");
					 Element leafletImageChild = SAXReaderUtil.createElement("dynamic-content");
					 leafletImageChild.addAttribute("language-id", "en_US");
					 leafletImageChild.setText("path immagine");
					 leafletImage.add(leafletImageChild);
					 Element leafletImageHigh  = SAXReaderUtil.createElement("dynamic-element");
					 leafletImageHigh.addAttribute("name", "Leaflet_Image_high");
					 leafletImage.addAttribute("index", indexNode+"");
					 leafletImage.addAttribute("type", "image");
					 leafletImage.addAttribute("index-type", "");
					 Element leafletImageHighChild = SAXReaderUtil.createElement("dynamic-content");
					 leafletImageHighChild.addAttribute("language-id", "en_US");
					 leafletImageHighChild.setText("path immagine");
					 leafletImageHigh.add(leafletImageHighChild);
					 leafletImage.add(leafletImageHigh);
					 root.add(leafletImage);
				}else{
					if (LeafletImageNode != null) {
						LeafletImageNode.setText(f.getQr_code());
					}
				}	
			}

		} catch (DocumentException e) {
			_log.error(e);
		}
		article.setContent(document.asXML());
		return article;
	}
	
}
