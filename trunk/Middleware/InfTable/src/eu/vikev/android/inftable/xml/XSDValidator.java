package eu.vikev.android.inftable.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.webpki.android.org.apache.xerces.jaxp.validation.XMLSchemaFactory;

import android.util.Log;

public class XSDValidator {
	public static boolean isXmlValid(Document doc, InputStream xsd) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Source xmlSource = new DOMSource(doc);
			Result outputTarget = new StreamResult(outputStream);
			TransformerFactory.newInstance().newTransformer()
					.transform(xmlSource, outputTarget);
			InputStream is = new ByteArrayInputStream(
					outputStream.toByteArray());

			Source schemaFile = new StreamSource(xsd);
			Source xmlFile = new StreamSource(is);

			SchemaFactory factory = new XMLSchemaFactory();
			Schema schema = factory.newSchema(schemaFile);

			Validator validator = schema.newValidator();
			validator.validate(xmlFile);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(XSDValidator.class.getName(), "Error while validating xml: "
					+ e.getMessage());
		}

		return false;
	}
}
