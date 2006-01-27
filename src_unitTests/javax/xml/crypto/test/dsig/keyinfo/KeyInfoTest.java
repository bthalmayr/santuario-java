/*
 * Copyright 2006 The Apache Software Foundation.
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
 *
 */
package javax.xml.crypto.test.dsig.keyinfo;

import java.util.*;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dom.*;
import javax.xml.crypto.dsig.keyinfo.*;

import junit.framework.*;

/**
 * Unit test for javax.xml.crypto.dsig.keyinfo.KeyInfo
 *
 * @version $Id$
 * @author Valerie Peng
 */
public class KeyInfoTest extends TestCase {

    private KeyInfoFactory fac;

    public KeyInfoTest() {
	super("KeyInfoTest");
    }

    public KeyInfoTest(String name) {
	super(name);
    }

    public void setUp() throws Exception { 
	fac = KeyInfoFactory.getInstance
	    ("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
    }

    public void tearDown() { }

    public void testgetId() {
	KeyInfo ki = fac.newKeyInfo
	    (Collections.singletonList(fac.newKeyName("foo")), "skeleton");
	assertNotNull(ki.getId());
    }

    public void testgetContent() {
	KeyInfo[] infos = new KeyInfo[2];
	infos[0] = fac.newKeyInfo
	    (Collections.singletonList(fac.newKeyName("foo")), "skeleton");
	infos[1] = fac.newKeyInfo
	    (Collections.singletonList(fac.newKeyName("foo")));
	for (int j=0; j < infos.length; j++) {
	    KeyInfo ki = infos[j];
	    List li = ki.getContent();
	    assertNotNull(ki.getContent());
	    if (!li.isEmpty()) {
		Object[] content = li.toArray();
		for (int i=0; i<content.length; i++) {
		    if (!(content[i] instanceof XMLStructure)) {
			fail("KeyInfo element has the wrong type");
		    };
		}
	    } else {
		try {
		    li.add(new Object());
		    fail("Added KeyInfo element of wrong type");
		} catch (ClassCastException ex) {
		    // expected
		}
	    }
	}
    }

    public void testConstructor() {
	final String id = "keyId";
	// test newKeyInfo(List, String id)
	KeyInfo ki = fac.newKeyInfo
	    (Collections.singletonList(fac.newKeyName("foo")), id);
	assertEquals(id, ki.getId());
	try {
	    ki = fac.newKeyInfo(null, id); 
	    fail("Should raise a NullPointerException"); 
	} catch (NullPointerException npe) {}
	// test newKeyInfo(List)
	ki = fac.newKeyInfo(Collections.singletonList(fac.newKeyName("foo")));
    }

    public void testisFeatureSupported() {
	KeyInfo ki = fac.newKeyInfo
	    (Collections.singletonList(fac.newKeyName("foo")), "keyid");
	try {
	    ki.isFeatureSupported(null); 
	    fail("Should raise a NPE for null feature"); 
	} catch (NullPointerException npe) {}

	assertTrue(!ki.isFeatureSupported("not supported"));
    }
}
