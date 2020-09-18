package eu.fbk.mIDAssistant.Utill;

import com.android.utils.HtmlBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AppLinkTableCell {

    @NotNull
    String getHtmlText(boolean z);

    @NotNull
    static String generateHtmlText(@Nullable String content, @NotNull String suffix, boolean isSelected) {
        if (StringUtil.isEmpty(content)) {
            return "";
        }
        return new HtmlBuilder().openHtmlBody().add(content).coloredText(isSelected ? new JBColor(Gray._208, Gray._160) : JBColor.GRAY, " (" + suffix + ")").closeHtmlBody().getHtml();
    }

    @NotNull
    static String generateRegularText(@Nullable String content, @NotNull String suffix) {
        if (StringUtil.isEmpty(content)) {
            return "";
        }
        return content + " (" + suffix + ")";
    }
}
