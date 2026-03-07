package com.showedup.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.showedup.app.data.entity.Converters;
import com.showedup.app.data.entity.SecurityEventEntity;
import com.showedup.app.data.entity.SecurityEventType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SecurityEventDao_Impl implements SecurityEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SecurityEventEntity> __insertionAdapterOfSecurityEventEntity;

  private final Converters __converters = new Converters();

  public SecurityEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSecurityEventEntity = new EntityInsertionAdapter<SecurityEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `security_events` (`id`,`eventType`,`timestamp`,`data`,`chainHash`,`previousChainHash`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SecurityEventEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromSecurityEventType(entity.getEventType());
        statement.bindString(2, _tmp);
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getData());
        statement.bindString(5, entity.getChainHash());
        if (entity.getPreviousChainHash() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPreviousChainHash());
        }
      }
    };
  }

  @Override
  public Object insert(final SecurityEventEntity event,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSecurityEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SecurityEventEntity>> getAll() {
    final String _sql = "SELECT * FROM security_events ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"security_events"}, new Callable<List<SecurityEventEntity>>() {
      @Override
      @NonNull
      public List<SecurityEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<SecurityEventEntity> _result = new ArrayList<SecurityEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SecurityEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SecurityEventType _tmpEventType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfEventType);
            _tmpEventType = __converters.toSecurityEventType(_tmp);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpData;
            _tmpData = _cursor.getString(_cursorIndexOfData);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new SecurityEventEntity(_tmpId,_tmpEventType,_tmpTimestamp,_tmpData,_tmpChainHash,_tmpPreviousChainHash);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SecurityEventEntity>> getByType(final String type) {
    final String _sql = "SELECT * FROM security_events WHERE eventType = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"security_events"}, new Callable<List<SecurityEventEntity>>() {
      @Override
      @NonNull
      public List<SecurityEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<SecurityEventEntity> _result = new ArrayList<SecurityEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SecurityEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SecurityEventType _tmpEventType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfEventType);
            _tmpEventType = __converters.toSecurityEventType(_tmp);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpData;
            _tmpData = _cursor.getString(_cursorIndexOfData);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new SecurityEventEntity(_tmpId,_tmpEventType,_tmpTimestamp,_tmpData,_tmpChainHash,_tmpPreviousChainHash);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLatest(final Continuation<? super SecurityEventEntity> $completion) {
    final String _sql = "SELECT * FROM security_events ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SecurityEventEntity>() {
      @Override
      @Nullable
      public SecurityEventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final SecurityEventEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SecurityEventType _tmpEventType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfEventType);
            _tmpEventType = __converters.toSecurityEventType(_tmp);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpData;
            _tmpData = _cursor.getString(_cursorIndexOfData);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _result = new SecurityEventEntity(_tmpId,_tmpEventType,_tmpTimestamp,_tmpData,_tmpChainHash,_tmpPreviousChainHash);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countBiometricFailuresSince(final long since,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM security_events WHERE eventType = 'BIOMETRIC_FAILURE' AND timestamp > ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
