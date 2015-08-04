package bnfc.abs;
import bnfc.abs.Absyn.*;

public class PrettyPrinter
{
  //For certain applications increasing the initial size of the buffer may improve performance.
  private static final int INITIAL_BUFFER_SIZE = 128;
  private static final int INDENT_WIDTH = 2;
  //You may wish to change the parentheses used in precedence.
  private static final String _L_PAREN = new String("(");
  private static final String _R_PAREN = new String(")");
  //You may wish to change render
  private static void render(String s)
  {
    if (s.equals("{"))
    {
       buf_.append("\n");
       indent();
       buf_.append(s);
       _n_ = _n_ + INDENT_WIDTH;
       buf_.append("\n");
       indent();
    }
    else if (s.equals("(") || s.equals("["))
       buf_.append(s);
    else if (s.equals(")") || s.equals("]"))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals("}"))
    {
       int t;
       _n_ = _n_ - INDENT_WIDTH;
       for(t=0; t<INDENT_WIDTH; t++) {
         backup();
       }
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals(","))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals(";"))
    {
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals("")) return;
    else
    {
       buf_.append(s);
       buf_.append(" ");
    }
  }


  //  print and show methods are defined for each category.
  public static String print(bnfc.abs.Absyn.AnyIdent foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.AnyIdent foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListAnyIdent foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListAnyIdent foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Program foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Program foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListModule foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListModule foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Module foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Module foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Export foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Export foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListExport foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListExport foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Import foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Import foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListImport foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListImport foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ImportType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ImportType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Type foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Type foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListQType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListQType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.QType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.QType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.QTypeSegment foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.QTypeSegment foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListQTypeSegment foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListQTypeSegment foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.TType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.TType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.TTypeSegment foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.TTypeSegment foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListTTypeSegment foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListTTypeSegment foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListDecl foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListDecl foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Decl foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Decl foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ConstrIdent foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ConstrIdent foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ConstrType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ConstrType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListConstrType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListConstrType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListUIdent foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListUIdent foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListConstrIdent foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListConstrIdent foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.FunBody foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.FunBody foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.MethSignat foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.MethSignat foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListMethSignat foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListMethSignat foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ClassBody foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ClassBody foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListClassBody foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListClassBody foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Block foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Block foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.MaybeBlock foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.MaybeBlock foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListParam foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListParam foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Param foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Param foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListStm foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListStm foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Stm foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Stm foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.CatchBranch foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.CatchBranch foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListCatchBranch foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListCatchBranch foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.MaybeFinally foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.MaybeFinally foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Guard foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Guard foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Exp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Exp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListPureExp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListPureExp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.PureExp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.PureExp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.CaseBranch foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.CaseBranch foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListCaseBranch foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListCaseBranch foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListPattern foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListPattern foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Pattern foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Pattern foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Literal foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Literal foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.EffExp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.EffExp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.Ann foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.Ann foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListAnn foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListAnn foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.AnnDecl foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.AnnDecl foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListAnnDecl foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListAnnDecl foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.AnnType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.AnnType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(bnfc.abs.Absyn.ListAnnType foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(bnfc.abs.Absyn.ListAnnType foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  /***   You shouldn't need to change anything beyond this point.   ***/

  private static void pp(bnfc.abs.Absyn.AnyIdent foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.AnyIden)
    {
       bnfc.abs.Absyn.AnyIden _anyiden = (bnfc.abs.Absyn.AnyIden) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_anyiden.lident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.AnyTyIden)
    {
       bnfc.abs.Absyn.AnyTyIden _anytyiden = (bnfc.abs.Absyn.AnyTyIden) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_anytyiden.uident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListAnyIdent foo, int _i_)
  {
     for (java.util.Iterator<AnyIdent> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Program foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.Prog)
    {
       bnfc.abs.Absyn.Prog _prog = (bnfc.abs.Absyn.Prog) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_prog.listmodule_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListModule foo, int _i_)
  {
     for (java.util.Iterator<Module> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Module foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.Modul)
    {
       bnfc.abs.Absyn.Modul _modul = (bnfc.abs.Absyn.Modul) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("module");
       pp(_modul.qtype_, 0);
       render(";");
       pp(_modul.listexport_, 0);
       pp(_modul.listimport_, 0);
       pp(_modul.listdecl_, 0);
       pp(_modul.maybeblock_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.Export foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.AnyExport)
    {
       bnfc.abs.Absyn.AnyExport _anyexport = (bnfc.abs.Absyn.AnyExport) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("export");
       pp(_anyexport.listanyident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.AnyFromExport)
    {
       bnfc.abs.Absyn.AnyFromExport _anyfromexport = (bnfc.abs.Absyn.AnyFromExport) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("export");
       pp(_anyfromexport.listanyident_, 0);
       render("from");
       pp(_anyfromexport.qtype_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.StarExport)
    {
       bnfc.abs.Absyn.StarExport _starexport = (bnfc.abs.Absyn.StarExport) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("export");
       render("*");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.StarFromExport)
    {
       bnfc.abs.Absyn.StarFromExport _starfromexport = (bnfc.abs.Absyn.StarFromExport) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("export");
       render("*");
       render("from");
       pp(_starfromexport.qtype_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListExport foo, int _i_)
  {
     for (java.util.Iterator<Export> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(";");
       } else {
         render(";");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Import foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.AnyImport)
    {
       bnfc.abs.Absyn.AnyImport _anyimport = (bnfc.abs.Absyn.AnyImport) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_anyimport.importtype_, 0);
       pp(_anyimport.ttype_, 0);
       pp(_anyimport.anyident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.AnyFromImport)
    {
       bnfc.abs.Absyn.AnyFromImport _anyfromimport = (bnfc.abs.Absyn.AnyFromImport) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_anyfromimport.importtype_, 0);
       pp(_anyfromimport.listanyident_, 0);
       render("from");
       pp(_anyfromimport.qtype_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.StarFromImport)
    {
       bnfc.abs.Absyn.StarFromImport _starfromimport = (bnfc.abs.Absyn.StarFromImport) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_starfromimport.importtype_, 0);
       render("*");
       render("from");
       pp(_starfromimport.qtype_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListImport foo, int _i_)
  {
     for (java.util.Iterator<Import> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(";");
       } else {
         render(";");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ImportType foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.ForeignImport)
    {
       bnfc.abs.Absyn.ForeignImport _foreignimport = (bnfc.abs.Absyn.ForeignImport) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("fimport");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.NormalImport)
    {
       bnfc.abs.Absyn.NormalImport _normalimport = (bnfc.abs.Absyn.NormalImport) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("import");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.Type foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.TUnderscore)
    {
       bnfc.abs.Absyn.TUnderscore _tunderscore = (bnfc.abs.Absyn.TUnderscore) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("_");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.TSimple)
    {
       bnfc.abs.Absyn.TSimple _tsimple = (bnfc.abs.Absyn.TSimple) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_tsimple.qtype_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.TGen)
    {
       bnfc.abs.Absyn.TGen _tgen = (bnfc.abs.Absyn.TGen) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_tgen.qtype_, 0);
       render("<");
       pp(_tgen.listtype_, 0);
       render(">");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListType foo, int _i_)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ListQType foo, int _i_)
  {
     for (java.util.Iterator<QType> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.QType foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.QTyp)
    {
       bnfc.abs.Absyn.QTyp _qtyp = (bnfc.abs.Absyn.QTyp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_qtyp.listqtypesegment_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.QTypeSegment foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.QTypeSegmen)
    {
       bnfc.abs.Absyn.QTypeSegmen _qtypesegmen = (bnfc.abs.Absyn.QTypeSegmen) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_qtypesegmen.uident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListQTypeSegment foo, int _i_)
  {
     for (java.util.Iterator<QTypeSegment> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(".");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.TType foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.TTyp)
    {
       bnfc.abs.Absyn.TTyp _ttyp = (bnfc.abs.Absyn.TTyp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_ttyp.listttypesegment_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.TTypeSegment foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.TTypeSegmen)
    {
       bnfc.abs.Absyn.TTypeSegmen _ttypesegmen = (bnfc.abs.Absyn.TTypeSegmen) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_ttypesegmen.uident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListTTypeSegment foo, int _i_)
  {
     for (java.util.Iterator<TTypeSegment> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(".");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ListDecl foo, int _i_)
  {
     for (java.util.Iterator<Decl> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Decl foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.TypeDecl)
    {
       bnfc.abs.Absyn.TypeDecl _typedecl = (bnfc.abs.Absyn.TypeDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("type");
       pp(_typedecl.uident_, 0);
       render("=");
       pp(_typedecl.type_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.TypeParDecl)
    {
       bnfc.abs.Absyn.TypeParDecl _typepardecl = (bnfc.abs.Absyn.TypeParDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("type");
       pp(_typepardecl.uident_, 0);
       render("<");
       pp(_typepardecl.listuident_, 0);
       render(">");
       render("=");
       pp(_typepardecl.type_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ExceptionDecl)
    {
       bnfc.abs.Absyn.ExceptionDecl _exceptiondecl = (bnfc.abs.Absyn.ExceptionDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("exception");
       pp(_exceptiondecl.constrident_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.DataDecl)
    {
       bnfc.abs.Absyn.DataDecl _datadecl = (bnfc.abs.Absyn.DataDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("data");
       pp(_datadecl.uident_, 0);
       render("=");
       pp(_datadecl.listconstrident_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.DataParDecl)
    {
       bnfc.abs.Absyn.DataParDecl _datapardecl = (bnfc.abs.Absyn.DataParDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("data");
       pp(_datapardecl.uident_, 0);
       render("<");
       pp(_datapardecl.listuident_, 0);
       render(">");
       render("=");
       pp(_datapardecl.listconstrident_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.FunDecl)
    {
       bnfc.abs.Absyn.FunDecl _fundecl = (bnfc.abs.Absyn.FunDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("def");
       pp(_fundecl.type_, 0);
       pp(_fundecl.lident_, 0);
       render("(");
       pp(_fundecl.listparam_, 0);
       render(")");
       render("=");
       pp(_fundecl.funbody_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.FunParDecl)
    {
       bnfc.abs.Absyn.FunParDecl _funpardecl = (bnfc.abs.Absyn.FunParDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("def");
       pp(_funpardecl.type_, 0);
       pp(_funpardecl.lident_, 0);
       render("<");
       pp(_funpardecl.listuident_, 0);
       render(">");
       render("(");
       pp(_funpardecl.listparam_, 0);
       render(")");
       render("=");
       pp(_funpardecl.funbody_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.InterfDecl)
    {
       bnfc.abs.Absyn.InterfDecl _interfdecl = (bnfc.abs.Absyn.InterfDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("interface");
       pp(_interfdecl.uident_, 0);
       render("{");
       pp(_interfdecl.listmethsignat_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ExtendsDecl)
    {
       bnfc.abs.Absyn.ExtendsDecl _extendsdecl = (bnfc.abs.Absyn.ExtendsDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("interface");
       pp(_extendsdecl.uident_, 0);
       render("extends");
       pp(_extendsdecl.listqtype_, 0);
       render("{");
       pp(_extendsdecl.listmethsignat_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ClassDecl)
    {
       bnfc.abs.Absyn.ClassDecl _classdecl = (bnfc.abs.Absyn.ClassDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("class");
       pp(_classdecl.uident_, 0);
       render("{");
       pp(_classdecl.listclassbody_1, 0);
       pp(_classdecl.maybeblock_, 0);
       pp(_classdecl.listclassbody_2, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ClassParamDecl)
    {
       bnfc.abs.Absyn.ClassParamDecl _classparamdecl = (bnfc.abs.Absyn.ClassParamDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("class");
       pp(_classparamdecl.uident_, 0);
       render("(");
       pp(_classparamdecl.listparam_, 0);
       render(")");
       render("{");
       pp(_classparamdecl.listclassbody_1, 0);
       pp(_classparamdecl.maybeblock_, 0);
       pp(_classparamdecl.listclassbody_2, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ClassImplements)
    {
       bnfc.abs.Absyn.ClassImplements _classimplements = (bnfc.abs.Absyn.ClassImplements) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("class");
       pp(_classimplements.uident_, 0);
       render("implements");
       pp(_classimplements.listqtype_, 0);
       render("{");
       pp(_classimplements.listclassbody_1, 0);
       pp(_classimplements.maybeblock_, 0);
       pp(_classimplements.listclassbody_2, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ClassParamImplements)
    {
       bnfc.abs.Absyn.ClassParamImplements _classparamimplements = (bnfc.abs.Absyn.ClassParamImplements) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("class");
       pp(_classparamimplements.uident_, 0);
       render("(");
       pp(_classparamimplements.listparam_, 0);
       render(")");
       render("implements");
       pp(_classparamimplements.listqtype_, 0);
       render("{");
       pp(_classparamimplements.listclassbody_1, 0);
       pp(_classparamimplements.maybeblock_, 0);
       pp(_classparamimplements.listclassbody_2, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ConstrIdent foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.SinglConstrIdent)
    {
       bnfc.abs.Absyn.SinglConstrIdent _singlconstrident = (bnfc.abs.Absyn.SinglConstrIdent) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_singlconstrident.uident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ParamConstrIdent)
    {
       bnfc.abs.Absyn.ParamConstrIdent _paramconstrident = (bnfc.abs.Absyn.ParamConstrIdent) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_paramconstrident.uident_, 0);
       render("(");
       pp(_paramconstrident.listconstrtype_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ConstrType foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.EmptyConstrType)
    {
       bnfc.abs.Absyn.EmptyConstrType _emptyconstrtype = (bnfc.abs.Absyn.EmptyConstrType) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_emptyconstrtype.type_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.RecordConstrType)
    {
       bnfc.abs.Absyn.RecordConstrType _recordconstrtype = (bnfc.abs.Absyn.RecordConstrType) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_recordconstrtype.type_, 0);
       pp(_recordconstrtype.lident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListConstrType foo, int _i_)
  {
     for (java.util.Iterator<ConstrType> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ListUIdent foo, int _i_)
  {
     for (java.util.Iterator<String> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ListConstrIdent foo, int _i_)
  {
     for (java.util.Iterator<ConstrIdent> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("|");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.FunBody foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.BuiltinFunBody)
    {
       bnfc.abs.Absyn.BuiltinFunBody _builtinfunbody = (bnfc.abs.Absyn.BuiltinFunBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("builtin");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.NormalFunBody)
    {
       bnfc.abs.Absyn.NormalFunBody _normalfunbody = (bnfc.abs.Absyn.NormalFunBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_normalfunbody.pureexp_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.MethSignat foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.MethSig)
    {
       bnfc.abs.Absyn.MethSig _methsig = (bnfc.abs.Absyn.MethSig) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_methsig.type_, 0);
       pp(_methsig.lident_, 0);
       render("(");
       pp(_methsig.listparam_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListMethSignat foo, int _i_)
  {
     for (java.util.Iterator<MethSignat> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(";");
       } else {
         render(";");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ClassBody foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.FieldClassBody)
    {
       bnfc.abs.Absyn.FieldClassBody _fieldclassbody = (bnfc.abs.Absyn.FieldClassBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fieldclassbody.type_, 0);
       pp(_fieldclassbody.lident_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.FieldAssignClassBody)
    {
       bnfc.abs.Absyn.FieldAssignClassBody _fieldassignclassbody = (bnfc.abs.Absyn.FieldAssignClassBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_fieldassignclassbody.type_, 0);
       pp(_fieldassignclassbody.lident_, 0);
       render("=");
       pp(_fieldassignclassbody.pureexp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.MethClassBody)
    {
       bnfc.abs.Absyn.MethClassBody _methclassbody = (bnfc.abs.Absyn.MethClassBody) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_methclassbody.type_, 0);
       pp(_methclassbody.lident_, 0);
       render("(");
       pp(_methclassbody.listparam_, 0);
       render(")");
       pp(_methclassbody.block_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListClassBody foo, int _i_)
  {
     for (java.util.Iterator<ClassBody> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Block foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.Bloc)
    {
       bnfc.abs.Absyn.Bloc _bloc = (bnfc.abs.Absyn.Bloc) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_bloc.liststm_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.MaybeBlock foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.JustBlock)
    {
       bnfc.abs.Absyn.JustBlock _justblock = (bnfc.abs.Absyn.JustBlock) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_justblock.block_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.NoBlock)
    {
       bnfc.abs.Absyn.NoBlock _noblock = (bnfc.abs.Absyn.NoBlock) foo;
       if (_i_ > 0) render(_L_PAREN);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListParam foo, int _i_)
  {
     for (java.util.Iterator<Param> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Param foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.Par)
    {
       bnfc.abs.Absyn.Par _par = (bnfc.abs.Absyn.Par) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_par.type_, 0);
       pp(_par.lident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListStm foo, int _i_)
  {
     for (java.util.Iterator<Stm> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Stm foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.SExp)
    {
       bnfc.abs.Absyn.SExp _sexp = (bnfc.abs.Absyn.SExp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sexp.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SBlock)
    {
       bnfc.abs.Absyn.SBlock _sblock = (bnfc.abs.Absyn.SBlock) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_sblock.liststm_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SWhile)
    {
       bnfc.abs.Absyn.SWhile _swhile = (bnfc.abs.Absyn.SWhile) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("while");
       render("(");
       pp(_swhile.pureexp_, 0);
       render(")");
       pp(_swhile.stm_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SReturn)
    {
       bnfc.abs.Absyn.SReturn _sreturn = (bnfc.abs.Absyn.SReturn) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("return");
       pp(_sreturn.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SAss)
    {
       bnfc.abs.Absyn.SAss _sass = (bnfc.abs.Absyn.SAss) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sass.lident_, 0);
       render("=");
       pp(_sass.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SFieldAss)
    {
       bnfc.abs.Absyn.SFieldAss _sfieldass = (bnfc.abs.Absyn.SFieldAss) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("this");
       render(".");
       pp(_sfieldass.lident_, 0);
       render("=");
       pp(_sfieldass.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SDec)
    {
       bnfc.abs.Absyn.SDec _sdec = (bnfc.abs.Absyn.SDec) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sdec.type_, 0);
       pp(_sdec.lident_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SDecAss)
    {
       bnfc.abs.Absyn.SDecAss _sdecass = (bnfc.abs.Absyn.SDecAss) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sdecass.type_, 0);
       pp(_sdecass.lident_, 0);
       render("=");
       pp(_sdecass.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SIf)
    {
       bnfc.abs.Absyn.SIf _sif = (bnfc.abs.Absyn.SIf) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_sif.pureexp_, 0);
       render(")");
       pp(_sif.stm_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SIfElse)
    {
       bnfc.abs.Absyn.SIfElse _sifelse = (bnfc.abs.Absyn.SIfElse) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       render("(");
       pp(_sifelse.pureexp_, 0);
       render(")");
       pp(_sifelse.stm_1, 0);
       render("else");
       pp(_sifelse.stm_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SSuspend)
    {
       bnfc.abs.Absyn.SSuspend _ssuspend = (bnfc.abs.Absyn.SSuspend) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("suspend");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SSkip)
    {
       bnfc.abs.Absyn.SSkip _sskip = (bnfc.abs.Absyn.SSkip) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("skip");
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SAssert)
    {
       bnfc.abs.Absyn.SAssert _sassert = (bnfc.abs.Absyn.SAssert) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("assert");
       pp(_sassert.pureexp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SAwait)
    {
       bnfc.abs.Absyn.SAwait _sawait = (bnfc.abs.Absyn.SAwait) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("await");
       pp(_sawait.guard_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SThrow)
    {
       bnfc.abs.Absyn.SThrow _sthrow = (bnfc.abs.Absyn.SThrow) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("throw");
       pp(_sthrow.pureexp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.STryCatchFinally)
    {
       bnfc.abs.Absyn.STryCatchFinally _strycatchfinally = (bnfc.abs.Absyn.STryCatchFinally) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("try");
       pp(_strycatchfinally.stm_, 0);
       render("catch");
       render("{");
       pp(_strycatchfinally.listcatchbranch_, 0);
       render("}");
       pp(_strycatchfinally.maybefinally_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SPrint)
    {
       bnfc.abs.Absyn.SPrint _sprint = (bnfc.abs.Absyn.SPrint) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("println");
       pp(_sprint.pureexp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.CatchBranch foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.CatchBranc)
    {
       bnfc.abs.Absyn.CatchBranc _catchbranc = (bnfc.abs.Absyn.CatchBranc) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_catchbranc.pattern_, 0);
       render("=>");
       pp(_catchbranc.stm_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListCatchBranch foo, int _i_)
  {
     for (java.util.Iterator<CatchBranch> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.MaybeFinally foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.JustFinally)
    {
       bnfc.abs.Absyn.JustFinally _justfinally = (bnfc.abs.Absyn.JustFinally) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("finally");
       pp(_justfinally.stm_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.NoFinally)
    {
       bnfc.abs.Absyn.NoFinally _nofinally = (bnfc.abs.Absyn.NoFinally) foo;
       if (_i_ > 0) render(_L_PAREN);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.Guard foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.VarGuard)
    {
       bnfc.abs.Absyn.VarGuard _varguard = (bnfc.abs.Absyn.VarGuard) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_varguard.lident_, 0);
       render("?");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.FieldGuard)
    {
       bnfc.abs.Absyn.FieldGuard _fieldguard = (bnfc.abs.Absyn.FieldGuard) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("this");
       render(".");
       pp(_fieldguard.lident_, 0);
       render("?");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ExpGuard)
    {
       bnfc.abs.Absyn.ExpGuard _expguard = (bnfc.abs.Absyn.ExpGuard) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_expguard.pureexp_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.AndGuard)
    {
       bnfc.abs.Absyn.AndGuard _andguard = (bnfc.abs.Absyn.AndGuard) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_andguard.guard_1, 0);
       render("&");
       pp(_andguard.guard_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.Exp foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.ExpP)
    {
       bnfc.abs.Absyn.ExpP _expp = (bnfc.abs.Absyn.ExpP) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_expp.pureexp_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ExpE)
    {
       bnfc.abs.Absyn.ExpE _expe = (bnfc.abs.Absyn.ExpE) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_expe.effexp_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListPureExp foo, int _i_)
  {
     for (java.util.Iterator<PureExp> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.PureExp foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.EOr)
    {
       bnfc.abs.Absyn.EOr _eor = (bnfc.abs.Absyn.EOr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_eor.pureexp_1, 0);
       render("||");
       pp(_eor.pureexp_2, 1);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.Let)
    {
       bnfc.abs.Absyn.Let _let = (bnfc.abs.Absyn.Let) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("let");
       render("(");
       pp(_let.param_, 0);
       render(")");
       render("=");
       pp(_let.pureexp_1, 0);
       render("in");
       pp(_let.pureexp_2, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.If)
    {
       bnfc.abs.Absyn.If _if = (bnfc.abs.Absyn.If) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("if");
       pp(_if.pureexp_1, 0);
       render("then");
       pp(_if.pureexp_2, 0);
       render("else");
       pp(_if.pureexp_3, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.Case)
    {
       bnfc.abs.Absyn.Case _case = (bnfc.abs.Absyn.Case) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("case");
       pp(_case.pureexp_, 0);
       render("{");
       pp(_case.listcasebranch_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EAnd)
    {
       bnfc.abs.Absyn.EAnd _eand = (bnfc.abs.Absyn.EAnd) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_eand.pureexp_1, 1);
       render("&&");
       pp(_eand.pureexp_2, 2);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EEq)
    {
       bnfc.abs.Absyn.EEq _eeq = (bnfc.abs.Absyn.EEq) foo;
       if (_i_ > 2) render(_L_PAREN);
       pp(_eeq.pureexp_1, 2);
       render("==");
       pp(_eeq.pureexp_2, 3);
       if (_i_ > 2) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ENeq)
    {
       bnfc.abs.Absyn.ENeq _eneq = (bnfc.abs.Absyn.ENeq) foo;
       if (_i_ > 2) render(_L_PAREN);
       pp(_eneq.pureexp_1, 2);
       render("!=");
       pp(_eneq.pureexp_2, 3);
       if (_i_ > 2) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ELt)
    {
       bnfc.abs.Absyn.ELt _elt = (bnfc.abs.Absyn.ELt) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_elt.pureexp_1, 3);
       render("<");
       pp(_elt.pureexp_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ELe)
    {
       bnfc.abs.Absyn.ELe _ele = (bnfc.abs.Absyn.ELe) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_ele.pureexp_1, 3);
       render("<=");
       pp(_ele.pureexp_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EGt)
    {
       bnfc.abs.Absyn.EGt _egt = (bnfc.abs.Absyn.EGt) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_egt.pureexp_1, 3);
       render(">");
       pp(_egt.pureexp_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EGe)
    {
       bnfc.abs.Absyn.EGe _ege = (bnfc.abs.Absyn.EGe) foo;
       if (_i_ > 3) render(_L_PAREN);
       pp(_ege.pureexp_1, 3);
       render(">=");
       pp(_ege.pureexp_2, 4);
       if (_i_ > 3) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EAdd)
    {
       bnfc.abs.Absyn.EAdd _eadd = (bnfc.abs.Absyn.EAdd) foo;
       if (_i_ > 4) render(_L_PAREN);
       pp(_eadd.pureexp_1, 4);
       render("+");
       pp(_eadd.pureexp_2, 5);
       if (_i_ > 4) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ESub)
    {
       bnfc.abs.Absyn.ESub _esub = (bnfc.abs.Absyn.ESub) foo;
       if (_i_ > 4) render(_L_PAREN);
       pp(_esub.pureexp_1, 4);
       render("-");
       pp(_esub.pureexp_2, 5);
       if (_i_ > 4) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EMul)
    {
       bnfc.abs.Absyn.EMul _emul = (bnfc.abs.Absyn.EMul) foo;
       if (_i_ > 5) render(_L_PAREN);
       pp(_emul.pureexp_1, 5);
       render("*");
       pp(_emul.pureexp_2, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EDiv)
    {
       bnfc.abs.Absyn.EDiv _ediv = (bnfc.abs.Absyn.EDiv) foo;
       if (_i_ > 5) render(_L_PAREN);
       pp(_ediv.pureexp_1, 5);
       render("/");
       pp(_ediv.pureexp_2, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EMod)
    {
       bnfc.abs.Absyn.EMod _emod = (bnfc.abs.Absyn.EMod) foo;
       if (_i_ > 5) render(_L_PAREN);
       pp(_emod.pureexp_1, 5);
       render("%");
       pp(_emod.pureexp_2, 6);
       if (_i_ > 5) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ELogNeg)
    {
       bnfc.abs.Absyn.ELogNeg _elogneg = (bnfc.abs.Absyn.ELogNeg) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("~");
       pp(_elogneg.pureexp_, 6);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EIntNeg)
    {
       bnfc.abs.Absyn.EIntNeg _eintneg = (bnfc.abs.Absyn.EIntNeg) foo;
       if (_i_ > 6) render(_L_PAREN);
       render("-");
       pp(_eintneg.pureexp_, 6);
       if (_i_ > 6) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EFunCall)
    {
       bnfc.abs.Absyn.EFunCall _efuncall = (bnfc.abs.Absyn.EFunCall) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_efuncall.lident_, 0);
       render("(");
       pp(_efuncall.listpureexp_, 0);
       render(")");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EQualFunCall)
    {
       bnfc.abs.Absyn.EQualFunCall _equalfuncall = (bnfc.abs.Absyn.EQualFunCall) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_equalfuncall.ttype_, 0);
       pp(_equalfuncall.lident_, 0);
       render("(");
       pp(_equalfuncall.listpureexp_, 0);
       render(")");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ENaryFunCall)
    {
       bnfc.abs.Absyn.ENaryFunCall _enaryfuncall = (bnfc.abs.Absyn.ENaryFunCall) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_enaryfuncall.lident_, 0);
       render("[");
       pp(_enaryfuncall.listpureexp_, 0);
       render("]");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ENaryQualFunCall)
    {
       bnfc.abs.Absyn.ENaryQualFunCall _enaryqualfuncall = (bnfc.abs.Absyn.ENaryQualFunCall) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_enaryqualfuncall.ttype_, 0);
       pp(_enaryqualfuncall.lident_, 0);
       render("[");
       pp(_enaryqualfuncall.listpureexp_, 0);
       render("]");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EVar)
    {
       bnfc.abs.Absyn.EVar _evar = (bnfc.abs.Absyn.EVar) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_evar.lident_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EThis)
    {
       bnfc.abs.Absyn.EThis _ethis = (bnfc.abs.Absyn.EThis) foo;
       if (_i_ > 7) render(_L_PAREN);
       render("this");
       render(".");
       pp(_ethis.lident_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EQualVar)
    {
       bnfc.abs.Absyn.EQualVar _equalvar = (bnfc.abs.Absyn.EQualVar) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_equalvar.ttype_, 0);
       pp(_equalvar.lident_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ESinglConstr)
    {
       bnfc.abs.Absyn.ESinglConstr _esinglconstr = (bnfc.abs.Absyn.ESinglConstr) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_esinglconstr.qtype_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.EParamConstr)
    {
       bnfc.abs.Absyn.EParamConstr _eparamconstr = (bnfc.abs.Absyn.EParamConstr) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_eparamconstr.qtype_, 0);
       render("(");
       pp(_eparamconstr.listpureexp_, 0);
       render(")");
       if (_i_ > 7) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ELit)
    {
       bnfc.abs.Absyn.ELit _elit = (bnfc.abs.Absyn.ELit) foo;
       if (_i_ > 7) render(_L_PAREN);
       pp(_elit.literal_, 0);
       if (_i_ > 7) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.CaseBranch foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.CaseBranc)
    {
       bnfc.abs.Absyn.CaseBranc _casebranc = (bnfc.abs.Absyn.CaseBranc) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_casebranc.pattern_, 0);
       render("=>");
       pp(_casebranc.pureexp_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListCaseBranch foo, int _i_)
  {
     for (java.util.Iterator<CaseBranch> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(";");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.ListPattern foo, int _i_)
  {
     for (java.util.Iterator<Pattern> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.Pattern foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.PIdent)
    {
       bnfc.abs.Absyn.PIdent _pident = (bnfc.abs.Absyn.PIdent) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_pident.lident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.PLit)
    {
       bnfc.abs.Absyn.PLit _plit = (bnfc.abs.Absyn.PLit) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_plit.literal_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.PSinglConstr)
    {
       bnfc.abs.Absyn.PSinglConstr _psinglconstr = (bnfc.abs.Absyn.PSinglConstr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_psinglconstr.uident_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.PParamConstr)
    {
       bnfc.abs.Absyn.PParamConstr _pparamconstr = (bnfc.abs.Absyn.PParamConstr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_pparamconstr.uident_, 0);
       render("(");
       pp(_pparamconstr.listpattern_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.PUnderscore)
    {
       bnfc.abs.Absyn.PUnderscore _punderscore = (bnfc.abs.Absyn.PUnderscore) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("_");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.Literal foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.LNull)
    {
       bnfc.abs.Absyn.LNull _lnull = (bnfc.abs.Absyn.LNull) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("null");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.LThis)
    {
       bnfc.abs.Absyn.LThis _lthis = (bnfc.abs.Absyn.LThis) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("this");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.LThisDC)
    {
       bnfc.abs.Absyn.LThisDC _lthisdc = (bnfc.abs.Absyn.LThisDC) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("thisDC");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.LStr)
    {
       bnfc.abs.Absyn.LStr _lstr = (bnfc.abs.Absyn.LStr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_lstr.string_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.LInt)
    {
       bnfc.abs.Absyn.LInt _lint = (bnfc.abs.Absyn.LInt) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_lint.integer_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.LFalse)
    {
       bnfc.abs.Absyn.LFalse _lfalse = (bnfc.abs.Absyn.LFalse) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("False");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.LTrue)
    {
       bnfc.abs.Absyn.LTrue _ltrue = (bnfc.abs.Absyn.LTrue) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("True");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.EffExp foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.New)
    {
       bnfc.abs.Absyn.New _new = (bnfc.abs.Absyn.New) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("new");
       pp(_new.type_, 0);
       render("(");
       pp(_new.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.NewLocal)
    {
       bnfc.abs.Absyn.NewLocal _newlocal = (bnfc.abs.Absyn.NewLocal) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("new");
       render("local");
       pp(_newlocal.type_, 0);
       render("(");
       pp(_newlocal.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.SyncMethCall)
    {
       bnfc.abs.Absyn.SyncMethCall _syncmethcall = (bnfc.abs.Absyn.SyncMethCall) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_syncmethcall.pureexp_, 0);
       render(".");
       pp(_syncmethcall.lident_, 0);
       render("(");
       pp(_syncmethcall.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ThisSyncMethCall)
    {
       bnfc.abs.Absyn.ThisSyncMethCall _thissyncmethcall = (bnfc.abs.Absyn.ThisSyncMethCall) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("this");
       render(".");
       pp(_thissyncmethcall.lident_, 0);
       render("(");
       pp(_thissyncmethcall.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.AsyncMethCall)
    {
       bnfc.abs.Absyn.AsyncMethCall _asyncmethcall = (bnfc.abs.Absyn.AsyncMethCall) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_asyncmethcall.pureexp_, 0);
       render("!");
       pp(_asyncmethcall.lident_, 0);
       render("(");
       pp(_asyncmethcall.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.ThisAsyncMethCall)
    {
       bnfc.abs.Absyn.ThisAsyncMethCall _thisasyncmethcall = (bnfc.abs.Absyn.ThisAsyncMethCall) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("this");
       render("!");
       pp(_thisasyncmethcall.lident_, 0);
       render("(");
       pp(_thisasyncmethcall.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.Get)
    {
       bnfc.abs.Absyn.Get _get = (bnfc.abs.Absyn.Get) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_get.pureexp_, 0);
       render(".");
       render("get");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof bnfc.abs.Absyn.Spawns)
    {
       bnfc.abs.Absyn.Spawns _spawns = (bnfc.abs.Absyn.Spawns) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_spawns.pureexp_, 0);
       render("spawns");
       pp(_spawns.type_, 0);
       render("(");
       pp(_spawns.listpureexp_, 0);
       render(")");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.Ann foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.SimpleAnn)
    {
       bnfc.abs.Absyn.SimpleAnn _simpleann = (bnfc.abs.Absyn.SimpleAnn) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("[");
       pp(_simpleann.pureexp_, 0);
       render("]");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListAnn foo, int _i_)
  {
     for (java.util.Iterator<Ann> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.AnnDecl foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.AnnDec)
    {
       bnfc.abs.Absyn.AnnDec _anndec = (bnfc.abs.Absyn.AnnDec) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_anndec.listann_, 0);
       pp(_anndec.decl_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListAnnDecl foo, int _i_)
  {
     for (java.util.Iterator<AnnDecl> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }  }

  private static void pp(bnfc.abs.Absyn.AnnType foo, int _i_)
  {
    if (foo instanceof bnfc.abs.Absyn.AnnTyp)
    {
       bnfc.abs.Absyn.AnnTyp _anntyp = (bnfc.abs.Absyn.AnnTyp) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_anntyp.listann_, 0);
       pp(_anntyp.type_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(bnfc.abs.Absyn.ListAnnType foo, int _i_)
  {
     for (java.util.Iterator<AnnType> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), _i_);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }  }


  private static void sh(bnfc.abs.Absyn.AnyIdent foo)
  {
    if (foo instanceof bnfc.abs.Absyn.AnyIden)
    {
       bnfc.abs.Absyn.AnyIden _anyiden = (bnfc.abs.Absyn.AnyIden) foo;
       render("(");
       render("AnyIden");
       sh(_anyiden.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.AnyTyIden)
    {
       bnfc.abs.Absyn.AnyTyIden _anytyiden = (bnfc.abs.Absyn.AnyTyIden) foo;
       render("(");
       render("AnyTyIden");
       sh(_anytyiden.uident_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListAnyIdent foo)
  {
     for (java.util.Iterator<AnyIdent> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Program foo)
  {
    if (foo instanceof bnfc.abs.Absyn.Prog)
    {
       bnfc.abs.Absyn.Prog _prog = (bnfc.abs.Absyn.Prog) foo;
       render("(");
       render("Prog");
       render("[");
       sh(_prog.listmodule_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListModule foo)
  {
     for (java.util.Iterator<Module> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Module foo)
  {
    if (foo instanceof bnfc.abs.Absyn.Modul)
    {
       bnfc.abs.Absyn.Modul _modul = (bnfc.abs.Absyn.Modul) foo;
       render("(");
       render("Modul");
       sh(_modul.qtype_);
       render("[");
       sh(_modul.listexport_);
       render("]");
       render("[");
       sh(_modul.listimport_);
       render("]");
       render("[");
       sh(_modul.listdecl_);
       render("]");
       sh(_modul.maybeblock_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.Export foo)
  {
    if (foo instanceof bnfc.abs.Absyn.AnyExport)
    {
       bnfc.abs.Absyn.AnyExport _anyexport = (bnfc.abs.Absyn.AnyExport) foo;
       render("(");
       render("AnyExport");
       render("[");
       sh(_anyexport.listanyident_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.AnyFromExport)
    {
       bnfc.abs.Absyn.AnyFromExport _anyfromexport = (bnfc.abs.Absyn.AnyFromExport) foo;
       render("(");
       render("AnyFromExport");
       render("[");
       sh(_anyfromexport.listanyident_);
       render("]");
       sh(_anyfromexport.qtype_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.StarExport)
    {
       bnfc.abs.Absyn.StarExport _starexport = (bnfc.abs.Absyn.StarExport) foo;
       render("StarExport");
    }
    if (foo instanceof bnfc.abs.Absyn.StarFromExport)
    {
       bnfc.abs.Absyn.StarFromExport _starfromexport = (bnfc.abs.Absyn.StarFromExport) foo;
       render("(");
       render("StarFromExport");
       sh(_starfromexport.qtype_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListExport foo)
  {
     for (java.util.Iterator<Export> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Import foo)
  {
    if (foo instanceof bnfc.abs.Absyn.AnyImport)
    {
       bnfc.abs.Absyn.AnyImport _anyimport = (bnfc.abs.Absyn.AnyImport) foo;
       render("(");
       render("AnyImport");
       sh(_anyimport.importtype_);
       sh(_anyimport.ttype_);
       sh(_anyimport.anyident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.AnyFromImport)
    {
       bnfc.abs.Absyn.AnyFromImport _anyfromimport = (bnfc.abs.Absyn.AnyFromImport) foo;
       render("(");
       render("AnyFromImport");
       sh(_anyfromimport.importtype_);
       render("[");
       sh(_anyfromimport.listanyident_);
       render("]");
       sh(_anyfromimport.qtype_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.StarFromImport)
    {
       bnfc.abs.Absyn.StarFromImport _starfromimport = (bnfc.abs.Absyn.StarFromImport) foo;
       render("(");
       render("StarFromImport");
       sh(_starfromimport.importtype_);
       sh(_starfromimport.qtype_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListImport foo)
  {
     for (java.util.Iterator<Import> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ImportType foo)
  {
    if (foo instanceof bnfc.abs.Absyn.ForeignImport)
    {
       bnfc.abs.Absyn.ForeignImport _foreignimport = (bnfc.abs.Absyn.ForeignImport) foo;
       render("ForeignImport");
    }
    if (foo instanceof bnfc.abs.Absyn.NormalImport)
    {
       bnfc.abs.Absyn.NormalImport _normalimport = (bnfc.abs.Absyn.NormalImport) foo;
       render("NormalImport");
    }
  }

  private static void sh(bnfc.abs.Absyn.Type foo)
  {
    if (foo instanceof bnfc.abs.Absyn.TUnderscore)
    {
       bnfc.abs.Absyn.TUnderscore _tunderscore = (bnfc.abs.Absyn.TUnderscore) foo;
       render("TUnderscore");
    }
    if (foo instanceof bnfc.abs.Absyn.TSimple)
    {
       bnfc.abs.Absyn.TSimple _tsimple = (bnfc.abs.Absyn.TSimple) foo;
       render("(");
       render("TSimple");
       sh(_tsimple.qtype_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.TGen)
    {
       bnfc.abs.Absyn.TGen _tgen = (bnfc.abs.Absyn.TGen) foo;
       render("(");
       render("TGen");
       sh(_tgen.qtype_);
       render("[");
       sh(_tgen.listtype_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListType foo)
  {
     for (java.util.Iterator<Type> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ListQType foo)
  {
     for (java.util.Iterator<QType> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.QType foo)
  {
    if (foo instanceof bnfc.abs.Absyn.QTyp)
    {
       bnfc.abs.Absyn.QTyp _qtyp = (bnfc.abs.Absyn.QTyp) foo;
       render("(");
       render("QTyp");
       render("[");
       sh(_qtyp.listqtypesegment_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.QTypeSegment foo)
  {
    if (foo instanceof bnfc.abs.Absyn.QTypeSegmen)
    {
       bnfc.abs.Absyn.QTypeSegmen _qtypesegmen = (bnfc.abs.Absyn.QTypeSegmen) foo;
       render("(");
       render("QTypeSegmen");
       sh(_qtypesegmen.uident_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListQTypeSegment foo)
  {
     for (java.util.Iterator<QTypeSegment> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.TType foo)
  {
    if (foo instanceof bnfc.abs.Absyn.TTyp)
    {
       bnfc.abs.Absyn.TTyp _ttyp = (bnfc.abs.Absyn.TTyp) foo;
       render("(");
       render("TTyp");
       render("[");
       sh(_ttyp.listttypesegment_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.TTypeSegment foo)
  {
    if (foo instanceof bnfc.abs.Absyn.TTypeSegmen)
    {
       bnfc.abs.Absyn.TTypeSegmen _ttypesegmen = (bnfc.abs.Absyn.TTypeSegmen) foo;
       render("(");
       render("TTypeSegmen");
       sh(_ttypesegmen.uident_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListTTypeSegment foo)
  {
     for (java.util.Iterator<TTypeSegment> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ListDecl foo)
  {
     for (java.util.Iterator<Decl> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Decl foo)
  {
    if (foo instanceof bnfc.abs.Absyn.TypeDecl)
    {
       bnfc.abs.Absyn.TypeDecl _typedecl = (bnfc.abs.Absyn.TypeDecl) foo;
       render("(");
       render("TypeDecl");
       sh(_typedecl.uident_);
       sh(_typedecl.type_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.TypeParDecl)
    {
       bnfc.abs.Absyn.TypeParDecl _typepardecl = (bnfc.abs.Absyn.TypeParDecl) foo;
       render("(");
       render("TypeParDecl");
       sh(_typepardecl.uident_);
       render("[");
       sh(_typepardecl.listuident_);
       render("]");
       sh(_typepardecl.type_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ExceptionDecl)
    {
       bnfc.abs.Absyn.ExceptionDecl _exceptiondecl = (bnfc.abs.Absyn.ExceptionDecl) foo;
       render("(");
       render("ExceptionDecl");
       sh(_exceptiondecl.constrident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.DataDecl)
    {
       bnfc.abs.Absyn.DataDecl _datadecl = (bnfc.abs.Absyn.DataDecl) foo;
       render("(");
       render("DataDecl");
       sh(_datadecl.uident_);
       render("[");
       sh(_datadecl.listconstrident_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.DataParDecl)
    {
       bnfc.abs.Absyn.DataParDecl _datapardecl = (bnfc.abs.Absyn.DataParDecl) foo;
       render("(");
       render("DataParDecl");
       sh(_datapardecl.uident_);
       render("[");
       sh(_datapardecl.listuident_);
       render("]");
       render("[");
       sh(_datapardecl.listconstrident_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.FunDecl)
    {
       bnfc.abs.Absyn.FunDecl _fundecl = (bnfc.abs.Absyn.FunDecl) foo;
       render("(");
       render("FunDecl");
       sh(_fundecl.type_);
       sh(_fundecl.lident_);
       render("[");
       sh(_fundecl.listparam_);
       render("]");
       sh(_fundecl.funbody_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.FunParDecl)
    {
       bnfc.abs.Absyn.FunParDecl _funpardecl = (bnfc.abs.Absyn.FunParDecl) foo;
       render("(");
       render("FunParDecl");
       sh(_funpardecl.type_);
       sh(_funpardecl.lident_);
       render("[");
       sh(_funpardecl.listuident_);
       render("]");
       render("[");
       sh(_funpardecl.listparam_);
       render("]");
       sh(_funpardecl.funbody_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.InterfDecl)
    {
       bnfc.abs.Absyn.InterfDecl _interfdecl = (bnfc.abs.Absyn.InterfDecl) foo;
       render("(");
       render("InterfDecl");
       sh(_interfdecl.uident_);
       render("[");
       sh(_interfdecl.listmethsignat_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ExtendsDecl)
    {
       bnfc.abs.Absyn.ExtendsDecl _extendsdecl = (bnfc.abs.Absyn.ExtendsDecl) foo;
       render("(");
       render("ExtendsDecl");
       sh(_extendsdecl.uident_);
       render("[");
       sh(_extendsdecl.listqtype_);
       render("]");
       render("[");
       sh(_extendsdecl.listmethsignat_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ClassDecl)
    {
       bnfc.abs.Absyn.ClassDecl _classdecl = (bnfc.abs.Absyn.ClassDecl) foo;
       render("(");
       render("ClassDecl");
       sh(_classdecl.uident_);
       render("[");
       sh(_classdecl.listclassbody_1);
       render("]");
       sh(_classdecl.maybeblock_);
       render("[");
       sh(_classdecl.listclassbody_2);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ClassParamDecl)
    {
       bnfc.abs.Absyn.ClassParamDecl _classparamdecl = (bnfc.abs.Absyn.ClassParamDecl) foo;
       render("(");
       render("ClassParamDecl");
       sh(_classparamdecl.uident_);
       render("[");
       sh(_classparamdecl.listparam_);
       render("]");
       render("[");
       sh(_classparamdecl.listclassbody_1);
       render("]");
       sh(_classparamdecl.maybeblock_);
       render("[");
       sh(_classparamdecl.listclassbody_2);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ClassImplements)
    {
       bnfc.abs.Absyn.ClassImplements _classimplements = (bnfc.abs.Absyn.ClassImplements) foo;
       render("(");
       render("ClassImplements");
       sh(_classimplements.uident_);
       render("[");
       sh(_classimplements.listqtype_);
       render("]");
       render("[");
       sh(_classimplements.listclassbody_1);
       render("]");
       sh(_classimplements.maybeblock_);
       render("[");
       sh(_classimplements.listclassbody_2);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ClassParamImplements)
    {
       bnfc.abs.Absyn.ClassParamImplements _classparamimplements = (bnfc.abs.Absyn.ClassParamImplements) foo;
       render("(");
       render("ClassParamImplements");
       sh(_classparamimplements.uident_);
       render("[");
       sh(_classparamimplements.listparam_);
       render("]");
       render("[");
       sh(_classparamimplements.listqtype_);
       render("]");
       render("[");
       sh(_classparamimplements.listclassbody_1);
       render("]");
       sh(_classparamimplements.maybeblock_);
       render("[");
       sh(_classparamimplements.listclassbody_2);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ConstrIdent foo)
  {
    if (foo instanceof bnfc.abs.Absyn.SinglConstrIdent)
    {
       bnfc.abs.Absyn.SinglConstrIdent _singlconstrident = (bnfc.abs.Absyn.SinglConstrIdent) foo;
       render("(");
       render("SinglConstrIdent");
       sh(_singlconstrident.uident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ParamConstrIdent)
    {
       bnfc.abs.Absyn.ParamConstrIdent _paramconstrident = (bnfc.abs.Absyn.ParamConstrIdent) foo;
       render("(");
       render("ParamConstrIdent");
       sh(_paramconstrident.uident_);
       render("[");
       sh(_paramconstrident.listconstrtype_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ConstrType foo)
  {
    if (foo instanceof bnfc.abs.Absyn.EmptyConstrType)
    {
       bnfc.abs.Absyn.EmptyConstrType _emptyconstrtype = (bnfc.abs.Absyn.EmptyConstrType) foo;
       render("(");
       render("EmptyConstrType");
       sh(_emptyconstrtype.type_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.RecordConstrType)
    {
       bnfc.abs.Absyn.RecordConstrType _recordconstrtype = (bnfc.abs.Absyn.RecordConstrType) foo;
       render("(");
       render("RecordConstrType");
       sh(_recordconstrtype.type_);
       sh(_recordconstrtype.lident_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListConstrType foo)
  {
     for (java.util.Iterator<ConstrType> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ListUIdent foo)
  {
     for (java.util.Iterator<String> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ListConstrIdent foo)
  {
     for (java.util.Iterator<ConstrIdent> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.FunBody foo)
  {
    if (foo instanceof bnfc.abs.Absyn.BuiltinFunBody)
    {
       bnfc.abs.Absyn.BuiltinFunBody _builtinfunbody = (bnfc.abs.Absyn.BuiltinFunBody) foo;
       render("BuiltinFunBody");
    }
    if (foo instanceof bnfc.abs.Absyn.NormalFunBody)
    {
       bnfc.abs.Absyn.NormalFunBody _normalfunbody = (bnfc.abs.Absyn.NormalFunBody) foo;
       render("(");
       render("NormalFunBody");
       sh(_normalfunbody.pureexp_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.MethSignat foo)
  {
    if (foo instanceof bnfc.abs.Absyn.MethSig)
    {
       bnfc.abs.Absyn.MethSig _methsig = (bnfc.abs.Absyn.MethSig) foo;
       render("(");
       render("MethSig");
       sh(_methsig.type_);
       sh(_methsig.lident_);
       render("[");
       sh(_methsig.listparam_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListMethSignat foo)
  {
     for (java.util.Iterator<MethSignat> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ClassBody foo)
  {
    if (foo instanceof bnfc.abs.Absyn.FieldClassBody)
    {
       bnfc.abs.Absyn.FieldClassBody _fieldclassbody = (bnfc.abs.Absyn.FieldClassBody) foo;
       render("(");
       render("FieldClassBody");
       sh(_fieldclassbody.type_);
       sh(_fieldclassbody.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.FieldAssignClassBody)
    {
       bnfc.abs.Absyn.FieldAssignClassBody _fieldassignclassbody = (bnfc.abs.Absyn.FieldAssignClassBody) foo;
       render("(");
       render("FieldAssignClassBody");
       sh(_fieldassignclassbody.type_);
       sh(_fieldassignclassbody.lident_);
       sh(_fieldassignclassbody.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.MethClassBody)
    {
       bnfc.abs.Absyn.MethClassBody _methclassbody = (bnfc.abs.Absyn.MethClassBody) foo;
       render("(");
       render("MethClassBody");
       sh(_methclassbody.type_);
       sh(_methclassbody.lident_);
       render("[");
       sh(_methclassbody.listparam_);
       render("]");
       sh(_methclassbody.block_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListClassBody foo)
  {
     for (java.util.Iterator<ClassBody> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Block foo)
  {
    if (foo instanceof bnfc.abs.Absyn.Bloc)
    {
       bnfc.abs.Absyn.Bloc _bloc = (bnfc.abs.Absyn.Bloc) foo;
       render("(");
       render("Bloc");
       render("[");
       sh(_bloc.liststm_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.MaybeBlock foo)
  {
    if (foo instanceof bnfc.abs.Absyn.JustBlock)
    {
       bnfc.abs.Absyn.JustBlock _justblock = (bnfc.abs.Absyn.JustBlock) foo;
       render("(");
       render("JustBlock");
       sh(_justblock.block_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.NoBlock)
    {
       bnfc.abs.Absyn.NoBlock _noblock = (bnfc.abs.Absyn.NoBlock) foo;
       render("NoBlock");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListParam foo)
  {
     for (java.util.Iterator<Param> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Param foo)
  {
    if (foo instanceof bnfc.abs.Absyn.Par)
    {
       bnfc.abs.Absyn.Par _par = (bnfc.abs.Absyn.Par) foo;
       render("(");
       render("Par");
       sh(_par.type_);
       sh(_par.lident_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListStm foo)
  {
     for (java.util.Iterator<Stm> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Stm foo)
  {
    if (foo instanceof bnfc.abs.Absyn.SExp)
    {
       bnfc.abs.Absyn.SExp _sexp = (bnfc.abs.Absyn.SExp) foo;
       render("(");
       render("SExp");
       sh(_sexp.exp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SBlock)
    {
       bnfc.abs.Absyn.SBlock _sblock = (bnfc.abs.Absyn.SBlock) foo;
       render("(");
       render("SBlock");
       render("[");
       sh(_sblock.liststm_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SWhile)
    {
       bnfc.abs.Absyn.SWhile _swhile = (bnfc.abs.Absyn.SWhile) foo;
       render("(");
       render("SWhile");
       sh(_swhile.pureexp_);
       sh(_swhile.stm_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SReturn)
    {
       bnfc.abs.Absyn.SReturn _sreturn = (bnfc.abs.Absyn.SReturn) foo;
       render("(");
       render("SReturn");
       sh(_sreturn.exp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SAss)
    {
       bnfc.abs.Absyn.SAss _sass = (bnfc.abs.Absyn.SAss) foo;
       render("(");
       render("SAss");
       sh(_sass.lident_);
       sh(_sass.exp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SFieldAss)
    {
       bnfc.abs.Absyn.SFieldAss _sfieldass = (bnfc.abs.Absyn.SFieldAss) foo;
       render("(");
       render("SFieldAss");
       sh(_sfieldass.lident_);
       sh(_sfieldass.exp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SDec)
    {
       bnfc.abs.Absyn.SDec _sdec = (bnfc.abs.Absyn.SDec) foo;
       render("(");
       render("SDec");
       sh(_sdec.type_);
       sh(_sdec.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SDecAss)
    {
       bnfc.abs.Absyn.SDecAss _sdecass = (bnfc.abs.Absyn.SDecAss) foo;
       render("(");
       render("SDecAss");
       sh(_sdecass.type_);
       sh(_sdecass.lident_);
       sh(_sdecass.exp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SIf)
    {
       bnfc.abs.Absyn.SIf _sif = (bnfc.abs.Absyn.SIf) foo;
       render("(");
       render("SIf");
       sh(_sif.pureexp_);
       sh(_sif.stm_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SIfElse)
    {
       bnfc.abs.Absyn.SIfElse _sifelse = (bnfc.abs.Absyn.SIfElse) foo;
       render("(");
       render("SIfElse");
       sh(_sifelse.pureexp_);
       sh(_sifelse.stm_1);
       sh(_sifelse.stm_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SSuspend)
    {
       bnfc.abs.Absyn.SSuspend _ssuspend = (bnfc.abs.Absyn.SSuspend) foo;
       render("SSuspend");
    }
    if (foo instanceof bnfc.abs.Absyn.SSkip)
    {
       bnfc.abs.Absyn.SSkip _sskip = (bnfc.abs.Absyn.SSkip) foo;
       render("SSkip");
    }
    if (foo instanceof bnfc.abs.Absyn.SAssert)
    {
       bnfc.abs.Absyn.SAssert _sassert = (bnfc.abs.Absyn.SAssert) foo;
       render("(");
       render("SAssert");
       sh(_sassert.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SAwait)
    {
       bnfc.abs.Absyn.SAwait _sawait = (bnfc.abs.Absyn.SAwait) foo;
       render("(");
       render("SAwait");
       sh(_sawait.guard_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SThrow)
    {
       bnfc.abs.Absyn.SThrow _sthrow = (bnfc.abs.Absyn.SThrow) foo;
       render("(");
       render("SThrow");
       sh(_sthrow.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.STryCatchFinally)
    {
       bnfc.abs.Absyn.STryCatchFinally _strycatchfinally = (bnfc.abs.Absyn.STryCatchFinally) foo;
       render("(");
       render("STryCatchFinally");
       sh(_strycatchfinally.stm_);
       render("[");
       sh(_strycatchfinally.listcatchbranch_);
       render("]");
       sh(_strycatchfinally.maybefinally_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SPrint)
    {
       bnfc.abs.Absyn.SPrint _sprint = (bnfc.abs.Absyn.SPrint) foo;
       render("(");
       render("SPrint");
       sh(_sprint.pureexp_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.CatchBranch foo)
  {
    if (foo instanceof bnfc.abs.Absyn.CatchBranc)
    {
       bnfc.abs.Absyn.CatchBranc _catchbranc = (bnfc.abs.Absyn.CatchBranc) foo;
       render("(");
       render("CatchBranc");
       sh(_catchbranc.pattern_);
       sh(_catchbranc.stm_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListCatchBranch foo)
  {
     for (java.util.Iterator<CatchBranch> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.MaybeFinally foo)
  {
    if (foo instanceof bnfc.abs.Absyn.JustFinally)
    {
       bnfc.abs.Absyn.JustFinally _justfinally = (bnfc.abs.Absyn.JustFinally) foo;
       render("(");
       render("JustFinally");
       sh(_justfinally.stm_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.NoFinally)
    {
       bnfc.abs.Absyn.NoFinally _nofinally = (bnfc.abs.Absyn.NoFinally) foo;
       render("NoFinally");
    }
  }

  private static void sh(bnfc.abs.Absyn.Guard foo)
  {
    if (foo instanceof bnfc.abs.Absyn.VarGuard)
    {
       bnfc.abs.Absyn.VarGuard _varguard = (bnfc.abs.Absyn.VarGuard) foo;
       render("(");
       render("VarGuard");
       sh(_varguard.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.FieldGuard)
    {
       bnfc.abs.Absyn.FieldGuard _fieldguard = (bnfc.abs.Absyn.FieldGuard) foo;
       render("(");
       render("FieldGuard");
       sh(_fieldguard.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ExpGuard)
    {
       bnfc.abs.Absyn.ExpGuard _expguard = (bnfc.abs.Absyn.ExpGuard) foo;
       render("(");
       render("ExpGuard");
       sh(_expguard.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.AndGuard)
    {
       bnfc.abs.Absyn.AndGuard _andguard = (bnfc.abs.Absyn.AndGuard) foo;
       render("(");
       render("AndGuard");
       sh(_andguard.guard_1);
       sh(_andguard.guard_2);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.Exp foo)
  {
    if (foo instanceof bnfc.abs.Absyn.ExpP)
    {
       bnfc.abs.Absyn.ExpP _expp = (bnfc.abs.Absyn.ExpP) foo;
       render("(");
       render("ExpP");
       sh(_expp.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ExpE)
    {
       bnfc.abs.Absyn.ExpE _expe = (bnfc.abs.Absyn.ExpE) foo;
       render("(");
       render("ExpE");
       sh(_expe.effexp_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListPureExp foo)
  {
     for (java.util.Iterator<PureExp> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.PureExp foo)
  {
    if (foo instanceof bnfc.abs.Absyn.EOr)
    {
       bnfc.abs.Absyn.EOr _eor = (bnfc.abs.Absyn.EOr) foo;
       render("(");
       render("EOr");
       sh(_eor.pureexp_1);
       sh(_eor.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.Let)
    {
       bnfc.abs.Absyn.Let _let = (bnfc.abs.Absyn.Let) foo;
       render("(");
       render("Let");
       sh(_let.param_);
       sh(_let.pureexp_1);
       sh(_let.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.If)
    {
       bnfc.abs.Absyn.If _if = (bnfc.abs.Absyn.If) foo;
       render("(");
       render("If");
       sh(_if.pureexp_1);
       sh(_if.pureexp_2);
       sh(_if.pureexp_3);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.Case)
    {
       bnfc.abs.Absyn.Case _case = (bnfc.abs.Absyn.Case) foo;
       render("(");
       render("Case");
       sh(_case.pureexp_);
       render("[");
       sh(_case.listcasebranch_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EAnd)
    {
       bnfc.abs.Absyn.EAnd _eand = (bnfc.abs.Absyn.EAnd) foo;
       render("(");
       render("EAnd");
       sh(_eand.pureexp_1);
       sh(_eand.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EEq)
    {
       bnfc.abs.Absyn.EEq _eeq = (bnfc.abs.Absyn.EEq) foo;
       render("(");
       render("EEq");
       sh(_eeq.pureexp_1);
       sh(_eeq.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ENeq)
    {
       bnfc.abs.Absyn.ENeq _eneq = (bnfc.abs.Absyn.ENeq) foo;
       render("(");
       render("ENeq");
       sh(_eneq.pureexp_1);
       sh(_eneq.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ELt)
    {
       bnfc.abs.Absyn.ELt _elt = (bnfc.abs.Absyn.ELt) foo;
       render("(");
       render("ELt");
       sh(_elt.pureexp_1);
       sh(_elt.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ELe)
    {
       bnfc.abs.Absyn.ELe _ele = (bnfc.abs.Absyn.ELe) foo;
       render("(");
       render("ELe");
       sh(_ele.pureexp_1);
       sh(_ele.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EGt)
    {
       bnfc.abs.Absyn.EGt _egt = (bnfc.abs.Absyn.EGt) foo;
       render("(");
       render("EGt");
       sh(_egt.pureexp_1);
       sh(_egt.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EGe)
    {
       bnfc.abs.Absyn.EGe _ege = (bnfc.abs.Absyn.EGe) foo;
       render("(");
       render("EGe");
       sh(_ege.pureexp_1);
       sh(_ege.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EAdd)
    {
       bnfc.abs.Absyn.EAdd _eadd = (bnfc.abs.Absyn.EAdd) foo;
       render("(");
       render("EAdd");
       sh(_eadd.pureexp_1);
       sh(_eadd.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ESub)
    {
       bnfc.abs.Absyn.ESub _esub = (bnfc.abs.Absyn.ESub) foo;
       render("(");
       render("ESub");
       sh(_esub.pureexp_1);
       sh(_esub.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EMul)
    {
       bnfc.abs.Absyn.EMul _emul = (bnfc.abs.Absyn.EMul) foo;
       render("(");
       render("EMul");
       sh(_emul.pureexp_1);
       sh(_emul.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EDiv)
    {
       bnfc.abs.Absyn.EDiv _ediv = (bnfc.abs.Absyn.EDiv) foo;
       render("(");
       render("EDiv");
       sh(_ediv.pureexp_1);
       sh(_ediv.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EMod)
    {
       bnfc.abs.Absyn.EMod _emod = (bnfc.abs.Absyn.EMod) foo;
       render("(");
       render("EMod");
       sh(_emod.pureexp_1);
       sh(_emod.pureexp_2);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ELogNeg)
    {
       bnfc.abs.Absyn.ELogNeg _elogneg = (bnfc.abs.Absyn.ELogNeg) foo;
       render("(");
       render("ELogNeg");
       sh(_elogneg.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EIntNeg)
    {
       bnfc.abs.Absyn.EIntNeg _eintneg = (bnfc.abs.Absyn.EIntNeg) foo;
       render("(");
       render("EIntNeg");
       sh(_eintneg.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EFunCall)
    {
       bnfc.abs.Absyn.EFunCall _efuncall = (bnfc.abs.Absyn.EFunCall) foo;
       render("(");
       render("EFunCall");
       sh(_efuncall.lident_);
       render("[");
       sh(_efuncall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EQualFunCall)
    {
       bnfc.abs.Absyn.EQualFunCall _equalfuncall = (bnfc.abs.Absyn.EQualFunCall) foo;
       render("(");
       render("EQualFunCall");
       sh(_equalfuncall.ttype_);
       sh(_equalfuncall.lident_);
       render("[");
       sh(_equalfuncall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ENaryFunCall)
    {
       bnfc.abs.Absyn.ENaryFunCall _enaryfuncall = (bnfc.abs.Absyn.ENaryFunCall) foo;
       render("(");
       render("ENaryFunCall");
       sh(_enaryfuncall.lident_);
       render("[");
       sh(_enaryfuncall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ENaryQualFunCall)
    {
       bnfc.abs.Absyn.ENaryQualFunCall _enaryqualfuncall = (bnfc.abs.Absyn.ENaryQualFunCall) foo;
       render("(");
       render("ENaryQualFunCall");
       sh(_enaryqualfuncall.ttype_);
       sh(_enaryqualfuncall.lident_);
       render("[");
       sh(_enaryqualfuncall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EVar)
    {
       bnfc.abs.Absyn.EVar _evar = (bnfc.abs.Absyn.EVar) foo;
       render("(");
       render("EVar");
       sh(_evar.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EThis)
    {
       bnfc.abs.Absyn.EThis _ethis = (bnfc.abs.Absyn.EThis) foo;
       render("(");
       render("EThis");
       sh(_ethis.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EQualVar)
    {
       bnfc.abs.Absyn.EQualVar _equalvar = (bnfc.abs.Absyn.EQualVar) foo;
       render("(");
       render("EQualVar");
       sh(_equalvar.ttype_);
       sh(_equalvar.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ESinglConstr)
    {
       bnfc.abs.Absyn.ESinglConstr _esinglconstr = (bnfc.abs.Absyn.ESinglConstr) foo;
       render("(");
       render("ESinglConstr");
       sh(_esinglconstr.qtype_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.EParamConstr)
    {
       bnfc.abs.Absyn.EParamConstr _eparamconstr = (bnfc.abs.Absyn.EParamConstr) foo;
       render("(");
       render("EParamConstr");
       sh(_eparamconstr.qtype_);
       render("[");
       sh(_eparamconstr.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ELit)
    {
       bnfc.abs.Absyn.ELit _elit = (bnfc.abs.Absyn.ELit) foo;
       render("(");
       render("ELit");
       sh(_elit.literal_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.CaseBranch foo)
  {
    if (foo instanceof bnfc.abs.Absyn.CaseBranc)
    {
       bnfc.abs.Absyn.CaseBranc _casebranc = (bnfc.abs.Absyn.CaseBranc) foo;
       render("(");
       render("CaseBranc");
       sh(_casebranc.pattern_);
       sh(_casebranc.pureexp_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListCaseBranch foo)
  {
     for (java.util.Iterator<CaseBranch> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.ListPattern foo)
  {
     for (java.util.Iterator<Pattern> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.Pattern foo)
  {
    if (foo instanceof bnfc.abs.Absyn.PIdent)
    {
       bnfc.abs.Absyn.PIdent _pident = (bnfc.abs.Absyn.PIdent) foo;
       render("(");
       render("PIdent");
       sh(_pident.lident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.PLit)
    {
       bnfc.abs.Absyn.PLit _plit = (bnfc.abs.Absyn.PLit) foo;
       render("(");
       render("PLit");
       sh(_plit.literal_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.PSinglConstr)
    {
       bnfc.abs.Absyn.PSinglConstr _psinglconstr = (bnfc.abs.Absyn.PSinglConstr) foo;
       render("(");
       render("PSinglConstr");
       sh(_psinglconstr.uident_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.PParamConstr)
    {
       bnfc.abs.Absyn.PParamConstr _pparamconstr = (bnfc.abs.Absyn.PParamConstr) foo;
       render("(");
       render("PParamConstr");
       sh(_pparamconstr.uident_);
       render("[");
       sh(_pparamconstr.listpattern_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.PUnderscore)
    {
       bnfc.abs.Absyn.PUnderscore _punderscore = (bnfc.abs.Absyn.PUnderscore) foo;
       render("PUnderscore");
    }
  }

  private static void sh(bnfc.abs.Absyn.Literal foo)
  {
    if (foo instanceof bnfc.abs.Absyn.LNull)
    {
       bnfc.abs.Absyn.LNull _lnull = (bnfc.abs.Absyn.LNull) foo;
       render("LNull");
    }
    if (foo instanceof bnfc.abs.Absyn.LThis)
    {
       bnfc.abs.Absyn.LThis _lthis = (bnfc.abs.Absyn.LThis) foo;
       render("LThis");
    }
    if (foo instanceof bnfc.abs.Absyn.LThisDC)
    {
       bnfc.abs.Absyn.LThisDC _lthisdc = (bnfc.abs.Absyn.LThisDC) foo;
       render("LThisDC");
    }
    if (foo instanceof bnfc.abs.Absyn.LStr)
    {
       bnfc.abs.Absyn.LStr _lstr = (bnfc.abs.Absyn.LStr) foo;
       render("(");
       render("LStr");
       sh(_lstr.string_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.LInt)
    {
       bnfc.abs.Absyn.LInt _lint = (bnfc.abs.Absyn.LInt) foo;
       render("(");
       render("LInt");
       sh(_lint.integer_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.LFalse)
    {
       bnfc.abs.Absyn.LFalse _lfalse = (bnfc.abs.Absyn.LFalse) foo;
       render("LFalse");
    }
    if (foo instanceof bnfc.abs.Absyn.LTrue)
    {
       bnfc.abs.Absyn.LTrue _ltrue = (bnfc.abs.Absyn.LTrue) foo;
       render("LTrue");
    }
  }

  private static void sh(bnfc.abs.Absyn.EffExp foo)
  {
    if (foo instanceof bnfc.abs.Absyn.New)
    {
       bnfc.abs.Absyn.New _new = (bnfc.abs.Absyn.New) foo;
       render("(");
       render("New");
       sh(_new.type_);
       render("[");
       sh(_new.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.NewLocal)
    {
       bnfc.abs.Absyn.NewLocal _newlocal = (bnfc.abs.Absyn.NewLocal) foo;
       render("(");
       render("NewLocal");
       sh(_newlocal.type_);
       render("[");
       sh(_newlocal.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.SyncMethCall)
    {
       bnfc.abs.Absyn.SyncMethCall _syncmethcall = (bnfc.abs.Absyn.SyncMethCall) foo;
       render("(");
       render("SyncMethCall");
       sh(_syncmethcall.pureexp_);
       sh(_syncmethcall.lident_);
       render("[");
       sh(_syncmethcall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ThisSyncMethCall)
    {
       bnfc.abs.Absyn.ThisSyncMethCall _thissyncmethcall = (bnfc.abs.Absyn.ThisSyncMethCall) foo;
       render("(");
       render("ThisSyncMethCall");
       sh(_thissyncmethcall.lident_);
       render("[");
       sh(_thissyncmethcall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.AsyncMethCall)
    {
       bnfc.abs.Absyn.AsyncMethCall _asyncmethcall = (bnfc.abs.Absyn.AsyncMethCall) foo;
       render("(");
       render("AsyncMethCall");
       sh(_asyncmethcall.pureexp_);
       sh(_asyncmethcall.lident_);
       render("[");
       sh(_asyncmethcall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.ThisAsyncMethCall)
    {
       bnfc.abs.Absyn.ThisAsyncMethCall _thisasyncmethcall = (bnfc.abs.Absyn.ThisAsyncMethCall) foo;
       render("(");
       render("ThisAsyncMethCall");
       sh(_thisasyncmethcall.lident_);
       render("[");
       sh(_thisasyncmethcall.listpureexp_);
       render("]");
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.Get)
    {
       bnfc.abs.Absyn.Get _get = (bnfc.abs.Absyn.Get) foo;
       render("(");
       render("Get");
       sh(_get.pureexp_);
       render(")");
    }
    if (foo instanceof bnfc.abs.Absyn.Spawns)
    {
       bnfc.abs.Absyn.Spawns _spawns = (bnfc.abs.Absyn.Spawns) foo;
       render("(");
       render("Spawns");
       sh(_spawns.pureexp_);
       sh(_spawns.type_);
       render("[");
       sh(_spawns.listpureexp_);
       render("]");
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.Ann foo)
  {
    if (foo instanceof bnfc.abs.Absyn.SimpleAnn)
    {
       bnfc.abs.Absyn.SimpleAnn _simpleann = (bnfc.abs.Absyn.SimpleAnn) foo;
       render("(");
       render("SimpleAnn");
       sh(_simpleann.pureexp_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListAnn foo)
  {
     for (java.util.Iterator<Ann> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.AnnDecl foo)
  {
    if (foo instanceof bnfc.abs.Absyn.AnnDec)
    {
       bnfc.abs.Absyn.AnnDec _anndec = (bnfc.abs.Absyn.AnnDec) foo;
       render("(");
       render("AnnDec");
       render("[");
       sh(_anndec.listann_);
       render("]");
       sh(_anndec.decl_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListAnnDecl foo)
  {
     for (java.util.Iterator<AnnDecl> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(bnfc.abs.Absyn.AnnType foo)
  {
    if (foo instanceof bnfc.abs.Absyn.AnnTyp)
    {
       bnfc.abs.Absyn.AnnTyp _anntyp = (bnfc.abs.Absyn.AnnTyp) foo;
       render("(");
       render("AnnTyp");
       render("[");
       sh(_anntyp.listann_);
       render("]");
       sh(_anntyp.type_);
       render(")");
    }
  }

  private static void sh(bnfc.abs.Absyn.ListAnnType foo)
  {
     for (java.util.Iterator<AnnType> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }


  private static void pp(Integer n, int _i_) { buf_.append(n); buf_.append(" "); }
  private static void pp(Double d, int _i_) { buf_.append(d); buf_.append(" "); }
  private static void pp(String s, int _i_) { buf_.append(s); buf_.append(" "); }
  private static void pp(Character c, int _i_) { buf_.append("'" + c.toString() + "'"); buf_.append(" "); }
  private static void sh(Integer n) { render(n.toString()); }
  private static void sh(Double d) { render(d.toString()); }
  private static void sh(Character c) { render(c.toString()); }
  private static void sh(String s) { printQuoted(s); }
  private static void printQuoted(String s) { render("\"" + s + "\""); }
  private static void indent()
  {
    int n = _n_;
    while (n > 0)
    {
      buf_.append(" ");
      n--;
    }
  }
  private static void backup()
  {
     if (buf_.charAt(buf_.length() - 1) == ' ') {
      buf_.setLength(buf_.length() - 1);
    }
  }
  private static void trim()
  {
     while (buf_.length() > 0 && buf_.charAt(0) == ' ')
        buf_.deleteCharAt(0); 
    while (buf_.length() > 0 && buf_.charAt(buf_.length()-1) == ' ')
        buf_.deleteCharAt(buf_.length()-1);
  }
  private static int _n_ = 0;
  private static StringBuilder buf_ = new StringBuilder(INITIAL_BUFFER_SIZE);
}

