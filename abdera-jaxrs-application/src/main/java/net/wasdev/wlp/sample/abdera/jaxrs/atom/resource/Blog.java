/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corp. 2012
 *
 * The source code for this program is not published or otherwise divested 
 * of its trade secrets, irrespective of what has been deposited with the 
 * U.S. Copyright Office.
 */

package net.wasdev.wlp.sample.abdera.jaxrs.atom.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;


public class Blog {

    private int id;
    private String title;
    private Date updated;
    private final Map<Integer, BlogEntry> entries = new HashMap<Integer, BlogEntry>();

    @GET
    @Produces("application/atom+xml")
    public Feed getBlog(@Context UriInfo uriInfo) {
        return toAtomFeed(uriInfo);
    }

    @Path("entries/{entryid}")
    public BlogEntry getEntry(@PathParam("entryid") Integer entryId) {
        BlogEntry entry = entries.get(entryId);
        if (entry == null)
            throw new WebApplicationException(404);
        return entry;
    }

    @POST
    @Path("entries")
    public Response postBlogEntry(@Context UriInfo uriInfo, Entry blogEntry) {
        BlogEntry newEntry = new BlogEntry(this);
        Author author = new Author();
        author.setName(blogEntry.getAuthors().get(0).getName());
        author.setEmail(blogEntry.getAuthors().get(0).getEmail());
        newEntry.setAuthor(author);
        newEntry.setId(BlogEntry.getNextId());
        newEntry.setPosting(blogEntry.getContent());
        newEntry.setTitle(blogEntry.getTitle());
        newEntry.setUpdated(new Date());
        entries.put(new Integer(newEntry.getId()), newEntry);
        try {
            URI uri = new URI(uriInfo.getBaseUri() + "blogservice/blogs/" + this.id + "/entries/" + newEntry.getId());
            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            throw new WebApplicationException(e);
        }
    }

    public Feed toAtomFeed(UriInfo uriInfo) {
    	Abdera abdera = new Abdera();
    	Factory factory = abdera.getFactory();
    	Feed feed = factory.newFeed();
        feed.setId(id + "");
        feed.setTitle(title);
        feed.setUpdated(updated);
        Set<Integer> ids = entries.keySet();
        List<Integer> idList = new ArrayList<Integer>(ids);
        Collections.sort(idList);
        Link link = null;
        for (Integer entryId : idList) {
            link = factory.newLink();
            link.setHref(uriInfo.getBaseUri() + "blogservice/blogs/"
                         + this.id
                         + "/entries/"
                         + entries.get(entryId).getId());
            link.setTitle(entries.get(entryId).getTitle());
            feed.addLink(link);
        }
        return feed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Map<Integer, BlogEntry> getEntries() {
        return entries;
    }

    public void addEntry(BlogEntry newEntry) {
        this.entries.put(newEntry.getId(), newEntry);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
