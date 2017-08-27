package pl.krystiankrawczyk.apidataexplorer.explorer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.krystiankrawczyk.apidataexplorer.R;
import pl.krystiankrawczyk.apidataexplorer.api.model.Person;

/**
 * Created by Krystian Krawczyk on 27.08.2017.
 */

public class DetailsFragment extends Fragment {

    public static final String DETAILED_PERSON_INFO_KEY = "personKey";

    @BindView(R.id.fragment_details_iv_avatar)
    ImageView ivProfileImage;
    @BindView(R.id.fragment_details_tv_cell_phone)
    TextView tvCellPhone;
    @BindView(R.id.fragment_details_tv_country)
    TextView tvCountry;
    @BindView(R.id.fragment_details_tv_state)
    TextView tvState;
    @BindView(R.id.fragment_details_tv_email)
    TextView tvEmail;
    @BindView(R.id.fragment_details_tv_name)
    TextView tvName;
    @BindView(R.id.fragment_details_tv_phone)
    TextView tvPhone;
    @BindView(R.id.fragment_details_tv_city)
    TextView tvCity;
    @BindView(R.id.fragment_details_tv_street)
    TextView tvStreet;
    @BindView(R.id.fragment_details_tv_birthday)
    TextView tvBirthday;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_item_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            setupView(new Gson().fromJson(getArguments().getString(DETAILED_PERSON_INFO_KEY), Person.class));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setupView(Person person) {
        String fullName = person.getName().getTitle() + " " + person.getName().getFirst() + " " + person.getName()
                .getLast();
        Glide.with(getActivity())
                .load(person.getPicture().getLarge())
                .into(ivProfileImage);
        tvCellPhone.setText(person.getCell());
        tvCity.setText(person.getLocation().getCity());
        tvCountry.setText(person.getNat());
        tvEmail.setText(person.getEmail());
        tvState.setText(person.getLocation().getState());
        tvName.setText(fullName);
        tvPhone.setText(person.getPhone());
        tvStreet.setText(person.getLocation().getStreet());
        tvBirthday.setText(person.getDob());
    }
}
