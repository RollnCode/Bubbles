package com.rollncode.bubbles.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rollncode.bubbles.util.ObjectsReceiver;

import java.lang.ref.WeakReference;

/**
 * @author Maxim Ambroskin kkxmshu@gmail.com
 * @since 01.07.16
 */
@SuppressWarnings("WeakerAccess")
abstract class BaseDialog {

    protected final Context mContext;
    final WeakReference<ObjectsReceiver> mReceiver;

    BaseDialog(Context context, @Nullable ObjectsReceiver receiver) {
        mContext = context;
        mReceiver = receiver == null ? null : new WeakReference<>(receiver);
    }

    public abstract void show(@NonNull Object... objects);

}
