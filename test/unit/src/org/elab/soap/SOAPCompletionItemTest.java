/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elab.soap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.text.JTextComponent;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.spi.editor.completion.CompletionTask;

/**
 *
 * @author eugine
 */
public class SOAPCompletionItemTest {

    SOAPCompletionItem ci;

    public SOAPCompletionItemTest() {
        SOAPFunction f = new SOAPFunction();
        SOAPFunctionArg a = new SOAPFunctionArg();
        a.name = "param1";
        f.args.add(a);
        f.args.add(a);
        f.name = "test";
        f.desc = "testDesc";
        ci = new SOAPCompletionItem(f, 0, 0);
    }

    /**
     * Test of getSortText method, of class SOAPCompletionItem.
     */
    @Test
    public void testGetSortText() {
        assertEquals("test", ci.getSortText());
    }

    /**
     * Test of getInsertPrefix method, of class SOAPCompletionItem.
     */
    @Test
    public void testGetInsertPrefix() {
        assertEquals("test", ci.getSortText());
    }

    /**
     * Test of getSortPriority method, of class SOAPCompletionItem.
     */
    @Test
    public void testGetSortPriority() {
        int result = ci.getSortPriority();
        assertEquals(1, result);
    }

}
