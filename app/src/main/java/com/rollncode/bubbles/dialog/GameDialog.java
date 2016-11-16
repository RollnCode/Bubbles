package com.rollncode.bubbles.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rollncode.bubbles.R;
import com.rollncode.bubbles.application.AContext;
import com.rollncode.bubbles.util.AnimatorUtils;
import com.rollncode.bubbles.util.ObjectsReceiver;
import com.rollncode.bubbles.util.Utils;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 01.07.16
 */
public class GameDialog extends BaseDialog
        implements OnClickListener, OnDismissListener {

    private int mCode;
    private final AlertDialog mDialog;

    private TextView mTvText;

    private TextView mBtnPositive;
    private TextView mBtnNegative;

    private boolean mDismissed;

    public GameDialog(Context context, @Nullable ObjectsReceiver receiver) {
        super(context, receiver);

        @SuppressLint("InflateParams")
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_game, null);
        {
            mTvText = (TextView) view.findViewById(R.id.tv_main);
            mBtnPositive = (TextView) view.findViewById(R.id.btn_positive);
            mBtnNegative = (TextView) view.findViewById(R.id.btn_negative);
        }

        mDialog = new Builder(context)
                .setView(view)
                .create();

        mTvText.setOnClickListener(this);
        mDialog.setOnDismissListener(this);
        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public void show(@NonNull Object... objects) {
        mDialog.show();
        mDismissed = true;

        mCode = (int) objects[0];
        switch (mCode) {
            case R.id.code_game_over:
                final int score = (int) objects[1];
                mTvText.setText(AContext.getString(R.string.your_score_d, score));
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                AnimatorUtils.flipHorizontally(mTvText, R.string.hint_formula).start();
                break;

            case R.id.btn_positive:
                Utils.receiveObjects(mReceiver, mCode, true);
                mDismissed = false;
                mDialog.cancel();
                break;

            case R.id.btn_negative:
                mDialog.cancel();
                break;

            default:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDismissed) {
            Utils.receiveObjects(mReceiver, mCode, false);
        }
    }
}
