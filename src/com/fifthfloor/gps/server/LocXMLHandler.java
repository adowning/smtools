//package com.fifthfloor.gps.server;
//
//import java.io.*;
//import org.xml.sax.*;
//import org.xml.sax.helpers.*;
//
//// using SAX
//public class LocXMLHandler implements ContentHandler {
//  class HowToHandler extends DefaultHandler implements ContentHandler {
//	
//    boolean title = false;
//    boolean url   = false;
//    
//    public void startElement(String nsURI, String strippedName,
//                            String tagName, Attributes attributes)
//       throws SAXException {
//
//     if (tagName.equalsIgnoreCase("title"))
//        title = true;
//     if (tagName.equalsIgnoreCase("url"))
//        url = true;
//    }
//
//    public void characters(char[] ch, int start, int length) {
//     if (title) {
//       title = false;
//       }
//     else if (url) {
//       System.out.println("Url: " + new String(ch, start,length));
//       url = false;
//       }
//     }
//    }
//
//@Override
//public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void endDocument() throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void endElement(String arg0, String arg1, String arg2)
//		throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void endPrefixMapping(String arg0) throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
//		throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void processingInstruction(String arg0, String arg1) throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void setDocumentLocator(Locator arg0) {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void skippedEntity(String arg0) throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void startDocument() throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
//		throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void startPrefixMapping(String arg0, String arg1) throws SAXException {
//	// TODO Auto-generated method stub
//	
//}
//}