/*
 * Copyright (C) 2017 Francisco Manuel Garcia Moreno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fgarmo.utilities;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final Color COLOR_FILE_EXPLORER_NODE			= Color.blue;
	public static final Color COLOR_LINE_HIGHTLIGH 				= new Color(0.2f, 0.8f, 1, 0.15f);
	public static final Color COLOR_TEXT_LINE_NUMBER_HIGHTLIGH 	= new Color(0.5f, 0.2f, 1, 1f);
	
	//Syntax colors
	public static final Color COLOR_SYNTAX_GOTO					= new Color(155, 30, 115);
	public static final Color COLOR_SYNTAX_IF					= COLOR_SYNTAX_GOTO;
	public static final Color COLOR_SYNTAX_ASSIGN				= new Color(55, 30, 255);
	public static final Color COLOR_LEFT_LABEL					= new Color(0, 150, 0);
	public static final Color COLOR_RIGHT_LABEL					= COLOR_LEFT_LABEL;
	public static final Color COLOR_VARS						= new Color(150, 80, 130);
	
	public static Map<String, Color> syntaxColors() {
        final Map<String, Color> numMap = new HashMap<String, Color>();
        numMap.put("[XYZ]([1-9][0-9]*)*", COLOR_VARS);
        numMap.put("<-|!=", COLOR_SYNTAX_ASSIGN);
        numMap.put("GOTO[\\s]{1,}[A-W]([1-9][0-9]*)*", COLOR_RIGHT_LABEL); //right label
        numMap.put("\\[[A-W]([1-9][0-9]*)*\\]", COLOR_LEFT_LABEL); //left label
        numMap.put(GOTO_KEYWORDS_REGEX, COLOR_SYNTAX_GOTO);
        return Collections.unmodifiableMap(numMap);
    }
	


    public static final String[] GOTO_KEYWORDS = new String[] { "IF","GOTO" };
	public static String GOTO_KEYWORDS_REGEX;
	static {
		StringBuilder buff = new StringBuilder("");
		buff.append("(");
		for (String keyword : GOTO_KEYWORDS) {
			buff.append("\\b").append(keyword).append("\\b").append("|");
		}
		buff.deleteCharAt(buff.length() - 1);
		buff.append(")");
		GOTO_KEYWORDS_REGEX = buff.toString();
	}

}
