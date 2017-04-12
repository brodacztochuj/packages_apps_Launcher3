package com.android.launcher3.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.launcher3.InfoDropTarget;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.android.launcher3.model.WidgetItem;
import com.android.launcher3.util.PackageUserKey;
import com.android.launcher3.util.Themes;
import com.android.launcher3.widget.WidgetsAndMore;

import java.util.List;

/**
 * Represents a system shortcut for a given app. The shortcut should have a static label and
 * icon, and an onClickListener that depends on the item that the shortcut services.
 *
 * Example system shortcuts, defined as inner classes, include Widgets and AppInfo.
 */
public abstract class SystemShortcut {
    private final int mIconResId;
    private final int mLabelResId;

    public SystemShortcut(int iconResId, int labelResId) {
        mIconResId = iconResId;
        mLabelResId = labelResId;
    }

    public Drawable getIcon(Context context, int colorAttr) {
        Drawable icon = context.getResources().getDrawable(mIconResId);
        icon.setTint(Themes.getAttrColor(context, colorAttr));
        return icon;
    }

    public String getLabel(Context context) {
        return context.getString(mLabelResId);
    }

    public abstract View.OnClickListener getOnClickListener(final Launcher launcher,
            final ItemInfo itemInfo);


    public static class Widgets extends SystemShortcut {

        public Widgets() {
            super(R.drawable.ic_widget, R.string.widgets_and_more);
        }

        @Override
        public View.OnClickListener getOnClickListener(final Launcher launcher,
                final ItemInfo itemInfo) {
            final List<WidgetItem> widgets = launcher.getWidgetsForPackageUser(new PackageUserKey(
                    itemInfo.getTargetComponent().getPackageName(), itemInfo.user));
            if (widgets == null) {
                return null;
            }
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupContainerWithArrow.getOpen(launcher).close(true);
                    WidgetsAndMore widgetsAndMore =
                            (WidgetsAndMore) launcher.getLayoutInflater().inflate(
                                    R.layout.widgets_and_more, launcher.getDragLayer(), false);
                    widgetsAndMore.populateAndShow(itemInfo);
                }
            };
        }
    }

    public static class AppInfo extends SystemShortcut {
        public AppInfo() {
            super(R.drawable.ic_info_no_shadow, R.string.app_info_drop_target_label);
        }

        @Override
        public View.OnClickListener getOnClickListener(final Launcher launcher,
                final ItemInfo itemInfo) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InfoDropTarget.startDetailsActivityForInfo(itemInfo, launcher, null);
                }
            };
        }
    }
}
