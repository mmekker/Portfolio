#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

/**
 * This program was made is CSC344 - Programming Languages
 * Takes a code file from standard in and checks that all open characters
 * have matching closing characters. It then prints out an errors and how
 * many identifiers there are.
 */

//#define PRINT_DEBUG

//checkValidity
//Takes in a char[] and returns a 1 if the string is valid and a 0 if it is invalid
//it is valid when it starts with a letter and contains letters numbers and underscores
int checkValidity(char iden[])
{
    if(!isalpha(iden[0]))//if first char isnt a letter return invalid
    {
        return 0;
    }
    int len = strlen(iden);
    int i;
    for(i = 0; i < len; i++)
    {
        if( !(isalpha(iden[i]) || isdigit(iden[i]) || iden[i] == '_' || iden[i] == '.') )
        {
            return 0;
        }
    }
    return 1;
}


/*sggd
gdffgsd
jhkhk
hhg
dfsgsdffg
dfgs*dff
*sdfgfsdg
dhghdfg*/


int main()
{
	char ch;

	char symbolStack[50];

  int a;
  for(a = 0; a < 50; a++)
  {
      symbolStack[a] = '\0';
  }

  int top = -1;
  int highwater = -1;

	int numIden = 0;
  int isIdentifier = 0;

  int endOfIden = -1;
  char iden[50];
  int slash = 0;

  int count = 0;


	while(1)
    {
      count++;
    	ch = fgetc(stdin);
      	if( feof(stdin) ) //check for another character.
      	{
          if(checkValidity(iden) == 1) //check its validity
          {
              numIden++; //+1 if valid
          }
       	  break;
      	}


        if(ch == '\\' && slash == 0)//ignores next character if there is a slash
        {
            slash = 1;
        }
        else if(ch == '\\' && slash == 1)
        {
          slash = 0;
          continue;
        }
        //Start check for open close symbols//
        //**********************************//


        if(slash == 0)
        {
          if(top == -1 && (ch == '{' || ch == '[' || ch == '(' || ch == '\'' || ch == '\"') )
          {
              symbolStack[top+1] = ch; //sets squiggle as new top of the stack (current open bracket)
              top++;

              #ifdef PRINT_DEBUG
              printf("    push- %c, %d\n", ch, top);
              int in;
              for( in = top; in > -1; in--)
              {
                printf("%c", symbolStack[in]);
              }
              printf("\n");
              #endif

          }
         	else if(ch == '{' && symbolStack[top] != '\"' && symbolStack[top] != '\'') //check if it is an open bracket and not inside of a single or double quote
        	{
        		symbolStack[top+1] = '{'; //sets squiggle as new top of the stack (current open bracket)
            top++;

            #ifdef PRINT_DEBUG
            printf("    push{ %c, %d\n", ch,top);
            int in;
            for( in = top; in > -1; in--)
            {
              printf("%c", symbolStack[in]);
            }
            printf("\n");
            #endif

        	}
        	else if(ch == '}' && symbolStack[top] != '\"' && symbolStack[top] != '\'') //check if it is an closed bracket and not inside of a single or double quote
        	{
        		if(symbolStack[top] != '{' || top < 0) //if squiggle bracket isnt the current open symbol then show error
        		{
        			printf("**Error. Improperly nested symbol at character %d. %c current top = \'%c\' %d** \n", count, ch, symbolStack[top], top);
        			#ifdef PRINT_DEBUG
              printf("highwater = %d\n", highwater);
              #endif
              continue;
              //return 0;
        		}
            symbolStack[top] = '\0';
            top--;

            #ifdef PRINT_DEBUG
            printf("     pop %c, %d\n", ch, top);
            int in;
            for( in = top; in > -1; in--)
            {
              printf("%c", symbolStack[in]);
            }
            printf("\n");
            #endif

        	}
        	else if(ch == '[' && symbolStack[top] != '\"' && symbolStack[top] != '\'') //check if it is an open bracket and not inside of a single or double quote
        	{
        		symbolStack[top+1] = '[';
            top++;

            #ifdef PRINT_DEBUG
            printf("    push[ %c\n", ch);
            int in;
            for( in = top; in > -1; in--)
            {
              printf("%c", symbolStack[in]);
            }
            printf("\n");
            #endif

        	}
        	else if(ch == ']' && symbolStack[top] != '\"' && symbolStack[top] != '\'') //check if it is an closed bracket and not inside of a single or double quote
        	{
        		if(symbolStack[top] != '[')
        		{
        			printf("**Error. Improperly nested symbol at character %d. %c current top = \'%c\'** \n", count, ch, symbolStack[top]);
        			
              #ifdef PRINT_DEBUG
              printf("highwater = %d\n", highwater);
              #endif

              continue;
              //return 0;
        		}
            symbolStack[top] = '\0';
            top--;

            #ifdef PRINT_DEBUG
            printf("     pop %c\n", ch);
            int in;
            for( in = top; in > -1; in--)
            {
              printf("%c", symbolStack[in]);
            }
            printf("\n");
            #endif

        	}
        	else if(ch == '(' && symbolStack[top] != '\"' && symbolStack[top] != '\'') //check if it is an open paren and not inside of a single or double quote
        	{
        		symbolStack[top+1] = '(';
            top++;

            #ifdef PRINT_DEBUG
            printf("    push( %c\n", ch);
            int in;
            for( in = top; in > -1; in--)
            {
              printf("%c", symbolStack[in]);
            }
            printf("\n");
            #endif

        	}
        	else if(ch == ')' && symbolStack[top] != '\"' && symbolStack[top] != '\'') //check if it is an closed paren and not inside of a single or double quote
        	{
        		if(symbolStack[top] != '(')
        		{
        			printf("**Error. Improperly nested symbol at character %d. %c current top = \'%c\'** \n", count, ch, symbolStack[top]);
              #ifdef PRINT_DEBUG
              printf("highwater = %d\n", highwater);
              #endif
              continue;
        			//return 0;
        		}
            symbolStack[top] = '\0';
            top--;

            #ifdef PRINT_DEBUG
            printf("     pop %c\n", ch);
            int in;
            for( in = top; in > -1; in--)
            {
              printf("%c", symbolStack[in]);
            }
            printf("\n");
            #endif

        	}
        	else if(ch == '\"' && symbolStack[top] != '\'') //check if it is a double quote and not inside of a single quote
        	{
        		if(symbolStack[top] == '\"') 
        		{
              symbolStack[top] = '\0';
              top--;

              #ifdef PRINT_DEBUG
              printf("     pop %c\n", ch);
              int in;
              for( in = top; in > -1; in--)
              {
                printf("%c", symbolStack[in]);
              }
              printf("\n");
              #endif

        		}
        		else 
        		{
              symbolStack[top+1] = '\"';
              top++;

              #ifdef PRINT_DEBUG
              printf("    push\" %c\n", ch);
              int in;
              for( in = top; in > -1; in--)
              {
                printf("%c", symbolStack[in]);
              }
              printf("\n");
              #endif

        		}
        	}
        	else if(ch == '\'' && symbolStack[top] != '\"') //check if it is a single and not inside of a double quote
        	{
        		if(symbolStack[top] == '\'') 
            {
              symbolStack[top] = '\0';
              top--;

              #ifdef PRINT_DEBUG
              printf("     pop %c\n", ch);
              int in;
              for( in = top; in > -1; in--)
              {
                printf("%c", symbolStack[in]);
              }
              printf("\n");
              #endif

            }
            else 
            {
              symbolStack[top+1] = '\'';
              top++;

              #ifdef PRINT_DEBUG
              printf("    push\' %c\n", ch);
              int in;
              for( in = top; in > -1; in--)
              {
                printf("%c", symbolStack[in]);
              }
              printf("\n");
              #endif

            }
        	}
            #ifdef PRINT_DEBUG
            if(top>highwater)highwater = top;
            #endif



          //********************************//
          //End check for open close symbols//

          //Start counting identifiers//
          //**************************//
          if(ch != ' ' && ch != ',' && ch != '{' && ch != '}' && ch != '[' && ch != ']' && ch != '(' && ch != ')' && ch != '\"' && ch != '\'' && ch != ';' && ch != ':' && ch != '\n' && ch != '#' && ch != '<' && ch != '>' && ch != '\t')
          {
              iden[endOfIden+1] = ch; //if it isnt one of the above symbols then add it to the identifier string
              endOfIden++;
          }
          else
          {
              if(checkValidity(iden) == 1) //check its validity
              {
                  numIden++; //+1 if valid
              }

              //reset string
              int x;
              for(x = 0; x < 30; x++)
              {
                  iden[x] = '\0';
              }
              endOfIden = -1;
          }




          //************************//
          //End counting identifiers//





        }//end \ if
        if(ch != '\\')
        {
            slash = 0;
        }
    }//end while
    if(top >= 0)
    {
       printf("\n**Error. There are extra symbols. \n");
       int eSquiggle = 0;
       int eSquare = 0;
       int eParen = 0;
       int eSQuote = 0;
       int eDQuote = 0;
       //printf("top = %d\n", top);
       #ifdef PRINT_DEBUG
       printf("highwater = %d\n", highwater);
       #endif
       while(top > -1)
       {
          if(symbolStack[top] == '{')
          {
              eSquiggle++;
          }
          else if(symbolStack[top] == '[')
          {
              eSquare++;
          }
          else if(symbolStack[top] == '(')
          {
              eParen++;
          }
          else if(symbolStack[top] == '\"')
          {
              eDQuote++;
          }
          else if(symbolStack[top] == '\'')
          {
              eSQuote++;
          }
          top--;
       }
       printf("Extra Symbols: \n");
       printf("   Squiggle Brackets %d\n", eSquiggle);
       printf("   Square Brackets %d\n", eSquare);
       printf("   Parenthesis %d\n", eParen);
       printf("   Double Quotes %d\n", eDQuote);
       printf("   Single Quotes %d\n", eSQuote);
       printf("There are %d identifiers.\n", numIden);
       return 0;
    }

    printf("\nThere are %d identifier(s).\n", numIden);
    printf("\n**No Errors.** \n\n");





	return 1;
}