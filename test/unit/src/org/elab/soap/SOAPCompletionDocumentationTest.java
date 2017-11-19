/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elab.soap;

import java.net.URL;
import javax.swing.Action;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.spi.editor.completion.CompletionDocumentation;

/**
 *
 * @author eugine
 */
public class SOAPCompletionDocumentationTest {

    SOAPCompletionDocumentation cd;

    public SOAPCompletionDocumentationTest() {
        SOAPFunction f = new SOAPFunction();
        SOAPFunctionArg a = new SOAPFunctionArg();
        a.name = "param1";
        f.args.add(a);
        f.args.add(a);
        f.name = "test";
        f.desc = "testDesc";
        SOAPCompletionItem ci = new SOAPCompletionItem(f, 0, 0);
        cd = new SOAPCompletionDocumentation(ci);
    }

    /**
     * Test of getText method, of class SOAPCompletionDocumentation.
     */
    @Test
    public void testGetText() {
        String result = cd.getText();
        assertEquals("<b>testDesc", result.substring(0,11));
    }

    /**
     * Test of getURL method, of class SOAPCompletionDocumentation.
     */
    @Test
    public void testGetURL() {
        URL result = cd.getURL();
        assertEquals("http", result.toString().substring(0,4));
    }

}
