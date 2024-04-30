package com.example.project_part2.entities;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PostDao_Impl implements PostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Post> __insertionAdapterOfPost;

  private final EntityDeletionOrUpdateAdapter<Post> __deletionAdapterOfPost;

  private final EntityDeletionOrUpdateAdapter<Post> __updateAdapterOfPost;

  public PostDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPost = new EntityInsertionAdapter<Post>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Post` (`_lid`,`_id`,`content`,`image`,`authorId`,`authorPfp`,`authorDisplayName`,`date`,`likes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.get_lid());
        if (entity.get_id() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.get_id());
        }
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        if (entity.getImage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getImage());
        }
        if (entity.getAuthorId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAuthorId());
        }
        if (entity.getAuthorPfp() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAuthorPfp());
        }
        if (entity.getAuthorDisplayName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthorDisplayName());
        }
        if (entity.getDate() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDate());
        }
        statement.bindLong(9, entity.getLikes());
      }
    };
    this.__deletionAdapterOfPost = new EntityDeletionOrUpdateAdapter<Post>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Post` WHERE `_lid` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.get_lid());
      }
    };
    this.__updateAdapterOfPost = new EntityDeletionOrUpdateAdapter<Post>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Post` SET `_lid` = ?,`_id` = ?,`content` = ?,`image` = ?,`authorId` = ?,`authorPfp` = ?,`authorDisplayName` = ?,`date` = ?,`likes` = ? WHERE `_lid` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.get_lid());
        if (entity.get_id() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.get_id());
        }
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        if (entity.getImage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getImage());
        }
        if (entity.getAuthorId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAuthorId());
        }
        if (entity.getAuthorPfp() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAuthorPfp());
        }
        if (entity.getAuthorDisplayName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthorDisplayName());
        }
        if (entity.getDate() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDate());
        }
        statement.bindLong(9, entity.getLikes());
        statement.bindLong(10, entity.get_lid());
      }
    };
  }

  @Override
  public void insert(final Post... posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPost.insert(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Post... posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPost.handleMultiple(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Post... posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPost.handleMultiple(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Post> index() {
    final String _sql = "SELECT * FROM post";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLid = CursorUtil.getColumnIndexOrThrow(_cursor, "_lid");
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "authorId");
      final int _cursorIndexOfAuthorPfp = CursorUtil.getColumnIndexOrThrow(_cursor, "authorPfp");
      final int _cursorIndexOfAuthorDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "authorDisplayName");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Post _item;
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        final String _tmpImage;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmpImage = null;
        } else {
          _tmpImage = _cursor.getString(_cursorIndexOfImage);
        }
        final String _tmpAuthorId;
        if (_cursor.isNull(_cursorIndexOfAuthorId)) {
          _tmpAuthorId = null;
        } else {
          _tmpAuthorId = _cursor.getString(_cursorIndexOfAuthorId);
        }
        final String _tmpAuthorPfp;
        if (_cursor.isNull(_cursorIndexOfAuthorPfp)) {
          _tmpAuthorPfp = null;
        } else {
          _tmpAuthorPfp = _cursor.getString(_cursorIndexOfAuthorPfp);
        }
        final String _tmpAuthorDisplayName;
        if (_cursor.isNull(_cursorIndexOfAuthorDisplayName)) {
          _tmpAuthorDisplayName = null;
        } else {
          _tmpAuthorDisplayName = _cursor.getString(_cursorIndexOfAuthorDisplayName);
        }
        _item = new Post(_tmpContent,_tmpImage,_tmpAuthorId,_tmpAuthorPfp,_tmpAuthorDisplayName);
        final long _tmp_lid;
        _tmp_lid = _cursor.getLong(_cursorIndexOfLid);
        _item.set_lid(_tmp_lid);
        final String _tmp_id;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmp_id = null;
        } else {
          _tmp_id = _cursor.getString(_cursorIndexOfId);
        }
        _item.set_id(_tmp_id);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        _item.setLikes(_tmpLikes);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Post get(final int id) {
    final String _sql = "SELECT * FROM post WHERE _lid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLid = CursorUtil.getColumnIndexOrThrow(_cursor, "_lid");
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "authorId");
      final int _cursorIndexOfAuthorPfp = CursorUtil.getColumnIndexOrThrow(_cursor, "authorPfp");
      final int _cursorIndexOfAuthorDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "authorDisplayName");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final Post _result;
      if (_cursor.moveToFirst()) {
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        final String _tmpImage;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmpImage = null;
        } else {
          _tmpImage = _cursor.getString(_cursorIndexOfImage);
        }
        final String _tmpAuthorId;
        if (_cursor.isNull(_cursorIndexOfAuthorId)) {
          _tmpAuthorId = null;
        } else {
          _tmpAuthorId = _cursor.getString(_cursorIndexOfAuthorId);
        }
        final String _tmpAuthorPfp;
        if (_cursor.isNull(_cursorIndexOfAuthorPfp)) {
          _tmpAuthorPfp = null;
        } else {
          _tmpAuthorPfp = _cursor.getString(_cursorIndexOfAuthorPfp);
        }
        final String _tmpAuthorDisplayName;
        if (_cursor.isNull(_cursorIndexOfAuthorDisplayName)) {
          _tmpAuthorDisplayName = null;
        } else {
          _tmpAuthorDisplayName = _cursor.getString(_cursorIndexOfAuthorDisplayName);
        }
        _result = new Post(_tmpContent,_tmpImage,_tmpAuthorId,_tmpAuthorPfp,_tmpAuthorDisplayName);
        final long _tmp_lid;
        _tmp_lid = _cursor.getLong(_cursorIndexOfLid);
        _result.set_lid(_tmp_lid);
        final String _tmp_id;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmp_id = null;
        } else {
          _tmp_id = _cursor.getString(_cursorIndexOfId);
        }
        _result.set_id(_tmp_id);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _result.setDate(_tmpDate);
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        _result.setLikes(_tmpLikes);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
