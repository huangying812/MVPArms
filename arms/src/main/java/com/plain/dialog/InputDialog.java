package com.plain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Range;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.plain.R;
import com.plain.base.BaseDialog;
import com.plain.base.BaseDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/02/27
 * desc   : 输入对话框
 */
public final class InputDialog {

    public static final class Builder
            extends BaseDialogFragment.Builder<Builder>
            implements View.OnClickListener,
            BaseDialog.OnShowListener,
            BaseDialog.OnDismissListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true; // 设置点击按钮后自动消失

        private TextView mTitleView;
        private EditText mInputView;

        private TextView mCancelView;
        private View mLineView;
        private TextView mConfirmView;

        private InputMethodManager mInputManager;

        public Builder(FragmentActivity activity) {
            super(activity);

            setContentView(R.layout.dialog_input);
            setAnimStyle(BaseDialog.AnimStyle.IOS);
            setGravity(Gravity.CENTER);

            mTitleView = findViewById(R.id.tv_dialog_input_title);
            mInputView = findViewById(R.id.tv_dialog_input_message);

            mCancelView = findViewById(R.id.tv_dialog_input_cancel);
            mLineView = findViewById(R.id.v_dialog_input_line);
            mConfirmView = findViewById(R.id.tv_dialog_input_confirm);

            mCancelView.setOnClickListener(this);
            mConfirmView.setOnClickListener(this);

            mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        public Builder setTitle(int resId) {
            return setTitle(getText(resId));
        }

        public Builder setTitle(CharSequence text) {
            mTitleView.setText(text);
            return this;
        }

        public Builder setHint(int resId) {
            return setHint(getText(resId));
        }

        public Builder setHint(CharSequence text) {
            mInputView.setHint(text);
            return this;
        }

        public Builder setContent(int resId) {
            return setContent(getText(resId));
        }

        @NotNull
        public Builder setInputType(int inputType) {
            mInputView.setInputType(inputType);
            return this;
        }

        @NotNull
        public Builder setMaxEms(int maxEms) {
            mInputView.setMaxEms(maxEms);
            return this;
        }

        @NotNull
        public Builder setValueRange(@Nullable Range<BigDecimal> valueRange) {
            if (valueRange != null) {
                mInputView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            BigDecimal value = new BigDecimal(s.toString());
                            if (!valueRange.contains(value)) {
                                s.clear();
                                BigDecimal lower = valueRange.getLower();
                                if (lower.compareTo(value) > 0) {
                                    s.append(lower.toPlainString());
                                } else {
                                    BigDecimal upper = valueRange.getUpper();
                                    s.append(upper.toPlainString());
                                }
                            }
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    }
                });
            }
            return this;
        }

        public Builder setContent(CharSequence text) {
            mInputView.setText(text);
            int index = mInputView.getText().toString().length();
            if (index > 0) {
                mInputView.requestFocus();
                mInputView.setSelection(index);
            }
            return this;
        }

        public Builder setCancel(int resId) {
            return setCancel(getText(resId));
        }

        public Builder setCancel(CharSequence text) {
            mCancelView.setText(text);

            mCancelView.setVisibility((text == null || "".equals(text.toString())) ? View.GONE : View.VISIBLE);
            mLineView.setVisibility((text == null || "".equals(text.toString())) ? View.GONE : View.VISIBLE);
            mConfirmView.setBackgroundResource((text == null || "".equals(text.toString())) ?
                    R.drawable.dialog_message_one_button : R.drawable.dialog_message_right_button);
            return this;
        }

        public Builder setConfirm(int resId) {
            return setConfirm(getText(resId));
        }

        public Builder setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            return this;
        }

        public Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return this;
        }

        public Builder setListener(OnListener l) {
            mListener = l;
            return this;
        }

        @Override
        public BaseDialog create() {
            // 如果标题为空就隐藏
            if ("".equals(mTitleView.getText().toString())) {
                mTitleView.setVisibility(View.GONE);
            }
            addOnShowListener(this);
            addOnDismissListener(this);
            return super.create();
        }

        /**
         * {@link BaseDialog.OnShowListener}
         */
        @Override
        public void onShow(BaseDialog dialog) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mInputManager.showSoftInput(mInputView, 0);
                }
            }, 500);
        }

        /**
         * {@link BaseDialog.OnDismissListener}
         */
        @Override
        public void onDismiss(BaseDialog dialog) {
            mInputManager.hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
        }

        /**
         * {@link View.OnClickListener}
         */
        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener == null) return;

            if (v == mConfirmView) {
                // 判断输入是否为空
                mListener.onConfirm(getDialog(), mInputView.getText().toString());
            } else if (v == mCancelView) {
                mListener.onCancel(getDialog());
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(Dialog dialog, String content);

        /**
         * 点击取消时回调
         */
        void onCancel(Dialog dialog);
    }
}