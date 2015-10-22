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

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Entry;


public class Comment {

    private String content;
    private Author author;
    private String title;

    @GET
    @Produces("application/atom+xml")
    public Entry getComment() {
        return toAtomEntry();
    }

    @PUT
    @Produces("application/atom+xml")
    public Entry updateComment(Entry comment) {
        Author author = getAuthor();
        author.setName(comment.getAuthors().get(0).getName());
        author.setEmail(comment.getAuthors().get(0).getEmail());
        setTitle(comment.getTitle());
        setContent(comment.getContent());
        return toAtomEntry();
    }

    public Entry toAtomEntry() {
    	Abdera abdera = new Abdera();
    	Factory factory = abdera.getFactory();
        Entry entry = factory.newEntry();
        Content content = factory.newContent();
        content.setValue(this.content);
        content.setMimeType("text");
        entry.setContent(content);
        entry.getAuthors().add(this.author.toAtomPerson(entry));
        entry.setTitle(this.title);
        return entry;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
