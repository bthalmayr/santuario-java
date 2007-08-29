/*
 * Copyright 2005 The Apache Software Foundation.
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
package com.r_bg.stax.transforms;

import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.r_bg.stax.StaxProvider;
import com.r_bg.stax.StaxWatcher;
import com.r_bg.stax.StaxWorker;

/**
 * STaX-based abstract implementation of Transform.
 *
 * @author Sean Mullan
 */
public class StaxTransform implements Transform, StaxWorker {

    private TransformService spi;

    public StaxWorker read(XMLStreamReader reader) {
        switch (reader.getEventType()) {
            case XMLStreamReader.START_ELEMENT:
                if (XMLSignature.XMLNS.equals(reader.getNamespaceURI()) &&
		    reader.getLocalName().equals("Transform")) {
                    String alg = reader.getAttributeValue(null,"Algorithm");
		    try {
        	        spi = TransformService.getInstance
			    (alg, "Stax", new StaxProvider());
		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
                    }
                }
                break;
        }
        return null;
    }

    public StaxWatcher remove() {
        return null;
    }

    public final AlgorithmParameterSpec getParameterSpec() {
	return spi.getParameterSpec();
    }

    public final String getAlgorithm() {
	return spi.getAlgorithm();
    }

    /**
     * This method invokes the abstract {@link #marshalParams marshalParams} 
     * method to marshal any algorithm-specific parameters.
     */
    public void marshal(XMLStreamWriter writer, String dsPrefix, 
	XMLCryptoContext context) throws XMLStreamException {

	writer.writeStartElement(dsPrefix, XMLSignature.XMLNS, "Transforms");
	writer.writeAttribute("Algorithm", getAlgorithm());

//        spi.marshalParams
//	    (new javax.xml.crypto.dom.DOMStructure(transformElem), context);

	writer.writeEndElement();
    }

    /**
     * Transforms the specified data using the underlying transform algorithm.
     *
     * @param data the data to be transformed
     * @param sc the <code>XMLCryptoContext</code> containing
     *    additional context (may be <code>null</code> if not applicable)
     * @return the transformed data
     * @throws NullPointerException if <code>data</code> is <code>null</code>
     * @throws XMLSignatureException if an unexpected error occurs while
     *    executing the transform
     */
    public Data transform(Data data, XMLCryptoContext xc) 
	throws TransformException {
	return spi.transform(data, xc);
    }

    /**
     * Transforms the specified data using the underlying transform algorithm.
     *
     * @param data the data to be transformed
     * @param sc the <code>XMLCryptoContext</code> containing
     *    additional context (may be <code>null</code> if not applicable)
     * @param os the <code>OutputStream</code> that should be used to write
     *    the transformed data to
     * @return the transformed data
     * @throws NullPointerException if <code>data</code> is <code>null</code>
     * @throws XMLSignatureException if an unexpected error occurs while
     *    executing the transform
     */
    public Data transform(Data data, XMLCryptoContext xc, OutputStream os) 
	throws TransformException {
	return spi.transform(data, xc, os);
    }

    public boolean equals(Object o) {
	if (this == o) {
            return true;
	}

        if (!(o instanceof Transform)) {
            return false;
	}
        Transform otransform = (Transform) o;

	return (getAlgorithm().equals(otransform.getAlgorithm()));
/*
	 && 
	    DOMUtils.paramsEqual
		(getParameterSpec(), otransform.getParameterSpec()));
*/
    }

    public int hashCode() {
	// uncomment when JDK 1.4 is required
	// assert false : "hashCode not designed";
	return 58;
    }

    public boolean isFeatureSupported(String feature) {
	return false;
    }
}
