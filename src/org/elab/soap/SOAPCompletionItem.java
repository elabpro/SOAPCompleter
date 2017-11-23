/*
 * Completion item
 */
package org.elab.soap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author eugine
 */
public class SOAPCompletionItem implements CompletionItem {

    protected String text;
    protected String documentation;
    private static ImageIcon fieldIcon
            = new ImageIcon(ImageUtilities.loadImage("org/elab/soap/icon.png"));
    private static Color fieldColor = Color.decode("0x0000B2");
    private int caretOffset;
    private int dotOffset;
    protected SOAPFunction item;

    /**
     * Constructor. Define documenation for a function
     *
     * @param item
     * @param dotOffset
     * @param caretOffset
     */
    SOAPCompletionItem(SOAPFunction item, int dotOffset, int caretOffset) {
        this.item = item;
        this.text = item.name;
        this.documentation = "<b>" + item.desc + "</b><p>" + item.longDesc.replace("\n", "<br />") + "</p><b>Аргументы:</b><p>"+item.getArgs()+"</p>";

        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;
    }

    @Override
    public int getPreferredWidth(Graphics graphics, Font font) {
        return CompletionUtilities.getPreferredWidth(text, null, graphics, font);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor,
            Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(fieldIcon, text, null, g, defaultFont,
                (selected ? Color.white : fieldColor), width, height, selected);
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }

    /**
     * Action for pressing 'Enter'
     * @param jtc
     */
    @Override
    public void defaultAction(JTextComponent jtc) {
        try {
            StyledDocument doc = (StyledDocument) jtc.getDocument();
            text = item.name + item.getArgs();
            doc.remove(caretOffset, dotOffset);
            doc.insertString(caretOffset, text, null);
            //This statement will close the code completion box:
            Completion.get().hideAll();
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void processKeyEvent(KeyEvent ke) {

    }

    /**
     * Show documentation
     *
     * @return
     */
    @Override
    public CompletionTask createDocumentationTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                completionResultSet.setDocumentation(new SOAPCompletionDocumentation(SOAPCompletionItem.this));
                completionResultSet.finish();
            }
        });
    }

    /**
     * Tooltip for a function
     *
     * @return
     */
    @Override
    public CompletionTask createToolTipTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                JToolTip toolTip = new JToolTip();
                toolTip.setTipText("Press Enter to insert \"" + text + "\"");
                completionResultSet.setToolTip(toolTip);
                completionResultSet.finish();
            }
        });
    }

    @Override
    public boolean instantSubstitution(JTextComponent jtc) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 1;
    }
}
