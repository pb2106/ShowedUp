package com.showedup.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.showedup.app.data.entity.Converters;
import com.showedup.app.data.entity.TimetableEntry;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.DayOfWeek;
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
public final class TimetableDao_Impl implements TimetableDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TimetableEntry> __insertionAdapterOfTimetableEntry;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<TimetableEntry> __deletionAdapterOfTimetableEntry;

  private final EntityDeletionOrUpdateAdapter<TimetableEntry> __updateAdapterOfTimetableEntry;

  public TimetableDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTimetableEntry = new EntityInsertionAdapter<TimetableEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `timetable_entries` (`id`,`courseName`,`courseCode`,`dayOfWeek`,`startTimeMinutes`,`endTimeMinutes`,`room`,`instructor`,`locationLat`,`locationLng`,`geofenceRadius`,`isActive`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TimetableEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCourseName());
        statement.bindString(3, entity.getCourseCode());
        final int _tmp = __converters.fromDayOfWeek(entity.getDayOfWeek());
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getStartTimeMinutes());
        statement.bindLong(6, entity.getEndTimeMinutes());
        statement.bindString(7, entity.getRoom());
        statement.bindString(8, entity.getInstructor());
        if (entity.getLocationLat() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLocationLat());
        }
        if (entity.getLocationLng() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLocationLng());
        }
        statement.bindDouble(11, entity.getGeofenceRadius());
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(12, _tmp_1);
        statement.bindLong(13, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfTimetableEntry = new EntityDeletionOrUpdateAdapter<TimetableEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `timetable_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TimetableEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTimetableEntry = new EntityDeletionOrUpdateAdapter<TimetableEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `timetable_entries` SET `id` = ?,`courseName` = ?,`courseCode` = ?,`dayOfWeek` = ?,`startTimeMinutes` = ?,`endTimeMinutes` = ?,`room` = ?,`instructor` = ?,`locationLat` = ?,`locationLng` = ?,`geofenceRadius` = ?,`isActive` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TimetableEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCourseName());
        statement.bindString(3, entity.getCourseCode());
        final int _tmp = __converters.fromDayOfWeek(entity.getDayOfWeek());
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getStartTimeMinutes());
        statement.bindLong(6, entity.getEndTimeMinutes());
        statement.bindString(7, entity.getRoom());
        statement.bindString(8, entity.getInstructor());
        if (entity.getLocationLat() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLocationLat());
        }
        if (entity.getLocationLng() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLocationLng());
        }
        statement.bindDouble(11, entity.getGeofenceRadius());
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(12, _tmp_1);
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final TimetableEntry entry, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTimetableEntry.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final TimetableEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTimetableEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TimetableEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTimetableEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TimetableEntry>> getAllActive() {
    final String _sql = "SELECT * FROM timetable_entries WHERE isActive = 1 ORDER BY dayOfWeek, startTimeMinutes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"timetable_entries"}, new Callable<List<TimetableEntry>>() {
      @Override
      @NonNull
      public List<TimetableEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfLocationLat = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLat");
          final int _cursorIndexOfLocationLng = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLng");
          final int _cursorIndexOfGeofenceRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceRadius");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TimetableEntry> _result = new ArrayList<TimetableEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TimetableEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final String _tmpCourseCode;
            _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            final DayOfWeek _tmpDayOfWeek;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toDayOfWeek(_tmp);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final Double _tmpLocationLat;
            if (_cursor.isNull(_cursorIndexOfLocationLat)) {
              _tmpLocationLat = null;
            } else {
              _tmpLocationLat = _cursor.getDouble(_cursorIndexOfLocationLat);
            }
            final Double _tmpLocationLng;
            if (_cursor.isNull(_cursorIndexOfLocationLng)) {
              _tmpLocationLng = null;
            } else {
              _tmpLocationLng = _cursor.getDouble(_cursorIndexOfLocationLng);
            }
            final float _tmpGeofenceRadius;
            _tmpGeofenceRadius = _cursor.getFloat(_cursorIndexOfGeofenceRadius);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TimetableEntry(_tmpId,_tmpCourseName,_tmpCourseCode,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpInstructor,_tmpLocationLat,_tmpLocationLng,_tmpGeofenceRadius,_tmpIsActive,_tmpCreatedAt);
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
  public Flow<List<TimetableEntry>> getByDayOfWeek(final int dayOfWeek) {
    final String _sql = "SELECT * FROM timetable_entries WHERE isActive = 1 AND dayOfWeek = ? ORDER BY startTimeMinutes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayOfWeek);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"timetable_entries"}, new Callable<List<TimetableEntry>>() {
      @Override
      @NonNull
      public List<TimetableEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfLocationLat = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLat");
          final int _cursorIndexOfLocationLng = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLng");
          final int _cursorIndexOfGeofenceRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceRadius");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TimetableEntry> _result = new ArrayList<TimetableEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TimetableEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final String _tmpCourseCode;
            _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            final DayOfWeek _tmpDayOfWeek;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toDayOfWeek(_tmp);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final Double _tmpLocationLat;
            if (_cursor.isNull(_cursorIndexOfLocationLat)) {
              _tmpLocationLat = null;
            } else {
              _tmpLocationLat = _cursor.getDouble(_cursorIndexOfLocationLat);
            }
            final Double _tmpLocationLng;
            if (_cursor.isNull(_cursorIndexOfLocationLng)) {
              _tmpLocationLng = null;
            } else {
              _tmpLocationLng = _cursor.getDouble(_cursorIndexOfLocationLng);
            }
            final float _tmpGeofenceRadius;
            _tmpGeofenceRadius = _cursor.getFloat(_cursorIndexOfGeofenceRadius);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TimetableEntry(_tmpId,_tmpCourseName,_tmpCourseCode,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpInstructor,_tmpLocationLat,_tmpLocationLng,_tmpGeofenceRadius,_tmpIsActive,_tmpCreatedAt);
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
  public Object getById(final long id, final Continuation<? super TimetableEntry> $completion) {
    final String _sql = "SELECT * FROM timetable_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TimetableEntry>() {
      @Override
      @Nullable
      public TimetableEntry call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfLocationLat = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLat");
          final int _cursorIndexOfLocationLng = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLng");
          final int _cursorIndexOfGeofenceRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceRadius");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final TimetableEntry _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final String _tmpCourseCode;
            _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            final DayOfWeek _tmpDayOfWeek;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toDayOfWeek(_tmp);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final Double _tmpLocationLat;
            if (_cursor.isNull(_cursorIndexOfLocationLat)) {
              _tmpLocationLat = null;
            } else {
              _tmpLocationLat = _cursor.getDouble(_cursorIndexOfLocationLat);
            }
            final Double _tmpLocationLng;
            if (_cursor.isNull(_cursorIndexOfLocationLng)) {
              _tmpLocationLng = null;
            } else {
              _tmpLocationLng = _cursor.getDouble(_cursorIndexOfLocationLng);
            }
            final float _tmpGeofenceRadius;
            _tmpGeofenceRadius = _cursor.getFloat(_cursorIndexOfGeofenceRadius);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new TimetableEntry(_tmpId,_tmpCourseName,_tmpCourseCode,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpInstructor,_tmpLocationLat,_tmpLocationLng,_tmpGeofenceRadius,_tmpIsActive,_tmpCreatedAt);
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
  public Flow<List<TimetableEntry>> getAll() {
    final String _sql = "SELECT * FROM timetable_entries ORDER BY dayOfWeek, startTimeMinutes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"timetable_entries"}, new Callable<List<TimetableEntry>>() {
      @Override
      @NonNull
      public List<TimetableEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfLocationLat = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLat");
          final int _cursorIndexOfLocationLng = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLng");
          final int _cursorIndexOfGeofenceRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceRadius");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TimetableEntry> _result = new ArrayList<TimetableEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TimetableEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final String _tmpCourseCode;
            _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            final DayOfWeek _tmpDayOfWeek;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toDayOfWeek(_tmp);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final Double _tmpLocationLat;
            if (_cursor.isNull(_cursorIndexOfLocationLat)) {
              _tmpLocationLat = null;
            } else {
              _tmpLocationLat = _cursor.getDouble(_cursorIndexOfLocationLat);
            }
            final Double _tmpLocationLng;
            if (_cursor.isNull(_cursorIndexOfLocationLng)) {
              _tmpLocationLng = null;
            } else {
              _tmpLocationLng = _cursor.getDouble(_cursorIndexOfLocationLng);
            }
            final float _tmpGeofenceRadius;
            _tmpGeofenceRadius = _cursor.getFloat(_cursorIndexOfGeofenceRadius);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TimetableEntry(_tmpId,_tmpCourseName,_tmpCourseCode,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpInstructor,_tmpLocationLat,_tmpLocationLng,_tmpGeofenceRadius,_tmpIsActive,_tmpCreatedAt);
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
