package pl.krystiankrawczyk.apidataexplorer.explorer.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pl.krystiankrawczyk.apidataexplorer.ExplorerActivity;
import pl.krystiankrawczyk.apidataexplorer.Preferences.UserSession;
import pl.krystiankrawczyk.apidataexplorer.R;
import pl.krystiankrawczyk.apidataexplorer.api.model.Person;
import pl.krystiankrawczyk.apidataexplorer.explorer.fragments.DataListFragment;
import pl.krystiankrawczyk.apidataexplorer.explorer.viewholders.ListItemViewHolder;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Person> displayedList;
    private List<Person> listData;
    private Context context;
    private DataListFragment.DataListListener listListener;

    public ListAdapter(Context context, List<Person> listData, DataListFragment.DataListListener listListener) {
        this.displayedList = listData;
        this.listData = listData;
        this.context = context;
        this.listListener = listListener;
        setTextChangeListenerForSearchField();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListItemViewHolder) {
            Glide.with(context)
                    .load(displayedList.get(position).getPicture().getThumbnail())
                    .into(((ListItemViewHolder) holder).getAvatar());
            ((ListItemViewHolder) holder).getName().setText(displayedList.get(position).getName().getFirst());
            ((ListItemViewHolder) holder).getSecondName().setText(displayedList.get(position).getName().getLast());
            setFavouriteIcon(((ListItemViewHolder) holder).getFavouriteIcon(), position);
            setOnSelectedListener(((ListItemViewHolder) holder).getAvatar(), position);
            setOnFavouriteSelectedListener(((ListItemViewHolder) holder).getFavouriteIcon(), position);
        }
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    private void setOnSelectedListener(ImageView ivAvatar, final int position) {
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.onShowDetailsClicked(displayedList.get(position));
            }
        });
    }

    private void setOnFavouriteSelectedListener(final ImageView favouriteIcon, final int position) {
        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayedList.get(position).setFavourite(!displayedList.get(position).isFavourite());
                UserSession.getInstance(context).getPersonList()
                        .get(position).setFavourite(!displayedList.get(position).isFavourite());
                listListener.onFavouriteSelected(position);
                setFavouriteIcon(favouriteIcon, position);
            }
        });
    }

    private void setFavouriteIcon(ImageView favouriteImage, int position) {
        if (displayedList.get(position).isFavourite()) {
            favouriteImage.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_24dp));
        } else {
            favouriteImage.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));
        }
    }

    private void setTextChangeListenerForSearchField() {
        if (context instanceof ExplorerActivity) {
            ((ExplorerActivity) context).getEtSearchName().setText("");
            ((ExplorerActivity) context).getEtSearchName().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filterPeople(s.toString());
                }
            });
        }
    }

    private void filterPeople(String phrase) {
        List<Person> temporaryFilteredList = new ArrayList<>();

        if (phrase.isEmpty()) {
            temporaryFilteredList = listData;
        } else {
            for (Person person : listData) {
                String fullName = person.getName().getFirst() + " " + person.getName().getLast();
                if (fullName.toLowerCase().contains(phrase)) {
                    temporaryFilteredList.add(person);
                }
            }
        }
        showResults(temporaryFilteredList);
    }

    private void showResults(List<Person> list) {
        displayedList = list;
        notifyDataSetChanged();
    }
}
