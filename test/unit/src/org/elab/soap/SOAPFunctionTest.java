/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elab.soap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eugine
 */
public class SOAPFunctionTest {

    public SOAPFunctionTest() {
    }

    @Test
    public void testGetArgs() {
        SOAPFunction f = new SOAPFunction();
        SOAPFunctionArg a = new SOAPFunctionArg();
        a.name = "param1";
        f.args.add(a);
        f.args.add(a);
        String result = f.getArgs();
        assertEquals("(param1,param1);",result);
    }

}
