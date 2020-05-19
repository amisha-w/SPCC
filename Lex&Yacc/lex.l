%{
#include <stdio.h>
#include "y.tab.h"
extern int yylval;
%}

%%

if				return IF;
else				return ELSE;
for				return FOR;
into				return INTO;
int				return INT;
insert				return INSERT;
values				return VALUES;
delete				return DELETE;
from				return FROM;
where				return WHERE;
update				return UPDATE;
set				return SET;
like				return LIKE;
select				return SELECT;
and				return ANDS;
or				return ORS;
"=="				return EE;
"<="				return LE;
">="				return GE;
"!="				return NE;
[<]				return *yytext;
[>]				return *yytext;
[=]				return *yytext;
[(]				return *yytext;
[)]				return *yytext;
[{]				return *yytext;
[}]				return *yytext;
"++"				return PP;
"--"				return MM;
"||"				return OR;
"&&"				return AND;
[+]				return *yytext;
[-]				return *yytext;
[*]				return *yytext;
[/]				return *yytext;
[,]				return *yytext;
["]				return *yytext;
[;]				return *yytext;
[a-zA-Z][a-zA-Z0-9]*		return IDENTIFIER;
[0-9]+				return NUMBER;
[0-9]+[a-zA-Z][a-zA-Z0-9]*	return STRING;
[\n]				return *yytext;
[\t]+				;

%%