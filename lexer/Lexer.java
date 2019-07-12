package lexer;

import java.io.IOException;

/**
 *  The Lexer class is responsible for scanning the source file
 *  which is a stream of characters and returning a stream of
 *  tokens; each token object will contain the string (or access
 *  to the string) that describes the token along with an
 *  indication of its location in the source program to be used
 *  for error reporting; we are tracking line numbers; white spaces
 *  are space, tab, newlines
 */
public class Lexer {
  private boolean atEOF = false;
  // next character to process
  private char ch;
  private SourceReader source;
  // positions in line of current token
  private int startPosition, endPosition;
  /**
   *  Lexer constructor
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer( String sourceFile ) throws Exception {
    // init token table
    new TokenType();
    source = new SourceReader( sourceFile );
    ch = source.read();
  }
  private String isScientific(String number) throws IOException {
    String tempNumber;
    tempNumber = number;
    ch = source.read();

      if(Character.isDigit(ch)) {
        tempNumber += ch;
        ch = source.read();
        if (Character.isDigit(ch)) {
          tempNumber += ch;
          ch = source.read();
          if (ch == 'E' || ch == 'e') {
            tempNumber += ch;
            ch = source.read();
            if (ch == '+' || ch == '-') {
              tempNumber += ch;
              ch = source.read();
              if (Character.isDigit(ch)) {
                do {
                  tempNumber += ch;
                  ch = source.read();
                } while (Character.isDigit(ch));
                return tempNumber;
              }
            }
          }
        }
      }
      printError(ch + "");
    return tempNumber;
  }
  private void printError(String ch){
    System.out.println( "******** illegal character: " + ch );
    atEOF = true;
    nextToken();
  }
  /**
   *  newIdTokens are either ids or reserved words; new id's will be inserted
   *  in the symbol table with an indication that they are id's
   *  @param id is the String just scanned - it's either an id or reserved word
   *  @param startPosition is the column in the source file where the token begins
   *  @param endPosition is the column in the source file where the token ends
   *  @return the Token; either an id or one for the reserved words
   */
  public Token newIdToken( String id, int startPosition, int endPosition ) {
    return new Token(
      startPosition,
      endPosition,
      Symbol.symbol( id, Tokens.Identifier )
    );
  }
  public Token newCharToken( String Char, int startPosition, int endPosition ) {
    return new Token(
            startPosition,
            endPosition,
            Symbol.symbol( Char, Tokens.CHARacter )
    );
  }
  public Token newScientificToken( String Scientific, int startPosition, int endPosition ) {
    return new Token(
            startPosition,
            endPosition,
            Symbol.symbol( Scientific, Tokens.Scientific )
    );
  }
  /**
   *  number tokens are inserted in the symbol table; we don't convert the
   *  numeric strings to numbers until we load the bytecodes for interpreting;
   *  this ensures that any machine numeric dependencies are deferred
   *  until we actually run the program; i.e. the numeric constraints of the
   *  hardware used to compile the source program are not used
   *  @param number is the int String just scanned
   *  @param startPosition is the column in the source file where the int begins
   *  @param endPosition is the column in the source file where the int ends
   *  @return the int Token
   */
  public Token newNumberToken( String number, int startPosition, int endPosition) {
    return new Token(
      startPosition,
      endPosition,
      Symbol.symbol( number, Tokens.INTeger )
    );
  }

  /**
   *  build the token for operators (+ -) or separators (parens, braces)
   *  filter out comments which begin with two slashes
   *  @param s is the String representing the token
   *  @param startPosition is the column in the source file where the token begins
   *  @param endPosition is the column in the source file where the token ends
   *  @return the Token just found
   */
  public Token makeToken( String s, int startPosition, int endPosition ) {
    // filter comments
    if( s.equals("//") ) {
      try {
        int oldLine = source.getLineno();

        do {
          ch = source.read();
        } while( oldLine == source.getLineno() );
      } catch (Exception e) {
        atEOF = true;
      }

      return nextToken();
    }

    // ensure it's a valid token
    Symbol sym = Symbol.symbol( s, Tokens.BogusToken );
    if( sym == null) {
      printError(s);
    }

    return new Token( startPosition, endPosition, sym );
  }

  /**
   *  @return the next Token found in the source file
   */
  public Token nextToken() {
    // ch is always the next char to process
    if( atEOF ) {
      if( source != null ) {
        source.close();
        source = null;
      }

      return null;
    }

    try {
      // scan past whitespace
      while( Character.isWhitespace( ch )) {
        ch = source.read();
      }
    } catch( Exception e ) {
      atEOF = true;
      return nextToken();
    }

    startPosition = source.getPosition();
    endPosition = startPosition - 1;
    if( Character.isJavaIdentifierStart( ch )) {
      // return tokens for ids and reserved words
      String id = "";

      try {
        do {
          endPosition++;
          id += ch;
          ch = source.read();
        } while( Character.isJavaIdentifierPart( ch ));
      } catch( Exception e ) {
        atEOF = true;
      }

      return newIdToken( id, startPosition, endPosition );
    }
    if(ch == '\''){
        try{

            char character= source.read();
            ch=source.read();
            if(character == '\''){
              printError(character + "");
              return nextToken();
            }
            else if(ch == '\''){
                endPosition += 3;
                String charOut = "'" + character + "'";
                ch=source.read();
                return newCharToken(charOut + "", startPosition,endPosition);
            }

        }catch(Exception e){
            atEOF = true;
        }

    }
    try {
      if( Character.isDigit( ch )) {
        // return number tokens
        String number = "";
        try {
            do {
              endPosition++;
              number += ch;
              ch = source.read();
              if(number.length() == 1 && ch == '.' ){
                number += ch;
                String newNumber = isScientific(number);
                if(newNumber.length() >= 7) {
                  endPosition += newNumber.length();
                  return newScientificToken(newNumber, startPosition, endPosition);
                }
              }
            } while (Character.isDigit(ch));
        } catch( Exception e ) {
          atEOF = true;
        }
          return newNumberToken(number, startPosition, endPosition);

      }
    } catch (Exception e) {
      atEOF = true;
    }

    // At this point the only tokens to check for are one or two
    // characters; we must also check for comments that begin with
    // 2 slashes
    String charOld = "" + ch;
    String op = charOld;
    Symbol sym;
    try {
      endPosition++;
      ch = source.read();
      op += ch;

      // check if valid 2 char operator; if it's not in the symbol
      // table then don't insert it since we really have a one char
      // token
      sym = Symbol.symbol( op, Tokens.BogusToken );
      if (sym == null) {
        // it must be a one char token
        return makeToken( charOld, startPosition, endPosition );
      }

      endPosition++;
      ch = source.read();

      return makeToken( op, startPosition, endPosition );
    } catch( Exception e ) { /* no-op */ }

    atEOF = true;
    if( startPosition == endPosition ) {
      op = charOld;
    }

    return makeToken( op, startPosition, endPosition );
  }

/*  public static void main(String args[]) {
    Token token;

    try {
      Lexer lex = new Lexer( args[0] );
      while( true ) {
        token = lex.nextToken();

        System.out.printf("%-11s left: %-8s right: %-8s line: %-8s %s\n",token.getSymbol(), token.getLeftPosition(), token.getRightPosition(),
              lex.source.getLineno(), token.getKind());
      }
    } catch (Exception e) {}
  }*/
}