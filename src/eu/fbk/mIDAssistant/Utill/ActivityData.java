package eu.fbk.mIDAssistant.Utill;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActivityData implements AppLinkTableCell {

    @Nullable
    private final PsiClass myActivityClass;
    private String myActivityName;
    private XmlTag myIntentFilterTag;
    private Module myModule;

    public ActivityData(@NotNull Module module, @NotNull String activityName, @Nullable XmlTag intentFilterTag, @Nullable PsiClass activityClass) {
        this.myModule = module;
        this.myActivityName = activityName;
        this.myIntentFilterTag = intentFilterTag;
        this.myActivityClass = activityClass;
    }

    @Nullable
    public PsiClass getActivityClass() {
        return this.myActivityClass;
    }

    @Nullable
    public VirtualFile getActivityVirtualFile() {
        return this.myActivityClass == null ? null : this.myActivityClass.getContainingFile().getVirtualFile();
    }

    @NotNull
    public String getActivityName() {
        return this.myActivityName;
    }

    @NotNull
    public String getActivityAndModuleName() {
        return String.format("%s (%s)", new Object[]{this.myActivityName, getModuleName()});
    }

    @NotNull
    public String getModuleName() {
        return this.myModule.getName();
    }

    @NotNull
    public Module getModule() {
        return this.myModule;
    }

    public void setModule(@NotNull Module module) {
        this.myModule = module;
    }

    @Nullable
    public XmlTag getIntentFilterTag() {
        return this.myIntentFilterTag;
    }

    public void setIntentFilterTag(@NotNull XmlTag intentFilterTag) {
        this.myIntentFilterTag = intentFilterTag;
    }

    @NotNull
    public String getHtmlText(boolean isSelected) {
        return AppLinkTableCell.generateHtmlText(this.myActivityName, getModuleName(), isSelected);
    }

    public String toString() {
        return AppLinkTableCell.generateRegularText(this.myActivityName, getModuleName());
    }


}
