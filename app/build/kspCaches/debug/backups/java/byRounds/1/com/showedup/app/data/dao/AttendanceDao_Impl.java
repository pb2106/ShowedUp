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
import com.showedup.app.data.entity.AttendanceRecordEntity;
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
public final class AttendanceDao_Impl implements AttendanceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttendanceRecordEntity> __insertionAdapterOfAttendanceRecordEntity;

  public AttendanceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttendanceRecordEntity = new EntityInsertionAdapter<AttendanceRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `attendance_records` (`id`,`timetableEntryId`,`courseName`,`timestamp`,`date`,`gpsHash`,`wifiHash`,`bluetoothHash`,`audioHash`,`sensorHash`,`gpsAvailable`,`wifiAvailable`,`bluetoothAvailable`,`audioAvailable`,`sensorAvailable`,`chainHash`,`previousChainHash`,`signature`,`canonicalJson`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceRecordEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimetableEntryId());
        statement.bindString(3, entity.getCourseName());
        statement.bindLong(4, entity.getTimestamp());
        statement.bindString(5, entity.getDate());
        if (entity.getGpsHash() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getGpsHash());
        }
        if (entity.getWifiHash() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getWifiHash());
        }
        if (entity.getBluetoothHash() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getBluetoothHash());
        }
        if (entity.getAudioHash() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAudioHash());
        }
        if (entity.getSensorHash() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSensorHash());
        }
        final int _tmp = entity.getGpsAvailable() ? 1 : 0;
        statement.bindLong(11, _tmp);
        final int _tmp_1 = entity.getWifiAvailable() ? 1 : 0;
        statement.bindLong(12, _tmp_1);
        final int _tmp_2 = entity.getBluetoothAvailable() ? 1 : 0;
        statement.bindLong(13, _tmp_2);
        final int _tmp_3 = entity.getAudioAvailable() ? 1 : 0;
        statement.bindLong(14, _tmp_3);
        final int _tmp_4 = entity.getSensorAvailable() ? 1 : 0;
        statement.bindLong(15, _tmp_4);
        statement.bindString(16, entity.getChainHash());
        if (entity.getPreviousChainHash() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getPreviousChainHash());
        }
        statement.bindString(18, entity.getSignature());
        statement.bindString(19, entity.getCanonicalJson());
      }
    };
  }

  @Override
  public Object insert(final AttendanceRecordEntity record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAttendanceRecordEntity.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AttendanceRecordEntity>> getAll() {
    final String _sql = "SELECT * FROM attendance_records ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecordEntity>>() {
      @Override
      @NonNull
      public List<AttendanceRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimetableEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "timetableEntryId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGpsHash = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsHash");
          final int _cursorIndexOfWifiHash = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiHash");
          final int _cursorIndexOfBluetoothHash = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothHash");
          final int _cursorIndexOfAudioHash = CursorUtil.getColumnIndexOrThrow(_cursor, "audioHash");
          final int _cursorIndexOfSensorHash = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorHash");
          final int _cursorIndexOfGpsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAvailable");
          final int _cursorIndexOfWifiAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiAvailable");
          final int _cursorIndexOfBluetoothAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothAvailable");
          final int _cursorIndexOfAudioAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "audioAvailable");
          final int _cursorIndexOfSensorAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorAvailable");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfCanonicalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalJson");
          final List<AttendanceRecordEntity> _result = new ArrayList<AttendanceRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimetableEntryId;
            _tmpTimetableEntryId = _cursor.getLong(_cursorIndexOfTimetableEntryId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpGpsHash;
            if (_cursor.isNull(_cursorIndexOfGpsHash)) {
              _tmpGpsHash = null;
            } else {
              _tmpGpsHash = _cursor.getString(_cursorIndexOfGpsHash);
            }
            final String _tmpWifiHash;
            if (_cursor.isNull(_cursorIndexOfWifiHash)) {
              _tmpWifiHash = null;
            } else {
              _tmpWifiHash = _cursor.getString(_cursorIndexOfWifiHash);
            }
            final String _tmpBluetoothHash;
            if (_cursor.isNull(_cursorIndexOfBluetoothHash)) {
              _tmpBluetoothHash = null;
            } else {
              _tmpBluetoothHash = _cursor.getString(_cursorIndexOfBluetoothHash);
            }
            final String _tmpAudioHash;
            if (_cursor.isNull(_cursorIndexOfAudioHash)) {
              _tmpAudioHash = null;
            } else {
              _tmpAudioHash = _cursor.getString(_cursorIndexOfAudioHash);
            }
            final String _tmpSensorHash;
            if (_cursor.isNull(_cursorIndexOfSensorHash)) {
              _tmpSensorHash = null;
            } else {
              _tmpSensorHash = _cursor.getString(_cursorIndexOfSensorHash);
            }
            final boolean _tmpGpsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfGpsAvailable);
            _tmpGpsAvailable = _tmp != 0;
            final boolean _tmpWifiAvailable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWifiAvailable);
            _tmpWifiAvailable = _tmp_1 != 0;
            final boolean _tmpBluetoothAvailable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfBluetoothAvailable);
            _tmpBluetoothAvailable = _tmp_2 != 0;
            final boolean _tmpAudioAvailable;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAudioAvailable);
            _tmpAudioAvailable = _tmp_3 != 0;
            final boolean _tmpSensorAvailable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfSensorAvailable);
            _tmpSensorAvailable = _tmp_4 != 0;
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            final String _tmpSignature;
            _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            final String _tmpCanonicalJson;
            _tmpCanonicalJson = _cursor.getString(_cursorIndexOfCanonicalJson);
            _item = new AttendanceRecordEntity(_tmpId,_tmpTimetableEntryId,_tmpCourseName,_tmpTimestamp,_tmpDate,_tmpGpsHash,_tmpWifiHash,_tmpBluetoothHash,_tmpAudioHash,_tmpSensorHash,_tmpGpsAvailable,_tmpWifiAvailable,_tmpBluetoothAvailable,_tmpAudioAvailable,_tmpSensorAvailable,_tmpChainHash,_tmpPreviousChainHash,_tmpSignature,_tmpCanonicalJson);
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
  public Flow<List<AttendanceRecordEntity>> getByDate(final String date) {
    final String _sql = "SELECT * FROM attendance_records WHERE date = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecordEntity>>() {
      @Override
      @NonNull
      public List<AttendanceRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimetableEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "timetableEntryId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGpsHash = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsHash");
          final int _cursorIndexOfWifiHash = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiHash");
          final int _cursorIndexOfBluetoothHash = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothHash");
          final int _cursorIndexOfAudioHash = CursorUtil.getColumnIndexOrThrow(_cursor, "audioHash");
          final int _cursorIndexOfSensorHash = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorHash");
          final int _cursorIndexOfGpsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAvailable");
          final int _cursorIndexOfWifiAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiAvailable");
          final int _cursorIndexOfBluetoothAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothAvailable");
          final int _cursorIndexOfAudioAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "audioAvailable");
          final int _cursorIndexOfSensorAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorAvailable");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfCanonicalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalJson");
          final List<AttendanceRecordEntity> _result = new ArrayList<AttendanceRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimetableEntryId;
            _tmpTimetableEntryId = _cursor.getLong(_cursorIndexOfTimetableEntryId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpGpsHash;
            if (_cursor.isNull(_cursorIndexOfGpsHash)) {
              _tmpGpsHash = null;
            } else {
              _tmpGpsHash = _cursor.getString(_cursorIndexOfGpsHash);
            }
            final String _tmpWifiHash;
            if (_cursor.isNull(_cursorIndexOfWifiHash)) {
              _tmpWifiHash = null;
            } else {
              _tmpWifiHash = _cursor.getString(_cursorIndexOfWifiHash);
            }
            final String _tmpBluetoothHash;
            if (_cursor.isNull(_cursorIndexOfBluetoothHash)) {
              _tmpBluetoothHash = null;
            } else {
              _tmpBluetoothHash = _cursor.getString(_cursorIndexOfBluetoothHash);
            }
            final String _tmpAudioHash;
            if (_cursor.isNull(_cursorIndexOfAudioHash)) {
              _tmpAudioHash = null;
            } else {
              _tmpAudioHash = _cursor.getString(_cursorIndexOfAudioHash);
            }
            final String _tmpSensorHash;
            if (_cursor.isNull(_cursorIndexOfSensorHash)) {
              _tmpSensorHash = null;
            } else {
              _tmpSensorHash = _cursor.getString(_cursorIndexOfSensorHash);
            }
            final boolean _tmpGpsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfGpsAvailable);
            _tmpGpsAvailable = _tmp != 0;
            final boolean _tmpWifiAvailable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWifiAvailable);
            _tmpWifiAvailable = _tmp_1 != 0;
            final boolean _tmpBluetoothAvailable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfBluetoothAvailable);
            _tmpBluetoothAvailable = _tmp_2 != 0;
            final boolean _tmpAudioAvailable;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAudioAvailable);
            _tmpAudioAvailable = _tmp_3 != 0;
            final boolean _tmpSensorAvailable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfSensorAvailable);
            _tmpSensorAvailable = _tmp_4 != 0;
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            final String _tmpSignature;
            _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            final String _tmpCanonicalJson;
            _tmpCanonicalJson = _cursor.getString(_cursorIndexOfCanonicalJson);
            _item = new AttendanceRecordEntity(_tmpId,_tmpTimetableEntryId,_tmpCourseName,_tmpTimestamp,_tmpDate,_tmpGpsHash,_tmpWifiHash,_tmpBluetoothHash,_tmpAudioHash,_tmpSensorHash,_tmpGpsAvailable,_tmpWifiAvailable,_tmpBluetoothAvailable,_tmpAudioAvailable,_tmpSensorAvailable,_tmpChainHash,_tmpPreviousChainHash,_tmpSignature,_tmpCanonicalJson);
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
  public Flow<List<AttendanceRecordEntity>> getByDateRange(final String startDate,
      final String endDate) {
    final String _sql = "SELECT * FROM attendance_records WHERE date BETWEEN ? AND ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecordEntity>>() {
      @Override
      @NonNull
      public List<AttendanceRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimetableEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "timetableEntryId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGpsHash = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsHash");
          final int _cursorIndexOfWifiHash = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiHash");
          final int _cursorIndexOfBluetoothHash = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothHash");
          final int _cursorIndexOfAudioHash = CursorUtil.getColumnIndexOrThrow(_cursor, "audioHash");
          final int _cursorIndexOfSensorHash = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorHash");
          final int _cursorIndexOfGpsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAvailable");
          final int _cursorIndexOfWifiAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiAvailable");
          final int _cursorIndexOfBluetoothAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothAvailable");
          final int _cursorIndexOfAudioAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "audioAvailable");
          final int _cursorIndexOfSensorAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorAvailable");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfCanonicalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalJson");
          final List<AttendanceRecordEntity> _result = new ArrayList<AttendanceRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimetableEntryId;
            _tmpTimetableEntryId = _cursor.getLong(_cursorIndexOfTimetableEntryId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpGpsHash;
            if (_cursor.isNull(_cursorIndexOfGpsHash)) {
              _tmpGpsHash = null;
            } else {
              _tmpGpsHash = _cursor.getString(_cursorIndexOfGpsHash);
            }
            final String _tmpWifiHash;
            if (_cursor.isNull(_cursorIndexOfWifiHash)) {
              _tmpWifiHash = null;
            } else {
              _tmpWifiHash = _cursor.getString(_cursorIndexOfWifiHash);
            }
            final String _tmpBluetoothHash;
            if (_cursor.isNull(_cursorIndexOfBluetoothHash)) {
              _tmpBluetoothHash = null;
            } else {
              _tmpBluetoothHash = _cursor.getString(_cursorIndexOfBluetoothHash);
            }
            final String _tmpAudioHash;
            if (_cursor.isNull(_cursorIndexOfAudioHash)) {
              _tmpAudioHash = null;
            } else {
              _tmpAudioHash = _cursor.getString(_cursorIndexOfAudioHash);
            }
            final String _tmpSensorHash;
            if (_cursor.isNull(_cursorIndexOfSensorHash)) {
              _tmpSensorHash = null;
            } else {
              _tmpSensorHash = _cursor.getString(_cursorIndexOfSensorHash);
            }
            final boolean _tmpGpsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfGpsAvailable);
            _tmpGpsAvailable = _tmp != 0;
            final boolean _tmpWifiAvailable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWifiAvailable);
            _tmpWifiAvailable = _tmp_1 != 0;
            final boolean _tmpBluetoothAvailable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfBluetoothAvailable);
            _tmpBluetoothAvailable = _tmp_2 != 0;
            final boolean _tmpAudioAvailable;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAudioAvailable);
            _tmpAudioAvailable = _tmp_3 != 0;
            final boolean _tmpSensorAvailable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfSensorAvailable);
            _tmpSensorAvailable = _tmp_4 != 0;
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            final String _tmpSignature;
            _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            final String _tmpCanonicalJson;
            _tmpCanonicalJson = _cursor.getString(_cursorIndexOfCanonicalJson);
            _item = new AttendanceRecordEntity(_tmpId,_tmpTimetableEntryId,_tmpCourseName,_tmpTimestamp,_tmpDate,_tmpGpsHash,_tmpWifiHash,_tmpBluetoothHash,_tmpAudioHash,_tmpSensorHash,_tmpGpsAvailable,_tmpWifiAvailable,_tmpBluetoothAvailable,_tmpAudioAvailable,_tmpSensorAvailable,_tmpChainHash,_tmpPreviousChainHash,_tmpSignature,_tmpCanonicalJson);
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
  public Flow<List<AttendanceRecordEntity>> search(final String query) {
    final String _sql = "SELECT * FROM attendance_records WHERE courseName LIKE '%' || ? || '%' ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecordEntity>>() {
      @Override
      @NonNull
      public List<AttendanceRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimetableEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "timetableEntryId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGpsHash = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsHash");
          final int _cursorIndexOfWifiHash = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiHash");
          final int _cursorIndexOfBluetoothHash = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothHash");
          final int _cursorIndexOfAudioHash = CursorUtil.getColumnIndexOrThrow(_cursor, "audioHash");
          final int _cursorIndexOfSensorHash = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorHash");
          final int _cursorIndexOfGpsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAvailable");
          final int _cursorIndexOfWifiAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiAvailable");
          final int _cursorIndexOfBluetoothAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothAvailable");
          final int _cursorIndexOfAudioAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "audioAvailable");
          final int _cursorIndexOfSensorAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorAvailable");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfCanonicalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalJson");
          final List<AttendanceRecordEntity> _result = new ArrayList<AttendanceRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimetableEntryId;
            _tmpTimetableEntryId = _cursor.getLong(_cursorIndexOfTimetableEntryId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpGpsHash;
            if (_cursor.isNull(_cursorIndexOfGpsHash)) {
              _tmpGpsHash = null;
            } else {
              _tmpGpsHash = _cursor.getString(_cursorIndexOfGpsHash);
            }
            final String _tmpWifiHash;
            if (_cursor.isNull(_cursorIndexOfWifiHash)) {
              _tmpWifiHash = null;
            } else {
              _tmpWifiHash = _cursor.getString(_cursorIndexOfWifiHash);
            }
            final String _tmpBluetoothHash;
            if (_cursor.isNull(_cursorIndexOfBluetoothHash)) {
              _tmpBluetoothHash = null;
            } else {
              _tmpBluetoothHash = _cursor.getString(_cursorIndexOfBluetoothHash);
            }
            final String _tmpAudioHash;
            if (_cursor.isNull(_cursorIndexOfAudioHash)) {
              _tmpAudioHash = null;
            } else {
              _tmpAudioHash = _cursor.getString(_cursorIndexOfAudioHash);
            }
            final String _tmpSensorHash;
            if (_cursor.isNull(_cursorIndexOfSensorHash)) {
              _tmpSensorHash = null;
            } else {
              _tmpSensorHash = _cursor.getString(_cursorIndexOfSensorHash);
            }
            final boolean _tmpGpsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfGpsAvailable);
            _tmpGpsAvailable = _tmp != 0;
            final boolean _tmpWifiAvailable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWifiAvailable);
            _tmpWifiAvailable = _tmp_1 != 0;
            final boolean _tmpBluetoothAvailable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfBluetoothAvailable);
            _tmpBluetoothAvailable = _tmp_2 != 0;
            final boolean _tmpAudioAvailable;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAudioAvailable);
            _tmpAudioAvailable = _tmp_3 != 0;
            final boolean _tmpSensorAvailable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfSensorAvailable);
            _tmpSensorAvailable = _tmp_4 != 0;
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            final String _tmpSignature;
            _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            final String _tmpCanonicalJson;
            _tmpCanonicalJson = _cursor.getString(_cursorIndexOfCanonicalJson);
            _item = new AttendanceRecordEntity(_tmpId,_tmpTimetableEntryId,_tmpCourseName,_tmpTimestamp,_tmpDate,_tmpGpsHash,_tmpWifiHash,_tmpBluetoothHash,_tmpAudioHash,_tmpSensorHash,_tmpGpsAvailable,_tmpWifiAvailable,_tmpBluetoothAvailable,_tmpAudioAvailable,_tmpSensorAvailable,_tmpChainHash,_tmpPreviousChainHash,_tmpSignature,_tmpCanonicalJson);
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
  public Object getLatest(final Continuation<? super AttendanceRecordEntity> $completion) {
    final String _sql = "SELECT * FROM attendance_records ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AttendanceRecordEntity>() {
      @Override
      @Nullable
      public AttendanceRecordEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimetableEntryId = CursorUtil.getColumnIndexOrThrow(_cursor, "timetableEntryId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGpsHash = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsHash");
          final int _cursorIndexOfWifiHash = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiHash");
          final int _cursorIndexOfBluetoothHash = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothHash");
          final int _cursorIndexOfAudioHash = CursorUtil.getColumnIndexOrThrow(_cursor, "audioHash");
          final int _cursorIndexOfSensorHash = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorHash");
          final int _cursorIndexOfGpsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAvailable");
          final int _cursorIndexOfWifiAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiAvailable");
          final int _cursorIndexOfBluetoothAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothAvailable");
          final int _cursorIndexOfAudioAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "audioAvailable");
          final int _cursorIndexOfSensorAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorAvailable");
          final int _cursorIndexOfChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "chainHash");
          final int _cursorIndexOfPreviousChainHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousChainHash");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfCanonicalJson = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalJson");
          final AttendanceRecordEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimetableEntryId;
            _tmpTimetableEntryId = _cursor.getLong(_cursorIndexOfTimetableEntryId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpGpsHash;
            if (_cursor.isNull(_cursorIndexOfGpsHash)) {
              _tmpGpsHash = null;
            } else {
              _tmpGpsHash = _cursor.getString(_cursorIndexOfGpsHash);
            }
            final String _tmpWifiHash;
            if (_cursor.isNull(_cursorIndexOfWifiHash)) {
              _tmpWifiHash = null;
            } else {
              _tmpWifiHash = _cursor.getString(_cursorIndexOfWifiHash);
            }
            final String _tmpBluetoothHash;
            if (_cursor.isNull(_cursorIndexOfBluetoothHash)) {
              _tmpBluetoothHash = null;
            } else {
              _tmpBluetoothHash = _cursor.getString(_cursorIndexOfBluetoothHash);
            }
            final String _tmpAudioHash;
            if (_cursor.isNull(_cursorIndexOfAudioHash)) {
              _tmpAudioHash = null;
            } else {
              _tmpAudioHash = _cursor.getString(_cursorIndexOfAudioHash);
            }
            final String _tmpSensorHash;
            if (_cursor.isNull(_cursorIndexOfSensorHash)) {
              _tmpSensorHash = null;
            } else {
              _tmpSensorHash = _cursor.getString(_cursorIndexOfSensorHash);
            }
            final boolean _tmpGpsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfGpsAvailable);
            _tmpGpsAvailable = _tmp != 0;
            final boolean _tmpWifiAvailable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfWifiAvailable);
            _tmpWifiAvailable = _tmp_1 != 0;
            final boolean _tmpBluetoothAvailable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfBluetoothAvailable);
            _tmpBluetoothAvailable = _tmp_2 != 0;
            final boolean _tmpAudioAvailable;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAudioAvailable);
            _tmpAudioAvailable = _tmp_3 != 0;
            final boolean _tmpSensorAvailable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfSensorAvailable);
            _tmpSensorAvailable = _tmp_4 != 0;
            final String _tmpChainHash;
            _tmpChainHash = _cursor.getString(_cursorIndexOfChainHash);
            final String _tmpPreviousChainHash;
            if (_cursor.isNull(_cursorIndexOfPreviousChainHash)) {
              _tmpPreviousChainHash = null;
            } else {
              _tmpPreviousChainHash = _cursor.getString(_cursorIndexOfPreviousChainHash);
            }
            final String _tmpSignature;
            _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            final String _tmpCanonicalJson;
            _tmpCanonicalJson = _cursor.getString(_cursorIndexOfCanonicalJson);
            _result = new AttendanceRecordEntity(_tmpId,_tmpTimetableEntryId,_tmpCourseName,_tmpTimestamp,_tmpDate,_tmpGpsHash,_tmpWifiHash,_tmpBluetoothHash,_tmpAudioHash,_tmpSensorHash,_tmpGpsAvailable,_tmpWifiAvailable,_tmpBluetoothAvailable,_tmpAudioAvailable,_tmpSensorAvailable,_tmpChainHash,_tmpPreviousChainHash,_tmpSignature,_tmpCanonicalJson);
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
  public Object countByDate(final String date, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM attendance_records WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
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

  @Override
  public Flow<List<String>> getAllDates() {
    final String _sql = "SELECT DISTINCT date FROM attendance_records ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
