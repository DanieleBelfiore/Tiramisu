package com.dreamingbetter.tiramisu.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dreamingbetter.tiramisu.entities.Content;

import java.util.List;

@Dao
public interface ContentDao {
    @Query("SELECT * FROM content")
    List<Content> getAll();

    @Query("SELECT * FROM content WHERE uid IN (:ids)")
    List<Content> loadAllByIds(String[] ids);

    @Query("SELECT * FROM content WHERE uid == :uid")
    Content findByUid(String uid);

    @Insert
    void insertAll(Content... contents);

    @Delete
    void delete(Content content);

    @Query("DELETE FROM content")
    void deleteAll();
}
