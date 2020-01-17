This project implements a tool to parse Windows .rc files and converts them to xml files. Parsing .rc files is very much like parsing C/C++ header files. As such this tool might be interesting for people who have the need to parse such header files. The tools comes with:

- Handling for unlimited nesting of include files
- Trigraph handling and line splicing in the input reader
- Complete macro handling, including charizing  and stringizing
- An evaluator for `#if`, `#ifdef` and `#ifndef` conditional expressions
- Support for some specialities used by (former) Borland Compilers


Compiling the Grammars
===

- Order is important: first expression parser then parser then lexer
- Copy token vocabulary from expression package (folder) to main package (folder)
- Adjust fixed token type constants (e.g. RCParserTokenTypes.LITERAL_auto3state, which is actually used in pure numerical form).

PreprocessorInputState:

- \n must be an ignored character or another ignored character must be used on return when starting to read a new include file and
  pushing the old state to create a new one.

Command line:

`-include="<your include path>" -symbol="RC_INVOKED" -symbol="_WIN32" -symbol="UNICODE" -symbol="APSTUDIO_INVOKED" -symbol="_WIN32_WINNT 0x0400" -symbol="_WIN32_IE 0x0600" -symbol="_MSC_VER 0x1300" -symbol="_INTEGRAL_MAX_BITS 32"`

VM arguments:

`-Dinclude-paths="${env_var:include}"`
