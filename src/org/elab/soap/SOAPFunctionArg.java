/*
 * Class to store function arguments
 */
package org.elab.soap;

/**
 *
 * @author eugine
 */
public class SOAPFunctionArg {

    String name = "";
    String type = "";
    String value = "";

    @Override
    public String toString() {
        return name + " " + type + " default:" + value;
    }
}
