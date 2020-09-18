package eu.fbk.mIDAssistant;

import com.google.common.collect.Lists;
import eu.fbk.mIDAssistant.Utill.InsertCodeUtils;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class InsertCodeHandlerGov {


    private static final List<String> PARAMETER_TYPE_ON_CREATE_METHOD = Lists.newArrayList(new String[] {"android.os.Bundle"});
    private static final List<String> IMPORT_CLASSES_LIST = Lists.newArrayList(new String[] {"android.content.Intent", "android.support.annotation.NonNull", "android.support.customtabs.CustomTabsIntent", "android.util.Log", "android.view.View", "android.widget.Button", "net.openid.appauth.AppAuthConfiguration", "net.openid.appauth.AuthState","net.openid.appauth.AuthorizationException", "net.openid.appauth.AuthorizationRequest", "net.openid.appauth.AuthorizationService", "net.openid.appauth.AuthorizationServiceConfiguration", "net.openid.appauth.ResponseTypeValues", "net.openid.appauth.browser.AnyBrowserMatcher", "net.openid.appauth.browser.BrowserMatcher" , "java.util.concurrent.CountDownLatch", "java.util.concurrent.atomic.AtomicReference", "java.util.HashMap", "java.security.SecureRandom","android.util.Base64"});
    private static  List<String> list2;
    private  PsiCodeBlock myActivityOnCreateMethod;
    private PsiClass myActivity;

    public InsertCodeHandlerGov(@NotNull PsiClass activity)
    {
        String btn_name = GenerateClassiGov.getInstance().getBtnName();
        this.myActivity = activity;
        this.myActivityOnCreateMethod = InsertCodeUtils.getMethodBodyByName("onCreate", PARAMETER_TYPE_ON_CREATE_METHOD, this.myActivity);
        list2 = Lists.newArrayList(new String[] {"mAuthStateManager = AuthStateManager.getInstance(this);", "mConfiguration = Configuration.getInstance(this);", btn_name + " = (Button) findViewById(R.id." + btn_name +");", btn_name + ".setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {\n" +
                "                startAuth();\n" +
                "            }});", "initializeAppAuth();" });
    }

    public void invoke(@NotNull final Project project)
    {

        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        assert (editor != null);
        new WriteCommandAction(project, new PsiFile[0])
        {
            protected void run(@NotNull Result result)
                    throws Throwable
            {
                Pair<PsiElement, PsiElement> psiElementPair = InsertCodeUtils.maybeAddStatementsToMethod(InsertCodeHandlerGov.list2, InsertCodeHandlerGov.this.myActivityOnCreateMethod, project);
                if (psiElementPair == null) {
                    return;
                }
                InsertCodeUtils.maybeInsertImportList(InsertCodeHandlerGov.IMPORT_CLASSES_LIST, InsertCodeHandlerGov.this.myActivity, project);
                //InsertCodeUtils.addCommentsBefore(InsertCodeHandler.COMMENT_IN_JAVA, InsertCodeHandler.this.myActivityOnCreateMethod, project, (PsiElement)psiElementPair.first);
                CodeStyleManager.getInstance(project).reformat(InsertCodeHandlerGov.this.myActivityOnCreateMethod);

                editor.getSelectionModel().setSelection(((PsiElement)psiElementPair.first).getTextOffset(), ((PsiElement)psiElementPair.second)
                        .getTextOffset() + ((PsiElement)psiElementPair.second).getTextLength());
                editor.getScrollingModel().scrollTo(editor.offsetToLogicalPosition(((PsiElement)psiElementPair.first).getTextOffset()), ScrollType.CENTER_UP);
            }
        }.execute();
    }

}
