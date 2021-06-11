//coding
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.stringtemplate.v4.ST;


import java.io.FileReader;
import java.util.Locale;

public class CStandards extends CBaseVisitor{

    String inputfile = "t.expr";  //inputfile
    boolean global=false;            // check if we in global area -->true

    /* *********************************************************************************************************************************** */

    //read json file and return the wanted pattern
    public String get_pattern(String pattern)
    {

        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("config"));
            JSONObject jsonObject = (JSONObject)obj;
            pattern = (String)jsonObject.get(pattern);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return pattern;
    }

    /* ********************************************************************************************************************************************** */

    //enum global   // local

    @Override public Integer visitGlobal(CParser.GlobalContext ctx)
    {
        global=true;
        visit(ctx.declaration());
        return 0 ;
    }
    @Override public Integer visitDeclarationSpecifierstatm(CParser.DeclarationSpecifierstatmContext ctx)
    {
        visit(ctx.declarationSpecifiers());
        return 0;
    }
    @Override public Integer visitSpecifierstatment(CParser.SpecifierstatmentContext ctx)
    {
        if(ctx.getChildCount()==1)
            visit(ctx.getChild(0));
        if(ctx.getChildCount()==2)
            visit(ctx.getChild(1));

        return 0;
    }
    @Override public Integer visitTypestata(CParser.TypestataContext ctx)
    {
        if(ctx.getChildCount()==1)
            visit(ctx.getChild(0));
        if(ctx.getChildCount()==2)
            visit(ctx.getChild(1));
        return 0;
    }
    @Override public Integer visitOi(CParser.OiContext ctx)
    {
        visit(ctx.enumSpecifier());
        return 0;
    }

    @Override public Integer visitEnumdefination(CParser.EnumdefinationContext ctx)
    {
        if(global)
        {
            String pattern = get_pattern("Global_enum_pattern");
            String enum_id = ctx.Identifier().getText();
            if(enum_id.matches(pattern));
            else
            {
                int line = ctx.getStart().getLine();
                System.out.println("error in file  "+inputfile);
                System.out.print("line number "+line);
                System.out.println(": error \" enum " + (enum_id) + " \" should be like enum_"+enum_id.toUpperCase());
            }

        }
        else
        {
            String pattern = get_pattern("local_enum_pattern");
            String enum_id = ctx.Identifier().getText();
            if (enum_id.matches(pattern)) ;
            else {
                int line = ctx.getStart().getLine();
                System.out.println("error in file  "+inputfile);
                System.out.print("line number " + line);
                System.out.println(": error \" enum " + (enum_id) + " \" should be like \"enum_"+enum_id.toLowerCase()+"\"");
            }
        }
        global=false;


        return 0;
    }


    // visit start node in local area
    @Override public Integer visitDeclarationstat(CParser.DeclarationstatContext ctx)
    {
        visit(ctx.declaration());
        return 0;
    }


    /* **************************************************************************************************************************************** */


    //typedefName


    @Override public Integer visitVar(CParser.VarContext ctx)
    {
        visit(ctx.typedefName());
        return 0;
    }


    //Identifier
    @Override public Integer visitTypedefName(CParser.TypedefNameContext ctx)
    {

        if(global) {
            global=false;
            String var_global = ctx.Identifier().getText();


            String pattern = get_pattern("global_var");
            if (var_global.matches(pattern)) ;
            else {
                int line = ctx.getStart().getLine();
                System.out.println("error in file  "+inputfile);
                System.out.print("line number " + line);
                System.out.println(": error \""+var_global+"\" should be like \""+var_global.toUpperCase()+"\"");
            }
        }
        else
        {
            //here
            String var_local = ctx.Identifier().getText();
            String pattern = get_pattern("local_var");
            if (var_local.matches(pattern)) ;
            else {
                int line = ctx.getStart().getLine();
                System.out.println("error in file  "+inputfile);
                System.out.print("line number " + line);
                System.out.println(": error \""+var_local+"\" should be like \""+var_local.toLowerCase()+"\"");
            }
        }

        return 0;
    }







    ////////////////////////////////////////////////////////////////////
    //function identifier
    //declarationSpecifiers? declarator declarationList? compoundStatement




    //pointer? directDeclarator gccDeclaratorExtension*
    @Override public Integer visitDeclarator(CParser.DeclaratorContext ctx)
    {
        String pattern = get_pattern("function_pattern");
        String funcid = visit(ctx.directDeclarator()).toString();
        if(funcid.matches(pattern));
        else
        {
            int line = ctx.getStart().getLine();
            System.out.println("error in file  "+inputfile);
            System.out.print("line number "+line);
            System.out.println(": error \""+funcid+"\" should be like \""+"func_"+funcid+"\"");
        }
        return 0;
    }

    //directDeclarator '(' parameterTypeList ')'
    //directDeclarator '(' identifierList? ')'
    @Override public String visitFuncpars(CParser.FuncparsContext ctx)
    {
        String funcid = visit(ctx.directDeclarator()).toString();
        return funcid;
    }

    //id
    @Override public String visitFuncid(CParser.FuncidContext ctx)
    {
        String funcid = ctx.Identifier().getText();
        return funcid;
    }



    @Override public Integer visitStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx)
    {
        String struct_union = ctx.structOrUnion().getText();
        if(global) {
            if (struct_union.equals("struct")) {

                String pattern = get_pattern("Global_struct_pattern");
                String struct_id = ctx.Identifier().getText();
                if (struct_id.matches(pattern)) ;
                else {
                    int line = ctx.getStart().getLine();
                    System.out.println("error in file  "+inputfile);
                    System.out.print("line number " + line);
                    System.out.println(": error \" struct " + (struct_id) + " \" should be like struct_"+struct_id.toUpperCase());
                }
            } else {
                String pattern = get_pattern("Global_union_pattern");
                String union_id = ctx.Identifier().getText();
                if (union_id.matches(pattern)) ;
                else {
                    int line = ctx.getStart().getLine();
                    System.out.println("error in file  "+inputfile);
                    System.out.print("line number " + line);
                    System.out.println(": error \" union " + (union_id) + " \" should be like union_"+union_id.toUpperCase());
                }
            }
        }
        else
        {
            if (struct_union.equals("struct")) {

                String pattern = get_pattern("local_struct_pattern");
                String struct_id = ctx.Identifier().getText();
                if (struct_id.matches(pattern)) ;
                else {
                    int line = ctx.getStart().getLine();
                    System.out.println("error in file  "+inputfile);
                    System.out.print("line number " + line);
                    System.out.println(": error \" struct " + (struct_id) + " \" should be like struct_"+struct_id.toLowerCase());
                }
            } else {
                String pattern = get_pattern("local_union_pattern");
                String union_id = ctx.Identifier().getText();
                if (union_id.matches(pattern)) ;
                else {
                    int line = ctx.getStart().getLine();
                    System.out.println("error in file  "+inputfile);
                    System.out.print("line number "+line);
                    System.out.println(": error \" union " + (union_id) + " \" should be like union_"+union_id.toLowerCase());
                }
            }
        }

        global=false;


        return 0;
    }
    @Override public Integer visitGlobalstructorunion(CParser.GlobalstructorunionContext ctx)
    {
        visit(ctx.structOrUnionSpecifier());
        return 0;
    }







}
