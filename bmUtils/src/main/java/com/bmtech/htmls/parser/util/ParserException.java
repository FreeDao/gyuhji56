// HTMLParser Library $Name:  $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Claude Duguay
//
// Revision Control Information
//
// $Source: /liying/bmt_base/src/htmlps/com/bmtech/htmls/parser/util/ParserException.java,v $
// $Author: liying $
// $Date: 2012/07/31 06:46:38 $
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

package com.bmtech.htmls.parser.util;

/**
 * Library-specific support for chained exceptions.
 *
 * @see ChainedException
 **/
public class ParserException
  extends ChainedException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public ParserException() {}

  public ParserException(String message)
  {
    super(message);
  }

  public ParserException(Throwable throwable)
  {
    super(throwable);
  }

  public ParserException(String message, Throwable throwable)
  {
    super(message, throwable);
  }
}

