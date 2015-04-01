// HTMLParser Library $Name:  $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Joshua Kerievsky
//
// Revision Control Information
//
// $Source: /liying/bmt_base/src/htmlps/com/bmtech/htmls/parser/visitors/TagFindingVisitor.java,v $
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

package com.bmtech.htmls.parser.visitors;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.Tag;
import com.bmtech.htmls.parser.util.NodeList;

public class TagFindingVisitor extends NodeVisitor {
    private String [] tagsToBeFound;
    private int count [];
    private int endTagCount [];
    private NodeList [] tags;
    private NodeList [] endTags;
    private boolean endTagCheck;

    public TagFindingVisitor(String [] tagsToBeFound) {
        this(tagsToBeFound,false);
    }

    public TagFindingVisitor(String [] tagsToBeFound, boolean endTagCheck) {
        this.tagsToBeFound = tagsToBeFound;
        this.tags = new NodeList[tagsToBeFound.length];
        if (endTagCheck) {
            endTags = new NodeList[tagsToBeFound.length];
            endTagCount = new int[tagsToBeFound.length];
        }
        for (int i=0;i<tagsToBeFound.length;i++) {
            tags[i] = new NodeList();
            if (endTagCheck)
                endTags[i] = new NodeList();
        }
        this.count = new int[tagsToBeFound.length];
        this.endTagCheck = endTagCheck;
    }

    public int getTagCount(int index) {
        return count[index];
    }

    public void visitTag(Tag tag)
    {
        for (int i=0;i<tagsToBeFound.length;i++)
            if (tag.getTagName().equalsIgnoreCase(tagsToBeFound[i])) {
                count[i]++;
                tags[i].add(tag);
            }
    }

    public void visitEndTag(Tag tag)
    {
        if (!endTagCheck) return;
        for (int i=0;i<tagsToBeFound.length;i++)
            if (tag.getTagName().equalsIgnoreCase(tagsToBeFound[i]))
            {
                endTagCount[i]++;
                endTags[i].add(tag);
            }
    }

    public Node [] getTags(int index) {
        return tags[index].toNodeArray();
    }

    public int getEndTagCount(int index) {
        return endTagCount[index];
    }

}
