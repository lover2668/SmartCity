package com.tourcool.ui.mvp.account.contract;

import android.view.View;
import android.widget.ImageView;

import com.tourcool.core.module.mvp.IBaseView;
import com.tourcool.core.module.mvp.IBaseModel;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月24日14:45
 * @Email: 971613168@qq.com
 */
public interface RegisterContract {
    interface LoginModel extends IBaseModel {


    }

    interface LoginView extends IBaseView {

        void clearPassword(View view);

        ImageView getClearIcon();

        boolean showClearIcon();
    }

    interface LoginPresenter {

        void showClearIcon(boolean visible);
    }
}
