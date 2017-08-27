package pl.krystiankrawczyk.apidataexplorer.explorer.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.krystiankrawczyk.apidataexplorer.ExplorerActivity;
import pl.krystiankrawczyk.apidataexplorer.Preferences.UserSession;
import pl.krystiankrawczyk.apidataexplorer.R;
import pl.krystiankrawczyk.apidataexplorer.api.model.Person;
import pl.krystiankrawczyk.apidataexplorer.explorer.adapters.ListAdapter;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public class DataListFragment extends Fragment {

    @BindView(R.id.fragment_list_rv_person_list)
    RecyclerView rvPersonListView;

    private List<Person> personList;
    private Unbinder unbinder;

    private DataListListener dataListListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataListListener) {
            dataListListener = (DataListListener) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement dataListListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        personList = UserSession.getInstance(getActivity()).getPersonList();
        setupAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ExplorerActivity) getActivity()).getExplorerToolbar().setVisibility(View.GONE);
        unbinder.unbind();
    }

    private void setupAdapter() {
        ((ExplorerActivity) getActivity()).getExplorerToolbar().setVisibility(View.VISIBLE);
        ListAdapter listAdapter = new ListAdapter(getActivity(), personList, dataListListener);
        rvPersonListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPersonListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvPersonListView.setAdapter(listAdapter);
    }

    public interface DataListListener {
        void onFavouriteSelected(int itemIndex);

        void onShowDetailsClicked(Person selectedPerson);
    }
}
