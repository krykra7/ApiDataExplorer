package pl.krystiankrawczyk.apidataexplorer.explorer.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.krystiankrawczyk.apidataexplorer.R;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public class ListSizeDialogFragment extends DialogFragment {

    private static final int DIALOG_FRAGMENT_SCREEN_FRACTION = 2;

    private Unbinder unbinder;
    private ListSizeListener listSizeListener;

    public ListSizeDialogFragment() {
    }

    @OnClick(R.id.dialog_list_size_btn_ten)
    public void onSizeTenClicked() {
        listSizeListener.onSizeChosen(Integer.valueOf(getResources().getString(R.string.dialog_list_size_ten)));
    }

    @OnClick(R.id.dialog_list_size_btn_fifty)
    public void onSizeFiftyClicked() {
        listSizeListener.onSizeChosen(Integer.valueOf(getResources().getString(R.string.dialog_list_size_fifty)));
    }

    @OnClick(R.id.dialog_list_size_btn_hundred)
    public void onSizeHundredClicked() {
        listSizeListener.onSizeChosen(Integer.valueOf(getResources().getString(R.string.dialog_lists_size_hundred)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListSizeListener) {
            listSizeListener = (ListSizeListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement ListSizeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list_size, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int dialogHeight = getResources().getDisplayMetrics().heightPixels / DIALOG_FRAGMENT_SCREEN_FRACTION;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setWindowAnimations(R.style.ListSizeDialogAnimation);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface ListSizeListener {
        void onSizeChosen(int listSize);
    }
}
