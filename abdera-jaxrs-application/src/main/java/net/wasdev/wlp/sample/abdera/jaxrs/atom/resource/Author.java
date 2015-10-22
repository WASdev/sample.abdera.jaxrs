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

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Person;


public class Author {

    public String name;
    public String email;

    public Person toAtomPerson(Element element) {
    	Abdera abdera = new Abdera();
    	Factory factory = abdera.getFactory();
    	//QName qname = new QName("tag:example.org,2006:/namespaces", "foo", "f");
        Person person = factory.newAuthor(element);
        person.setName(name);
        person.setEmail(email);
        return person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
