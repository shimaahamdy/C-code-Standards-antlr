import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
public class TestC {
    public static void main (String[] args) throws  Exception
    {
        String inputfile = "t.expr";  //inputfile
        FileInputStream is = new FileInputStream(inputfile);
        ANTLRInputStream input = new ANTLRInputStream(is);
        CLexer lexr = new CLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexr);
        CParser parser = new CParser(tokens);
        ParseTree tree = parser.compilationUnit();

        //System.out.println(tree.toStringTree(parser));
        //ParseTreeWalker parsseTreeWalker = new ParseTreeWalker();
        //parseTreeWalker.walk(new calcualator(), tree );

        CStandards calc = new CStandards();
        calc.visit(tree);
    }

}
