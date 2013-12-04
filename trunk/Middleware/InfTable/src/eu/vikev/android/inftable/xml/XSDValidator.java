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
	// private String coursesXSD =
	// "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
	// + "  <xs:element name=\"list\">"
	// + "    <xs:complexType>"
	// + "      <xs:sequence>"
	// +
	// "        <xs:element name=\"course\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "          <xs:complexType>"
	// + "            <xs:sequence>"
	// + "              <xs:element type=\"xs:anyURI\" name=\"url\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"name\"/>"
	// + "              <xs:element type=\"xs:anyURI\" name=\"drps\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"euclid\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"acronym\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"ai\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"cg\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"cs\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"se\"/>"
	// + "              <xs:element type=\"xs:integer\" name=\"level\"/>"
	// + "              <xs:element type=\"xs:integer\" name=\"points\"/>"
	// + "              <xs:element type=\"xs:integer\" name=\"year\"/>"
	// +
	// "              <xs:element type=\"xs:string\" name=\"deliveryperiod\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"lecturer\"/>"
	// + "            </xs:sequence>"
	// + "          </xs:complexType>"
	// + "        </xs:element>"
	// + "      </xs:sequence>"
	// + "    </xs:complexType>" + "  </xs:element>" + "</xs:schema>";
	//
	// private String timetableXSD =
	// "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
	// + "  <xs:element name=\"timetable\">"
	// + "    <xs:complexType>"
	// + "      <xs:sequence>"
	// +
	// "        <xs:element name=\"semester\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "          <xs:complexType>"
	// + "            <xs:sequence>"
	// + "              <xs:element name=\"week\">"
	// + "                <xs:complexType>"
	// + "                  <xs:sequence>"
	// +
	// "                    <xs:element name=\"day\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "                      <xs:complexType>"
	// + "                        <xs:sequence>"
	// +
	// "                          <xs:element name=\"time\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "                            <xs:complexType mixed=\"true\">"
	// + "                              <xs:sequence>"
	// +
	// "                                <xs:element name=\"lecture\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "                                  <xs:complexType>"
	// + "                                    <xs:sequence>"
	// +
	// "                                      <xs:element type=\"xs:string\" name=\"course\"/>"
	// + "                                      <xs:element name=\"years\">"
	// + "                                        <xs:complexType>"
	// + "                                          <xs:sequence>"
	// +
	// "                                            <xs:element type=\"xs:integer\" name=\"year\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>"
	// + "                                          </xs:sequence>"
	// + "                                        </xs:complexType>"
	// + "                                      </xs:element>"
	// + "                                      <xs:element name=\"venue\">"
	// + "                                        <xs:complexType>"
	// + "                                          <xs:sequence>"
	// +
	// "                                            <xs:element type=\"xs:string\" name=\"room\"/>"
	// +
	// "                                            <xs:element type=\"xs:string\" name=\"building\"/>"
	// + "                                          </xs:sequence>"
	// + "                                        </xs:complexType>"
	// + "                                      </xs:element>"
	// +
	// "                                      <xs:element type=\"xs:string\" name=\"comment\"/>"
	// + "                                    </xs:sequence>"
	// + "                                  </xs:complexType>"
	// + "                                </xs:element>"
	// + "                              </xs:sequence>"
	// +
	// "                              <xs:attribute type=\"xs:string\" name=\"start\" use=\"optional\"/>"
	// +
	// "                              <xs:attribute type=\"xs:string\" name=\"finish\" use=\"optional\"/>"
	// + "                            </xs:complexType>"
	// + "                          </xs:element>"
	// + "                        </xs:sequence>"
	// +
	// "                        <xs:attribute type=\"xs:string\" name=\"name\" use=\"optional\"/>"
	// + "                      </xs:complexType>"
	// + "                    </xs:element>"
	// + "                  </xs:sequence>"
	// + "                </xs:complexType>"
	// + "              </xs:element>"
	// + "            </xs:sequence>"
	// +
	// "            <xs:attribute type=\"xs:integer\" name=\"number\" use=\"optional\"/>"
	// + "          </xs:complexType>"
	// + "        </xs:element>"
	// + "      </xs:sequence>"
	// + "    </xs:complexType>"
	// + "  </xs:element>" + "</xs:schema>";
	//
	// private String venuesXSD =
	// "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
	// + "  <xs:element name=\"venues\">"
	// + "    <xs:complexType>"
	// + "      <xs:sequence>"
	// +
	// "        <xs:element name=\"building\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "          <xs:complexType>"
	// + "            <xs:sequence>"
	// + "              <xs:element type=\"xs:string\" name=\"name\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"description\"/>"
	// + "              <xs:element type=\"xs:anyURI\" name=\"map\"/>"
	// + "            </xs:sequence>"
	// + "          </xs:complexType>"
	// + "        </xs:element>"
	// +
	// "        <xs:element name=\"room\" maxOccurs=\"unbounded\" minOccurs=\"0\">"
	// + "          <xs:complexType>"
	// + "            <xs:sequence>"
	// + "              <xs:element type=\"xs:string\" name=\"name\"/>"
	// + "              <xs:element type=\"xs:string\" name=\"description\"/>"
	// + "            </xs:sequence>"
	// + "          </xs:complexType>"
	// + "        </xs:element>"
	// + "      </xs:sequence>"
	// + "    </xs:complexType>" + "  </xs:element>" + "</xs:schema>";
	//

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
