package com.showedup.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.showedup.app.data.entity.Converters;
import com.showedup.app.data.entity.DayOffType;
import com.showedup.app.data.entity.PlannedDayOffEntity;
import com.showedup.app.data.entity.PlannedDayOffStatus;
import java.lang.Class;
import java.lang.Exception;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlannedDayOffDao_Impl implements PlannedDayOffDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlannedDayOffEntity> __insertionAdapterOfPlannedDayOffEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<PlannedDayOffEntity> __deletionAdapterOfPlannedDayOffEntity;

  private final EntityDeletionOrUpdateAdapter<PlannedDayOffEntity> __updateAdapterOfPlannedDayOffEntity;

  public PlannedDayOffDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlannedDayOffEntity = new EntityInsertionAdapter<PlannedDayOffEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `planned_day_offs` (`id`,`targetDate`,`eventName`,`type`,`status`,`createdAt`,`chainHash`,`previousChainHash`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlannedDayOffEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTargetDate());
        statement.bindString(3, entity.getEventName());
        final String _tmp = __converters.fromDayOffType(entity.getType());
        statement.bindString(4, _tmp);
        final String _tmp_1 = __converters.fromPlannedDayOffStatus(entity.getStatus());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindString(7, entity.getChainHash());
        if (entity.getPreviousChainHash() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPreviousChainHash());
        }
      }
    };
    this.__deletionAdapterOfPlannedDayOffEntity = new EntityDeletionOrUpdateAdapter<PlannedDayOffEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `planned_day_offs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlannedDayOffEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlannedDayOffEntity = new EntityDeletionOrUpdateAdapter<PlannedDayOffEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `planned_day_offs` SET `id` = ?,`targetDate` = ?,`eventName` = ?,`type` = ?,`status` = ?,`createdAt` = ?,`chainHash` = ?,`previousChainHash` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlannedDayOffEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTargetDate());
        statement.bindString(3, entity.getEventName());
        final String _tmp = __converters.fromDayOffType(entity.getType());
        statement.bindString(4, _tmp);
        final String _tmp_1 = __converters.fromPlannedDayOffStatus(entity.getStatus());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindString(7, entity.getChainHash());
        if (entity.getPreviousChainHash() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPreviousChainHash());
        }
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final PlannedDayOffEntity record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlannedDayOffEntity.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PlannedDayOffEntity record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlannedDayOffEntity.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PlannedDayOffEntity record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlannedDayOffEntity.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlannedDayOffEntity>> getPending() {
    final String _sql = "SELECT * FROM planned_day_offs WHERE status = 'PENDING' ORDER BY targetDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"planned_day_offs"}, new Callable<List<PlannedDayOffEntity>>() {
      @Override
      @NonNull
      public List<PlannedDayOffEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTargetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetDate");
          final int _cursorIndexOfEventName = CursorUtil.getColumnIndexOrThrow(_cursor, "eventName");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<PlannedDayOffEntity> _result = new ArrayList<PlannedDayOffEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlannedDayOffEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTargetDate;
            _tmpTargetDate = _cursor.getString(_cursorIndexOfTargetDate);
            final String _tmpEventName;
            _tmpEventName = _cursor.getString(_cursorIndexOfEventName);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final PlannedDayOffStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toPlannedDayOffStatus(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new PlannedDayOffEntity(_tmpId,_tmpTargetDate,_tmpEventName,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpChainHash,_tmpPreviousChainHash);
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
  public Object getByDate(final String date,
      final Continuation<? super List<PlannedDayOffEntity>> $completion) {
    final String _sql = "SELECT * FROM planned_day_offs WHERE targetDate = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PlannedDayOffEntity>>() {
      @Override
      @NonNull
      public List<PlannedDayOffEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTargetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetDate");
          final int _cursorIndexOfEventName = CursorUtil.getColumnIndexOrThrow(_cursor, "eventName");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<PlannedDayOffEntity> _result = new ArrayList<PlannedDayOffEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlannedDayOffEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTargetDate;
            _tmpTargetDate = _cursor.getString(_cursorIndexOfTargetDate);
            final String _tmpEventName;
            _tmpEventName = _cursor.getString(_cursorIndexOfEventName);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final PlannedDayOffStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toPlannedDayOffStatus(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new PlannedDayOffEntity(_tmpId,_tmpTargetDate,_tmpEventName,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpChainHash,_tmpPreviousChainHash);
            _result.add(_item);
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
  public Flow<List<PlannedDayOffEntity>> getAll() {
    final String _sql = "SELECT * FROM planned_day_offs ORDER BY targetDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"planned_day_offs"}, new Callable<List<PlannedDayOffEntity>>() {
      @Override
      @NonNull
      public List<PlannedDayOffEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTargetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetDate");
          final int _cursorIndexOfEventName = CursorUtil.getColumnIndexOrThrow(_cursor, "eventName");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<PlannedDayOffEntity> _result = new ArrayList<PlannedDayOffEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlannedDayOffEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTargetDate;
            _tmpTargetDate = _cursor.getString(_cursorIndexOfTargetDate);
            final String _tmpEventName;
            _tmpEventName = _cursor.getString(_cursorIndexOfEventName);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final PlannedDayOffStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toPlannedDayOffStatus(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new PlannedDayOffEntity(_tmpId,_tmpTargetDate,_tmpEventName,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpChainHash,_tmpPreviousChainHash);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
