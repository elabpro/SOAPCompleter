/*
 * Module for Netbeans
 */
package org.elab.soap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Completion Provider for PHP
 *
 * @author eugine
 */
@MimeRegistration(mimeType = "text/x-php5", service = CompletionProvider.class)
public class SOAPCompletionProvider implements CompletionProvider {

    private List<SOAPFunction> listSOAPFunctions = new ArrayList<SOAPFunction>();

    public SOAPCompletionProvider() {
        initList();
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent jtc) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {

                String filter = null;
                int startOffset = caretOffset - 1;

                try {
                    final StyledDocument bDoc = (StyledDocument) document;
                    final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
                    final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    final int whiteOffset = indexOfWhite(line);
                    filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                    if (whiteOffset > 0) {
                        startOffset = lineStartOffset + whiteOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
                if (listSOAPFunctions == null) {
                    initList();
                }
                for (int i = 0; i < listSOAPFunctions.size(); i++) {
                    final String itemName = SOAPConfig.FunctionPrefix + listSOAPFunctions.get(i).name;
                    if (!itemName.equals("") && itemName.startsWith(filter)) {
                        completionResultSet.addItem(new SOAPCompletionItem(listSOAPFunctions.get(i), startOffset, caretOffset));
                    }
                }

                completionResultSet.finish();
            }

            private int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
                Element lineElement = doc.getParagraphElement(offset);
                int start = lineElement.getStartOffset();
                while (start + 1 < lineElement.getEndOffset()) {
                    try {
                        if (doc.getText(start, 1).charAt(0) != ' ') {
                            break;
                        }
                    } catch (BadLocationException ex) {
                        throw (BadLocationException) new BadLocationException(
                                "calling getText(" + start + ", " + (start + 1)
                                + ") on doc of length: " + doc.getLength(), start
                        ).initCause(ex);
                    }
                    start++;
                }
                return start;
            }

            private int indexOfWhite(char[] line) {
                int i = line.length;
                while (--i > -1) {
                    final char c = line[i];
                    if (Character.isWhitespace(c)) {
                        return i;
                    }
                }
                return -1;
            }

        }, jtc);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }

    /**
     * Load function's description
     *
     */
    private void initList() {
        String filename = getFileName();

        DocumentBuilderFactory builderFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document document;
            if (filename.equals(SOAPConfig.FileTmpName)) {
                document = builder.parse(new FileInputStream(filename));
            } else {
                document = builder.parse(getClass().getResourceAsStream(filename));
            }
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "//function";
            //read a nodelist using xpath
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                SOAPFunction sf = new SOAPFunction();
                org.w3c.dom.Element el = (org.w3c.dom.Element) nodeList.item(i);
                String name = el.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
                String value = "";
                try {
                    value = el.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
                } catch (Exception e) {
                    value = "";
                }
                sf.name = name;
                sf.desc = value;
                try {
                    value = el.getElementsByTagName("long-description").item(0).getFirstChild().getNodeValue();
                } catch (Exception e) {
                    value = "";
                }
                sf.longDesc = value;
                NodeList args = el.getElementsByTagName("argument");
                for (int a = 0; a < args.getLength(); a++) {
                    SOAPFunctionArg arg = new SOAPFunctionArg();
                    arg.name = args.item(a).getChildNodes().item(1).getTextContent();
                    arg.value = args.item(a).getChildNodes().item(3).getTextContent();
                    arg.type = args.item(a).getChildNodes().item(5).getTextContent();
                    sf.args.add(arg);
                }
                listSOAPFunctions.add(sf);
            }
        } catch (Exception e) {
//            System.out.println("EXCEPTION:" + e);
        }
    }

    /**
     * Download file with Stream
     *
     * @param urlStr
     * @param file
     * @throws IOException
     */
    private static void downloadUsingStream(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = readInputStreamWithTimeout(bis, buffer, 6000)) > 0) {
            System.out.println("READ");
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    /**
     * Get XML-filename with SOAP functions
     *
     * @return
     */
    private String getFileName() {
        String filename = "/org/elab/soap/structure.xml";
        String tmpName = "SOAPConfig.FileTmpName";
        try {
            if (new File(tmpName).exists() == false) {
                System.out.println("Trying to download");
                downloadUsingStream(SOAPConfig.FileRemoteURL, tmpName);
                System.out.println("Done");
            }
            filename = tmpName;
        } catch (Exception e) {
//
        }
        return filename;
    }

    /**
     * Read from remote site with timeout
     *
     * @param is InputStream
     * @param b Buffer
     * @param timeoutMillis
     * @return
     * @throws IOException
     */
    public static int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
            throws IOException {
        int bufferOffset = 0;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
            int readLength = java.lang.Math.min(is.available(), b.length - bufferOffset);
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) {
                break;
            }
            bufferOffset += readResult;
        }
        return bufferOffset;
    }
}
