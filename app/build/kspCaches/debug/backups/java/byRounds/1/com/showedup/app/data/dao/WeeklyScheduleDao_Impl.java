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
import com.showedup.app.data.entity.WeeklyScheduleEntity;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WeeklyScheduleDao_Impl implements WeeklyScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WeeklyScheduleEntity> __insertionAdapterOfWeeklyScheduleEntity;

  public WeeklyScheduleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWeeklyScheduleEntity = new EntityInsertionAdapter<WeeklyScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `weekly_schedules` (`id`,`activeDays`,`inactiveDays`,`effectiveFrom`,`effectiveTo`,`createdAt`,`chainHash`,`previousChainHash`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeeklyScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getActiveDays());
        statement.bindString(3, entity.getInactiveDays());
        statement.bindString(4, entity.getEffectiveFrom());
        if (entity.getEffectiveTo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEffectiveTo());
        }
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindString(7, entity.getChainHash());
        if (entity.getPreviousChainHash() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPreviousChainHash());
        }
      }
    };
  }

  @Override
  public Object insert(final WeeklyScheduleEntity schedule,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWeeklyScheduleEntity.insertAndReturnId(schedule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLatest(final Continuation<? super WeeklyScheduleEntity> $completion) {
    final String _sql = "SELECT * FROM weekly_schedules ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WeeklyScheduleEntity>() {
      @Override
      @Nullable
      public WeeklyScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfActiveDays = CursorUtil.getColumnIndexOrThrow(_cursor, "activeDays");
          final int _cursorIndexOfInactiveDays = CursorUtil.getColumnIndexOrThrow(_cursor, "inactiveDays");
          final int _cursorIndexOfEffectiveFrom = CursorUtil.getColumnIndexOrThrow(_cursor, "effectiveFrom");
          final int _cursorIndexOfEffectiveTo = CursorUtil.getColumnIndexOrThrow(_cursor, "effectiveTo");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final WeeklyScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpActiveDays;
            _tmpActiveDays = _cursor.getString(_cursorIndexOfActiveDays);
            final String _tmpInactiveDays;
            _tmpInactiveDays = _cursor.getString(_cursorIndexOfInactiveDays);
            final String _tmpEffectiveFrom;
            _tmpEffectiveFrom = _cursor.getString(_cursorIndexOfEffectiveFrom);
            final String _tmpEffectiveTo;
            if (_cursor.isNull(_cursorIndexOfEffectiveTo)) {
              _tmpEffectiveTo = null;
            } else {
              _tmpEffectiveTo = _cursor.getString(_cursorIndexOfEffectiveTo);
            }
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
            _result = new WeeklyScheduleEntity(_tmpId,_tmpActiveDays,_tmpInactiveDays,_tmpEffectiveFrom,_tmpEffectiveTo,_tmpCreatedAt,_tmpChainHash,_tmpPreviousChainHash);
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
  public Flow<WeeklyScheduleEntity> getLatestFlow() {
    final String _sql = "SELECT * FROM weekly_schedules ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"weekly_schedules"}, new Callable<WeeklyScheduleEntity>() {
      @Override
      @Nullable
      public WeeklyScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfActiveDays = CursorUtil.getColumnIndexOrThrow(_cursor, "activeDays");
          final int _cursorIndexOfInactiveDays = CursorUtil.getColumnIndexOrThrow(_cursor, "inactiveDays");
          final int _cursorIndexOfEffectiveFrom = CursorUtil.getColumnIndexOrThrow(_cursor, "effectiveFrom");
          final int _cursorIndexOfEffectiveTo = CursorUtil.getColumnIndexOrThrow(_cursor, "effectiveTo");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final WeeklyScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpActiveDays;
            _tmpActiveDays = _cursor.getString(_cursorIndexOfActiveDays);
            final String _tmpInactiveDays;
            _tmpInactiveDays = _cursor.getString(_cursorIndexOfInactiveDays);
            final String _tmpEffectiveFrom;
            _tmpEffectiveFrom = _cursor.getString(_cursorIndexOfEffectiveFrom);
            final String _tmpEffectiveTo;
            if (_cursor.isNull(_cursorIndexOfEffectiveTo)) {
              _tmpEffectiveTo = null;
            } else {
              _tmpEffectiveTo = _cursor.getString(_cursorIndexOfEffectiveTo);
            }
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
            _result = new WeeklyScheduleEntity(_tmpId,_tmpActiveDays,_tmpInactiveDays,_tmpEffectiveFrom,_tmpEffectiveTo,_tmpCreatedAt,_tmpChainHash,_tmpPreviousChainHash);
          } else {
            _result = null;
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
  public Flow<List<WeeklyScheduleEntity>> getAll() {
    final String _sql = "SELECT * FROM weekly_schedules ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"weekly_schedules"}, new Callable<List<WeeklyScheduleEntity>>() {
      @Override
      @NonNull
      public List<WeeklyScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfActiveDays = CursorUtil.getColumnIndexOrThrow(_cursor, "activeDays");
          final int _cursorIndexOfInactiveDays = CursorUtil.getColumnIndexOrThrow(_cursor, "inactiveDays");
          final int _cursorIndexOfEffectiveFrom = CursorUtil.getColumnIndexOrThrow(_cursor, "effectiveFrom");
          final int _cursorIndexOfEffectiveTo = CursorUtil.getColumnIndexOrThrow(_cursor, "effectiveTo");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<WeeklyScheduleEntity> _result = new ArrayList<WeeklyScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WeeklyScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpActiveDays;
            _tmpActiveDays = _cursor.getString(_cursorIndexOfActiveDays);
            final String _tmpInactiveDays;
            _tmpInactiveDays = _cursor.getString(_cursorIndexOfInactiveDays);
            final String _tmpEffectiveFrom;
            _tmpEffectiveFrom = _cursor.getString(_cursorIndexOfEffectiveFrom);
            final String _tmpEffectiveTo;
            if (_cursor.isNull(_cursorIndexOfEffectiveTo)) {
              _tmpEffectiveTo = null;
            } else {
              _tmpEffectiveTo = _cursor.getString(_cursorIndexOfEffectiveTo);
            }
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
            _item = new WeeklyScheduleEntity(_tmpId,_tmpActiveDays,_tmpInactiveDays,_tmpEffectiveFrom,_tmpEffectiveTo,_tmpCreatedAt,_tmpChainHash,_tmpPreviousChainHash);
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
