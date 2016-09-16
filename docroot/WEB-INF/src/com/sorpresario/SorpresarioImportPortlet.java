package com.sorpresario;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.io.FileFilter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class SorpresarioImportPortlet extends MVCPortlet {

	private static Log _log = LogFactoryUtil.getLog(SorpresarioImportPortlet.class);

	public void upload(ActionRequest actionRequest, ActionResponse response) throws Exception {

		String msg = "";
		File filePath = null;
//		String folder = getInitParameter("uploadFolder");
		String realPath = getPortletContext().getRealPath("/");
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		_log.debug("RealPath" + realPath);
		if (filePath == null) {
			try {
				_log.debug("Siamo nel try");
				_log.debug("Size: " + uploadRequest.getSize("fileName"));

				if (uploadRequest.getSize("fileName") == 0) {
					SessionErrors.add(actionRequest, "error");
				}

				String sourceFileName = uploadRequest.getFileName("fileName");
				System.out.println(uploadRequest.getFile("fileName").getAbsolutePath());
				File file = uploadRequest.getFile("fileName");

				_log.debug("Nome file:" + uploadRequest.getFileName("fileName"));
				File newFile = null;
				newFile = new File(sourceFileName);
				_log.debug("New file name: " + newFile.getName());
				_log.debug("New file path: " + newFile.getPath());
				filePath = newFile;
				InputStream in = new BufferedInputStream(uploadRequest.getFileAsStream("fileName"));
				FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(newFile);

				byte[] bytes_ = FileUtil.getBytes(in);
				int i = fis.read(bytes_);

				while (i != -1) {
					fos.write(bytes_, 0, i);
					i = fis.read(bytes_);
				}
				fis.close();
				fos.close();
				Float size = (float) newFile.length();
				System.out.println("file size bytes:" + size);
				System.out.println("file size Mb:" + size / 1048576);

				System.out.println("File created: " + newFile.getName());
				SessionMessages.add(actionRequest, "success");
			} catch (FileNotFoundException e) {
				filePath = null;
				_log.debug("File Not Found.");
				e.printStackTrace();
				SessionMessages.add(actionRequest, "error");
			} catch (NullPointerException e) {
				filePath = null;
				_log.debug("File Not Found");
				e.printStackTrace();
				SessionMessages.add(actionRequest, "error");
			}

			catch (IOException e1) {
				filePath = null;
				_log.debug("Error Reading The File.");
				SessionMessages.add(actionRequest, "error");
				e1.printStackTrace();
			}
		}

		if (ParamUtil.getString(uploadRequest, "analyze") != null && ParamUtil.getString(uploadRequest, "analyze").equalsIgnoreCase("analyze")) {
			//leggo file da cartella
			String pathLocale = "C:\\Users\\c.pagliaro\\Documents\\CarmineReply\\PLM Integration\\TracciatiTest";		
			SorpresarioStoreData.getInstance().resetStatistiche();
			File fileDir = new File(pathLocale);
			FileFilter filter = new FileFilter(){
				private final String[] okFileExtensions = new String[] {"csv"};
				  public boolean accept(File file)
				  {
				    for (String extension : okFileExtensions)
				    {
				      if (file.getName().toLowerCase().endsWith(extension))
				      {
				        return true;
				      }
				    }
				    return false;
				  }
			};
			if(fileDir.isDirectory()){
				File[] listaFile = fileDir.listFiles(filter);
				_log.debug("File trovati: "+listaFile.length);				
				for(File f : listaFile){
					_log.debug("Elaborazione del file ==> "+f.getName());
					if (f != null && f.canRead()){
						SorpresarioStoreCSV.getInstance().reset();
						if (SorpresarioAnalyzeCSV.CSVRead(f.getAbsolutePath(),actionRequest, response) == "success") {

								if (SorpresarioImportCSV.CSVRead(f.getAbsolutePath(),actionRequest, response) == "success") {
								} else {
									filePath = null;
									msg = "Import fallito per il file: "+f.getName();
									System.out.println(msg);
									_log.error(msg);
								}			
						}
					}else{
						msg += "File "+f.getName()+" non valido <br/>";
						System.out.println(msg);
						_log.error(msg);
					}
				}
				actionRequest.setAttribute("statistiche", SorpresarioStoreData.getInstance().getStatistiche());
			}

		}
	}

	@Override
	public void render(RenderRequest renderRequest,	RenderResponse renderResponse) throws PortletException, IOException {
		include("/view.jsp", renderRequest, renderResponse);
	}
	
}
