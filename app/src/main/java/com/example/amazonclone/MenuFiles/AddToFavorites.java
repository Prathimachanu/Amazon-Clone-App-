package com.example.amazonclone.MenuFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazonclone.PlaceOrderActivity;
import com.example.amazonclone.R;
import com.example.amazonclone.model.Favorites;
import com.example.amazonclone.viewholder.FavoritesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AddToFavorites extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView totalprice;
    private int overallPrice = 0;

    private FirebaseAuth auth;
    private DatabaseReference favListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_favorites);

        auth = FirebaseAuth.getInstance();
        favListRef = FirebaseDatabase.getInstance().getReference().child("Fav List");

        recyclerView = findViewById(R.id.fav_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalprice = findViewById(R.id.totalprice);

        AppCompatButton nextBtn = findViewById(R.id.next_button);
        nextBtn.setBackgroundResource(R.drawable.bg_color);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String productId = intent.getStringExtra("pid");
                String productName = intent.getStringExtra("name");
                String productPrice = intent.getStringExtra("price");
                if (productId != null && productName != null && productPrice != null) {
                    addToFavorites(productId, productName, productPrice);
                } else {
                    Toast.makeText(AddToFavorites.this, "Error: Missing product details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Favorites> options =
                new FirebaseRecyclerOptions.Builder<Favorites>()
                        .setQuery(favListRef.child("User View").child(auth.getCurrentUser().getUid()).child("Products"), Favorites.class)
                        .build();

        FirebaseRecyclerAdapter<Favorites, FavoritesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Favorites, FavoritesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FavoritesViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Favorites model) {
                        String name = model.getName().replaceAll("\n", " ");
                        holder.favProductName.setText(name);
                        holder.favProductPrice.setText(model.getPrice());
                        String intPrice = model.getPrice().replace("₹", "");
                        overallPrice += Integer.valueOf(intPrice);
                        totalprice.setText("₹" + String.valueOf(overallPrice));

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddToFavorites.this);
                                builder.setTitle("Delete item");
                                builder.setMessage("Do you want to remove this product from favorites?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                String pid = getRef(position).getKey();
                                                if (pid != null) {
                                                    removeFromFavorites(pid);
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @NotNull
                    @Override
                    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_items_layout, parent, false);
                        return new FavoritesViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void addToFavorites(String productId, String productName, String productPrice) {
        Favorites newFavorite = new Favorites(productId, productName, productPrice);
        favListRef.child("User View")
                .child(auth.getCurrentUser().getUid())
                .child("Products")
                .child(productId)
                .setValue(newFavorite)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddToFavorites.this, "Item added to favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddToFavorites.this, "Failed to add item to favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void removeFromFavorites(String productId) {
        favListRef.child("User View")
                .child(auth.getCurrentUser().getUid())
                .child("Products")
                .child(productId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddToFavorites.this, "Item removed from favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddToFavorites.this, "Failed to remove item from favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
