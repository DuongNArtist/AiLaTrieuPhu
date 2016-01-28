package com.skynet.ailatrieuphu.dialogs;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.BuddyAdapter;
import com.skynet.ailatrieuphu.audios.AudioManager;

public class SelectorDialog extends BaseDialog {

    public static final String TAG = SelectorDialog.class.getSimpleName();

    private GridView mgvBuddies;

    public SelectorDialog(Context context) {
        super(context);
        if (AiLaTrieuPhuActivity.mEnglish) {
            AudioManager.getInstance().playSound(AudioManager.HELPER_BUDDY_EN,
                    false);
        } else {
            AudioManager.getInstance().playSound(AudioManager.HELPER_BUDDY_VI,
                    false);
        }
        String[] buddyNames = mContext.getResources().getStringArray(
                R.array.dnt_buddy_names);
        BuddyAdapter buddyAdapter = new BuddyAdapter(mContext, buddyNames);
        mgvBuddies.setAdapter(buddyAdapter);
        show();
    }

    public GridView getGridView() {
        return mgvBuddies;
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_selector;
    }

    @Override
    public void bindView() {
        mgvBuddies = (GridView) findViewById(R.id.gv_selector_buddies);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mgvBuddies.setOnItemClickListener(listener);
    }

}
