// HTMLParser Library $Name:  $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /liying/bmt_base/src/htmlps/com/bmtech/htmls/parser/tags/ScriptTag.java,v $
// $Author: liying $
// $Date: 2012/07/31 06:46:37 $
// $Revision: 1.2 $
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//

package com.bmtech.htmls.parser.tags;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.scanners.ScriptScanner;
import com.bmtech.htmls.parser.util.SimpleNodeIterator;

/**
 * A script tag.
 */
public class ScriptTag extends CompositeTag
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"SCRIPT"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};

    /**
     * Script code if different from the page contents.
     */
    protected String mCode;

    /**
     * Create a new script tag.
     */
    public ScriptTag ()
    {
        setThisScanner (new ScriptScanner ());
    }

    /**
     * Return the set of names handled by this tag.
     * @return The names to be matched that create tags of this type.
     */
    public String[] getIds ()
    {
        return (mIds);
    }
    

    public String toPlainTextString ()
    {
        return ("");
    }
    /**
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }

    /**
     * Get the <code>LANGUAGE</code> attribute, if any.
     * @return The scripting language.
     */
    public String getLanguage()
    {
        return (getAttribute ("LANGUAGE"));
    }

    /**
     * Get the script code.
     * Normally this is the contents of the children, but in the rare case that
     * the script is encoded, this is the plaintext decrypted code.
     * @return The plaintext or overridden code contents of the tag.
     */
    public String getScriptCode ()
    {
        String ret;
        
        if (null != mCode)
            ret = mCode;
        else
            ret = getChildrenHTML ();

        return (ret);
    }

    /**
     * Set the code contents.
     * @param code The new code contents of this tag.
     */
    public void setScriptCode (String code)
    {
        mCode = code;
    }

    /**
     * Get the <code>TYPE</code> attribute, if any.
     * @return The script mime type.
     */
    public String getType()
    {
        return (getAttribute ("TYPE"));
    }

    /**
     * Set the language of the script tag.
     * @param language The new language value.
     */
    public void setLanguage (String language)
    {
        setAttribute ("LANGUAGE", language);
    }

    /**
     * Set the mime type of the script tag.
     * @param type The new mime type.
     */
    public void setType (String type)
    {
        setAttribute ("TYPE", type);
    }

    /**
     * Places the script contents into the provided buffer.
     * @param verbatim If <code>true</code> return as close to the original
     * page text as possible.
     * @param sb The buffer to add the script to.
     */
    protected void putChildrenInto (StringBuffer sb, boolean verbatim)
    {
        Node node;

        if (null != getScriptCode ())
            sb.append (getScriptCode ());
        else
            for (SimpleNodeIterator e = children (); e.hasMoreNodes ();)
            {
                node = e.nextNode ();
                // eliminate virtual tags
                if (!verbatim || !(node.getStartPosition () == node.getEndPosition ()))
                    sb.append (node.toHtml (verbatim));
            }
    }

    /**
     * Print the contents of the script tag suitable for debugging display.
     * @return The script language or type and code as a string.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Script Node : \n");
        if (getLanguage () != null || getType () != null)
        {
            sb.append("Properties -->\n");
            if (getLanguage () != null && getLanguage ().length () !=0)
                sb.append("[Language : "+ getLanguage ()+"]\n");
            if (getType () != null && getType ().length () != 0)
                sb.append("[Type : "+ getType ()+"]\n");
        }
        sb.append("\n");
        sb.append("Code\n");
        sb.append("****\n");
        sb.append(getScriptCode()+"\n");
        return sb.toString();
    }
}
