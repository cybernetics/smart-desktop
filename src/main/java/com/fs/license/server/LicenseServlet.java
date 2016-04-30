package com.fs.license.server;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LicenseServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String licenseInfo=req.getHeader("license-info");
		if(licenseInfo==null || licenseInfo.trim().equals("")){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid License Request, License info unavailable");
		}else{
			String reponse=LicenseProtocolHandler.getInstance().validateLicense(licenseInfo);
		    Writer out= resp.getWriter();
		    out.write(reponse);
		    out.flush();
		}
	}
}
