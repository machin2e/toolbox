#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "Clay.h"

int main () {

  puts("> ");
  for(;;) {
    // Get a string from console
    static char buff[256];
    // char *expr = gets_s(buff, sizeof(buff));
    char *expr = gets(buff);

    // If the string is empty, then exit
    if(*expr == '\0')
      return 0;

    // Evaluate the expression
    /*
    ExprEval eval;
    double res = eval.Eval(expr);
    if(eval.GetErr() != EEE_NO_ERROR) {
      printf("  Error: %s at %s\n", errors[eval.GetErr()], eval.GetErrPos());
    } else {
      printf("  = %g\n", res);
    }
    */
  }

  return 0;
}
