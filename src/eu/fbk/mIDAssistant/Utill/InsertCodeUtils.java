package eu.fbk.mIDAssistant.Utill;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.JVMElementFactories;
import com.intellij.psi.JVMElementFactory;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.siyeh.ig.psiutils.ImportUtils;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class InsertCodeUtils {

    // add a list of import into the project

    public static void maybeInsertImportList(@NotNull List<String> importClassesList, @NotNull PsiClass activity, @NotNull Project project) {
        for (String importClass : importClassesList) {
            maybeInsertImportStatement(importClass, activity, project);
        }
    }

    private static boolean maybeInsertImportStatement(@NotNull String className, @NotNull PsiClass activity, @NotNull Project project) {
        PsiImportList importList = ((PsiJavaFile) activity.getContainingFile()).getImportList();
        if (importList == null) {
            return false;
        }
        if (ImportUtils.hasOnDemandImportConflict(className, activity) || !ImportUtils.nameCanBeImported(className, activity) || hasImportStatement(importList, className)) {
            return false;
        }
        PsiImportList dummyImportList = ((PsiJavaFile) PsiFileFactory.getInstance(project).createFileFromText("_Dummy_" + className + "_." + JavaFileType.INSTANCE.getDefaultExtension(), JavaFileType.INSTANCE, "import " + className + ";")).getImportList();
        if (dummyImportList == null) {
            return false;
        }
        importList.add((PsiImportStatement) CodeStyleManager.getInstance(project).reformat(dummyImportList.getImportStatements()[0]));
        return true;
    }


    public static PsiCodeBlock getMethodBodyByName(@NotNull String name, @NotNull List<String> parametersTypeName, @NotNull PsiClass psiClass)
    {
        PsiMethod[] psiMethods = psiClass.findMethodsByName(name, false);
        for (PsiMethod psiMethod : psiMethods)
        {
            PsiType[] types = psiMethod.getSignature(PsiSubstitutor.EMPTY).getParameterTypes();
            if (types.length == parametersTypeName.size())
            {
                boolean correctSignature = true;
                for (int i = 0; i < types.length; i++) {
                    if (!types[i].getCanonicalText().equals(parametersTypeName.get(i)))
                    {
                        correctSignature = false;
                        break;
                    }
                }
                if (correctSignature) {
                    return psiMethod.getBody();
                }
            }
        }
        return null;
    }
    public static Pair<PsiElement, PsiElement> maybeAddStatementsToMethod(@NotNull List<String> statementsList, @NotNull PsiCodeBlock method, @NotNull Project project) {
        PsiElement beginElement = null;
        PsiElement endElement = null;
        JVMElementFactory elementFactory = JVMElementFactories.getFactory(JavaLanguage.INSTANCE, project);
        if (elementFactory == null) {
            elementFactory = JavaPsiFacade.getElementFactory(project);
        }
        for (String statement : statementsList) {
            PsiElement element = maybeAddStatementToMethod(statement, method, (PsiElementFactory) elementFactory);
            if (element != null) {
                if (beginElement == null) {
                    beginElement = element;
                }
                endElement = element;
            }
        }
        return beginElement == null ? null : Pair.create(beginElement, endElement);
    }



    private static PsiElement maybeAddStatementToMethod(@NotNull String statement, @NotNull PsiCodeBlock body, @NotNull PsiElementFactory factory) {
        if (StatementFilter.filterCodeBlock(statement, body).isEmpty()) {
            return body.add(factory.createStatementFromText(statement, null));
        }
        return null;
    }

    public static boolean hasImportStatement(@NotNull PsiImportList importList, @NotNull String className) {
        String packageName = className.substring(0, className.lastIndexOf(46));
        PsiImportStatement singleImport = importList.findSingleClassImportStatement(className);
        PsiImportStatement onDemandImport = importList.findOnDemandImportStatement(packageName);
        if (singleImport == null && onDemandImport == null) {
            return false;
        }
        return true;
    }


}
