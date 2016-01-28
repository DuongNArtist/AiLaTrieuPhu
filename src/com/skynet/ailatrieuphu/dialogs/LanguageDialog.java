package com.skynet.ailatrieuphu.dialogs;

import java.util.ArrayList;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.devsmart.android.ui.HorizontalListView;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.LanguageAdapter;
import com.skynet.ailatrieuphu.models.LanguageModel;

public class LanguageDialog extends BaseDialog {

    private static LanguageDialog mInstance;

    private ImageView mivTitle;
    private HorizontalListView mlvList;

    private LanguageDialog(Context context) {
        super(context);
        setCancelable(true);
    }

    public static LanguageDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LanguageDialog(context);
        }
        return mInstance;
    }

    @Override
    public void show() {
        super.show();
        if (AiLaTrieuPhuActivity.mEnglish) {
            mivTitle.setImageResource(R.drawable.img_language_en);
        } else {
            mivTitle.setImageResource(R.drawable.img_language_vi);
        }
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_language;
    }

    @Override
    public void bindView() {
        mivTitle = (ImageView) findViewById(R.id.iv_language_title);
        mlvList = (HorizontalListView) findViewById(R.id.lv_language_list);
        String[] languages = mContext.getResources().getStringArray(
                R.array.dnt_languages);
        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();
        for (int index = 0; index < languages.length; index++) {
            LanguageModel languageModel = new LanguageModel();
            languageModel.setId(R.drawable.flag_00_selected + 2 * index);
            languageModel.setLanguage(languages[index]);
            languageModels.add(languageModel);
        }
        mlvList.setAdapter(new LanguageAdapter(mContext, 0, languageModels));
    }

    public LanguageAdapter getAdapter() {
        return (LanguageAdapter) mlvList.getAdapter();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlvList.setOnItemClickListener(listener);
    }
}
