package eu.fbk.mIDAssistant;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GenerateDialogiGov extends DialogWrapper {

    private iGovGUI iGovGUI;

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    public GenerateDialogiGov(PsiClass psiClass) {
        super(psiClass.getProject());
        getPreferredSize();
        iGovGUI = new iGovGUI();
        init();
        setTitle("welcome to the mIDAssistant plugin for OIDC-iGov Profile");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return iGovGUI.getContent();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return super.doValidate();
    }
}
