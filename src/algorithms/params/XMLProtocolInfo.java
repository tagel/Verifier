package algorithms.params;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This class implements a Protocol Info data structure from a given XML file.
 * It extract the protocol info data from the XML file.
 * 
 * @author Daniel 
 */
public class XMLProtocolInfo implements ProtocolInfo {

	private String version;
	private String sessionId;
	private Integer numOfParties;
	private Integer threshold;
	private Integer ne;
	private Integer nr;
	private Integer nv;
	private String hashFunction;
	private String prg;
	private String gq;
	private Integer width;

	/**
	 * @param xmlPath
	 *            the path for the XML file to parser.
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * @throws IllegalXmlFormatException 
	 */
	public XMLProtocolInfo(String xmlPath) throws FileNotFoundException,
			XMLStreamException, FactoryConfigurationError, IllegalXmlFormatException {
		XMLStreamReader xmlReader = XMLInputFactory.newInstance()
				.createXMLStreamReader(new FileInputStream(xmlPath));
		getValues(xmlReader);
		validate();
		xmlReader.close();
	}

	private void validate() throws IllegalXmlFormatException {
		if (isNull(version) || isNull(sessionId) || isNull(numOfParties)
				|| isNull(threshold) || isNull(ne) || isNull(nv) || isNull(nr)
				|| isNull(hashFunction) || isNull(prg) || isNull(gq)
				|| isNull(width)) {
			throw new IllegalXmlFormatException();
		}
	}

	private boolean isNull(Object o) {
		return (o == null);
	}

	private void getValues(XMLStreamReader xmlReader) throws XMLStreamException {
		String localName;
		
		while (xmlReader.hasNext()) {
			if (xmlReader.next() == XMLStreamConstants.START_ELEMENT) {

				localName = xmlReader.getLocalName();

				if (localName == FieldType.VERSION.getValueType()) {
					this.version = xmlReader.getElementText();
				} else if (localName == FieldType.SESSION_ID.getValueType()) {
					this.sessionId = xmlReader.getElementText();
				} else if (localName == FieldType.NUM_OF_PARTIES.getValueType()) {
					this.numOfParties = Integer.parseInt(xmlReader
							.getElementText());
				} else if (localName == FieldType.THRESHOLD.getValueType()) {
					this.threshold = Integer.parseInt(xmlReader
							.getElementText());
				} else if (localName == FieldType.NE.getValueType()) {
					this.ne = Integer.parseInt(xmlReader.getElementText());
				} else if (localName == FieldType.NR.getValueType()) {
					this.nr = Integer.parseInt(xmlReader.getElementText());
				} else if (localName == FieldType.NV.getValueType()) {
					this.nv = Integer.parseInt(xmlReader.getElementText());
				} else if (localName == FieldType.HASH_FUNCTION.getValueType()) {
					this.hashFunction = xmlReader.getElementText();
				} else if (localName == FieldType.PRG.getValueType()) {
					this.prg = xmlReader.getElementText();
				} else if (localName == FieldType.GQ.getValueType()) {
					this.gq = xmlReader.getElementText();
				} else if (localName == FieldType.WIDTH.getValueType()) {
					this.width = Integer.parseInt(xmlReader.getElementText());
				}
			}
		}
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public int getNumOfParties() {
		return numOfParties;
	}

	@Override
	public int getThreshold() {
		return threshold;
	}

	@Override
	public int getNe() {
		return ne;
	}

	@Override
	public int getNr() {
		return nr;
	}

	@Override
	public int getNv() {
		return nv;
	}

	@Override
	public String getHashFunction() {
		return hashFunction;
	}

	@Override
	public String getPrg() {
		return prg;
	}

	@Override
	public String getGq() {
		return gq;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public enum FieldType {
		VERSION("version"), SESSION_ID("sid"), NUM_OF_PARTIES("nopart"), THRESHOLD(
				"thres"), NE("vbitlenro"), NR("statdist"), NV("cbitlenro"), HASH_FUNCTION(
				"rohash"), PRG("prg"), GQ("pgroup"), WIDTH("width");

		FieldType(String str) {
			this.value = str;
		}

		private String value;

		public String getValueType() {
			return value;
		}
	}
}
