/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.community.core.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.fd.pdfutility.services.PDFUtilityService;


import java.util.*;
import com.adobe.livecycle.pdfutility.client.*;
import java.io.*;
import com.adobe.idp.Document;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactory;
import com.adobe.idp.dsc.clientsdk.ServiceClientFactoryProperties;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Solr Index Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/ConvertPDFToXDPJavaAPI" })
public class ConvertPDFToXDPServletJavaAPI extends SlingSafeMethodsServlet {

	@Reference
	private PDFUtilityService cpdfService;

	private static final long serialVersionUid = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");

		try
        {
            //Set connection properties required to invoke AEM Forms
            Properties connectionProps = new Properties();
            connectionProps.setProperty(ServiceClientFactoryProperties.DSC_DEFAULT_SOAP_ENDPOINT, "https://'[server]:[port]'");
            connectionProps.setProperty(ServiceClientFactoryProperties.DSC_TRANSPORT_PROTOCOL,ServiceClientFactoryProperties.DSC_SOAP_PROTOCOL);
            connectionProps.setProperty(ServiceClientFactoryProperties.DSC_SERVER_TYPE, "JBoss");
            connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_USERNAME, "administrator");
            connectionProps.setProperty(ServiceClientFactoryProperties.DSC_CREDENTIAL_PASSWORD, "password");

            // Create a ServiceClientFactory object
            ServiceClientFactory myFactory = ServiceClientFactory.createInstance(connectionProps);

            // Create a PDF Utility client
            PDFUtilityServiceClient pdfUt = new PDFUtilityServiceClient(myFactory);

            // Specify a PDF document to convert to an XDP file
            FileInputStream fileInputStream = new FileInputStream("C:/temp/sample.pdf");
            Document inDoc = new Document(fileInputStream);

            // Convert the PDF document to an XDP file
            Document myXDP = pdfUt.convertPDFtoXDP(inDoc);

            //Save the returned Document object as an XDP file
            File xdpFile = new File("C:/temp/sample.xdp");
            myXDP.copyToFile(xdpFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

		resp.getWriter().write("File generated successfully!");

	}

}
