package com.constaapps.constacalc.ui.main;


class CalcScanner implements Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CalcScanner (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CalcScanner (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CalcScanner () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NOT_ACCEPT,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NOT_ACCEPT,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NOT_ACCEPT,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NOT_ACCEPT,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NOT_ACCEPT,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NOT_ACCEPT,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NOT_ACCEPT,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NOT_ACCEPT,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NOT_ACCEPT,
		/* 61 */ YY_NOT_ACCEPT,
		/* 62 */ YY_NOT_ACCEPT,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NOT_ACCEPT,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NOT_ACCEPT,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"32:10,31,32:2,31,32:26,9,10,7,1,32,15,30,8,29,16,29:8,32:11,23,32:3,26,32:6" +
",25,32:13,24,32:2,14,28,11,17,18,32,19,32,3,32:2,20,2,4,12,27,21,22,6,13,5," +
"32:10,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,90,
"0,1:2,2,1:4,3,1:7,4,5,6,1:15,7,1,8,9,10,11,12,13,14,15,16,17,18,19,3,20,21," +
"22,23,8,24,25,26,27,8,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45" +
",46,47,48,49,50,51,52,53,54,55,56,57,58")[0];

	private int yy_nxt[][] = unpackFromString(59,33,
"1,2,3,35,38,35,40,4,5,6,7,42,35,44,46,48,8,35,9,35,50,35,52,10,11,54,35,56," +
"35,8,58,12,35,-1:36,34,-1:45,8,-1:12,8,53,-1:17,68,-1:32,70,-1:32,71,-1:21," +
"59,-1:44,36,-1:12,36,-1:22,15,-1:31,37,-1:18,16,-1:31,39,-1:11,41,-1,43,-1:" +
"3,45,-1:27,60,-1:28,47,-1:2,88,-1,80,-1:33,61,-1:28,49,89,-1,83,-1:37,62,-1" +
":38,51,-1:10,17,-1:30,18,-1:32,13,-1:7,55,-1:26,19,-1:38,57,-1:46,14,-1:25," +
"20,-1:31,81,-1:26,65,-1:25,67,-1:44,69,-1:34,21,-1:26,22,-1:38,23,-1:32,24," +
"-1:26,25,-1:30,85,-1:27,26,-1:42,27,-1:34,72,-1:30,28,-1:32,29,-1:35,30,-1:" +
"32,31,-1:32,32,-1:17,76,-1:41,77,-1:33,78,-1:37,79,-1:31,33,-1:32,63,-1:36," +
"66,-1:28,73,-1:32,64,-1:32,74,-1:32,75,-1:31,82,-1:32,84,-1:31,86,-1:32,87," +
"-1:16");

	public Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
 return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(CalcSymbol.PLUS); }
					case -3:
						break;
					case 3:
						{}
					case -4:
						break;
					case 4:
						{ return new Symbol(CalcSymbol.TIMES); }
					case -5:
						break;
					case 5:
						{ return new Symbol(CalcSymbol.DIVIDE); }
					case -6:
						break;
					case 6:
						{ return new Symbol(CalcSymbol.LPAREN); }
					case -7:
						break;
					case 7:
						{ return new Symbol(CalcSymbol.RPAREN); }
					case -8:
						break;
					case 8:
						{ return new Symbol(CalcSymbol.NUMBER, new Double(yytext()));}
					case -9:
						break;
					case 9:
						{ return new Symbol(CalcSymbol.NATURAL); }
					case -10:
						break;
					case 10:
						{ return new Symbol(CalcSymbol.EXP); }
					case -11:
						break;
					case 11:
						{ return new Symbol(CalcSymbol.EXPONENT); }
					case -12:
						break;
					case 12:
						{}
					case -13:
						break;
					case 13:
						{ return new Symbol(CalcSymbol.LN); }
					case -14:
						break;
					case 14:
						{ return new Symbol(CalcSymbol.PI); }
					case -15:
						break;
					case 15:
						{ return new Symbol(CalcSymbol.NEGATE); }
					case -16:
						break;
					case 16:
						{ return new Symbol(CalcSymbol.SIN); }
					case -17:
						break;
					case 17:
						{ return new Symbol(CalcSymbol.COS); }
					case -18:
						break;
					case 18:
						{ return new Symbol(CalcSymbol.TAN); }
					case -19:
						break;
					case 19:
						{ return new Symbol(CalcSymbol.ABSOLUTE); }
					case -20:
						break;
					case 20:
						{ return new Symbol(CalcSymbol.LOG); }
					case -21:
						break;
					case 21:
						{ return new Symbol(CalcSymbol.SINDEG); }
					case -22:
						break;
					case 22:
						{ return new Symbol(CalcSymbol.SQRT); }
					case -23:
						break;
					case 23:
						{ return new Symbol(CalcSymbol.COSDEG); }
					case -24:
						break;
					case 24:
						{ return new Symbol(CalcSymbol.TANDEG); }
					case -25:
						break;
					case 25:
						{ return new Symbol(CalcSymbol.ROOT); }
					case -26:
						break;
					case 26:
						{ return new Symbol(CalcSymbol.MINUS); }
					case -27:
						break;
					case 27:
						{ return new Symbol(CalcSymbol.SININV); }
					case -28:
						break;
					case 28:
						{ return new Symbol(CalcSymbol.COSINV); }
					case -29:
						break;
					case 29:
						{ return new Symbol(CalcSymbol.TANINV); }
					case -30:
						break;
					case 30:
						{ return new Symbol(CalcSymbol.SININVDEG); }
					case -31:
						break;
					case 31:
						{ return new Symbol(CalcSymbol.COSINVDEG); }
					case -32:
						break;
					case 32:
						{ return new Symbol(CalcSymbol.TANINVDEG); }
					case -33:
						break;
					case 33:
						{ return new Symbol(CalcSymbol.PERCENTAGE); }
					case -34:
						break;
					case 35:
						{}
					case -35:
						break;
					case 36:
						{ return new Symbol(CalcSymbol.NUMBER, new Double(yytext()));}
					case -36:
						break;
					case 38:
						{}
					case -37:
						break;
					case 40:
						{}
					case -38:
						break;
					case 42:
						{}
					case -39:
						break;
					case 44:
						{}
					case -40:
						break;
					case 46:
						{}
					case -41:
						break;
					case 48:
						{}
					case -42:
						break;
					case 50:
						{}
					case -43:
						break;
					case 52:
						{}
					case -44:
						break;
					case 54:
						{}
					case -45:
						break;
					case 56:
						{}
					case -46:
						break;
					case 58:
						{}
					case -47:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
