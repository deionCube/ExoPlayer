package com.fongmi.android.tv.ui.custom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fongmi.android.tv.databinding.DialogTransferBinding;
import com.fongmi.android.tv.ui.PlayerActivity;
import com.fongmi.android.tv.utils.Token;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class TransferDialog extends BottomSheetDialogFragment {

	private DialogTransferBinding binding;

	public static void show(PlayerActivity context) {
		for (Fragment fragment : context.getSupportFragmentManager().getFragments())
			if (fragment instanceof BottomSheetDialogFragment) return;
		new TransferDialog().show(context.getSupportFragmentManager(), null);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = DialogTransferBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		dialog.setOnShowListener((DialogInterface f) -> setBehavior(dialog));
		return dialog;
	}

	private void setBehavior(BottomSheetDialog dialog) {
		FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
		BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
		behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
		behavior.setSkipCollapsed(true);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initEvent();
	}

	protected void initEvent() {
		binding.confirm.setOnClickListener(this::transfer);
		binding.uuid.setOnEditorActionListener(this::onEditorAction);
	}

	private boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) binding.confirm.performClick();
		return true;
	}

	private void transfer(View view) {
		String uuid = binding.uuid.getText().toString().trim().toLowerCase(Locale.ROOT);
		if (TextUtils.isEmpty(uuid)) return;
		Token.transfer(uuid);
		dismiss();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
