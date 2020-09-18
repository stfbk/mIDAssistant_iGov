package eu.fbk.mIDAssistant.Utill;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.intellij.psi.*;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class StatementFilter
        extends JavaRecursiveElementVisitor
{
    private String myFilterString;
    private List<PsiStatement> myStatements = Lists.newArrayList();

    public StatementFilter(@NotNull String filterString)
    {
        this.myFilterString = CharMatcher.WHITESPACE.removeFrom(filterString);
    }

    @NotNull
    public List<PsiStatement> getStatements()
    {
        return this.myStatements;
    }

    public void visitExpressionStatement(PsiExpressionStatement statement)
    {
        if (CharMatcher.WHITESPACE.removeFrom(statement.getText()).contains(this.myFilterString)) {
            this.myStatements.add(statement);
        }
    }

    public void visitDeclarationStatement(PsiDeclarationStatement statement)
    {
        if (CharMatcher.WHITESPACE.removeFrom(statement.getText()).contains(this.myFilterString)) {
            this.myStatements.add(statement);
        }
    }

    @NotNull
    public static List<PsiStatement> filterCodeBlock(@NotNull String filterString, @NotNull PsiCodeBlock codeBlock)
    {
        StatementFilter filter = new StatementFilter(filterString);
        codeBlock.accept(filter);
        return filter.getStatements();
    }

    @NotNull
    public static List<PsiStatement> filterStatements(@NotNull String filterString, @NotNull List<PsiStatement> statements)
    {
        StatementFilter filter = new StatementFilter(filterString);
        for (PsiStatement statement : statements) {
            statement.accept(filter);
        }
        return filter.getStatements();
    }
}
