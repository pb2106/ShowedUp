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
import com.showedup.app.data.entity.DayOffRecordEntity;
import com.showedup.app.data.entity.DayOffType;
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
public final class DayOffDao_Impl implements DayOffDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DayOffRecordEntity> __insertionAdapterOfDayOffRecordEntity;

  private final Converters __converters = new Converters();

  public DayOffDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDayOffRecordEntity = new EntityInsertionAdapter<DayOffRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `day_off_records` (`id`,`type`,`date`,`reason`,`declaredAt`,`classesSuppressed`,`chainHash`,`previousChainHash`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DayOffRecordEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromDayOffType(entity.getType());
        statement.bindString(2, _tmp);
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getReason());
        statement.bindLong(5, entity.getDeclaredAt());
        statement.bindString(6, entity.getClassesSuppressed());
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
  public Object insert(final DayOffRecordEntity record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDayOffRecordEntity.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DayOffRecordEntity>> getAll() {
    final String _sql = "SELECT * FROM day_off_records ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"day_off_records"}, new Callable<List<DayOffRecordEntity>>() {
      @Override
      @NonNull
      public List<DayOffRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDeclaredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "declaredAt");
          final int _cursorIndexOfClassesSuppressed = CursorUtil.getColumnIndexOrThrow(_cursor, "classesSuppressed");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<DayOffRecordEntity> _result = new ArrayList<DayOffRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DayOffRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpDeclaredAt;
            _tmpDeclaredAt = _cursor.getLong(_cursorIndexOfDeclaredAt);
            final String _tmpClassesSuppressed;
            _tmpClassesSuppressed = _cursor.getString(_cursorIndexOfClassesSuppressed);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new DayOffRecordEntity(_tmpId,_tmpType,_tmpDate,_tmpReason,_tmpDeclaredAt,_tmpClassesSuppressed,_tmpChainHash,_tmpPreviousChainHash);
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
      final Continuation<? super List<DayOffRecordEntity>> $completion) {
    final String _sql = "SELECT * FROM day_off_records WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DayOffRecordEntity>>() {
      @Override
      @NonNull
      public List<DayOffRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDeclaredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "declaredAt");
          final int _cursorIndexOfClassesSuppressed = CursorUtil.getColumnIndexOrThrow(_cursor, "classesSuppressed");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<DayOffRecordEntity> _result = new ArrayList<DayOffRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DayOffRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpDeclaredAt;
            _tmpDeclaredAt = _cursor.getLong(_cursorIndexOfDeclaredAt);
            final String _tmpClassesSuppressed;
            _tmpClassesSuppressed = _cursor.getString(_cursorIndexOfClassesSuppressed);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new DayOffRecordEntity(_tmpId,_tmpType,_tmpDate,_tmpReason,_tmpDeclaredAt,_tmpClassesSuppressed,_tmpChainHash,_tmpPreviousChainHash);
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
  public Flow<List<DayOffRecordEntity>> getByDateRange(final String startDate,
      final String endDate) {
    final String _sql = "SELECT * FROM day_off_records WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"day_off_records"}, new Callable<List<DayOffRecordEntity>>() {
      @Override
      @NonNull
      public List<DayOffRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDeclaredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "declaredAt");
          final int _cursorIndexOfClassesSuppressed = CursorUtil.getColumnIndexOrThrow(_cursor, "classesSuppressed");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final List<DayOffRecordEntity> _result = new ArrayList<DayOffRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DayOffRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpDeclaredAt;
            _tmpDeclaredAt = _cursor.getLong(_cursorIndexOfDeclaredAt);
            final String _tmpClassesSuppressed;
            _tmpClassesSuppressed = _cursor.getString(_cursorIndexOfClassesSuppressed);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _item = new DayOffRecordEntity(_tmpId,_tmpType,_tmpDate,_tmpReason,_tmpDeclaredAt,_tmpClassesSuppressed,_tmpChainHash,_tmpPreviousChainHash);
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
  public Object getLatest(final Continuation<? super DayOffRecordEntity> $completion) {
    final String _sql = "SELECT * FROM day_off_records ORDER BY declaredAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DayOffRecordEntity>() {
      @Override
      @Nullable
      public DayOffRecordEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDeclaredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "declaredAt");
          final int _cursorIndexOfClassesSuppressed = CursorUtil.getColumnIndexOrThrow(_cursor, "classesSuppressed");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final DayOffRecordEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final DayOffType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toDayOffType(_tmp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpDeclaredAt;
            _tmpDeclaredAt = _cursor.getLong(_cursorIndexOfDeclaredAt);
            final String _tmpClassesSuppressed;
            _tmpClassesSuppressed = _cursor.getString(_cursorIndexOfClassesSuppressed);
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            _result = new DayOffRecordEntity(_tmpId,_tmpType,_tmpDate,_tmpReason,_tmpDeclaredAt,_tmpClassesSuppressed,_tmpChainHash,_tmpPreviousChainHash);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
