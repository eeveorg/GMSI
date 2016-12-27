/*
 * Decompiled with CFR 0_119.
 */
package script.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import script.LRhighTokens.Statement;
import script.Token;
import script.parse.Aktion;
import script.parse.Production;
import script.parse.SyntaxTable;

public class ErrorMessageGenerator {
    private static HashMap<String, String> aliases = null;

    private static void generateAliases() {
        if (aliases != null) {
            return;
        }
        aliases = new HashMap();
        aliases.put("Comma", "Comma \",\"");
        aliases.put("WordExtern", "Keyword \"extern\"");
        aliases.put("DereferOperator", "Derefer Operator \"&\"");
        aliases.put("StringConstant", "String Literal");
        aliases.put("WordExtends", "Keyword \"extends\"");
        aliases.put("OpenScriptBracket", "Start of block statement \"{\"");
        aliases.put("OpenBracket", "Opening Bracket \"(\"");
        aliases.put("CloseBracket", "Closing Bracket \")\"");
        aliases.put("OpenSharpBracket", "Opening Sharp Bracket \"<\"");
        aliases.put("CloseSharpBracket", "Closing Sharp Bracket \">\"");
        aliases.put("Semicolon", "End of statement \";\"");
        aliases.put("WordTypeof", "Keyword \"typeof\"");
        aliases.put("Identifyer", "Var or Function Name");
        aliases.put("SimpleTypeName", "Simple Type Name, example: \"int\"");
        aliases.put("Assignment", "Assignment \"=\"");
        aliases.put("Colon", "Colon \":\"");
    }

    private static String alias(String s) {
        if (aliases.containsKey(s)) {
            return aliases.get(s);
        }
        System.out.println("No alias for " + s);
        return s;
    }

    public static void buildErrorTable(SyntaxTable t) throws IOException {
        ErrorMessageGenerator.generateAliases();
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("misc/parseErrors.err")));
        for (Integer i : t.aktion.keySet()) {
            String output;
            t.aktion.get(i).remove("SRART");
            if (t.aktion.get(i).keySet().size() <= 1) {
                for (String s : t.aktion.get(i).keySet()) {
                    out.write("@" + i + ":Expected: " + ErrorMessageGenerator.alias(s) + "\n");
                }
                continue;
            }
            if (t.aktion.get(i).keySet().size() <= 2) {
                output = "";
                for (String s : t.aktion.get(i).keySet()) {
                    output = String.valueOf(output) + " or " + ErrorMessageGenerator.alias(s);
                }
                out.write("@" + i + ":Expected: " + output.substring(4) + "\n");
                continue;
            }
            if (t.aktion.get(i).keySet().size() <= 4) {
                output = "";
                for (String s : t.aktion.get(i).keySet()) {
                    output = String.valueOf(output) + ErrorMessageGenerator.alias(s) + "\n";
                }
                out.write("@" + i + ":Expected one of the following:\n" + output);
                continue;
            }
            if (!t.aktion.get(i).containsKey("Semicolon") || ((Aktion)t.aktion.get((Object)i).get((Object)"Semicolon")).type != 1) continue;
            Aktion a = (Aktion)t.aktion.get(i).get("Semicolon");
            Production p = t.getProduction(a.zustand);
            if (Statement.class.isAssignableFrom(p.lValue) || Statement.class.isAssignableFrom(p.rValue.get((int)(p.rValue.size() - 1)).wantedClass)) {
                System.out.println("GEFUNDEN:" + p.lValue);
                continue;
            }
            out.write("@" + i + ": " + i + "Insert semicolon \";\" to finish previous statement\n");
        }
        out.close();
    }
}

