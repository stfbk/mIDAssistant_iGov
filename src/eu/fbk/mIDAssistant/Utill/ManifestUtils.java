package eu.fbk.mIDAssistant.Utill;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.android.dom.manifest.Activity;
import org.jetbrains.android.dom.manifest.IntentFilter;
import org.jetbrains.android.dom.manifest.Manifest;
import org.jetbrains.android.facet.AndroidFacet;
import org.jetbrains.android.util.AndroidResourceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManifestUtils {

    public static final String ACTION_VIEW = "android.intent.action.VIEW";
    private static final String ATTRIBUTE_TEST_URL = "testUrl";
    public static final String CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE";
    public static final String ORDER = "order";
    private static final String TAG_VALIDATION = "validation";

    @NotNull
    public static List<ActivityData> getAllActivities(@NotNull Module[] modules, boolean supportsAppLinks, boolean mustHaveSourceFile) {
        List<ActivityData> result = new ArrayList();
        for (Module module : modules) {
            getAllActivitiesFromModule(module, result, supportsAppLinks, mustHaveSourceFile);
        }
        return result;
    }

    private static void getAllActivitiesFromModule(@NotNull Module module, @NotNull List<ActivityData> resultList, boolean supportsAppLinks, boolean mustHaveSourceFile) {
        AndroidFacet facet = AndroidFacet.getInstance(module);
        if (facet != null) {
            Manifest manifest = facet.getManifest();
            if (manifest != null && manifest.getModule() != null && manifest.getApplication() != null) {
                for (Activity activity : manifest.getApplication().getActivities()) {
                    String activityName = activity.getActivityClass().getStringValue();
                    if (activityName != null) {
                        PsiClass activityClass = (PsiClass) activity.getActivityClass().getValue();
                        if (!mustHaveSourceFile || (activityClass != null && activityClass.getContainingFile().getVirtualFile() != null)) {
                            if (supportsAppLinks) {
                                for (IntentFilter filter : activity.getIntentFilters()) {
                                    XmlTag intentFilter = filter.getXmlTag();
                                    if (isValidAppLink(intentFilter)) {
                                        resultList.add(new ActivityData(module, activityName, intentFilter, activityClass));
                                        break;
                                    }
                                }
                            }
                            resultList.add(new ActivityData(module, activityName, null, activityClass));
                        }
                    }
                }
            }
        }
    }

    public static boolean isValidAppLink(@NotNull XmlTag intentFilter) {
        if (!isValidDeepLink(intentFilter)) {
            return false;
        }
        int hostCnt = 0;
        boolean hasScheme = false;
        for (XmlTag data : searchXmlTagsByName(intentFilter, "data")) {
            String scheme = data.getAttributeValue("scheme", "http://schemas.android.com/apk/res/android");
            if (FullUrl.SCHEME_HTTP.equals(scheme) || FullUrl.SCHEME_HTTPS.equals(scheme)) {
                hasScheme = true;
            }
            if (!StringUtil.isEmpty(data.getAttributeValue("host", "http://schemas.android.com/apk/res/android"))) {
                hostCnt++;
            }
            if (hostCnt > 0 && hasScheme) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidDeepLink(@NotNull XmlTag intentFilter) {
        boolean hasCorrectAction = false;
        for (XmlTag action : searchXmlTagsByName(intentFilter, "action")) {
            if (ACTION_VIEW.equals(action.getAttributeValue("name", "http://schemas.android.com/apk/res/android"))) {
                hasCorrectAction = true;
                break;
            }
        }
        if (!hasCorrectAction) {
            return false;
        }
        boolean hasCategoryDefault = false;
        boolean hasCategoryBrowsable = false;
        for (XmlTag category : searchXmlTagsByName(intentFilter, "category")) {
            String name = category.getAttributeValue("name", "http://schemas.android.com/apk/res/android");
            if ("android.intent.category.DEFAULT".equals(name)) {
                hasCategoryDefault = true;
            }
            if (CATEGORY_BROWSABLE.equals(name)) {
                hasCategoryBrowsable = true;
            }
        }
        if (hasCategoryDefault && hasCategoryBrowsable) {
            return true;
        }
        return false;
    }

    @Nullable
    public static VirtualFile getAndroidManiFest(@NotNull Module module) {
        AndroidFacet facet = AndroidFacet.getInstance(module);
        return facet == null ? null : getAndroidManiFest(facet);
    }

    @Nullable
    public static VirtualFile getAndroidManiFest(@NotNull AndroidFacet facet) {
        return LocalFileSystem.getInstance().findFileByIoFile(facet.getMainSourceProvider().getManifestFile());
    }

    @Nullable
    public static XmlFile getAndroidManifestPsi(@NotNull Module module) {
        VirtualFile manifest = getAndroidManiFest(module);
        if (manifest != null) {
            PsiFile psiFile = PsiManager.getInstance(module.getProject()).findFile(manifest);
            if (psiFile instanceof XmlFile) {
                return (XmlFile) psiFile;
            }
        }
        return null;
    }

    @NotNull
    public static List<XmlTag> searchXmlTagsByName(@NotNull XmlTag root, @NotNull String tagName) {
        return searchXmlTagsByName(root, tagName, null);
    }

    @NotNull
    public static List<XmlTag> searchXmlTagsByName(@NotNull XmlTag root, @NotNull final String tagName, @Nullable final String namespace) {
        final List<XmlTag> tags = Lists.newArrayList();
        root.accept(new XmlRecursiveElementVisitor() {
            public void visitXmlTag(XmlTag tag) {
                super.visitXmlTag(tag);
                if ((namespace == null && tag.getName().equalsIgnoreCase(tagName)) || (tag.getLocalName().equalsIgnoreCase(tagName) && tag.getNamespace().equals(namespace))) {
                    tags.add(tag);
                }
            }
        });
        return tags;
    }

    @Nullable
    public static XmlTag addTestUrl(@NotNull ActivityData activityData, @NotNull String testUrl) {
        if (activityData.getIntentFilterTag() == null) {
            return null;
        }
        XmlTag activityTag = activityData.getIntentFilterTag().getParentTag();
        if (activityTag == null) {
            return null;
        }
        AndroidResourceUtil.ensureNamespaceImported((XmlFile) activityTag.getContainingFile(), "http://schemas.android.com/tools", "tools");
        XmlTag validationTag = activityTag.createChildTag(TAG_VALIDATION, "http://schemas.android.com/tools", null, false);
        validationTag.setAttribute(ATTRIBUTE_TEST_URL, testUrl.trim());
        return activityTag.addSubTag(validationTag, true);
    }

    public static boolean isTestUrlAdded(@NotNull ActivityData activityData, @NotNull String testUrl) {
        if (activityData.getIntentFilterTag() == null) {
            return false;
        }
        XmlTag activityTag = activityData.getIntentFilterTag().getParentTag();
        if (activityTag == null) {
            return false;
        }
        for (XmlTag xmlTag : searchXmlTagsByName(activityTag, TAG_VALIDATION, "http://schemas.android.com/tools")) {
            if (testUrl.trim().equalsIgnoreCase(xmlTag.getAttributeValue(ATTRIBUTE_TEST_URL))) {
                return true;
            }
        }
        return false;
    }
}
