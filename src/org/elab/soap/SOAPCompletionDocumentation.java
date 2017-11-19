/*
 * Show documentation for a function
 */
package org.elab.soap;

import org.netbeans.spi.editor.completion.CompletionDocumentation;
import java.net.URL;
import javax.swing.Action;

/**
 *
 * @author eugine
 */
public class SOAPCompletionDocumentation implements CompletionDocumentation {

    private SOAPCompletionItem item;

    public SOAPCompletionDocumentation(SOAPCompletionItem item) {
        this.item = item;
    }

    @Override
    public String getText() {
        if (item != null && item.documentation != null) {
            return item.documentation;
        } else {
            return "No information about this";
        }
    }

    /**
     * Generate URL to view documentation online
     *
     * @return
     */
    @Override
    public URL getURL() {
        try {
            URL docURL = new URL(SOAPConfig.URLforFunction.replace("{FUNCTION_NAME}", item.item.name));
            return docURL;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CompletionDocumentation resolveLink(String string) {
        return null;
    }

    @Override
    public Action getGotoSourceAction() {
        return null;
    }
}
