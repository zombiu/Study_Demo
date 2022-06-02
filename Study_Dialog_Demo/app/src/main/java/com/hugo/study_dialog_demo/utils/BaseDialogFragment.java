package com.hugo.study_dialog_demo.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.LogUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 内部加一个handle可以修改成 一个支持延时弹出的dialog
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private BaseDialogViewModel baseDialogViewModel;
    private FragmentManager fragmentManager;


    public BaseDialogFragment(FragmentActivity fragmentActivity) {
        baseDialogViewModel = new ViewModelProvider(fragmentActivity, new ViewModelProvider.NewInstanceFactory()).get(BaseDialogViewModel.class);

        fragmentManager = fragmentActivity.getSupportFragmentManager();
        //初始化value
        baseDialogViewModel.getDialogLiveData().setValue(null);
        //添加观察
        baseDialogViewModel.getDialogLiveData().observe(fragmentActivity, isShow -> {
            LogUtils.d(" getDialogLiveData= " + isShow + "    " + this.toString());
            if (isShow == null) {
                return;
            }
            if (isShow) {
                show(fragmentManager, getFragmentTag());
            } else {
                //dismiss前 取消观察
                baseDialogViewModel.getDialogLiveData().removeObservers(fragmentActivity);
                baseDialogViewModel.getDialogLiveData().setValue(null);
                realDismiss();
            }
        });
    }

    private void realDismiss() {
        if (isAdded()) {
            dismissAllowingStateLoss();
        } else {
            LogUtils.e("-->>", "dismiss fragment 还未attach");
        }
    }

    private String getFragmentTag() {
        return "null";
    }

    public BaseDialogFragment(Fragment fragment) {
        this(fragment.getActivity());
        /*baseDialogViewModel = new ViewModelProvider(fragment)
                .get(BaseDialogViewModel.class);
        fragmentManager = fragment.getChildFragmentManager();
        baseDialogViewModel.getDialogLiveData().setValue(null);
        baseDialogViewModel.getDialogLiveData().observe(fragment, isShow -> {
            LogUtils.d(" getDialogLiveData= " + isShow + "    " + this.toString());
            if (isShow == null) {
                return;
            }
            if (isShow) {
                show(fragmentManager, getFragmentTag());
            } else {
                baseDialogViewModel.getDialogLiveData().removeObservers(fragment);
                dismiss();
            }
        });*/
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    //showDialog和dismissDialog不在显示调用
    //通过post liveData value
    //利用liveData的特点，规避什么周期问题（即saveInstance时saveState变更）
    //同时能够在条件不符时，保留数据，直至onResume发送数据
    //或onDestory销毁
    public void showDialog() {
        LogUtils.d("showDialog");
        if (baseDialogViewModel != null) {
            baseDialogViewModel.getDialogLiveData().postValue(true);
        } else {
            LogUtils.d("初始化viewModel失败_show");
        }
    }

    public void dismissDialog() {
        LogUtils.d("dismissDialog");
        if (baseDialogViewModel != null) {
            baseDialogViewModel.getDialogLiveData().postValue(false);
        } else {
            LogUtils.d("初始化viewModel失败_dismiss");
        }
    }

    /**
     * @return layoutId
     */
    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void bindView(View v);

}
