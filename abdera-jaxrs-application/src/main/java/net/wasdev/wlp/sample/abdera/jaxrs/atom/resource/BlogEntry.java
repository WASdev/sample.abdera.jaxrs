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
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Link;


public class BlogEntry {

    private static int nextId = 0;

    private final Blog parent;
    private String posting;
    private int id;
    private String title;
    private Date updated;
    private Author author;
    private final List<Comment> comments = new ArrayList<Comment>();

    public BlogEntry(Blog parent) {
        this.parent = parent;
    }

    @GET
    @Produces("application/atom+xml")
    public Entry getEntry(@Context UriInfo uriInfo) {
        return toAtomEntry(uriInfo);
    }

    @PUT
    @Produces("application/atom+xml")
    public Entry updateBlogEntry(@Context UriInfo uriInfo, Entry updatedEntry) {
        Author author = getAuthor();
        author.setName(updatedEntry.getAuthors().get(0).getName());
        author.setEmail(updatedEntry.getAuthors().get(0).getEmail());
        setPosting(updatedEntry.getContent());
        setTitle(updatedEntry.getTitle());
        setUpdated(new Date());
        return toAtomEntry(uriInfo);
    }

    @Path("comments/{commentid}")
    public Comment getComments(@PathParam("commentid") Integer commentId) {
        Comment comment = this.comments.get(commentId);
        if (comment == null)
            throw new WebApplicationException(404);
        return comment;
    }

    @POST
    @Path("comments")
    public Response postComment(@Context UriInfo uriInfo, Entry comment) {
        Comment newComment = new Comment();
        Author author = new Author();
        author.setName(comment.getAuthors().get(0).getName());
        author.setEmail(comment.getAuthors().get(0).getEmail());
        newComment.setAuthor(author);
        newComment.setTitle(comment.getTitle());
        newComment.setContent(comment.getContent());
        comments.add(newComment);
        try {
            URI uri = new URI(uriInfo.getBaseUri() + "blogservice/blogs/" + this.parent.getId() + "/entries/" + this.id + "/comments/" + (comments.size() - 1));
            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            throw new WebApplicationException(e);
        }
    }

    public Entry toAtomEntry(UriInfo uriInfo) {
        Abdera abdera = new Abdera();
        Factory factory = abdera.getFactory();
    	Entry entry = factory.newEntry();
        entry.setId("" + this.id);
        entry.setTitle(this.title);
        entry.setUpdated(this.updated);
        entry.addAuthor(author.toAtomPerson(entry));        
        entry.setContent(this.posting, Content.Type.TEXT);
        Link link = null;
        int i = 0;
        for (Comment comment : comments) {
            link = factory.newLink();
            link.setHref(uriInfo.getBaseUri() + "blogservice/blogs/"
                         + parent.getId()
                         + "/entries/"
                         + this.id
                         + "/comments/"
                         + i);
            entry.addLink(link);
            ++i;
        }
        return entry;
    }

    public static int getNextId() {
        return BlogEntry.nextId++;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
