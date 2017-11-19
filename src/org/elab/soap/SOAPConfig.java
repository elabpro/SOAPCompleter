/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elab.soap;

/**
 * Parameters for Module
 *
 * @author eugine
 */
public class SOAPConfig {
    // Use {FUNCTION_NAME} as a metaname
    final static String URLforFunction = "http://localhost/docs/api/function-{FUNCTION_NAME}.html";
    final static String FunctionPrefix = "$client->";
    final static String FileTmpName = "/tmp/structure.xml";
    final static String FileRemoteURL = "http://localhost/docs/api/structure.xml";
}
