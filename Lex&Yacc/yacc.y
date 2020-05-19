%{
#include <stdio.h>
int nocolumns=0;
int noid=0;
void yyerror (const char *str)
{
	fprintf(stderr, "\nError: %s\n", str);
}
int yywrap()
{
	return 1;
}
int main(void)
{
	printf("Enter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
	yyparse();
	return 0;
}
%}

%%

%token IF ELSE EE LE GE NE IDENTIFIER NUMBER FOR INT PP MM OR AND INSERT INTO VALUES DELETE FROM WHERE UPDATE SET LIKE STRING SELECT ANDS ORS;

program: line '\n' program|
	 line

line: IF '(' condition ')' '{' '}'
      {
	printf("if syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      IF '(' condition ')' '{' '}' elif
      {
	printf("if-elseif syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      FOR '(' initialize ';' condition ';' modification ')' '{' '}'
      {
	printf("for syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      INSERT INTO columns VALUES '(' values ')'
      {
	printf("insert sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      DELETE FROM identifiers WHERE sqlcondition
      {
	printf("delete sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      DELETE FROM identifiers
      {
	printf("delete sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      UPDATE IDENTIFIER SET changes WHERE sqlcondition
      {
	printf("update sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      UPDATE IDENTIFIER SET changes
      {
	printf("update sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      SELECT '*' FROM identifiers
      {
	printf("select sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      SELECT identifiers FROM identifiers
      {
	printf("select sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      SELECT '*' FROM identifiers WHERE sqlcondition
      {
	printf("select sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }|
      SELECT identifiers FROM identifiers WHERE sqlcondition
      {
	printf("select sql syntax correct!\n\nEnter a statement: (if-then-else or forloop or SQL statements for insert, delete, update or select):-\n");
      }

condition: variable compare variable AND condition|
	   variable compare variable OR condition|
	   variable compare variable

elif: ELSE IF '(' condition ')' '{' '}'|
      ELSE IF '(' condition ')' '{' '}' elif|
      ELSE '{' '}'
	 
variable: IDENTIFIER|
	  NUMBER

initialize: INT IDENTIFIER '=' NUMBER|
	    INT IDENTIFIER '=' IDENTIFIER|
	    INT IDENTIFIER '=' NUMBER operator IDENTIFIER|
	    INT IDENTIFIER '=' IDENTIFIER operator NUMBER|
	    INT IDENTIFIER '=' IDENTIFIER operator IDENTIFIER|
	    INT IDENTIFIER '=' NUMBER ',' oldvariables|
	    INT IDENTIFIER '=' IDENTIFIER ',' oldvariables|
	    INT IDENTIFIER '=' NUMBER operator IDENTIFIER ',' oldvariables|
	    INT IDENTIFIER '=' IDENTIFIER operator NUMBER ',' oldvariables|
	    INT IDENTIFIER '=' IDENTIFIER operator IDENTIFIER ',' oldvariables|
	    oldvariables
	    
oldvariables: IDENTIFIER '=' NUMBER|
	      IDENTIFIER '=' IDENTIFIER|
	      IDENTIFIER '=' IDENTIFIER operator NUMBER|
	      IDENTIFIER '=' NUMBER operator IDENTIFIER|
	      IDENTIFIER '=' IDENTIFIER operator IDENTIFIER|
	      IDENTIFIER '=' NUMBER ',' oldvariables|
	      IDENTIFIER '=' IDENTIFIER ',' oldvariables|
	      IDENTIFIER '=' IDENTIFIER operator NUMBER ',' oldvariables|
	      IDENTIFIER '=' NUMBER operator IDENTIFIER ',' oldvariables|
	      IDENTIFIER '=' IDENTIFIER operator IDENTIFIER ',' oldvariables

modification: IDENTIFIER '=' NUMBER|
	      IDENTIFIER '=' IDENTIFIER|
	      IDENTIFIER '=' IDENTIFIER operator NUMBER|
	      IDENTIFIER '=' NUMBER operator IDENTIFIER|
	      IDENTIFIER '=' IDENTIFIER operator IDENTIFIER|
	      IDENTIFIER '=' NUMBER ',' modification|
	      IDENTIFIER '=' IDENTIFIER ',' modification|
	      IDENTIFIER '=' IDENTIFIER operator NUMBER ',' modification|
	      IDENTIFIER '=' NUMBER operator IDENTIFIER ',' modification|
	      IDENTIFIER '=' IDENTIFIER operator IDENTIFIER ',' modification|
	      IDENTIFIER PP|
	      IDENTIFIER MM|
	      PP IDENTIFIER|
	      MM IDENTIFIER|
	      IDENTIFIER PP ',' modification|
	      IDENTIFIER MM ',' modification|
	      PP IDENTIFIER ',' modification|
	      MM IDENTIFIER ',' modification
	      
compare: EE|
	 LE|
	 GE|
	 NE|
	 '<'|
	 '>'
	 
operator: '+'|
	  '-'|
	  '*'|
	  '/'

columns: IDENTIFIER|
	 IDENTIFIER '(' identifiers ')'

values: IDENTIFIER|
	NUMBER|
	'\"' STRING '\"'|
	'\"' IDENTIFIER '\"'|
	'\"' NUMBER '\"'|
	IDENTIFIER ',' values|
	NUMBER ',' values|
	'\"' STRING '\"' ',' values|
	'\"' IDENTIFIER '\"' ',' values|
	'\"' NUMBER '\"' ',' values

identifiers: IDENTIFIER|
	     IDENTIFIER ',' identifiers

changes: IDENTIFIER '=' IDENTIFIER|
	 IDENTIFIER '=' NUMBER|
	 IDENTIFIER '=' '\"' STRING '\"'|
	 IDENTIFIER '=' '\"' IDENTIFIER '\"'|
	 IDENTIFIER '=' '\"' NUMBER '\"'|
	 IDENTIFIER '=' IDENTIFIER ',' changes|
	 IDENTIFIER '=' NUMBER ',' changes|
	 IDENTIFIER '=' '\"' STRING '\"' ',' changes|
	 IDENTIFIER '=' '\"' IDENTIFIER '\"' ',' changes|
	 IDENTIFIER '=' '\"' NUMBER '\"' ',' changes

sqlcondition: IDENTIFIER compare IDENTIFIER|
	      IDENTIFIER compare NUMBER|
	      IDENTIFIER '=' IDENTIFIER|
	      IDENTIFIER '=' NUMBER|
	      IDENTIFIER '=' '\"' STRING '\"'|
	      IDENTIFIER LIKE '\"' STRING '\"'|
	      IDENTIFIER '=' '\"' IDENTIFIER '\"'|
	      IDENTIFIER LIKE '\"' IDENTIFIER '\"'|
	      IDENTIFIER '=' '\"' NUMBER '\"'|
	      IDENTIFIER LIKE '\"' NUMBER '\"'|
	      IDENTIFIER LIKE IDENTIFIER|
	      IDENTIFIER compare IDENTIFIER ANDS sqlcondition|
	      IDENTIFIER compare NUMBER ANDS sqlcondition|
	      IDENTIFIER '=' IDENTIFIER ANDS sqlcondition|
	      IDENTIFIER '=' NUMBER ANDS sqlcondition|
	      IDENTIFIER '=' '\"' STRING '\"' ANDS sqlcondition|
	      IDENTIFIER LIKE '\"' STRING '\"' ANDS sqlcondition|
	      IDENTIFIER '=' '\"' IDENTIFIER '\"' ANDS sqlcondition|
	      IDENTIFIER LIKE '\"' IDENTIFIER '\"' ANDS sqlcondition|
	      IDENTIFIER '=' '\"' NUMBER '\"' ANDS sqlcondition|
	      IDENTIFIER LIKE '\"' NUMBER '\"' ANDS sqlcondition|
	      IDENTIFIER LIKE IDENTIFIER ANDS sqlcondition|
	      IDENTIFIER compare IDENTIFIER ORS sqlcondition|
	      IDENTIFIER compare NUMBER ORS sqlcondition|
	      IDENTIFIER '=' IDENTIFIER ORS sqlcondition|
	      IDENTIFIER '=' NUMBER ORS sqlcondition|
	      IDENTIFIER '=' '\"' STRING '\"' ORS sqlcondition|
	      IDENTIFIER LIKE '\"' STRING '\"' ORS sqlcondition|
	      IDENTIFIER '=' '\"' IDENTIFIER '\"' ORS sqlcondition|
	      IDENTIFIER LIKE '\"' IDENTIFIER '\"' ORS sqlcondition|
	      IDENTIFIER '=' '\"' NUMBER '\"' ORS sqlcondition|
	      IDENTIFIER LIKE '\"' NUMBER '\"' ORS sqlcondition|
	      IDENTIFIER LIKE IDENTIFIER ORS sqlcondition

%%