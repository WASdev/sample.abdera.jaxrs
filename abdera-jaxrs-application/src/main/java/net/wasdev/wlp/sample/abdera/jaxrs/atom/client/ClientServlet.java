/*******************************************************************************
 * Copyright (c) 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package net.wasdev.wlp.sample.abdera.jaxrs.atom.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import net.wasdev.wlp.sample.abdera.jaxrs.atom.AtomEntryProvider;
import net.wasdev.wlp.sample.abdera.jaxrs.atom.AtomFeedProvider;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;




@WebServlet(urlPatterns="/abdera")
public class ClientServlet extends HttpServlet{

	
	private static final long serialVersionUID = 1818527173176741042L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Client client = null;
		PrintWriter writer = null;
		
		try {
			client = ClientBuilder.newBuilder().build();
			client.register(AtomFeedProvider.class);
			client.register(AtomEntryProvider.class);
			Feed feed = client.target("http://localhost:9081/abdera-jaxrs-application/"  + req.getParameter("path")).request().accept(MediaType.APPLICATION_ATOM_XML_TYPE).get(Feed.class);
			
			
			System.out.println(feed.toString());
			writer = resp.getWriter();
			writer.print("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>JAX-RS 2.0 Examples</title></head><body>"
					+ "-------------------------------------------------------</br>");
			writer.print("Feed id: " + feed.getId() +"</br>");
			writer.print("Feed title: " + feed.getTitle() +"</br>");
			
			writer.print("-------------------------------------------------------</br>");
			List<Link> links = feed.getLinks();
			for(int i=0;i<links.size();i++){				
				writer.print("Feed Link(" +i+ ") Title: " + links.get(i).getTitle()+"</br>");
				writer.print("Feed Link(" +i+ ") Href: " + links.get(i).getHref() +"</br>");
				
				if("ibm-blog-service".equalsIgnoreCase(feed.getTitle())){
					Feed feedi = client.target(links.get(i).getHref().toString()).request().accept(MediaType.APPLICATION_ATOM_XML_TYPE).get(Feed.class);
					writer.print("Feed id: " + feedi.getId() +"</br>");
					writer.print("Feed title: " + feedi.getTitle() +"</br>");
					
				}else{
					Entry entry = client.target(links.get(i).getHref().toString()).request().accept(MediaType.APPLICATION_ATOM_XML_TYPE).get(Entry.class);
					System.out.println("entry: "+entry+"</br>");
					writer.print("Entry(" +i+ ") Author Name: " + entry.getAuthor().getName()+"</br>");
					writer.print("Entry(" +i+ ") Author Email: " + entry.getAuthor().getEmail()+"</br>");
				}
				
				writer.print("-------------------------------------------------------</br>");
			}			
			writer.print("</body></html>");
			
			writer.flush();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			if(client!=null)
				client.close();
			if(writer!=null)
			writer.close();
		}
		
	}

	
}
