package com.bmtech.utils.superTrimer;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.log.BmtLogger;
import java.util.ArrayList;

public class LinkExtractor extends SuperTrimer
{
  public static final String NAME = "lnkext";
  boolean reverse = false;

  public void setArgs(ArrayList<String> paramArrayList)
    throws Exception
  {
  }

  public String getName()
  {
    return "lnkext";
  }

  public String toDefineStr()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    localStringBuilder.append(toDefineString(getName()));
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }

  public String trim(String paramString)
  {
    try
    {
      Parser localParser = new Parser(paramString);
      NodeClassFilter localNodeClassFilter = new NodeClassFilter(LinkTag.class);
      NodeList localNodeList = localParser.parse(localNodeClassFilter);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<div class='lnkextor'>\n");
      for (int i = 0; i < localNodeList.size(); i++)
      {
        Node localNode = localNodeList.elementAt(i);
        localStringBuilder.append(localNode.toHtml(true));
        localStringBuilder.append("<br>\n");
      }
      localStringBuilder.append("</div>");
      return localStringBuilder.toString();
    }
    catch (ParserException localParserException)
    {
      BmtLogger.instance().log(localParserException, "%s got for html %s", new Object[] { getClass(), paramString });
      localParserException.printStackTrace();
    }
    return paramString;
  }

  public String toString()
  {
    return String.format("[lnkext]", new Object[0]);
  }
}

/* Location:           C:\save_jar\tocomp\bmt_htmlps_util_aext_luc.jar
 * Qualified Name:     com.bmtech.utils.superTrimer.LinkExtractor
 * JD-Core Version:    0.6.0
 */