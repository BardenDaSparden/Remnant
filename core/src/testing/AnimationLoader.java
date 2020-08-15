package testing;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AnimationLoader {
	
	SAXParserFactory factory;
	SAXParser parser;
	AnimationHandler handler;
	
	public AnimationLoader(){
		factory = SAXParserFactory.newInstance();
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
		handler = new AnimationHandler();
	}
	
	public void load(String path){
		InputStream stream = ClassLoader.getSystemResourceAsStream(path);
		try {
			parser.parse(stream, handler);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
}

class AnimationHandler extends DefaultHandler {
	
	boolean bAnimation = false;
	boolean bState = false;
	boolean bFrame = false;
	
	//Receives notification at the beginning of each element in the XML document
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes){
		System.out.println("Begin: " + qName);
	}
	
	//Receives notification at the end of each element in the XML document
	@Override
	public void endElement(String uri, String localName, String qName){
		System.out.println("End: " + qName);
	}
	
	//Receives notification for each text-value within an element
	@Override
	public void characters(char[] ch, int start, int length){
		System.out.println(new String(ch, start, length));
	}
	
}
