package com.dreamingbetter.tiramisu.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@SuppressWarnings({"NullableProblems", "NotNullFieldNotInitialized"})
@Entity
public class ContentFavorite {
    @PrimaryKey
    @NonNull
    public String uid;

    @NonNull
    public long timestamp;
}