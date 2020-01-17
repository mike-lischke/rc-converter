// $ANTLR 2.7.4: "Expression parser.g" -> "ExpressionLexer.java"$

/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.expressions;

import java.io.StringReader;
import java.util.ArrayList;

import antlr.collections.AST;

import net.softgems.resourceparser.main.IParseEventListener;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class ExpressionLexer extends antlr.CharScanner implements ExpressionLexerTokenTypes, TokenStream
 {


  /** List of event listeners who want to get notified about an lexer event. */
  private ArrayList listeners = new ArrayList();
  boolean hadErrors;
  boolean hadWarnings;
  
  //------------------------------------------------------------------------------------------------
  
  public void addLexerEventListener(IParseEventListener listener)
  {
    listeners.add(listener);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void removeLexerEventListener(IParseEventListener listener)
  {
    listeners.remove(listener);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void newline()
  {
    super.newline();
    doEvent(IParseEventListener.NEW_LINE, null);
  }
  
  //------------------------------------------------------------------------------------------------

  public void panic()
  {
  	hadErrors = true;
    doEvent(IParseEventListener.PANIC, "RC lexer panic");
  }

  //------------------------------------------------------------------------------------------------
  
  /** 
   * This method is executed by ANTLR internally when it detected an illegal
   *  state that cannot be recovered from.
   */
  public void panic(String s)
  {
  	hadErrors = true;
    doEvent(IParseEventListener.PANIC, "RC lexer panic: " + s);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void reportError(RecognitionException ex)
  {
  	hadErrors = true;
    doEvent(IParseEventListener.ERROR, ex.toString());
  }

  //------------------------------------------------------------------------------------------------
  
  public void reportError(String s)
  {
  	hadErrors = true;
    if (getFilename() == null)
    {
      doEvent(IParseEventListener.ERROR, s);
    }
    else
    {
      doEvent(IParseEventListener.ERROR, getFilename() + ": " + s);
    }
  }

  //------------------------------------------------------------------------------------------------
  
  public void reportWarning(String s)
  {
  	hadWarnings = true;
    if (getFilename() == null)
    {
      doEvent(IParseEventListener.WARNING, s);
    }
    else
    {
      doEvent(IParseEventListener.WARNING, getFilename() + ": " + s);
    }
  }

  //------------------------------------------------------------------------------------------------
  
  private void doEvent(int event, String message)
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      IParseEventListener listener = (IParseEventListener)listeners.get(i);
      listener.handleEvent(event, message);
    }
  }
  
  //------------------------------------------------------------------------------------------------
  
  public boolean hadErrors()
  {
    return hadErrors;
  }
  
  //------------------------------------------------------------------------------------------------
  
  public boolean hadWarnings()
  {
    return hadWarnings;
  }
  
  //------------------------------------------------------------------------------------------------
  
public ExpressionLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public ExpressionLexer(Reader in) {
	this(new CharBuffer(in));
}
public ExpressionLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public ExpressionLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = false;
	setCaseSensitive(false);
	literals = new Hashtable();
	literals.put(new ANTLRHashString("false", this), new Integer(80));
	literals.put(new ANTLRHashString("true", this), new Integer(79));
	literals.put(new ANTLRHashString("not", this), new Integer(78));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '(':
				{
					mLEFT_PARENTHESE(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRIGHT_PARENTHESE(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLEFT_BRACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRIGHT_BRACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '[':
				{
					mLEFT_BRACKET(true);
					theRetToken=_returnToken;
					break;
				}
				case ']':
				{
					mRIGHT_BRACKET(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMICOLON(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case ':':
				{
					mCOLON(true);
					theRetToken=_returnToken;
					break;
				}
				case '~':
				{
					mBITWISE_NOT(true);
					theRetToken=_returnToken;
					break;
				}
				case '^':
				{
					mBITWISE_XOR(true);
					theRetToken=_returnToken;
					break;
				}
				case '*':
				{
					mSTAR(true);
					theRetToken=_returnToken;
					break;
				}
				case '/':
				{
					mDIV(true);
					theRetToken=_returnToken;
					break;
				}
				case '%':
				{
					mMOD(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case ' ':
				{
					mWHITE_SPACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':
				{
					mSTRING_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '.':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':
				{
					mNUMERAL(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='&') && (LA(2)=='&')) {
						mLOGICAL_AND(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='|')) {
						mLOGICAL_OR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='<')) {
						mSHIFT_LEFT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>')) {
						mSHIFT_RIGHT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='=')) {
						mEQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (LA(2)=='=')) {
						mUNEQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='=')) {
						mLESS_THAN_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='=')) {
						mGREATER_THAN_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (LA(2)=='+')) {
						mINC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (LA(2)=='-')) {
						mDEC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='\'') && (_tokenSet_0.member(LA(2)))) {
						mCHARACTER_LITERAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='\'') && (true)) {
						mAPOSTROPHE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (true)) {
						mBITWISE_AND(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (true)) {
						mBITWISE_OR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (true)) {
						mLOGICAL_NOT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (true)) {
						mPLUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (true)) {
						mMINUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (true)) {
						mLESS_THAN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (true)) {
						mGREATER_THAN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (true)) {
						mASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((_tokenSet_1.member(LA(1)))) {
						mIDENTIFIER(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	protected final void mUNICODE_LETTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UNICODE_LETTER;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('\u0061','\u007A');
			break;
		}
		case '\u00aa':
		{
			matchRange('\u00AA','\u00AA');
			break;
		}
		case '\u00b5':
		{
			matchRange('\u00B5','\u00B5');
			break;
		}
		case '\u00ba':
		{
			matchRange('\u00BA','\u00BA');
			break;
		}
		case '\u00c0':  case '\u00c1':  case '\u00c2':  case '\u00c3':
		case '\u00c4':  case '\u00c5':  case '\u00c6':  case '\u00c7':
		case '\u00c8':  case '\u00c9':  case '\u00ca':  case '\u00cb':
		case '\u00cc':  case '\u00cd':  case '\u00ce':  case '\u00cf':
		case '\u00d0':  case '\u00d1':  case '\u00d2':  case '\u00d3':
		case '\u00d4':  case '\u00d5':  case '\u00d6':
		{
			matchRange('\u00C0','\u00D6');
			break;
		}
		case '\u00d8':  case '\u00d9':  case '\u00da':  case '\u00db':
		case '\u00dc':  case '\u00dd':  case '\u00de':  case '\u00df':
		case '\u00e0':  case '\u00e1':  case '\u00e2':  case '\u00e3':
		case '\u00e4':  case '\u00e5':  case '\u00e6':  case '\u00e7':
		case '\u00e8':  case '\u00e9':  case '\u00ea':  case '\u00eb':
		case '\u00ec':  case '\u00ed':  case '\u00ee':  case '\u00ef':
		case '\u00f0':  case '\u00f1':  case '\u00f2':  case '\u00f3':
		case '\u00f4':  case '\u00f5':  case '\u00f6':
		{
			matchRange('\u00D8','\u00F6');
			break;
		}
		case '\u01fa':  case '\u01fb':  case '\u01fc':  case '\u01fd':
		case '\u01fe':  case '\u01ff':  case '\u0200':  case '\u0201':
		case '\u0202':  case '\u0203':  case '\u0204':  case '\u0205':
		case '\u0206':  case '\u0207':  case '\u0208':  case '\u0209':
		case '\u020a':  case '\u020b':  case '\u020c':  case '\u020d':
		case '\u020e':  case '\u020f':  case '\u0210':  case '\u0211':
		case '\u0212':  case '\u0213':  case '\u0214':  case '\u0215':
		case '\u0216':  case '\u0217':
		{
			matchRange('\u01FA','\u0217');
			break;
		}
		case '\u0250':  case '\u0251':  case '\u0252':  case '\u0253':
		case '\u0254':  case '\u0255':  case '\u0256':  case '\u0257':
		case '\u0258':  case '\u0259':  case '\u025a':  case '\u025b':
		case '\u025c':  case '\u025d':  case '\u025e':  case '\u025f':
		case '\u0260':  case '\u0261':  case '\u0262':  case '\u0263':
		case '\u0264':  case '\u0265':  case '\u0266':  case '\u0267':
		case '\u0268':  case '\u0269':  case '\u026a':  case '\u026b':
		case '\u026c':  case '\u026d':  case '\u026e':  case '\u026f':
		case '\u0270':  case '\u0271':  case '\u0272':  case '\u0273':
		case '\u0274':  case '\u0275':  case '\u0276':  case '\u0277':
		case '\u0278':  case '\u0279':  case '\u027a':  case '\u027b':
		case '\u027c':  case '\u027d':  case '\u027e':  case '\u027f':
		case '\u0280':  case '\u0281':  case '\u0282':  case '\u0283':
		case '\u0284':  case '\u0285':  case '\u0286':  case '\u0287':
		case '\u0288':  case '\u0289':  case '\u028a':  case '\u028b':
		case '\u028c':  case '\u028d':  case '\u028e':  case '\u028f':
		case '\u0290':  case '\u0291':  case '\u0292':  case '\u0293':
		case '\u0294':  case '\u0295':  case '\u0296':  case '\u0297':
		case '\u0298':  case '\u0299':  case '\u029a':  case '\u029b':
		case '\u029c':  case '\u029d':  case '\u029e':  case '\u029f':
		case '\u02a0':  case '\u02a1':  case '\u02a2':  case '\u02a3':
		case '\u02a4':  case '\u02a5':  case '\u02a6':  case '\u02a7':
		case '\u02a8':
		{
			matchRange('\u0250','\u02A8');
			break;
		}
		case '\u02b0':  case '\u02b1':  case '\u02b2':  case '\u02b3':
		case '\u02b4':  case '\u02b5':  case '\u02b6':  case '\u02b7':
		case '\u02b8':
		{
			matchRange('\u02B0','\u02B8');
			break;
		}
		case '\u02bb':  case '\u02bc':  case '\u02bd':  case '\u02be':
		case '\u02bf':  case '\u02c0':  case '\u02c1':
		{
			matchRange('\u02BB','\u02C1');
			break;
		}
		case '\u02d0':  case '\u02d1':
		{
			matchRange('\u02D0','\u02D1');
			break;
		}
		case '\u02e0':  case '\u02e1':  case '\u02e2':  case '\u02e3':
		case '\u02e4':
		{
			matchRange('\u02E0','\u02E4');
			break;
		}
		case '\u037a':
		{
			matchRange('\u037A','\u037A');
			break;
		}
		case '\u0386':
		{
			matchRange('\u0386','\u0386');
			break;
		}
		case '\u0388':  case '\u0389':  case '\u038a':
		{
			matchRange('\u0388','\u038A');
			break;
		}
		case '\u038c':
		{
			matchRange('\u038C','\u038C');
			break;
		}
		case '\u038e':  case '\u038f':  case '\u0390':  case '\u0391':
		case '\u0392':  case '\u0393':  case '\u0394':  case '\u0395':
		case '\u0396':  case '\u0397':  case '\u0398':  case '\u0399':
		case '\u039a':  case '\u039b':  case '\u039c':  case '\u039d':
		case '\u039e':  case '\u039f':  case '\u03a0':  case '\u03a1':
		{
			matchRange('\u038E','\u03A1');
			break;
		}
		case '\u03a3':  case '\u03a4':  case '\u03a5':  case '\u03a6':
		case '\u03a7':  case '\u03a8':  case '\u03a9':  case '\u03aa':
		case '\u03ab':  case '\u03ac':  case '\u03ad':  case '\u03ae':
		case '\u03af':  case '\u03b0':  case '\u03b1':  case '\u03b2':
		case '\u03b3':  case '\u03b4':  case '\u03b5':  case '\u03b6':
		case '\u03b7':  case '\u03b8':  case '\u03b9':  case '\u03ba':
		case '\u03bb':  case '\u03bc':  case '\u03bd':  case '\u03be':
		case '\u03bf':  case '\u03c0':  case '\u03c1':  case '\u03c2':
		case '\u03c3':  case '\u03c4':  case '\u03c5':  case '\u03c6':
		case '\u03c7':  case '\u03c8':  case '\u03c9':  case '\u03ca':
		case '\u03cb':  case '\u03cc':  case '\u03cd':  case '\u03ce':
		{
			matchRange('\u03A3','\u03CE');
			break;
		}
		case '\u03d0':  case '\u03d1':  case '\u03d2':  case '\u03d3':
		case '\u03d4':  case '\u03d5':  case '\u03d6':
		{
			matchRange('\u03D0','\u03D6');
			break;
		}
		case '\u03da':
		{
			matchRange('\u03DA','\u03DA');
			break;
		}
		case '\u03dc':
		{
			matchRange('\u03DC','\u03DC');
			break;
		}
		case '\u03de':
		{
			matchRange('\u03DE','\u03DE');
			break;
		}
		case '\u03e0':
		{
			matchRange('\u03E0','\u03E0');
			break;
		}
		case '\u03e2':  case '\u03e3':  case '\u03e4':  case '\u03e5':
		case '\u03e6':  case '\u03e7':  case '\u03e8':  case '\u03e9':
		case '\u03ea':  case '\u03eb':  case '\u03ec':  case '\u03ed':
		case '\u03ee':  case '\u03ef':  case '\u03f0':  case '\u03f1':
		case '\u03f2':  case '\u03f3':
		{
			matchRange('\u03E2','\u03F3');
			break;
		}
		case '\u0401':  case '\u0402':  case '\u0403':  case '\u0404':
		case '\u0405':  case '\u0406':  case '\u0407':  case '\u0408':
		case '\u0409':  case '\u040a':  case '\u040b':  case '\u040c':
		{
			matchRange('\u0401','\u040C');
			break;
		}
		case '\u040e':  case '\u040f':  case '\u0410':  case '\u0411':
		case '\u0412':  case '\u0413':  case '\u0414':  case '\u0415':
		case '\u0416':  case '\u0417':  case '\u0418':  case '\u0419':
		case '\u041a':  case '\u041b':  case '\u041c':  case '\u041d':
		case '\u041e':  case '\u041f':  case '\u0420':  case '\u0421':
		case '\u0422':  case '\u0423':  case '\u0424':  case '\u0425':
		case '\u0426':  case '\u0427':  case '\u0428':  case '\u0429':
		case '\u042a':  case '\u042b':  case '\u042c':  case '\u042d':
		case '\u042e':  case '\u042f':  case '\u0430':  case '\u0431':
		case '\u0432':  case '\u0433':  case '\u0434':  case '\u0435':
		case '\u0436':  case '\u0437':  case '\u0438':  case '\u0439':
		case '\u043a':  case '\u043b':  case '\u043c':  case '\u043d':
		case '\u043e':  case '\u043f':  case '\u0440':  case '\u0441':
		case '\u0442':  case '\u0443':  case '\u0444':  case '\u0445':
		case '\u0446':  case '\u0447':  case '\u0448':  case '\u0449':
		case '\u044a':  case '\u044b':  case '\u044c':  case '\u044d':
		case '\u044e':  case '\u044f':
		{
			matchRange('\u040E','\u044F');
			break;
		}
		case '\u0451':  case '\u0452':  case '\u0453':  case '\u0454':
		case '\u0455':  case '\u0456':  case '\u0457':  case '\u0458':
		case '\u0459':  case '\u045a':  case '\u045b':  case '\u045c':
		{
			matchRange('\u0451','\u045C');
			break;
		}
		case '\u045e':  case '\u045f':  case '\u0460':  case '\u0461':
		case '\u0462':  case '\u0463':  case '\u0464':  case '\u0465':
		case '\u0466':  case '\u0467':  case '\u0468':  case '\u0469':
		case '\u046a':  case '\u046b':  case '\u046c':  case '\u046d':
		case '\u046e':  case '\u046f':  case '\u0470':  case '\u0471':
		case '\u0472':  case '\u0473':  case '\u0474':  case '\u0475':
		case '\u0476':  case '\u0477':  case '\u0478':  case '\u0479':
		case '\u047a':  case '\u047b':  case '\u047c':  case '\u047d':
		case '\u047e':  case '\u047f':  case '\u0480':  case '\u0481':
		{
			matchRange('\u045E','\u0481');
			break;
		}
		case '\u0490':  case '\u0491':  case '\u0492':  case '\u0493':
		case '\u0494':  case '\u0495':  case '\u0496':  case '\u0497':
		case '\u0498':  case '\u0499':  case '\u049a':  case '\u049b':
		case '\u049c':  case '\u049d':  case '\u049e':  case '\u049f':
		case '\u04a0':  case '\u04a1':  case '\u04a2':  case '\u04a3':
		case '\u04a4':  case '\u04a5':  case '\u04a6':  case '\u04a7':
		case '\u04a8':  case '\u04a9':  case '\u04aa':  case '\u04ab':
		case '\u04ac':  case '\u04ad':  case '\u04ae':  case '\u04af':
		case '\u04b0':  case '\u04b1':  case '\u04b2':  case '\u04b3':
		case '\u04b4':  case '\u04b5':  case '\u04b6':  case '\u04b7':
		case '\u04b8':  case '\u04b9':  case '\u04ba':  case '\u04bb':
		case '\u04bc':  case '\u04bd':  case '\u04be':  case '\u04bf':
		case '\u04c0':  case '\u04c1':  case '\u04c2':  case '\u04c3':
		case '\u04c4':
		{
			matchRange('\u0490','\u04C4');
			break;
		}
		case '\u04c7':  case '\u04c8':
		{
			matchRange('\u04C7','\u04C8');
			break;
		}
		case '\u04cb':  case '\u04cc':
		{
			matchRange('\u04CB','\u04CC');
			break;
		}
		case '\u04d0':  case '\u04d1':  case '\u04d2':  case '\u04d3':
		case '\u04d4':  case '\u04d5':  case '\u04d6':  case '\u04d7':
		case '\u04d8':  case '\u04d9':  case '\u04da':  case '\u04db':
		case '\u04dc':  case '\u04dd':  case '\u04de':  case '\u04df':
		case '\u04e0':  case '\u04e1':  case '\u04e2':  case '\u04e3':
		case '\u04e4':  case '\u04e5':  case '\u04e6':  case '\u04e7':
		case '\u04e8':  case '\u04e9':  case '\u04ea':  case '\u04eb':
		{
			matchRange('\u04D0','\u04EB');
			break;
		}
		case '\u04ee':  case '\u04ef':  case '\u04f0':  case '\u04f1':
		case '\u04f2':  case '\u04f3':  case '\u04f4':  case '\u04f5':
		{
			matchRange('\u04EE','\u04F5');
			break;
		}
		case '\u04f8':  case '\u04f9':
		{
			matchRange('\u04F8','\u04F9');
			break;
		}
		case '\u0531':  case '\u0532':  case '\u0533':  case '\u0534':
		case '\u0535':  case '\u0536':  case '\u0537':  case '\u0538':
		case '\u0539':  case '\u053a':  case '\u053b':  case '\u053c':
		case '\u053d':  case '\u053e':  case '\u053f':  case '\u0540':
		case '\u0541':  case '\u0542':  case '\u0543':  case '\u0544':
		case '\u0545':  case '\u0546':  case '\u0547':  case '\u0548':
		case '\u0549':  case '\u054a':  case '\u054b':  case '\u054c':
		case '\u054d':  case '\u054e':  case '\u054f':  case '\u0550':
		case '\u0551':  case '\u0552':  case '\u0553':  case '\u0554':
		case '\u0555':  case '\u0556':
		{
			matchRange('\u0531','\u0556');
			break;
		}
		case '\u0559':
		{
			matchRange('\u0559','\u0559');
			break;
		}
		case '\u0561':  case '\u0562':  case '\u0563':  case '\u0564':
		case '\u0565':  case '\u0566':  case '\u0567':  case '\u0568':
		case '\u0569':  case '\u056a':  case '\u056b':  case '\u056c':
		case '\u056d':  case '\u056e':  case '\u056f':  case '\u0570':
		case '\u0571':  case '\u0572':  case '\u0573':  case '\u0574':
		case '\u0575':  case '\u0576':  case '\u0577':  case '\u0578':
		case '\u0579':  case '\u057a':  case '\u057b':  case '\u057c':
		case '\u057d':  case '\u057e':  case '\u057f':  case '\u0580':
		case '\u0581':  case '\u0582':  case '\u0583':  case '\u0584':
		case '\u0585':  case '\u0586':  case '\u0587':
		{
			matchRange('\u0561','\u0587');
			break;
		}
		case '\u05d0':  case '\u05d1':  case '\u05d2':  case '\u05d3':
		case '\u05d4':  case '\u05d5':  case '\u05d6':  case '\u05d7':
		case '\u05d8':  case '\u05d9':  case '\u05da':  case '\u05db':
		case '\u05dc':  case '\u05dd':  case '\u05de':  case '\u05df':
		case '\u05e0':  case '\u05e1':  case '\u05e2':  case '\u05e3':
		case '\u05e4':  case '\u05e5':  case '\u05e6':  case '\u05e7':
		case '\u05e8':  case '\u05e9':  case '\u05ea':
		{
			matchRange('\u05D0','\u05EA');
			break;
		}
		case '\u05f0':  case '\u05f1':  case '\u05f2':
		{
			matchRange('\u05F0','\u05F2');
			break;
		}
		case '\u0621':  case '\u0622':  case '\u0623':  case '\u0624':
		case '\u0625':  case '\u0626':  case '\u0627':  case '\u0628':
		case '\u0629':  case '\u062a':  case '\u062b':  case '\u062c':
		case '\u062d':  case '\u062e':  case '\u062f':  case '\u0630':
		case '\u0631':  case '\u0632':  case '\u0633':  case '\u0634':
		case '\u0635':  case '\u0636':  case '\u0637':  case '\u0638':
		case '\u0639':  case '\u063a':
		{
			matchRange('\u0621','\u063A');
			break;
		}
		case '\u0640':  case '\u0641':  case '\u0642':  case '\u0643':
		case '\u0644':  case '\u0645':  case '\u0646':  case '\u0647':
		case '\u0648':  case '\u0649':  case '\u064a':
		{
			matchRange('\u0640','\u064A');
			break;
		}
		case '\u0671':  case '\u0672':  case '\u0673':  case '\u0674':
		case '\u0675':  case '\u0676':  case '\u0677':  case '\u0678':
		case '\u0679':  case '\u067a':  case '\u067b':  case '\u067c':
		case '\u067d':  case '\u067e':  case '\u067f':  case '\u0680':
		case '\u0681':  case '\u0682':  case '\u0683':  case '\u0684':
		case '\u0685':  case '\u0686':  case '\u0687':  case '\u0688':
		case '\u0689':  case '\u068a':  case '\u068b':  case '\u068c':
		case '\u068d':  case '\u068e':  case '\u068f':  case '\u0690':
		case '\u0691':  case '\u0692':  case '\u0693':  case '\u0694':
		case '\u0695':  case '\u0696':  case '\u0697':  case '\u0698':
		case '\u0699':  case '\u069a':  case '\u069b':  case '\u069c':
		case '\u069d':  case '\u069e':  case '\u069f':  case '\u06a0':
		case '\u06a1':  case '\u06a2':  case '\u06a3':  case '\u06a4':
		case '\u06a5':  case '\u06a6':  case '\u06a7':  case '\u06a8':
		case '\u06a9':  case '\u06aa':  case '\u06ab':  case '\u06ac':
		case '\u06ad':  case '\u06ae':  case '\u06af':  case '\u06b0':
		case '\u06b1':  case '\u06b2':  case '\u06b3':  case '\u06b4':
		case '\u06b5':  case '\u06b6':  case '\u06b7':
		{
			matchRange('\u0671','\u06B7');
			break;
		}
		case '\u06ba':  case '\u06bb':  case '\u06bc':  case '\u06bd':
		case '\u06be':
		{
			matchRange('\u06BA','\u06BE');
			break;
		}
		case '\u06c0':  case '\u06c1':  case '\u06c2':  case '\u06c3':
		case '\u06c4':  case '\u06c5':  case '\u06c6':  case '\u06c7':
		case '\u06c8':  case '\u06c9':  case '\u06ca':  case '\u06cb':
		case '\u06cc':  case '\u06cd':  case '\u06ce':
		{
			matchRange('\u06C0','\u06CE');
			break;
		}
		case '\u06d0':  case '\u06d1':  case '\u06d2':  case '\u06d3':
		{
			matchRange('\u06D0','\u06D3');
			break;
		}
		case '\u06d5':
		{
			matchRange('\u06D5','\u06D5');
			break;
		}
		case '\u06e5':  case '\u06e6':
		{
			matchRange('\u06E5','\u06E6');
			break;
		}
		case '\u0905':  case '\u0906':  case '\u0907':  case '\u0908':
		case '\u0909':  case '\u090a':  case '\u090b':  case '\u090c':
		case '\u090d':  case '\u090e':  case '\u090f':  case '\u0910':
		case '\u0911':  case '\u0912':  case '\u0913':  case '\u0914':
		case '\u0915':  case '\u0916':  case '\u0917':  case '\u0918':
		case '\u0919':  case '\u091a':  case '\u091b':  case '\u091c':
		case '\u091d':  case '\u091e':  case '\u091f':  case '\u0920':
		case '\u0921':  case '\u0922':  case '\u0923':  case '\u0924':
		case '\u0925':  case '\u0926':  case '\u0927':  case '\u0928':
		case '\u0929':  case '\u092a':  case '\u092b':  case '\u092c':
		case '\u092d':  case '\u092e':  case '\u092f':  case '\u0930':
		case '\u0931':  case '\u0932':  case '\u0933':  case '\u0934':
		case '\u0935':  case '\u0936':  case '\u0937':  case '\u0938':
		case '\u0939':
		{
			matchRange('\u0905','\u0939');
			break;
		}
		case '\u093d':
		{
			matchRange('\u093D','\u093D');
			break;
		}
		case '\u0958':  case '\u0959':  case '\u095a':  case '\u095b':
		case '\u095c':  case '\u095d':  case '\u095e':  case '\u095f':
		case '\u0960':  case '\u0961':
		{
			matchRange('\u0958','\u0961');
			break;
		}
		case '\u0985':  case '\u0986':  case '\u0987':  case '\u0988':
		case '\u0989':  case '\u098a':  case '\u098b':  case '\u098c':
		{
			matchRange('\u0985','\u098C');
			break;
		}
		case '\u098f':  case '\u0990':
		{
			matchRange('\u098F','\u0990');
			break;
		}
		case '\u0993':  case '\u0994':  case '\u0995':  case '\u0996':
		case '\u0997':  case '\u0998':  case '\u0999':  case '\u099a':
		case '\u099b':  case '\u099c':  case '\u099d':  case '\u099e':
		case '\u099f':  case '\u09a0':  case '\u09a1':  case '\u09a2':
		case '\u09a3':  case '\u09a4':  case '\u09a5':  case '\u09a6':
		case '\u09a7':  case '\u09a8':
		{
			matchRange('\u0993','\u09A8');
			break;
		}
		case '\u09aa':  case '\u09ab':  case '\u09ac':  case '\u09ad':
		case '\u09ae':  case '\u09af':  case '\u09b0':
		{
			matchRange('\u09AA','\u09B0');
			break;
		}
		case '\u09b2':
		{
			matchRange('\u09B2','\u09B2');
			break;
		}
		case '\u09b6':  case '\u09b7':  case '\u09b8':  case '\u09b9':
		{
			matchRange('\u09B6','\u09B9');
			break;
		}
		case '\u09dc':  case '\u09dd':
		{
			matchRange('\u09DC','\u09DD');
			break;
		}
		case '\u09df':  case '\u09e0':  case '\u09e1':
		{
			matchRange('\u09DF','\u09E1');
			break;
		}
		case '\u09f0':  case '\u09f1':
		{
			matchRange('\u09F0','\u09F1');
			break;
		}
		case '\u0a05':  case '\u0a06':  case '\u0a07':  case '\u0a08':
		case '\u0a09':  case '\u0a0a':
		{
			matchRange('\u0A05','\u0A0A');
			break;
		}
		case '\u0a0f':  case '\u0a10':
		{
			matchRange('\u0A0F','\u0A10');
			break;
		}
		case '\u0a13':  case '\u0a14':  case '\u0a15':  case '\u0a16':
		case '\u0a17':  case '\u0a18':  case '\u0a19':  case '\u0a1a':
		case '\u0a1b':  case '\u0a1c':  case '\u0a1d':  case '\u0a1e':
		case '\u0a1f':  case '\u0a20':  case '\u0a21':  case '\u0a22':
		case '\u0a23':  case '\u0a24':  case '\u0a25':  case '\u0a26':
		case '\u0a27':  case '\u0a28':
		{
			matchRange('\u0A13','\u0A28');
			break;
		}
		case '\u0a2a':  case '\u0a2b':  case '\u0a2c':  case '\u0a2d':
		case '\u0a2e':  case '\u0a2f':  case '\u0a30':
		{
			matchRange('\u0A2A','\u0A30');
			break;
		}
		case '\u0a32':  case '\u0a33':
		{
			matchRange('\u0A32','\u0A33');
			break;
		}
		case '\u0a35':  case '\u0a36':
		{
			matchRange('\u0A35','\u0A36');
			break;
		}
		case '\u0a38':  case '\u0a39':
		{
			matchRange('\u0A38','\u0A39');
			break;
		}
		case '\u0a59':  case '\u0a5a':  case '\u0a5b':  case '\u0a5c':
		{
			matchRange('\u0A59','\u0A5C');
			break;
		}
		case '\u0a5e':
		{
			matchRange('\u0A5E','\u0A5E');
			break;
		}
		case '\u0a72':  case '\u0a73':  case '\u0a74':
		{
			matchRange('\u0A72','\u0A74');
			break;
		}
		case '\u0a85':  case '\u0a86':  case '\u0a87':  case '\u0a88':
		case '\u0a89':  case '\u0a8a':  case '\u0a8b':
		{
			matchRange('\u0A85','\u0A8B');
			break;
		}
		case '\u0a8d':
		{
			matchRange('\u0A8D','\u0A8D');
			break;
		}
		case '\u0a8f':  case '\u0a90':  case '\u0a91':
		{
			matchRange('\u0A8F','\u0A91');
			break;
		}
		case '\u0a93':  case '\u0a94':  case '\u0a95':  case '\u0a96':
		case '\u0a97':  case '\u0a98':  case '\u0a99':  case '\u0a9a':
		case '\u0a9b':  case '\u0a9c':  case '\u0a9d':  case '\u0a9e':
		case '\u0a9f':  case '\u0aa0':  case '\u0aa1':  case '\u0aa2':
		case '\u0aa3':  case '\u0aa4':  case '\u0aa5':  case '\u0aa6':
		case '\u0aa7':  case '\u0aa8':
		{
			matchRange('\u0A93','\u0AA8');
			break;
		}
		case '\u0aaa':  case '\u0aab':  case '\u0aac':  case '\u0aad':
		case '\u0aae':  case '\u0aaf':  case '\u0ab0':
		{
			matchRange('\u0AAA','\u0AB0');
			break;
		}
		case '\u0ab2':  case '\u0ab3':
		{
			matchRange('\u0AB2','\u0AB3');
			break;
		}
		case '\u0ab5':  case '\u0ab6':  case '\u0ab7':  case '\u0ab8':
		case '\u0ab9':
		{
			matchRange('\u0AB5','\u0AB9');
			break;
		}
		case '\u0abd':
		{
			matchRange('\u0ABD','\u0ABD');
			break;
		}
		case '\u0ae0':
		{
			matchRange('\u0AE0','\u0AE0');
			break;
		}
		case '\u0b05':  case '\u0b06':  case '\u0b07':  case '\u0b08':
		case '\u0b09':  case '\u0b0a':  case '\u0b0b':  case '\u0b0c':
		{
			matchRange('\u0B05','\u0B0C');
			break;
		}
		case '\u0b0f':  case '\u0b10':
		{
			matchRange('\u0B0F','\u0B10');
			break;
		}
		case '\u0b13':  case '\u0b14':  case '\u0b15':  case '\u0b16':
		case '\u0b17':  case '\u0b18':  case '\u0b19':  case '\u0b1a':
		case '\u0b1b':  case '\u0b1c':  case '\u0b1d':  case '\u0b1e':
		case '\u0b1f':  case '\u0b20':  case '\u0b21':  case '\u0b22':
		case '\u0b23':  case '\u0b24':  case '\u0b25':  case '\u0b26':
		case '\u0b27':  case '\u0b28':
		{
			matchRange('\u0B13','\u0B28');
			break;
		}
		case '\u0b2a':  case '\u0b2b':  case '\u0b2c':  case '\u0b2d':
		case '\u0b2e':  case '\u0b2f':  case '\u0b30':
		{
			matchRange('\u0B2A','\u0B30');
			break;
		}
		case '\u0b32':  case '\u0b33':
		{
			matchRange('\u0B32','\u0B33');
			break;
		}
		case '\u0b36':  case '\u0b37':  case '\u0b38':  case '\u0b39':
		{
			matchRange('\u0B36','\u0B39');
			break;
		}
		case '\u0b3d':
		{
			matchRange('\u0B3D','\u0B3D');
			break;
		}
		case '\u0b5c':  case '\u0b5d':
		{
			matchRange('\u0B5C','\u0B5D');
			break;
		}
		case '\u0b5f':  case '\u0b60':  case '\u0b61':
		{
			matchRange('\u0B5F','\u0B61');
			break;
		}
		case '\u0b85':  case '\u0b86':  case '\u0b87':  case '\u0b88':
		case '\u0b89':  case '\u0b8a':
		{
			matchRange('\u0B85','\u0B8A');
			break;
		}
		case '\u0b8e':  case '\u0b8f':  case '\u0b90':
		{
			matchRange('\u0B8E','\u0B90');
			break;
		}
		case '\u0b92':  case '\u0b93':  case '\u0b94':  case '\u0b95':
		{
			matchRange('\u0B92','\u0B95');
			break;
		}
		case '\u0b99':  case '\u0b9a':
		{
			matchRange('\u0B99','\u0B9A');
			break;
		}
		case '\u0b9c':
		{
			matchRange('\u0B9C','\u0B9C');
			break;
		}
		case '\u0b9e':  case '\u0b9f':
		{
			matchRange('\u0B9E','\u0B9F');
			break;
		}
		case '\u0ba3':  case '\u0ba4':
		{
			matchRange('\u0BA3','\u0BA4');
			break;
		}
		case '\u0ba8':  case '\u0ba9':  case '\u0baa':
		{
			matchRange('\u0BA8','\u0BAA');
			break;
		}
		case '\u0bae':  case '\u0baf':  case '\u0bb0':  case '\u0bb1':
		case '\u0bb2':  case '\u0bb3':  case '\u0bb4':  case '\u0bb5':
		{
			matchRange('\u0BAE','\u0BB5');
			break;
		}
		case '\u0bb7':  case '\u0bb8':  case '\u0bb9':
		{
			matchRange('\u0BB7','\u0BB9');
			break;
		}
		case '\u0c05':  case '\u0c06':  case '\u0c07':  case '\u0c08':
		case '\u0c09':  case '\u0c0a':  case '\u0c0b':  case '\u0c0c':
		{
			matchRange('\u0C05','\u0C0C');
			break;
		}
		case '\u0c0e':  case '\u0c0f':  case '\u0c10':
		{
			matchRange('\u0C0E','\u0C10');
			break;
		}
		case '\u0c12':  case '\u0c13':  case '\u0c14':  case '\u0c15':
		case '\u0c16':  case '\u0c17':  case '\u0c18':  case '\u0c19':
		case '\u0c1a':  case '\u0c1b':  case '\u0c1c':  case '\u0c1d':
		case '\u0c1e':  case '\u0c1f':  case '\u0c20':  case '\u0c21':
		case '\u0c22':  case '\u0c23':  case '\u0c24':  case '\u0c25':
		case '\u0c26':  case '\u0c27':  case '\u0c28':
		{
			matchRange('\u0C12','\u0C28');
			break;
		}
		case '\u0c2a':  case '\u0c2b':  case '\u0c2c':  case '\u0c2d':
		case '\u0c2e':  case '\u0c2f':  case '\u0c30':  case '\u0c31':
		case '\u0c32':  case '\u0c33':
		{
			matchRange('\u0C2A','\u0C33');
			break;
		}
		case '\u0c35':  case '\u0c36':  case '\u0c37':  case '\u0c38':
		case '\u0c39':
		{
			matchRange('\u0C35','\u0C39');
			break;
		}
		case '\u0c60':  case '\u0c61':
		{
			matchRange('\u0C60','\u0C61');
			break;
		}
		case '\u0c85':  case '\u0c86':  case '\u0c87':  case '\u0c88':
		case '\u0c89':  case '\u0c8a':  case '\u0c8b':  case '\u0c8c':
		{
			matchRange('\u0C85','\u0C8C');
			break;
		}
		case '\u0c8e':  case '\u0c8f':  case '\u0c90':
		{
			matchRange('\u0C8E','\u0C90');
			break;
		}
		case '\u0c92':  case '\u0c93':  case '\u0c94':  case '\u0c95':
		case '\u0c96':  case '\u0c97':  case '\u0c98':  case '\u0c99':
		case '\u0c9a':  case '\u0c9b':  case '\u0c9c':  case '\u0c9d':
		case '\u0c9e':  case '\u0c9f':  case '\u0ca0':  case '\u0ca1':
		case '\u0ca2':  case '\u0ca3':  case '\u0ca4':  case '\u0ca5':
		case '\u0ca6':  case '\u0ca7':  case '\u0ca8':
		{
			matchRange('\u0C92','\u0CA8');
			break;
		}
		case '\u0caa':  case '\u0cab':  case '\u0cac':  case '\u0cad':
		case '\u0cae':  case '\u0caf':  case '\u0cb0':  case '\u0cb1':
		case '\u0cb2':  case '\u0cb3':
		{
			matchRange('\u0CAA','\u0CB3');
			break;
		}
		case '\u0cb5':  case '\u0cb6':  case '\u0cb7':  case '\u0cb8':
		case '\u0cb9':
		{
			matchRange('\u0CB5','\u0CB9');
			break;
		}
		case '\u0cde':
		{
			matchRange('\u0CDE','\u0CDE');
			break;
		}
		case '\u0ce0':  case '\u0ce1':
		{
			matchRange('\u0CE0','\u0CE1');
			break;
		}
		case '\u0d05':  case '\u0d06':  case '\u0d07':  case '\u0d08':
		case '\u0d09':  case '\u0d0a':  case '\u0d0b':  case '\u0d0c':
		{
			matchRange('\u0D05','\u0D0C');
			break;
		}
		case '\u0d0e':  case '\u0d0f':  case '\u0d10':
		{
			matchRange('\u0D0E','\u0D10');
			break;
		}
		case '\u0d12':  case '\u0d13':  case '\u0d14':  case '\u0d15':
		case '\u0d16':  case '\u0d17':  case '\u0d18':  case '\u0d19':
		case '\u0d1a':  case '\u0d1b':  case '\u0d1c':  case '\u0d1d':
		case '\u0d1e':  case '\u0d1f':  case '\u0d20':  case '\u0d21':
		case '\u0d22':  case '\u0d23':  case '\u0d24':  case '\u0d25':
		case '\u0d26':  case '\u0d27':  case '\u0d28':
		{
			matchRange('\u0D12','\u0D28');
			break;
		}
		case '\u0d2a':  case '\u0d2b':  case '\u0d2c':  case '\u0d2d':
		case '\u0d2e':  case '\u0d2f':  case '\u0d30':  case '\u0d31':
		case '\u0d32':  case '\u0d33':  case '\u0d34':  case '\u0d35':
		case '\u0d36':  case '\u0d37':  case '\u0d38':  case '\u0d39':
		{
			matchRange('\u0D2A','\u0D39');
			break;
		}
		case '\u0d60':  case '\u0d61':
		{
			matchRange('\u0D60','\u0D61');
			break;
		}
		case '\u0e01':  case '\u0e02':  case '\u0e03':  case '\u0e04':
		case '\u0e05':  case '\u0e06':  case '\u0e07':  case '\u0e08':
		case '\u0e09':  case '\u0e0a':  case '\u0e0b':  case '\u0e0c':
		case '\u0e0d':  case '\u0e0e':  case '\u0e0f':  case '\u0e10':
		case '\u0e11':  case '\u0e12':  case '\u0e13':  case '\u0e14':
		case '\u0e15':  case '\u0e16':  case '\u0e17':  case '\u0e18':
		case '\u0e19':  case '\u0e1a':  case '\u0e1b':  case '\u0e1c':
		case '\u0e1d':  case '\u0e1e':  case '\u0e1f':  case '\u0e20':
		case '\u0e21':  case '\u0e22':  case '\u0e23':  case '\u0e24':
		case '\u0e25':  case '\u0e26':  case '\u0e27':  case '\u0e28':
		case '\u0e29':  case '\u0e2a':  case '\u0e2b':  case '\u0e2c':
		case '\u0e2d':  case '\u0e2e':
		{
			matchRange('\u0E01','\u0E2E');
			break;
		}
		case '\u0e30':
		{
			matchRange('\u0E30','\u0E30');
			break;
		}
		case '\u0e32':  case '\u0e33':
		{
			matchRange('\u0E32','\u0E33');
			break;
		}
		case '\u0e40':  case '\u0e41':  case '\u0e42':  case '\u0e43':
		case '\u0e44':  case '\u0e45':  case '\u0e46':
		{
			matchRange('\u0E40','\u0E46');
			break;
		}
		case '\u0e81':  case '\u0e82':
		{
			matchRange('\u0E81','\u0E82');
			break;
		}
		case '\u0e84':
		{
			matchRange('\u0E84','\u0E84');
			break;
		}
		case '\u0e87':  case '\u0e88':
		{
			matchRange('\u0E87','\u0E88');
			break;
		}
		case '\u0e8a':
		{
			matchRange('\u0E8A','\u0E8A');
			break;
		}
		case '\u0e8d':
		{
			matchRange('\u0E8D','\u0E8D');
			break;
		}
		case '\u0e94':  case '\u0e95':  case '\u0e96':  case '\u0e97':
		{
			matchRange('\u0E94','\u0E97');
			break;
		}
		case '\u0e99':  case '\u0e9a':  case '\u0e9b':  case '\u0e9c':
		case '\u0e9d':  case '\u0e9e':  case '\u0e9f':
		{
			matchRange('\u0E99','\u0E9F');
			break;
		}
		case '\u0ea1':  case '\u0ea2':  case '\u0ea3':
		{
			matchRange('\u0EA1','\u0EA3');
			break;
		}
		case '\u0ea5':
		{
			matchRange('\u0EA5','\u0EA5');
			break;
		}
		case '\u0ea7':
		{
			matchRange('\u0EA7','\u0EA7');
			break;
		}
		case '\u0eaa':  case '\u0eab':
		{
			matchRange('\u0EAA','\u0EAB');
			break;
		}
		case '\u0ead':  case '\u0eae':
		{
			matchRange('\u0EAD','\u0EAE');
			break;
		}
		case '\u0eb0':
		{
			matchRange('\u0EB0','\u0EB0');
			break;
		}
		case '\u0eb2':  case '\u0eb3':
		{
			matchRange('\u0EB2','\u0EB3');
			break;
		}
		case '\u0ebd':
		{
			matchRange('\u0EBD','\u0EBD');
			break;
		}
		case '\u0ec0':  case '\u0ec1':  case '\u0ec2':  case '\u0ec3':
		case '\u0ec4':
		{
			matchRange('\u0EC0','\u0EC4');
			break;
		}
		case '\u0ec6':
		{
			matchRange('\u0EC6','\u0EC6');
			break;
		}
		case '\u0edc':  case '\u0edd':
		{
			matchRange('\u0EDC','\u0EDD');
			break;
		}
		case '\u0f40':  case '\u0f41':  case '\u0f42':  case '\u0f43':
		case '\u0f44':  case '\u0f45':  case '\u0f46':  case '\u0f47':
		{
			matchRange('\u0F40','\u0F47');
			break;
		}
		case '\u0f49':  case '\u0f4a':  case '\u0f4b':  case '\u0f4c':
		case '\u0f4d':  case '\u0f4e':  case '\u0f4f':  case '\u0f50':
		case '\u0f51':  case '\u0f52':  case '\u0f53':  case '\u0f54':
		case '\u0f55':  case '\u0f56':  case '\u0f57':  case '\u0f58':
		case '\u0f59':  case '\u0f5a':  case '\u0f5b':  case '\u0f5c':
		case '\u0f5d':  case '\u0f5e':  case '\u0f5f':  case '\u0f60':
		case '\u0f61':  case '\u0f62':  case '\u0f63':  case '\u0f64':
		case '\u0f65':  case '\u0f66':  case '\u0f67':  case '\u0f68':
		case '\u0f69':
		{
			matchRange('\u0F49','\u0F69');
			break;
		}
		case '\u10a0':  case '\u10a1':  case '\u10a2':  case '\u10a3':
		case '\u10a4':  case '\u10a5':  case '\u10a6':  case '\u10a7':
		case '\u10a8':  case '\u10a9':  case '\u10aa':  case '\u10ab':
		case '\u10ac':  case '\u10ad':  case '\u10ae':  case '\u10af':
		case '\u10b0':  case '\u10b1':  case '\u10b2':  case '\u10b3':
		case '\u10b4':  case '\u10b5':  case '\u10b6':  case '\u10b7':
		case '\u10b8':  case '\u10b9':  case '\u10ba':  case '\u10bb':
		case '\u10bc':  case '\u10bd':  case '\u10be':  case '\u10bf':
		case '\u10c0':  case '\u10c1':  case '\u10c2':  case '\u10c3':
		case '\u10c4':  case '\u10c5':
		{
			matchRange('\u10A0','\u10C5');
			break;
		}
		case '\u10d0':  case '\u10d1':  case '\u10d2':  case '\u10d3':
		case '\u10d4':  case '\u10d5':  case '\u10d6':  case '\u10d7':
		case '\u10d8':  case '\u10d9':  case '\u10da':  case '\u10db':
		case '\u10dc':  case '\u10dd':  case '\u10de':  case '\u10df':
		case '\u10e0':  case '\u10e1':  case '\u10e2':  case '\u10e3':
		case '\u10e4':  case '\u10e5':  case '\u10e6':  case '\u10e7':
		case '\u10e8':  case '\u10e9':  case '\u10ea':  case '\u10eb':
		case '\u10ec':  case '\u10ed':  case '\u10ee':  case '\u10ef':
		case '\u10f0':  case '\u10f1':  case '\u10f2':  case '\u10f3':
		case '\u10f4':  case '\u10f5':  case '\u10f6':
		{
			matchRange('\u10D0','\u10F6');
			break;
		}
		case '\u1100':  case '\u1101':  case '\u1102':  case '\u1103':
		case '\u1104':  case '\u1105':  case '\u1106':  case '\u1107':
		case '\u1108':  case '\u1109':  case '\u110a':  case '\u110b':
		case '\u110c':  case '\u110d':  case '\u110e':  case '\u110f':
		case '\u1110':  case '\u1111':  case '\u1112':  case '\u1113':
		case '\u1114':  case '\u1115':  case '\u1116':  case '\u1117':
		case '\u1118':  case '\u1119':  case '\u111a':  case '\u111b':
		case '\u111c':  case '\u111d':  case '\u111e':  case '\u111f':
		case '\u1120':  case '\u1121':  case '\u1122':  case '\u1123':
		case '\u1124':  case '\u1125':  case '\u1126':  case '\u1127':
		case '\u1128':  case '\u1129':  case '\u112a':  case '\u112b':
		case '\u112c':  case '\u112d':  case '\u112e':  case '\u112f':
		case '\u1130':  case '\u1131':  case '\u1132':  case '\u1133':
		case '\u1134':  case '\u1135':  case '\u1136':  case '\u1137':
		case '\u1138':  case '\u1139':  case '\u113a':  case '\u113b':
		case '\u113c':  case '\u113d':  case '\u113e':  case '\u113f':
		case '\u1140':  case '\u1141':  case '\u1142':  case '\u1143':
		case '\u1144':  case '\u1145':  case '\u1146':  case '\u1147':
		case '\u1148':  case '\u1149':  case '\u114a':  case '\u114b':
		case '\u114c':  case '\u114d':  case '\u114e':  case '\u114f':
		case '\u1150':  case '\u1151':  case '\u1152':  case '\u1153':
		case '\u1154':  case '\u1155':  case '\u1156':  case '\u1157':
		case '\u1158':  case '\u1159':
		{
			matchRange('\u1100','\u1159');
			break;
		}
		case '\u115f':  case '\u1160':  case '\u1161':  case '\u1162':
		case '\u1163':  case '\u1164':  case '\u1165':  case '\u1166':
		case '\u1167':  case '\u1168':  case '\u1169':  case '\u116a':
		case '\u116b':  case '\u116c':  case '\u116d':  case '\u116e':
		case '\u116f':  case '\u1170':  case '\u1171':  case '\u1172':
		case '\u1173':  case '\u1174':  case '\u1175':  case '\u1176':
		case '\u1177':  case '\u1178':  case '\u1179':  case '\u117a':
		case '\u117b':  case '\u117c':  case '\u117d':  case '\u117e':
		case '\u117f':  case '\u1180':  case '\u1181':  case '\u1182':
		case '\u1183':  case '\u1184':  case '\u1185':  case '\u1186':
		case '\u1187':  case '\u1188':  case '\u1189':  case '\u118a':
		case '\u118b':  case '\u118c':  case '\u118d':  case '\u118e':
		case '\u118f':  case '\u1190':  case '\u1191':  case '\u1192':
		case '\u1193':  case '\u1194':  case '\u1195':  case '\u1196':
		case '\u1197':  case '\u1198':  case '\u1199':  case '\u119a':
		case '\u119b':  case '\u119c':  case '\u119d':  case '\u119e':
		case '\u119f':  case '\u11a0':  case '\u11a1':  case '\u11a2':
		{
			matchRange('\u115F','\u11A2');
			break;
		}
		case '\u11a8':  case '\u11a9':  case '\u11aa':  case '\u11ab':
		case '\u11ac':  case '\u11ad':  case '\u11ae':  case '\u11af':
		case '\u11b0':  case '\u11b1':  case '\u11b2':  case '\u11b3':
		case '\u11b4':  case '\u11b5':  case '\u11b6':  case '\u11b7':
		case '\u11b8':  case '\u11b9':  case '\u11ba':  case '\u11bb':
		case '\u11bc':  case '\u11bd':  case '\u11be':  case '\u11bf':
		case '\u11c0':  case '\u11c1':  case '\u11c2':  case '\u11c3':
		case '\u11c4':  case '\u11c5':  case '\u11c6':  case '\u11c7':
		case '\u11c8':  case '\u11c9':  case '\u11ca':  case '\u11cb':
		case '\u11cc':  case '\u11cd':  case '\u11ce':  case '\u11cf':
		case '\u11d0':  case '\u11d1':  case '\u11d2':  case '\u11d3':
		case '\u11d4':  case '\u11d5':  case '\u11d6':  case '\u11d7':
		case '\u11d8':  case '\u11d9':  case '\u11da':  case '\u11db':
		case '\u11dc':  case '\u11dd':  case '\u11de':  case '\u11df':
		case '\u11e0':  case '\u11e1':  case '\u11e2':  case '\u11e3':
		case '\u11e4':  case '\u11e5':  case '\u11e6':  case '\u11e7':
		case '\u11e8':  case '\u11e9':  case '\u11ea':  case '\u11eb':
		case '\u11ec':  case '\u11ed':  case '\u11ee':  case '\u11ef':
		case '\u11f0':  case '\u11f1':  case '\u11f2':  case '\u11f3':
		case '\u11f4':  case '\u11f5':  case '\u11f6':  case '\u11f7':
		case '\u11f8':  case '\u11f9':
		{
			matchRange('\u11A8','\u11F9');
			break;
		}
		case '\u1ea0':  case '\u1ea1':  case '\u1ea2':  case '\u1ea3':
		case '\u1ea4':  case '\u1ea5':  case '\u1ea6':  case '\u1ea7':
		case '\u1ea8':  case '\u1ea9':  case '\u1eaa':  case '\u1eab':
		case '\u1eac':  case '\u1ead':  case '\u1eae':  case '\u1eaf':
		case '\u1eb0':  case '\u1eb1':  case '\u1eb2':  case '\u1eb3':
		case '\u1eb4':  case '\u1eb5':  case '\u1eb6':  case '\u1eb7':
		case '\u1eb8':  case '\u1eb9':  case '\u1eba':  case '\u1ebb':
		case '\u1ebc':  case '\u1ebd':  case '\u1ebe':  case '\u1ebf':
		case '\u1ec0':  case '\u1ec1':  case '\u1ec2':  case '\u1ec3':
		case '\u1ec4':  case '\u1ec5':  case '\u1ec6':  case '\u1ec7':
		case '\u1ec8':  case '\u1ec9':  case '\u1eca':  case '\u1ecb':
		case '\u1ecc':  case '\u1ecd':  case '\u1ece':  case '\u1ecf':
		case '\u1ed0':  case '\u1ed1':  case '\u1ed2':  case '\u1ed3':
		case '\u1ed4':  case '\u1ed5':  case '\u1ed6':  case '\u1ed7':
		case '\u1ed8':  case '\u1ed9':  case '\u1eda':  case '\u1edb':
		case '\u1edc':  case '\u1edd':  case '\u1ede':  case '\u1edf':
		case '\u1ee0':  case '\u1ee1':  case '\u1ee2':  case '\u1ee3':
		case '\u1ee4':  case '\u1ee5':  case '\u1ee6':  case '\u1ee7':
		case '\u1ee8':  case '\u1ee9':  case '\u1eea':  case '\u1eeb':
		case '\u1eec':  case '\u1eed':  case '\u1eee':  case '\u1eef':
		case '\u1ef0':  case '\u1ef1':  case '\u1ef2':  case '\u1ef3':
		case '\u1ef4':  case '\u1ef5':  case '\u1ef6':  case '\u1ef7':
		case '\u1ef8':  case '\u1ef9':
		{
			matchRange('\u1EA0','\u1EF9');
			break;
		}
		case '\u1f00':  case '\u1f01':  case '\u1f02':  case '\u1f03':
		case '\u1f04':  case '\u1f05':  case '\u1f06':  case '\u1f07':
		case '\u1f08':  case '\u1f09':  case '\u1f0a':  case '\u1f0b':
		case '\u1f0c':  case '\u1f0d':  case '\u1f0e':  case '\u1f0f':
		case '\u1f10':  case '\u1f11':  case '\u1f12':  case '\u1f13':
		case '\u1f14':  case '\u1f15':
		{
			matchRange('\u1F00','\u1F15');
			break;
		}
		case '\u1f18':  case '\u1f19':  case '\u1f1a':  case '\u1f1b':
		case '\u1f1c':  case '\u1f1d':
		{
			matchRange('\u1F18','\u1F1D');
			break;
		}
		case '\u1f20':  case '\u1f21':  case '\u1f22':  case '\u1f23':
		case '\u1f24':  case '\u1f25':  case '\u1f26':  case '\u1f27':
		case '\u1f28':  case '\u1f29':  case '\u1f2a':  case '\u1f2b':
		case '\u1f2c':  case '\u1f2d':  case '\u1f2e':  case '\u1f2f':
		case '\u1f30':  case '\u1f31':  case '\u1f32':  case '\u1f33':
		case '\u1f34':  case '\u1f35':  case '\u1f36':  case '\u1f37':
		case '\u1f38':  case '\u1f39':  case '\u1f3a':  case '\u1f3b':
		case '\u1f3c':  case '\u1f3d':  case '\u1f3e':  case '\u1f3f':
		case '\u1f40':  case '\u1f41':  case '\u1f42':  case '\u1f43':
		case '\u1f44':  case '\u1f45':
		{
			matchRange('\u1F20','\u1F45');
			break;
		}
		case '\u1f48':  case '\u1f49':  case '\u1f4a':  case '\u1f4b':
		case '\u1f4c':  case '\u1f4d':
		{
			matchRange('\u1F48','\u1F4D');
			break;
		}
		case '\u1f50':  case '\u1f51':  case '\u1f52':  case '\u1f53':
		case '\u1f54':  case '\u1f55':  case '\u1f56':  case '\u1f57':
		{
			matchRange('\u1F50','\u1F57');
			break;
		}
		case '\u1f59':
		{
			matchRange('\u1F59','\u1F59');
			break;
		}
		case '\u1f5b':
		{
			matchRange('\u1F5B','\u1F5B');
			break;
		}
		case '\u1f5d':
		{
			matchRange('\u1F5D','\u1F5D');
			break;
		}
		case '\u1f5f':  case '\u1f60':  case '\u1f61':  case '\u1f62':
		case '\u1f63':  case '\u1f64':  case '\u1f65':  case '\u1f66':
		case '\u1f67':  case '\u1f68':  case '\u1f69':  case '\u1f6a':
		case '\u1f6b':  case '\u1f6c':  case '\u1f6d':  case '\u1f6e':
		case '\u1f6f':  case '\u1f70':  case '\u1f71':  case '\u1f72':
		case '\u1f73':  case '\u1f74':  case '\u1f75':  case '\u1f76':
		case '\u1f77':  case '\u1f78':  case '\u1f79':  case '\u1f7a':
		case '\u1f7b':  case '\u1f7c':  case '\u1f7d':
		{
			matchRange('\u1F5F','\u1F7D');
			break;
		}
		case '\u1f80':  case '\u1f81':  case '\u1f82':  case '\u1f83':
		case '\u1f84':  case '\u1f85':  case '\u1f86':  case '\u1f87':
		case '\u1f88':  case '\u1f89':  case '\u1f8a':  case '\u1f8b':
		case '\u1f8c':  case '\u1f8d':  case '\u1f8e':  case '\u1f8f':
		case '\u1f90':  case '\u1f91':  case '\u1f92':  case '\u1f93':
		case '\u1f94':  case '\u1f95':  case '\u1f96':  case '\u1f97':
		case '\u1f98':  case '\u1f99':  case '\u1f9a':  case '\u1f9b':
		case '\u1f9c':  case '\u1f9d':  case '\u1f9e':  case '\u1f9f':
		case '\u1fa0':  case '\u1fa1':  case '\u1fa2':  case '\u1fa3':
		case '\u1fa4':  case '\u1fa5':  case '\u1fa6':  case '\u1fa7':
		case '\u1fa8':  case '\u1fa9':  case '\u1faa':  case '\u1fab':
		case '\u1fac':  case '\u1fad':  case '\u1fae':  case '\u1faf':
		case '\u1fb0':  case '\u1fb1':  case '\u1fb2':  case '\u1fb3':
		case '\u1fb4':
		{
			matchRange('\u1F80','\u1FB4');
			break;
		}
		case '\u1fb6':  case '\u1fb7':  case '\u1fb8':  case '\u1fb9':
		case '\u1fba':  case '\u1fbb':  case '\u1fbc':
		{
			matchRange('\u1FB6','\u1FBC');
			break;
		}
		case '\u1fbe':
		{
			matchRange('\u1FBE','\u1FBE');
			break;
		}
		case '\u1fc2':  case '\u1fc3':  case '\u1fc4':
		{
			matchRange('\u1FC2','\u1FC4');
			break;
		}
		case '\u1fc6':  case '\u1fc7':  case '\u1fc8':  case '\u1fc9':
		case '\u1fca':  case '\u1fcb':  case '\u1fcc':
		{
			matchRange('\u1FC6','\u1FCC');
			break;
		}
		case '\u1fd0':  case '\u1fd1':  case '\u1fd2':  case '\u1fd3':
		{
			matchRange('\u1FD0','\u1FD3');
			break;
		}
		case '\u1fd6':  case '\u1fd7':  case '\u1fd8':  case '\u1fd9':
		case '\u1fda':  case '\u1fdb':
		{
			matchRange('\u1FD6','\u1FDB');
			break;
		}
		case '\u1fe0':  case '\u1fe1':  case '\u1fe2':  case '\u1fe3':
		case '\u1fe4':  case '\u1fe5':  case '\u1fe6':  case '\u1fe7':
		case '\u1fe8':  case '\u1fe9':  case '\u1fea':  case '\u1feb':
		case '\u1fec':
		{
			matchRange('\u1FE0','\u1FEC');
			break;
		}
		case '\u1ff2':  case '\u1ff3':  case '\u1ff4':
		{
			matchRange('\u1FF2','\u1FF4');
			break;
		}
		case '\u1ff6':  case '\u1ff7':  case '\u1ff8':  case '\u1ff9':
		case '\u1ffa':  case '\u1ffb':  case '\u1ffc':
		{
			matchRange('\u1FF6','\u1FFC');
			break;
		}
		case '\u207f':
		{
			matchRange('\u207F','\u207F');
			break;
		}
		case '\u2102':
		{
			matchRange('\u2102','\u2102');
			break;
		}
		case '\u2107':
		{
			matchRange('\u2107','\u2107');
			break;
		}
		case '\u210a':  case '\u210b':  case '\u210c':  case '\u210d':
		case '\u210e':  case '\u210f':  case '\u2110':  case '\u2111':
		case '\u2112':  case '\u2113':
		{
			matchRange('\u210A','\u2113');
			break;
		}
		case '\u2115':
		{
			matchRange('\u2115','\u2115');
			break;
		}
		case '\u2118':  case '\u2119':  case '\u211a':  case '\u211b':
		case '\u211c':  case '\u211d':
		{
			matchRange('\u2118','\u211D');
			break;
		}
		case '\u2124':
		{
			matchRange('\u2124','\u2124');
			break;
		}
		case '\u2126':
		{
			matchRange('\u2126','\u2126');
			break;
		}
		case '\u2128':
		{
			matchRange('\u2128','\u2128');
			break;
		}
		case '\u212a':  case '\u212b':  case '\u212c':  case '\u212d':
		case '\u212e':  case '\u212f':  case '\u2130':  case '\u2131':
		{
			matchRange('\u212A','\u2131');
			break;
		}
		case '\u2133':  case '\u2134':  case '\u2135':  case '\u2136':
		case '\u2137':  case '\u2138':
		{
			matchRange('\u2133','\u2138');
			break;
		}
		case '\u3005':
		{
			matchRange('\u3005','\u3005');
			break;
		}
		case '\u3031':  case '\u3032':  case '\u3033':  case '\u3034':
		case '\u3035':
		{
			matchRange('\u3031','\u3035');
			break;
		}
		case '\u3041':  case '\u3042':  case '\u3043':  case '\u3044':
		case '\u3045':  case '\u3046':  case '\u3047':  case '\u3048':
		case '\u3049':  case '\u304a':  case '\u304b':  case '\u304c':
		case '\u304d':  case '\u304e':  case '\u304f':  case '\u3050':
		case '\u3051':  case '\u3052':  case '\u3053':  case '\u3054':
		case '\u3055':  case '\u3056':  case '\u3057':  case '\u3058':
		case '\u3059':  case '\u305a':  case '\u305b':  case '\u305c':
		case '\u305d':  case '\u305e':  case '\u305f':  case '\u3060':
		case '\u3061':  case '\u3062':  case '\u3063':  case '\u3064':
		case '\u3065':  case '\u3066':  case '\u3067':  case '\u3068':
		case '\u3069':  case '\u306a':  case '\u306b':  case '\u306c':
		case '\u306d':  case '\u306e':  case '\u306f':  case '\u3070':
		case '\u3071':  case '\u3072':  case '\u3073':  case '\u3074':
		case '\u3075':  case '\u3076':  case '\u3077':  case '\u3078':
		case '\u3079':  case '\u307a':  case '\u307b':  case '\u307c':
		case '\u307d':  case '\u307e':  case '\u307f':  case '\u3080':
		case '\u3081':  case '\u3082':  case '\u3083':  case '\u3084':
		case '\u3085':  case '\u3086':  case '\u3087':  case '\u3088':
		case '\u3089':  case '\u308a':  case '\u308b':  case '\u308c':
		case '\u308d':  case '\u308e':  case '\u308f':  case '\u3090':
		case '\u3091':  case '\u3092':  case '\u3093':  case '\u3094':
		{
			matchRange('\u3041','\u3094');
			break;
		}
		case '\u309b':  case '\u309c':  case '\u309d':  case '\u309e':
		{
			matchRange('\u309B','\u309E');
			break;
		}
		case '\u30a1':  case '\u30a2':  case '\u30a3':  case '\u30a4':
		case '\u30a5':  case '\u30a6':  case '\u30a7':  case '\u30a8':
		case '\u30a9':  case '\u30aa':  case '\u30ab':  case '\u30ac':
		case '\u30ad':  case '\u30ae':  case '\u30af':  case '\u30b0':
		case '\u30b1':  case '\u30b2':  case '\u30b3':  case '\u30b4':
		case '\u30b5':  case '\u30b6':  case '\u30b7':  case '\u30b8':
		case '\u30b9':  case '\u30ba':  case '\u30bb':  case '\u30bc':
		case '\u30bd':  case '\u30be':  case '\u30bf':  case '\u30c0':
		case '\u30c1':  case '\u30c2':  case '\u30c3':  case '\u30c4':
		case '\u30c5':  case '\u30c6':  case '\u30c7':  case '\u30c8':
		case '\u30c9':  case '\u30ca':  case '\u30cb':  case '\u30cc':
		case '\u30cd':  case '\u30ce':  case '\u30cf':  case '\u30d0':
		case '\u30d1':  case '\u30d2':  case '\u30d3':  case '\u30d4':
		case '\u30d5':  case '\u30d6':  case '\u30d7':  case '\u30d8':
		case '\u30d9':  case '\u30da':  case '\u30db':  case '\u30dc':
		case '\u30dd':  case '\u30de':  case '\u30df':  case '\u30e0':
		case '\u30e1':  case '\u30e2':  case '\u30e3':  case '\u30e4':
		case '\u30e5':  case '\u30e6':  case '\u30e7':  case '\u30e8':
		case '\u30e9':  case '\u30ea':  case '\u30eb':  case '\u30ec':
		case '\u30ed':  case '\u30ee':  case '\u30ef':  case '\u30f0':
		case '\u30f1':  case '\u30f2':  case '\u30f3':  case '\u30f4':
		case '\u30f5':  case '\u30f6':  case '\u30f7':  case '\u30f8':
		case '\u30f9':  case '\u30fa':
		{
			matchRange('\u30A1','\u30FA');
			break;
		}
		case '\u30fc':  case '\u30fd':  case '\u30fe':
		{
			matchRange('\u30FC','\u30FE');
			break;
		}
		case '\u3105':  case '\u3106':  case '\u3107':  case '\u3108':
		case '\u3109':  case '\u310a':  case '\u310b':  case '\u310c':
		case '\u310d':  case '\u310e':  case '\u310f':  case '\u3110':
		case '\u3111':  case '\u3112':  case '\u3113':  case '\u3114':
		case '\u3115':  case '\u3116':  case '\u3117':  case '\u3118':
		case '\u3119':  case '\u311a':  case '\u311b':  case '\u311c':
		case '\u311d':  case '\u311e':  case '\u311f':  case '\u3120':
		case '\u3121':  case '\u3122':  case '\u3123':  case '\u3124':
		case '\u3125':  case '\u3126':  case '\u3127':  case '\u3128':
		case '\u3129':  case '\u312a':  case '\u312b':  case '\u312c':
		{
			matchRange('\u3105','\u312C');
			break;
		}
		case '\u3131':  case '\u3132':  case '\u3133':  case '\u3134':
		case '\u3135':  case '\u3136':  case '\u3137':  case '\u3138':
		case '\u3139':  case '\u313a':  case '\u313b':  case '\u313c':
		case '\u313d':  case '\u313e':  case '\u313f':  case '\u3140':
		case '\u3141':  case '\u3142':  case '\u3143':  case '\u3144':
		case '\u3145':  case '\u3146':  case '\u3147':  case '\u3148':
		case '\u3149':  case '\u314a':  case '\u314b':  case '\u314c':
		case '\u314d':  case '\u314e':  case '\u314f':  case '\u3150':
		case '\u3151':  case '\u3152':  case '\u3153':  case '\u3154':
		case '\u3155':  case '\u3156':  case '\u3157':  case '\u3158':
		case '\u3159':  case '\u315a':  case '\u315b':  case '\u315c':
		case '\u315d':  case '\u315e':  case '\u315f':  case '\u3160':
		case '\u3161':  case '\u3162':  case '\u3163':  case '\u3164':
		case '\u3165':  case '\u3166':  case '\u3167':  case '\u3168':
		case '\u3169':  case '\u316a':  case '\u316b':  case '\u316c':
		case '\u316d':  case '\u316e':  case '\u316f':  case '\u3170':
		case '\u3171':  case '\u3172':  case '\u3173':  case '\u3174':
		case '\u3175':  case '\u3176':  case '\u3177':  case '\u3178':
		case '\u3179':  case '\u317a':  case '\u317b':  case '\u317c':
		case '\u317d':  case '\u317e':  case '\u317f':  case '\u3180':
		case '\u3181':  case '\u3182':  case '\u3183':  case '\u3184':
		case '\u3185':  case '\u3186':  case '\u3187':  case '\u3188':
		case '\u3189':  case '\u318a':  case '\u318b':  case '\u318c':
		case '\u318d':  case '\u318e':
		{
			matchRange('\u3131','\u318E');
			break;
		}
		case '\ufb00':  case '\ufb01':  case '\ufb02':  case '\ufb03':
		case '\ufb04':  case '\ufb05':  case '\ufb06':
		{
			matchRange('\uFB00','\uFB06');
			break;
		}
		case '\ufb13':  case '\ufb14':  case '\ufb15':  case '\ufb16':
		case '\ufb17':
		{
			matchRange('\uFB13','\uFB17');
			break;
		}
		case '\ufb1f':  case '\ufb20':  case '\ufb21':  case '\ufb22':
		case '\ufb23':  case '\ufb24':  case '\ufb25':  case '\ufb26':
		case '\ufb27':  case '\ufb28':
		{
			matchRange('\uFB1F','\uFB28');
			break;
		}
		case '\ufb2a':  case '\ufb2b':  case '\ufb2c':  case '\ufb2d':
		case '\ufb2e':  case '\ufb2f':  case '\ufb30':  case '\ufb31':
		case '\ufb32':  case '\ufb33':  case '\ufb34':  case '\ufb35':
		case '\ufb36':
		{
			matchRange('\uFB2A','\uFB36');
			break;
		}
		case '\ufb38':  case '\ufb39':  case '\ufb3a':  case '\ufb3b':
		case '\ufb3c':
		{
			matchRange('\uFB38','\uFB3C');
			break;
		}
		case '\ufb3e':
		{
			matchRange('\uFB3E','\uFB3E');
			break;
		}
		case '\ufb40':  case '\ufb41':
		{
			matchRange('\uFB40','\uFB41');
			break;
		}
		case '\ufb43':  case '\ufb44':
		{
			matchRange('\uFB43','\uFB44');
			break;
		}
		case '\ufb46':  case '\ufb47':  case '\ufb48':  case '\ufb49':
		case '\ufb4a':  case '\ufb4b':  case '\ufb4c':  case '\ufb4d':
		case '\ufb4e':  case '\ufb4f':  case '\ufb50':  case '\ufb51':
		case '\ufb52':  case '\ufb53':  case '\ufb54':  case '\ufb55':
		case '\ufb56':  case '\ufb57':  case '\ufb58':  case '\ufb59':
		case '\ufb5a':  case '\ufb5b':  case '\ufb5c':  case '\ufb5d':
		case '\ufb5e':  case '\ufb5f':  case '\ufb60':  case '\ufb61':
		case '\ufb62':  case '\ufb63':  case '\ufb64':  case '\ufb65':
		case '\ufb66':  case '\ufb67':  case '\ufb68':  case '\ufb69':
		case '\ufb6a':  case '\ufb6b':  case '\ufb6c':  case '\ufb6d':
		case '\ufb6e':  case '\ufb6f':  case '\ufb70':  case '\ufb71':
		case '\ufb72':  case '\ufb73':  case '\ufb74':  case '\ufb75':
		case '\ufb76':  case '\ufb77':  case '\ufb78':  case '\ufb79':
		case '\ufb7a':  case '\ufb7b':  case '\ufb7c':  case '\ufb7d':
		case '\ufb7e':  case '\ufb7f':  case '\ufb80':  case '\ufb81':
		case '\ufb82':  case '\ufb83':  case '\ufb84':  case '\ufb85':
		case '\ufb86':  case '\ufb87':  case '\ufb88':  case '\ufb89':
		case '\ufb8a':  case '\ufb8b':  case '\ufb8c':  case '\ufb8d':
		case '\ufb8e':  case '\ufb8f':  case '\ufb90':  case '\ufb91':
		case '\ufb92':  case '\ufb93':  case '\ufb94':  case '\ufb95':
		case '\ufb96':  case '\ufb97':  case '\ufb98':  case '\ufb99':
		case '\ufb9a':  case '\ufb9b':  case '\ufb9c':  case '\ufb9d':
		case '\ufb9e':  case '\ufb9f':  case '\ufba0':  case '\ufba1':
		case '\ufba2':  case '\ufba3':  case '\ufba4':  case '\ufba5':
		case '\ufba6':  case '\ufba7':  case '\ufba8':  case '\ufba9':
		case '\ufbaa':  case '\ufbab':  case '\ufbac':  case '\ufbad':
		case '\ufbae':  case '\ufbaf':  case '\ufbb0':  case '\ufbb1':
		{
			matchRange('\uFB46','\uFBB1');
			break;
		}
		case '\ufd50':  case '\ufd51':  case '\ufd52':  case '\ufd53':
		case '\ufd54':  case '\ufd55':  case '\ufd56':  case '\ufd57':
		case '\ufd58':  case '\ufd59':  case '\ufd5a':  case '\ufd5b':
		case '\ufd5c':  case '\ufd5d':  case '\ufd5e':  case '\ufd5f':
		case '\ufd60':  case '\ufd61':  case '\ufd62':  case '\ufd63':
		case '\ufd64':  case '\ufd65':  case '\ufd66':  case '\ufd67':
		case '\ufd68':  case '\ufd69':  case '\ufd6a':  case '\ufd6b':
		case '\ufd6c':  case '\ufd6d':  case '\ufd6e':  case '\ufd6f':
		case '\ufd70':  case '\ufd71':  case '\ufd72':  case '\ufd73':
		case '\ufd74':  case '\ufd75':  case '\ufd76':  case '\ufd77':
		case '\ufd78':  case '\ufd79':  case '\ufd7a':  case '\ufd7b':
		case '\ufd7c':  case '\ufd7d':  case '\ufd7e':  case '\ufd7f':
		case '\ufd80':  case '\ufd81':  case '\ufd82':  case '\ufd83':
		case '\ufd84':  case '\ufd85':  case '\ufd86':  case '\ufd87':
		case '\ufd88':  case '\ufd89':  case '\ufd8a':  case '\ufd8b':
		case '\ufd8c':  case '\ufd8d':  case '\ufd8e':  case '\ufd8f':
		{
			matchRange('\uFD50','\uFD8F');
			break;
		}
		case '\ufd92':  case '\ufd93':  case '\ufd94':  case '\ufd95':
		case '\ufd96':  case '\ufd97':  case '\ufd98':  case '\ufd99':
		case '\ufd9a':  case '\ufd9b':  case '\ufd9c':  case '\ufd9d':
		case '\ufd9e':  case '\ufd9f':  case '\ufda0':  case '\ufda1':
		case '\ufda2':  case '\ufda3':  case '\ufda4':  case '\ufda5':
		case '\ufda6':  case '\ufda7':  case '\ufda8':  case '\ufda9':
		case '\ufdaa':  case '\ufdab':  case '\ufdac':  case '\ufdad':
		case '\ufdae':  case '\ufdaf':  case '\ufdb0':  case '\ufdb1':
		case '\ufdb2':  case '\ufdb3':  case '\ufdb4':  case '\ufdb5':
		case '\ufdb6':  case '\ufdb7':  case '\ufdb8':  case '\ufdb9':
		case '\ufdba':  case '\ufdbb':  case '\ufdbc':  case '\ufdbd':
		case '\ufdbe':  case '\ufdbf':  case '\ufdc0':  case '\ufdc1':
		case '\ufdc2':  case '\ufdc3':  case '\ufdc4':  case '\ufdc5':
		case '\ufdc6':  case '\ufdc7':
		{
			matchRange('\uFD92','\uFDC7');
			break;
		}
		case '\ufdf0':  case '\ufdf1':  case '\ufdf2':  case '\ufdf3':
		case '\ufdf4':  case '\ufdf5':  case '\ufdf6':  case '\ufdf7':
		case '\ufdf8':  case '\ufdf9':  case '\ufdfa':  case '\ufdfb':
		{
			matchRange('\uFDF0','\uFDFB');
			break;
		}
		case '\ufe70':  case '\ufe71':  case '\ufe72':
		{
			matchRange('\uFE70','\uFE72');
			break;
		}
		case '\ufe74':
		{
			matchRange('\uFE74','\uFE74');
			break;
		}
		case '\uff21':  case '\uff22':  case '\uff23':  case '\uff24':
		case '\uff25':  case '\uff26':  case '\uff27':  case '\uff28':
		case '\uff29':  case '\uff2a':  case '\uff2b':  case '\uff2c':
		case '\uff2d':  case '\uff2e':  case '\uff2f':  case '\uff30':
		case '\uff31':  case '\uff32':  case '\uff33':  case '\uff34':
		case '\uff35':  case '\uff36':  case '\uff37':  case '\uff38':
		case '\uff39':  case '\uff3a':
		{
			matchRange('\uFF21','\uFF3A');
			break;
		}
		case '\uff41':  case '\uff42':  case '\uff43':  case '\uff44':
		case '\uff45':  case '\uff46':  case '\uff47':  case '\uff48':
		case '\uff49':  case '\uff4a':  case '\uff4b':  case '\uff4c':
		case '\uff4d':  case '\uff4e':  case '\uff4f':  case '\uff50':
		case '\uff51':  case '\uff52':  case '\uff53':  case '\uff54':
		case '\uff55':  case '\uff56':  case '\uff57':  case '\uff58':
		case '\uff59':  case '\uff5a':
		{
			matchRange('\uFF41','\uFF5A');
			break;
		}
		case '\uff66':  case '\uff67':  case '\uff68':  case '\uff69':
		case '\uff6a':  case '\uff6b':  case '\uff6c':  case '\uff6d':
		case '\uff6e':  case '\uff6f':  case '\uff70':  case '\uff71':
		case '\uff72':  case '\uff73':  case '\uff74':  case '\uff75':
		case '\uff76':  case '\uff77':  case '\uff78':  case '\uff79':
		case '\uff7a':  case '\uff7b':  case '\uff7c':  case '\uff7d':
		case '\uff7e':  case '\uff7f':  case '\uff80':  case '\uff81':
		case '\uff82':  case '\uff83':  case '\uff84':  case '\uff85':
		case '\uff86':  case '\uff87':  case '\uff88':  case '\uff89':
		case '\uff8a':  case '\uff8b':  case '\uff8c':  case '\uff8d':
		case '\uff8e':  case '\uff8f':  case '\uff90':  case '\uff91':
		case '\uff92':  case '\uff93':  case '\uff94':  case '\uff95':
		case '\uff96':  case '\uff97':  case '\uff98':  case '\uff99':
		case '\uff9a':  case '\uff9b':  case '\uff9c':  case '\uff9d':
		case '\uff9e':  case '\uff9f':  case '\uffa0':  case '\uffa1':
		case '\uffa2':  case '\uffa3':  case '\uffa4':  case '\uffa5':
		case '\uffa6':  case '\uffa7':  case '\uffa8':  case '\uffa9':
		case '\uffaa':  case '\uffab':  case '\uffac':  case '\uffad':
		case '\uffae':  case '\uffaf':  case '\uffb0':  case '\uffb1':
		case '\uffb2':  case '\uffb3':  case '\uffb4':  case '\uffb5':
		case '\uffb6':  case '\uffb7':  case '\uffb8':  case '\uffb9':
		case '\uffba':  case '\uffbb':  case '\uffbc':  case '\uffbd':
		case '\uffbe':
		{
			matchRange('\uFF66','\uFFBE');
			break;
		}
		case '\uffc2':  case '\uffc3':  case '\uffc4':  case '\uffc5':
		case '\uffc6':  case '\uffc7':
		{
			matchRange('\uFFC2','\uFFC7');
			break;
		}
		case '\uffca':  case '\uffcb':  case '\uffcc':  case '\uffcd':
		case '\uffce':  case '\uffcf':
		{
			matchRange('\uFFCA','\uFFCF');
			break;
		}
		case '\uffd2':  case '\uffd3':  case '\uffd4':  case '\uffd5':
		case '\uffd6':  case '\uffd7':
		{
			matchRange('\uFFD2','\uFFD7');
			break;
		}
		case '\uffda':  case '\uffdb':  case '\uffdc':
		{
			matchRange('\uFFDA','\uFFDC');
			break;
		}
		default:
			if (((LA(1) >= '\u00f8' && LA(1) <= '\u01f5'))) {
				matchRange('\u00F8','\u01F5');
			}
			else if (((LA(1) >= '\u1e00' && LA(1) <= '\u1e9b'))) {
				matchRange('\u1E00','\u1E9B');
			}
			else if (((LA(1) >= '\u4e00' && LA(1) <= '\u9fa5'))) {
				matchRange('\u4E00','\u9FA5');
			}
			else if (((LA(1) >= '\uac00' && LA(1) <= '\ud7a3'))) {
				matchRange('\uAC00','\uD7A3');
			}
			else if (((LA(1) >= '\uf900' && LA(1) <= '\ufa2d'))) {
				matchRange('\uF900','\uFA2D');
			}
			else if (((LA(1) >= '\ufbd3' && LA(1) <= '\ufd3d'))) {
				matchRange('\uFBD3','\uFD3D');
			}
			else if (((LA(1) >= '\ufe76' && LA(1) <= '\ufefc'))) {
				matchRange('\uFE76','\uFEFC');
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUNICODE_CHARACTER_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UNICODE_CHARACTER_DIGIT;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			matchRange('\u0030','\u0039');
			break;
		}
		case '\u0660':  case '\u0661':  case '\u0662':  case '\u0663':
		case '\u0664':  case '\u0665':  case '\u0666':  case '\u0667':
		case '\u0668':  case '\u0669':
		{
			matchRange('\u0660','\u0669');
			break;
		}
		case '\u06f0':  case '\u06f1':  case '\u06f2':  case '\u06f3':
		case '\u06f4':  case '\u06f5':  case '\u06f6':  case '\u06f7':
		case '\u06f8':  case '\u06f9':
		{
			matchRange('\u06F0','\u06F9');
			break;
		}
		case '\u0966':  case '\u0967':  case '\u0968':  case '\u0969':
		case '\u096a':  case '\u096b':  case '\u096c':  case '\u096d':
		case '\u096e':  case '\u096f':
		{
			matchRange('\u0966','\u096F');
			break;
		}
		case '\u09e6':  case '\u09e7':  case '\u09e8':  case '\u09e9':
		case '\u09ea':  case '\u09eb':  case '\u09ec':  case '\u09ed':
		case '\u09ee':  case '\u09ef':
		{
			matchRange('\u09E6','\u09EF');
			break;
		}
		case '\u0a66':  case '\u0a67':  case '\u0a68':  case '\u0a69':
		case '\u0a6a':  case '\u0a6b':  case '\u0a6c':  case '\u0a6d':
		case '\u0a6e':  case '\u0a6f':
		{
			matchRange('\u0A66','\u0A6F');
			break;
		}
		case '\u0ae6':  case '\u0ae7':  case '\u0ae8':  case '\u0ae9':
		case '\u0aea':  case '\u0aeb':  case '\u0aec':  case '\u0aed':
		case '\u0aee':  case '\u0aef':
		{
			matchRange('\u0AE6','\u0AEF');
			break;
		}
		case '\u0b66':  case '\u0b67':  case '\u0b68':  case '\u0b69':
		case '\u0b6a':  case '\u0b6b':  case '\u0b6c':  case '\u0b6d':
		case '\u0b6e':  case '\u0b6f':
		{
			matchRange('\u0B66','\u0B6F');
			break;
		}
		case '\u0be7':  case '\u0be8':  case '\u0be9':  case '\u0bea':
		case '\u0beb':  case '\u0bec':  case '\u0bed':  case '\u0bee':
		case '\u0bef':
		{
			matchRange('\u0BE7','\u0BEF');
			break;
		}
		case '\u0c66':  case '\u0c67':  case '\u0c68':  case '\u0c69':
		case '\u0c6a':  case '\u0c6b':  case '\u0c6c':  case '\u0c6d':
		case '\u0c6e':  case '\u0c6f':
		{
			matchRange('\u0C66','\u0C6F');
			break;
		}
		case '\u0ce6':  case '\u0ce7':  case '\u0ce8':  case '\u0ce9':
		case '\u0cea':  case '\u0ceb':  case '\u0cec':  case '\u0ced':
		case '\u0cee':  case '\u0cef':
		{
			matchRange('\u0CE6','\u0CEF');
			break;
		}
		case '\u0d66':  case '\u0d67':  case '\u0d68':  case '\u0d69':
		case '\u0d6a':  case '\u0d6b':  case '\u0d6c':  case '\u0d6d':
		case '\u0d6e':  case '\u0d6f':
		{
			matchRange('\u0D66','\u0D6F');
			break;
		}
		case '\u0e50':  case '\u0e51':  case '\u0e52':  case '\u0e53':
		case '\u0e54':  case '\u0e55':  case '\u0e56':  case '\u0e57':
		case '\u0e58':  case '\u0e59':
		{
			matchRange('\u0E50','\u0E59');
			break;
		}
		case '\u0ed0':  case '\u0ed1':  case '\u0ed2':  case '\u0ed3':
		case '\u0ed4':  case '\u0ed5':  case '\u0ed6':  case '\u0ed7':
		case '\u0ed8':  case '\u0ed9':
		{
			matchRange('\u0ED0','\u0ED9');
			break;
		}
		case '\u0f20':  case '\u0f21':  case '\u0f22':  case '\u0f23':
		case '\u0f24':  case '\u0f25':  case '\u0f26':  case '\u0f27':
		case '\u0f28':  case '\u0f29':
		{
			matchRange('\u0F20','\u0F29');
			break;
		}
		case '\uff10':  case '\uff11':  case '\uff12':  case '\uff13':
		case '\uff14':  case '\uff15':  case '\uff16':  case '\uff17':
		case '\uff18':  case '\uff19':
		{
			matchRange('\uFF10','\uFF19');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLETTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LETTER;
		int _saveIndex;
		
		if ((_tokenSet_2.member(LA(1)))) {
			mUNICODE_LETTER(false);
		}
		else if ((LA(1)=='_')) {
			match('_');
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLETTER_OR_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LETTER_OR_DIGIT;
		int _saveIndex;
		
		if ((_tokenSet_1.member(LA(1)))) {
			mLETTER(false);
		}
		else if ((_tokenSet_3.member(LA(1)))) {
			mUNICODE_CHARACTER_DIGIT(false);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLEFT_PARENTHESE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LEFT_PARENTHESE;
		int _saveIndex;
		
		match("(");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRIGHT_PARENTHESE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHT_PARENTHESE;
		int _saveIndex;
		
		match(")");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLEFT_BRACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LEFT_BRACE;
		int _saveIndex;
		
		match("{");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRIGHT_BRACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHT_BRACE;
		int _saveIndex;
		
		match("}");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLEFT_BRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LEFT_BRACKET;
		int _saveIndex;
		
		match("[");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRIGHT_BRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RIGHT_BRACKET;
		int _saveIndex;
		
		match("]");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMICOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMICOLON;
		int _saveIndex;
		
		match(";");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		match(",");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mAPOSTROPHE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = APOSTROPHE;
		int _saveIndex;
		
		match("'");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DOT;
		int _saveIndex;
		
		match(".");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLON;
		int _saveIndex;
		
		match(":");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_AND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_AND;
		int _saveIndex;
		
		match("&&");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_OR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_OR;
		int _saveIndex;
		
		match("||");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBITWISE_AND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BITWISE_AND;
		int _saveIndex;
		
		match("&");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBITWISE_OR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BITWISE_OR;
		int _saveIndex;
		
		match("|");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOGICAL_NOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOGICAL_NOT;
		int _saveIndex;
		
		match("!");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBITWISE_NOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BITWISE_NOT;
		int _saveIndex;
		
		match("~");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBITWISE_XOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BITWISE_XOR;
		int _saveIndex;
		
		match("^");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS;
		int _saveIndex;
		
		match("+");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS;
		int _saveIndex;
		
		match("-");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR;
		int _saveIndex;
		
		match("*");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDIV(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIV;
		int _saveIndex;
		
		match("/");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMOD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MOD;
		int _saveIndex;
		
		match("%");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSHIFT_LEFT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SHIFT_LEFT;
		int _saveIndex;
		
		match("<<");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSHIFT_RIGHT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SHIFT_RIGHT;
		int _saveIndex;
		
		match(">>");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EQUAL;
		int _saveIndex;
		
		match("==");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mUNEQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UNEQUAL;
		int _saveIndex;
		
		match("!=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLESS_THAN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LESS_THAN;
		int _saveIndex;
		
		match("<");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGREATER_THAN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GREATER_THAN;
		int _saveIndex;
		
		match(">");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLESS_THAN_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LESS_THAN_EQUAL;
		int _saveIndex;
		
		match("<=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGREATER_THAN_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GREATER_THAN_EQUAL;
		int _saveIndex;
		
		match(">=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INC;
		int _saveIndex;
		
		match("++");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDEC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DEC;
		int _saveIndex;
		
		match("--");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASSIGN;
		int _saveIndex;
		
		match("=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHORIZONTAL_TABULATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HORIZONTAL_TABULATOR;
		int _saveIndex;
		
		match('\t');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mVERTICAL_TABULATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = VERTICAL_TABULATOR;
		int _saveIndex;
		
		match('\u0011');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLINE_FEED(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE_FEED;
		int _saveIndex;
		
		match('\n');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mFORM_FEED(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = FORM_FEED;
		int _saveIndex;
		
		match('\u000C');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mCARRIAGE_RETURN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CARRIAGE_RETURN;
		int _saveIndex;
		
		match('\r');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSPACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SPACE;
		int _saveIndex;
		
		match(' ');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLINE_SEPARATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LINE_SEPARATOR;
		int _saveIndex;
		
		match('\u2028');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mPARAGRAPH_SEPARATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PARAGRAPH_SEPARATOR;
		int _saveIndex;
		
		match('\u2029');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGIT;
		int _saveIndex;
		
		matchRange('0','9');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX_DIGIT;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			mDIGIT(false);
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':
		{
			matchRange('a','f');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mOCTAL_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCTAL_DIGIT;
		int _saveIndex;
		
		matchRange('0','7');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mZERO_TO_THREE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ZERO_TO_THREE;
		int _saveIndex;
		
		matchRange('0','3');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mOCTAL_NUMERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCTAL_NUMERAL;
		int _saveIndex;
		
		match('0');
		{
		_loop53:
		do {
			if (((LA(1) >= '0' && LA(1) <= '7'))) {
				mOCTAL_DIGIT(false);
			}
			else {
				break _loop53;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mEXPONENT_PART(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EXPONENT_PART;
		int _saveIndex;
		
		match('e');
		{
		switch ( LA(1)) {
		case '+':
		{
			match('+');
			break;
		}
		case '-':
		{
			match('-');
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		int _cnt57=0;
		_loop57:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				mDIGIT(false);
			}
			else {
				if ( _cnt57>=1 ) { break _loop57; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt57++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mFLOAT_SUFFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = FLOAT_SUFFIX;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'f':
		{
			match('f');
			break;
		}
		case 'd':
		{
			match('d');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWHITE_SPACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WHITE_SPACE;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case ' ':
		{
			mSPACE(false);
			break;
		}
		case '\t':
		{
			mHORIZONTAL_TABULATOR(false);
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRING_CHARACTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_CHARACTER;
		int _saveIndex;
		
		if ((_tokenSet_4.member(LA(1)))) {
			{
			match(_tokenSet_4);
			}
		}
		else if ((LA(1)=='\\')) {
			match("\\");
			matchNot(EOF_CHAR);
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCHARACTER_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CHARACTER_LITERAL;
		int _saveIndex;
		
		match('\'');
		mSTRING_CHARACTER(false);
		match('\'');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRING_LITERAL_PART(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_LITERAL_PART;
		int _saveIndex;
		
		match('"');
		{
		_loop66:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				mSTRING_CHARACTER(false);
			}
			else {
				break _loop66;
			}
			
		} while (true);
		}
		match('"');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_LITERAL;
		int _saveIndex;
		
		{
		int _cnt71=0;
		_loop71:
		do {
			if ((LA(1)=='"')) {
				mSTRING_LITERAL_PART(false);
				{
				_loop70:
				do {
					switch ( LA(1)) {
					case ' ':
					{
						mSPACE(false);
						break;
					}
					case '\t':
					{
						mHORIZONTAL_TABULATOR(false);
						break;
					}
					default:
					{
						break _loop70;
					}
					}
				} while (true);
				}
			}
			else {
				if ( _cnt71>=1 ) { break _loop71; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt71++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNUMERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NUMERAL;
		int _saveIndex;
		Token f1=null;
		Token f2=null;
		Token f3=null;
		Token f4=null;
		boolean isDecimal = false; Token t = null;
		
		{
		switch ( LA(1)) {
		case '.':
		{
			match('.');
			if ( inputState.guessing==0 ) {
				_ttype = DOT;
			}
			{
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				{
				int _cnt76=0;
				_loop76:
				do {
					if (((LA(1) >= '0' && LA(1) <= '9'))) {
						mDIGIT(false);
					}
					else {
						if ( _cnt76>=1 ) { break _loop76; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt76++;
				} while (true);
				}
				{
				if ((LA(1)=='e')) {
					mEXPONENT_PART(false);
				}
				else {
				}
				
				}
				{
				if ((LA(1)=='d'||LA(1)=='f')) {
					mFLOAT_SUFFIX(true);
					f1=_returnToken;
					if ( inputState.guessing==0 ) {
						t = f1;
					}
				}
				else {
				}
				
				}
				if ( inputState.guessing==0 ) {
					
						// Decide in code whether we have a float or a double here.
						if (t != null && t.getText().toUpperCase().indexOf('F') >= 0) 
						{
							_ttype = FLOAT_LITERAL;
					}
					else
					{
						_ttype = DOUBLE_LITERAL; // assume double
					}
					
				}
			}
			else {
			}
			
			}
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			{
			switch ( LA(1)) {
			case '0':
			{
				match('0');
				if ( inputState.guessing==0 ) {
					isDecimal = true;
				}
				{
				if ((LA(1)=='x')) {
					match('x');
					{
					int _cnt82=0;
					_loop82:
					do {
						if ((_tokenSet_5.member(LA(1))) && (true) && (true)) {
							mHEX_DIGIT(false);
						}
						else {
							if ( _cnt82>=1 ) { break _loop82; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt82++;
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						_ttype = HEX_LITERAL;
					}
				}
				else {
					boolean synPredMatched87 = false;
					if ((((LA(1) >= '0' && LA(1) <= '9')) && (true) && (true))) {
						int _m87 = mark();
						synPredMatched87 = true;
						inputState.guessing++;
						try {
							{
							{
							int _cnt85=0;
							_loop85:
							do {
								if (((LA(1) >= '0' && LA(1) <= '9'))) {
									mDIGIT(false);
								}
								else {
									if ( _cnt85>=1 ) { break _loop85; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
								}
								
								_cnt85++;
							} while (true);
							}
							{
							switch ( LA(1)) {
							case '.':
							{
								mDOT(false);
								break;
							}
							case 'e':
							{
								mEXPONENT_PART(false);
								break;
							}
							case 'd':  case 'f':
							{
								mFLOAT_SUFFIX(false);
								break;
							}
							default:
							{
								throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
							}
							}
							}
							}
						}
						catch (RecognitionException pe) {
							synPredMatched87 = false;
						}
						rewind(_m87);
						inputState.guessing--;
					}
					if ( synPredMatched87 ) {
						{
						int _cnt89=0;
						_loop89:
						do {
							if (((LA(1) >= '0' && LA(1) <= '9'))) {
								mDIGIT(false);
							}
							else {
								if ( _cnt89>=1 ) { break _loop89; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
							}
							
							_cnt89++;
						} while (true);
						}
					}
					else if (((LA(1) >= '0' && LA(1) <= '7')) && (true) && (true)) {
						{
						int _cnt91=0;
						_loop91:
						do {
							if (((LA(1) >= '0' && LA(1) <= '7'))) {
								mOCTAL_DIGIT(false);
							}
							else {
								if ( _cnt91>=1 ) { break _loop91; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
							}
							
							_cnt91++;
						} while (true);
						}
						if ( inputState.guessing==0 ) {
							_ttype = OCTAL_LITERAL;
						}
					}
					else {
					}
					}
					}
					break;
				}
				case '1':  case '2':  case '3':  case '4':
				case '5':  case '6':  case '7':  case '8':
				case '9':
				{
					{
					matchRange('1','9');
					}
					{
					_loop94:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							mDIGIT(false);
						}
						else {
							break _loop94;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						isDecimal = true;
					}
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				{
				if ((LA(1)=='i') && (LA(2)=='1') && (LA(3)=='6')) {
					match("i16");
					if ( inputState.guessing==0 ) {
						_ttype = SHORT_LITERAL;
					}
				}
				else if ((LA(1)=='i') && (LA(2)=='1') && (LA(3)=='2')) {
					match("i128");
					if ( inputState.guessing==0 ) {
						_ttype = BIGINT_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (LA(2)=='1') && (LA(3)=='6')) {
					match("u16");
					if ( inputState.guessing==0 ) {
						_ttype = SHORT_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (LA(2)=='1') && (LA(3)=='2')) {
					match("u128");
					if ( inputState.guessing==0 ) {
						_ttype = BIGINT_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (LA(2)=='l')) {
					match("ul");
					if ( inputState.guessing==0 ) {
						_ttype = LONG_LITERAL;
					}
				}
				else if ((LA(1)=='i') && (LA(2)=='8')) {
					match("i8");
					if ( inputState.guessing==0 ) {
						_ttype = BYTE_LITERAL;
					}
				}
				else if ((LA(1)=='i') && (LA(2)=='3')) {
					match("i32");
					if ( inputState.guessing==0 ) {
						_ttype = INTEGER_LITERAL;
					}
				}
				else if ((LA(1)=='i') && (LA(2)=='6')) {
					match("i64");
					if ( inputState.guessing==0 ) {
						_ttype = LONG_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (LA(2)=='8')) {
					match("u8");
					if ( inputState.guessing==0 ) {
						_ttype = BYTE_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (LA(2)=='3')) {
					match("u32");
					if ( inputState.guessing==0 ) {
						_ttype = INTEGER_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (LA(2)=='6')) {
					match("u64");
					if ( inputState.guessing==0 ) {
						_ttype = LONG_LITERAL;
					}
				}
				else if ((LA(1)=='l')) {
					match('l');
					if ( inputState.guessing==0 ) {
						_ttype = LONG_LITERAL;
					}
				}
				else if ((LA(1)=='u') && (true)) {
					match('u');
					if ( inputState.guessing==0 ) {
						_ttype = LONG_LITERAL;
					}
				}
				else if (((_tokenSet_6.member(LA(1))))&&(isDecimal)) {
					{
					switch ( LA(1)) {
					case '.':
					{
						mDOT(false);
						{
						_loop98:
						do {
							if (((LA(1) >= '0' && LA(1) <= '9'))) {
								mDIGIT(false);
							}
							else {
								break _loop98;
							}
							
						} while (true);
						}
						{
						if ((LA(1)=='e')) {
							mEXPONENT_PART(false);
						}
						else {
						}
						
						}
						{
						if ((LA(1)=='d'||LA(1)=='f')) {
							mFLOAT_SUFFIX(true);
							f2=_returnToken;
							if ( inputState.guessing==0 ) {
								t = f2;
							}
						}
						else {
						}
						
						}
						break;
					}
					case 'e':
					{
						mEXPONENT_PART(false);
						{
						if ((LA(1)=='d'||LA(1)=='f')) {
							mFLOAT_SUFFIX(true);
							f3=_returnToken;
							if ( inputState.guessing==0 ) {
								t = f3;
							}
						}
						else {
						}
						
						}
						break;
					}
					case 'd':  case 'f':
					{
						mFLOAT_SUFFIX(true);
						f4=_returnToken;
						if ( inputState.guessing==0 ) {
							t = f4;
						}
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						
							// Again, decide in code whether we have float or double here.
						if (t != null && t.getText().toUpperCase().indexOf('F') >= 0) 
						{
							_ttype = FLOAT_LITERAL;
						}
						else
						{
							_ttype = DOUBLE_LITERAL; // assume double
						}
						
					}
				}
				else {
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
			}
			_returnToken = _token;
		}
		
	public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENTIFIER;
		int _saveIndex;
		
		mLETTER(false);
		{
		_loop104:
		do {
			if ((_tokenSet_7.member(LA(1)))) {
				mLETTER_OR_DIGIT(false);
			}
			else {
				break _loop104;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[2048];
		data[0]=-17179920896L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[4088];
		data[1]=576460745860972544L;
		data[2]=297241973452963840L;
		data[3]=-36028797027352577L;
		for (int i = 4; i<=6; i++) { data[i]=-1L; }
		data[7]=-270215977642229761L;
		data[8]=16777215L;
		data[9]=-65536L;
		data[10]=-432624840181022721L;
		data[11]=133144182787L;
		data[13]=288230376151711744L;
		data[14]=-17179879616L;
		data[15]=4503588160110591L;
		data[16]=-8194L;
		data[17]=-536936449L;
		data[18]=-65533L;
		data[19]=234134404065073567L;
		data[20]=-562949953421312L;
		data[21]=-8547991553L;
		data[22]=255L;
		data[23]=1979120929931264L;
		data[24]=576460743713488896L;
		data[25]=-562949953419265L;
		data[26]=9007199254740991999L;
		data[27]=412319973375L;
		data[36]=2594073385365405664L;
		data[37]=17163091968L;
		data[38]=271902628478820320L;
		data[39]=844440767823872L;
		data[40]=247132830528276448L;
		data[41]=7881300924956672L;
		data[42]=2589004636761075680L;
		data[43]=4294967296L;
		data[44]=2579997437506199520L;
		data[45]=15837691904L;
		data[46]=270153412153034720L;
		data[48]=283724577500946400L;
		data[49]=12884901888L;
		data[50]=283724577500946400L;
		data[51]=13958643712L;
		data[52]=288228177128316896L;
		data[53]=12884901888L;
		data[56]=3799912185593854L;
		data[57]=127L;
		data[58]=2309621682768192918L;
		data[59]=805306463L;
		data[61]=4398046510847L;
		data[66]=-4294967296L;
		data[67]=36028797018898495L;
		data[68]=-1L;
		data[69]=-2080374785L;
		data[70]=-1065151889409L;
		data[71]=288230376151711743L;
		for (int i = 120; i<=121; i++) { data[i]=-1L; }
		data[122]=-4026531841L;
		data[123]=288230376151711743L;
		data[124]=-3233808385L;
		data[125]=4611686017001275199L;
		data[126]=6908521828386340863L;
		data[127]=2295745090394464220L;
		data[129]=-9223372036854775808L;
		data[132]=142986334291623044L;
		data[192]=17451448556060704L;
		data[193]=-2L;
		data[194]=-6574571521L;
		data[195]=8646911284551352319L;
		data[196]=-527765581332512L;
		data[197]=-1L;
		data[198]=32767L;
		for (int i = 312; i<=637; i++) { data[i]=-1L; }
		data[638]=274877906943L;
		for (int i = 688; i<=861; i++) { data[i]=-1L; }
		data[862]=68719476735L;
		for (int i = 996; i<=999; i++) { data[i]=-1L; }
		data[1000]=70368744177663L;
		data[1004]=6881498029467631743L;
		data[1005]=-37L;
		data[1006]=1125899906842623L;
		data[1007]=-524288L;
		for (int i = 1008; i<=1011; i++) { data[i]=-1L; }
		data[1012]=4611686018427387903L;
		data[1013]=-65536L;
		data[1014]=-196609L;
		data[1015]=1152640029630136575L;
		data[1017]=-11540474045136896L;
		data[1018]=-1L;
		data[1019]=2305843009213693951L;
		data[1020]=576460743713488896L;
		data[1021]=-274743689218L;
		data[1022]=9223372036854775807L;
		data[1023]=486341884L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[4088];
		data[1]=576460743713488896L;
		data[2]=297241973452963840L;
		data[3]=-36028797027352577L;
		for (int i = 4; i<=6; i++) { data[i]=-1L; }
		data[7]=-270215977642229761L;
		data[8]=16777215L;
		data[9]=-65536L;
		data[10]=-432624840181022721L;
		data[11]=133144182787L;
		data[13]=288230376151711744L;
		data[14]=-17179879616L;
		data[15]=4503588160110591L;
		data[16]=-8194L;
		data[17]=-536936449L;
		data[18]=-65533L;
		data[19]=234134404065073567L;
		data[20]=-562949953421312L;
		data[21]=-8547991553L;
		data[22]=255L;
		data[23]=1979120929931264L;
		data[24]=576460743713488896L;
		data[25]=-562949953419265L;
		data[26]=9007199254740991999L;
		data[27]=412319973375L;
		data[36]=2594073385365405664L;
		data[37]=17163091968L;
		data[38]=271902628478820320L;
		data[39]=844440767823872L;
		data[40]=247132830528276448L;
		data[41]=7881300924956672L;
		data[42]=2589004636761075680L;
		data[43]=4294967296L;
		data[44]=2579997437506199520L;
		data[45]=15837691904L;
		data[46]=270153412153034720L;
		data[48]=283724577500946400L;
		data[49]=12884901888L;
		data[50]=283724577500946400L;
		data[51]=13958643712L;
		data[52]=288228177128316896L;
		data[53]=12884901888L;
		data[56]=3799912185593854L;
		data[57]=127L;
		data[58]=2309621682768192918L;
		data[59]=805306463L;
		data[61]=4398046510847L;
		data[66]=-4294967296L;
		data[67]=36028797018898495L;
		data[68]=-1L;
		data[69]=-2080374785L;
		data[70]=-1065151889409L;
		data[71]=288230376151711743L;
		for (int i = 120; i<=121; i++) { data[i]=-1L; }
		data[122]=-4026531841L;
		data[123]=288230376151711743L;
		data[124]=-3233808385L;
		data[125]=4611686017001275199L;
		data[126]=6908521828386340863L;
		data[127]=2295745090394464220L;
		data[129]=-9223372036854775808L;
		data[132]=142986334291623044L;
		data[192]=17451448556060704L;
		data[193]=-2L;
		data[194]=-6574571521L;
		data[195]=8646911284551352319L;
		data[196]=-527765581332512L;
		data[197]=-1L;
		data[198]=32767L;
		for (int i = 312; i<=637; i++) { data[i]=-1L; }
		data[638]=274877906943L;
		for (int i = 688; i<=861; i++) { data[i]=-1L; }
		data[862]=68719476735L;
		for (int i = 996; i<=999; i++) { data[i]=-1L; }
		data[1000]=70368744177663L;
		data[1004]=6881498029467631743L;
		data[1005]=-37L;
		data[1006]=1125899906842623L;
		data[1007]=-524288L;
		for (int i = 1008; i<=1011; i++) { data[i]=-1L; }
		data[1012]=4611686018427387903L;
		data[1013]=-65536L;
		data[1014]=-196609L;
		data[1015]=1152640029630136575L;
		data[1017]=-11540474045136896L;
		data[1018]=-1L;
		data[1019]=2305843009213693951L;
		data[1020]=576460743713488896L;
		data[1021]=-274743689218L;
		data[1022]=9223372036854775807L;
		data[1023]=486341884L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2042];
		data[0]=287948901175001088L;
		data[25]=4393751543808L;
		data[27]=287948901175001088L;
		data[37]=281200098803712L;
		data[39]=281200098803712L;
		data[41]=281200098803712L;
		data[43]=281200098803712L;
		data[45]=281200098803712L;
		data[47]=280925220896768L;
		data[49]=281200098803712L;
		data[51]=281200098803712L;
		data[53]=281200098803712L;
		data[57]=67043328L;
		data[59]=67043328L;
		data[60]=4393751543808L;
		data[1020]=67043328L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[2048];
		data[0]=-17179920896L;
		data[1]=-268435457L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[1025];
		data[0]=287948901175001088L;
		data[1]=541165879296L;
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[1025];
		data[0]=70368744177664L;
		data[1]=481036337152L;
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[4088];
		data[0]=287948901175001088L;
		data[1]=576460745860972544L;
		data[2]=297241973452963840L;
		data[3]=-36028797027352577L;
		for (int i = 4; i<=6; i++) { data[i]=-1L; }
		data[7]=-270215977642229761L;
		data[8]=16777215L;
		data[9]=-65536L;
		data[10]=-432624840181022721L;
		data[11]=133144182787L;
		data[13]=288230376151711744L;
		data[14]=-17179879616L;
		data[15]=4503588160110591L;
		data[16]=-8194L;
		data[17]=-536936449L;
		data[18]=-65533L;
		data[19]=234134404065073567L;
		data[20]=-562949953421312L;
		data[21]=-8547991553L;
		data[22]=255L;
		data[23]=1979120929931264L;
		data[24]=576460743713488896L;
		data[25]=-558556201875457L;
		data[26]=9007199254740991999L;
		data[27]=287949313494974463L;
		data[36]=2594073385365405664L;
		data[37]=281217261895680L;
		data[38]=271902628478820320L;
		data[39]=1125640866627584L;
		data[40]=247132830528276448L;
		data[41]=8162501023760384L;
		data[42]=2589004636761075680L;
		data[43]=281204393771008L;
		data[44]=2579997437506199520L;
		data[45]=281215936495616L;
		data[46]=270153412153034720L;
		data[47]=280925220896768L;
		data[48]=283724577500946400L;
		data[49]=281212983705600L;
		data[50]=283724577500946400L;
		data[51]=281214057447424L;
		data[52]=288228177128316896L;
		data[53]=281212983705600L;
		data[56]=3799912185593854L;
		data[57]=67043455L;
		data[58]=2309621682768192918L;
		data[59]=872349791L;
		data[60]=4393751543808L;
		data[61]=4398046510847L;
		data[66]=-4294967296L;
		data[67]=36028797018898495L;
		data[68]=-1L;
		data[69]=-2080374785L;
		data[70]=-1065151889409L;
		data[71]=288230376151711743L;
		for (int i = 120; i<=121; i++) { data[i]=-1L; }
		data[122]=-4026531841L;
		data[123]=288230376151711743L;
		data[124]=-3233808385L;
		data[125]=4611686017001275199L;
		data[126]=6908521828386340863L;
		data[127]=2295745090394464220L;
		data[129]=-9223372036854775808L;
		data[132]=142986334291623044L;
		data[192]=17451448556060704L;
		data[193]=-2L;
		data[194]=-6574571521L;
		data[195]=8646911284551352319L;
		data[196]=-527765581332512L;
		data[197]=-1L;
		data[198]=32767L;
		for (int i = 312; i<=637; i++) { data[i]=-1L; }
		data[638]=274877906943L;
		for (int i = 688; i<=861; i++) { data[i]=-1L; }
		data[862]=68719476735L;
		for (int i = 996; i<=999; i++) { data[i]=-1L; }
		data[1000]=70368744177663L;
		data[1004]=6881498029467631743L;
		data[1005]=-37L;
		data[1006]=1125899906842623L;
		data[1007]=-524288L;
		for (int i = 1008; i<=1011; i++) { data[i]=-1L; }
		data[1012]=4611686018427387903L;
		data[1013]=-65536L;
		data[1014]=-196609L;
		data[1015]=1152640029630136575L;
		data[1017]=-11540474045136896L;
		data[1018]=-1L;
		data[1019]=2305843009213693951L;
		data[1020]=576460743780532224L;
		data[1021]=-274743689218L;
		data[1022]=9223372036854775807L;
		data[1023]=486341884L;
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	
	}
