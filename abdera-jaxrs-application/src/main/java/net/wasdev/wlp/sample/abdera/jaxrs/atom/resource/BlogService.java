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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

@Path("blogservice")
public class BlogService {
    public static final String ID = "ibm-blog-service";

    private static List<Blog> blogs = new ArrayList<Blog>();

    @Context
    private UriInfo baseUri;

    static {
        Date date = new Date();
        Blog devBlog = new Blog();
        devBlog.setId(0);
        devBlog.setTitle("ibm-developer-blog");
        devBlog.setUpdated(date);
        BlogEntry entry1 = new BlogEntry(devBlog);
        Author author = new Author();
        author.setName("Neal Hu");
        author.setEmail("xingjhu@cn.ibm.com");
        entry1.setAuthor(author);
        entry1.setId(BlogEntry.getNextId());
        entry1.setPosting("Welcome to the IBM developer blog!!");
        entry1.setTitle("welcomePosting");
        entry1.setUpdated(date);
        devBlog.addEntry(entry1);
        BlogEntry entry2 = new BlogEntry(devBlog);
        entry2.setAuthor(author);
        entry2.setId(BlogEntry.getNextId());
        entry2.setPosting("IBM developers,\n\nInstructions on how to set up the ibm development have been posted to the IBM wiki. Happy IBM development!\n\nblogadmin");
        entry2.setTitle("IBM Development Env");
        entry2.setUpdated(date);
        devBlog.addEntry(entry2);
        Comment comment = new Comment();
        Author author2 = new Author();
        author2.setName("IBM Coder");
        author2.setEmail("xingjhu@cn.ibm.com");
        comment.setAuthor(author2);
        comment.setContent("Instructions look great! Now I can begin IBM development!");
        comment.setTitle("Great!");
        entry2.addComment(comment);
        BlogService.blogs.add(devBlog);

        Blog userBlog = new Blog();
        userBlog.setId(1);
        userBlog.setTitle("ibm-user-blog");
        userBlog.setUpdated(date);
        BlogEntry entry3 = new BlogEntry(userBlog);
        Author author3 = new Author();
        author3.setName("Eager User");
        author3.setEmail("ibmuser@cn.ibm.com");
        entry3.setAuthor(author3);
        entry3.setId(BlogEntry.getNextId());
        entry3.setPosting("I hear that the 0.1 SNAPSHOT will be available soon! I can't wait!!!");
        entry3.setTitle("0.1 SNAPSHOT");
        entry3.setUpdated(date);
        userBlog.addEntry(entry3);
        Comment comment2 = new Comment();
        Author author4 = new Author();
        author4.setName("Blog Reader");
        author4.setEmail("blogreader@blogreaders.org");
        comment2.setAuthor(author4);
        comment2.setContent("This is good news. I'll be sure to try it out.");
        comment2.setTitle("Good news");
        entry3.addComment(comment2);
        BlogService.blogs.add(userBlog);
    }

    @GET
    @Produces("application/atom+xml")
    public Feed getBlogs() {
    	Abdera abdera = new Abdera();
    	Factory factory = abdera.getFactory();
        Feed ret = factory.newFeed();
        ret.setId(BlogService.ID);
        ret.setTitle(BlogService.ID);
        Link link = null;
        for (int i = 0; i < BlogService.blogs.size(); ++i) {
            link = factory.newLink();
            link.setHref(baseUri.getAbsolutePath() + "/blogs/" + i);
            link.setTitle(BlogService.blogs.get(i).getTitle());
            ret.addLink(link);
        }
        return ret;
    }

    @Path("blogs/{blogid}")
    public Blog getBlog(@PathParam("blogid") int blogId) {
        Blog blog = BlogService.blogs.get(blogId);
        if (blog == null)
            throw new WebApplicationException(404);
        return blog;
    }
}
