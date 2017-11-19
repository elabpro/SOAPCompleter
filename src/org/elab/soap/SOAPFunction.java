/*
 * Class to store function description
 */
package org.elab.soap;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for SOAP Function
 *
 * @author eugine
 */
public class SOAPFunction {

    String name = "";
    String desc = "";
    String longDesc = "";
    List<SOAPFunctionArg> args = new ArrayList<SOAPFunctionArg>();

    String getArgs() {
        String res = "(";
        for (int i = 0; i < args.size(); i++) {
            res = res + args.get(i).name;
            if (i + 1 < args.size()) {
                res = res + ",";
            }
        }
        res = res + ");";
        return res;
    }
}
