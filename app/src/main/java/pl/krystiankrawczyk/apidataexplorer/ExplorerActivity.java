package pl.krystiankrawczyk.apidataexplorer;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.krystiankrawczyk.apidataexplorer.Preferences.UserSession;
import pl.krystiankrawczyk.apidataexplorer.api.ServiceGenerator;
import pl.krystiankrawczyk.apidataexplorer.api.model.Person;
import pl.krystiankrawczyk.apidataexplorer.api.model.Results;
import pl.krystiankrawczyk.apidataexplorer.explorer.fragments.DataListFragment;
import pl.krystiankrawczyk.apidataexplorer.explorer.fragments.DetailsFragment;
import pl.krystiankrawczyk.apidataexplorer.explorer.fragments.ListSizeDialogFragment;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExplorerActivity extends AppCompatActivity implements ListSizeDialogFragment.ListSizeListener,
        DataListFragment.DataListListener {

    private static final String LIST_SIZE_DIALOG_FRAGMENT_TAG = "listSize";
    private static final int ACTIVITY_ACTIVE = 0;

    @BindView(R.id.explorer_activity_pb_progress)
    ProgressBar pbProgressBar;
    @BindView(R.id.explorer_activity_tv_welcome)
    TextView tvWelcomeInExplorer;
    @BindView(R.id.explorer_activity_tv_choose_option)
    TextView tvChooseOption;
    @BindView(R.id.explorer_activity_ll_buttons_layout)
    LinearLayout llButtonsLayout;
    @BindView(R.id.explorer_activity_et_toolbar_search)
    EditText etSearchName;
    @BindView(R.id.explorer_activity_toolbar)
    Toolbar explorerToolbar;

    private List<Person> personList = new ArrayList<>();

    @OnClick(R.id.explorer_activity_btn_existing_list)
    public void onExistingListClicked() {
        if (UserSession.getInstance(this).getPersonList().isEmpty()) {
            Toast.makeText(this, "First add any list", Toast.LENGTH_SHORT).show();
        } else {
            cleanScreen();
            personList = UserSession.getInstance(this).getPersonList();
            setupPersonListFragment();
        }
    }

    @OnClick(R.id.explorer_activity_btn_new_list)
    public void onNewListClicked() {
        showListSizeDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        UserSession.getInstance(this).retrieveListFromPreferences();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserSession.getInstance(this).savePeopleList();
    }

    @Override
    public void onSizeChosen(int listSize) {
        cleanScreen();
        removeDialog();
        changeProgressBarState();
        downloadPersonList(listSize);
    }

    @Override
    public void onFavouriteSelected(int itemIndex) {
        if (personList.get(itemIndex).isFavourite()) {
            personList.get(itemIndex).setFavourite(false);
        } else {
            personList.get(itemIndex).setFavourite(true);
        }
    }

    @Override
    public void onShowDetailsClicked(Person selectedPerson) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailsFragment.DETAILED_PERSON_INFO_KEY, new Gson().toJson(selectedPerson));
        detailsFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.explorer_activity_fragment_container, detailsFragment)
                .addToBackStack(DetailsFragment.class.getSimpleName())
                .commit();
    }

    public void setupActivityViews() {
        tvChooseOption.setVisibility(View.VISIBLE);
        tvWelcomeInExplorer.setVisibility(View.VISIBLE);
        llButtonsLayout.setVisibility(View.VISIBLE);
    }

    private void showListSizeDialog() {
        ListSizeDialogFragment listSizeDialogFragment = new ListSizeDialogFragment();
        listSizeDialogFragment.show(getFragmentManager(), LIST_SIZE_DIALOG_FRAGMENT_TAG);
    }

    private void cleanScreen() {
        tvChooseOption.setVisibility(View.GONE);
        tvWelcomeInExplorer.setVisibility(View.GONE);
        llButtonsLayout.setVisibility(View.GONE);
    }

    private void removeDialog() {
        Fragment fragment = getFragmentManager().findFragmentByTag(LIST_SIZE_DIALOG_FRAGMENT_TAG);
        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    private void downloadPersonList(final int listSize) {
        ServiceGenerator.getInstance()
                .getPersonList(listSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Results>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ApiDownload", e.getMessage());
                        downloadPersonList(listSize);
                    }

                    @Override
                    public void onNext(Results results) {
                        personList = results.getResults();
                        UserSession.getInstance(ExplorerActivity.this).setPersonList(personList);
                        changeProgressBarState();
                        setupPersonListFragment();
                    }
                });
    }

    private void changeProgressBarState() {
        if (pbProgressBar.getVisibility() == View.GONE) {
            pbProgressBar.setVisibility(View.VISIBLE);
        } else {
            pbProgressBar.setVisibility(View.GONE);
        }
    }

    private void setupPersonListFragment() {
        DataListFragment dataListFragment = new DataListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.explorer_activity_fragment_container, dataListFragment)
                .addToBackStack(DataListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int backStackCount = getFragmentManager().getBackStackEntryCount();

        if (backStackCount == ACTIVITY_ACTIVE) {
            setupActivityViews();
        }
    }

    public EditText getEtSearchName() {
        return etSearchName;
    }

    public Toolbar getExplorerToolbar() {
        return explorerToolbar;
    }
}
