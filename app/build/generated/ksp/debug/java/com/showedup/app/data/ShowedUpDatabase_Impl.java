package com.showedup.app.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.showedup.app.data.dao.AttendanceDao;
import com.showedup.app.data.dao.AttendanceDao_Impl;
import com.showedup.app.data.dao.DayOffDao;
import com.showedup.app.data.dao.DayOffDao_Impl;
import com.showedup.app.data.dao.PlannedDayOffDao;
import com.showedup.app.data.dao.PlannedDayOffDao_Impl;
import com.showedup.app.data.dao.SecurityEventDao;
import com.showedup.app.data.dao.SecurityEventDao_Impl;
import com.showedup.app.data.dao.TimetableDao;
import com.showedup.app.data.dao.TimetableDao_Impl;
import com.showedup.app.data.dao.WeeklyScheduleDao;
import com.showedup.app.data.dao.WeeklyScheduleDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ShowedUpDatabase_Impl extends ShowedUpDatabase {
  private volatile TimetableDao _timetableDao;

  private volatile AttendanceDao _attendanceDao;

  private volatile DayOffDao _dayOffDao;

  private volatile PlannedDayOffDao _plannedDayOffDao;

  private volatile WeeklyScheduleDao _weeklyScheduleDao;

  private volatile SecurityEventDao _securityEventDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `timetable_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `courseName` TEXT NOT NULL, `courseCode` TEXT NOT NULL, `dayOfWeek` INTEGER NOT NULL, `startTimeMinutes` INTEGER NOT NULL, `endTimeMinutes` INTEGER NOT NULL, `room` TEXT NOT NULL, `instructor` TEXT NOT NULL, `locationLat` REAL, `locationLng` REAL, `geofenceRadius` REAL NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attendance_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timetableEntryId` INTEGER NOT NULL, `courseName` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `date` TEXT NOT NULL, `gpsHash` TEXT, `wifiHash` TEXT, `bluetoothHash` TEXT, `audioHash` TEXT, `sensorHash` TEXT, `gpsAvailable` INTEGER NOT NULL, `wifiAvailable` INTEGER NOT NULL, `bluetoothAvailable` INTEGER NOT NULL, `audioAvailable` INTEGER NOT NULL, `sensorAvailable` INTEGER NOT NULL, `chainHash` TEXT NOT NULL, `previousChainHash` TEXT, `signature` TEXT NOT NULL, `canonicalJson` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `day_off_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `date` TEXT NOT NULL, `reason` TEXT NOT NULL, `declaredAt` INTEGER NOT NULL, `classesSuppressed` TEXT NOT NULL, `chainHash` TEXT NOT NULL, `previousChainHash` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `planned_day_offs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `targetDate` TEXT NOT NULL, `eventName` TEXT NOT NULL, `type` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `chainHash` TEXT NOT NULL, `previousChainHash` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `weekly_schedules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activeDays` TEXT NOT NULL, `inactiveDays` TEXT NOT NULL, `effectiveFrom` TEXT NOT NULL, `effectiveTo` TEXT, `createdAt` INTEGER NOT NULL, `chainHash` TEXT NOT NULL, `previousChainHash` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `security_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventType` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `data` TEXT NOT NULL, `chainHash` TEXT NOT NULL, `previousChainHash` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ebd4efb75b902281d2a09de1af82f506')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `timetable_entries`");
        db.execSQL("DROP TABLE IF EXISTS `attendance_records`");
        db.execSQL("DROP TABLE IF EXISTS `day_off_records`");
        db.execSQL("DROP TABLE IF EXISTS `planned_day_offs`");
        db.execSQL("DROP TABLE IF EXISTS `weekly_schedules`");
        db.execSQL("DROP TABLE IF EXISTS `security_events`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTimetableEntries = new HashMap<String, TableInfo.Column>(13);
        _columnsTimetableEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("courseName", new TableInfo.Column("courseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("courseCode", new TableInfo.Column("courseCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("dayOfWeek", new TableInfo.Column("dayOfWeek", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("startTimeMinutes", new TableInfo.Column("startTimeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("endTimeMinutes", new TableInfo.Column("endTimeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("room", new TableInfo.Column("room", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("instructor", new TableInfo.Column("instructor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("locationLat", new TableInfo.Column("locationLat", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("locationLng", new TableInfo.Column("locationLng", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("geofenceRadius", new TableInfo.Column("geofenceRadius", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTimetableEntries.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTimetableEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTimetableEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTimetableEntries = new TableInfo("timetable_entries", _columnsTimetableEntries, _foreignKeysTimetableEntries, _indicesTimetableEntries);
        final TableInfo _existingTimetableEntries = TableInfo.read(db, "timetable_entries");
        if (!_infoTimetableEntries.equals(_existingTimetableEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "timetable_entries(com.showedup.app.data.entity.TimetableEntry).\n"
                  + " Expected:\n" + _infoTimetableEntries + "\n"
                  + " Found:\n" + _existingTimetableEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsAttendanceRecords = new HashMap<String, TableInfo.Column>(19);
        _columnsAttendanceRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("timetableEntryId", new TableInfo.Column("timetableEntryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("courseName", new TableInfo.Column("courseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("gpsHash", new TableInfo.Column("gpsHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("wifiHash", new TableInfo.Column("wifiHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("bluetoothHash", new TableInfo.Column("bluetoothHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("audioHash", new TableInfo.Column("audioHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("sensorHash", new TableInfo.Column("sensorHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("gpsAvailable", new TableInfo.Column("gpsAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("wifiAvailable", new TableInfo.Column("wifiAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("bluetoothAvailable", new TableInfo.Column("bluetoothAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("audioAvailable", new TableInfo.Column("audioAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("sensorAvailable", new TableInfo.Column("sensorAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("chainHash", new TableInfo.Column("chainHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("previousChainHash", new TableInfo.Column("previousChainHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("signature", new TableInfo.Column("signature", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendanceRecords.put("canonicalJson", new TableInfo.Column("canonicalJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttendanceRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttendanceRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAttendanceRecords = new TableInfo("attendance_records", _columnsAttendanceRecords, _foreignKeysAttendanceRecords, _indicesAttendanceRecords);
        final TableInfo _existingAttendanceRecords = TableInfo.read(db, "attendance_records");
        if (!_infoAttendanceRecords.equals(_existingAttendanceRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "attendance_records(com.showedup.app.data.entity.AttendanceRecordEntity).\n"
                  + " Expected:\n" + _infoAttendanceRecords + "\n"
                  + " Found:\n" + _existingAttendanceRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsDayOffRecords = new HashMap<String, TableInfo.Column>(8);
        _columnsDayOffRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("reason", new TableInfo.Column("reason", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("declaredAt", new TableInfo.Column("declaredAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("classesSuppressed", new TableInfo.Column("classesSuppressed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("chainHash", new TableInfo.Column("chainHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayOffRecords.put("previousChainHash", new TableInfo.Column("previousChainHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDayOffRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDayOffRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDayOffRecords = new TableInfo("day_off_records", _columnsDayOffRecords, _foreignKeysDayOffRecords, _indicesDayOffRecords);
        final TableInfo _existingDayOffRecords = TableInfo.read(db, "day_off_records");
        if (!_infoDayOffRecords.equals(_existingDayOffRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "day_off_records(com.showedup.app.data.entity.DayOffRecordEntity).\n"
                  + " Expected:\n" + _infoDayOffRecords + "\n"
                  + " Found:\n" + _existingDayOffRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsPlannedDayOffs = new HashMap<String, TableInfo.Column>(8);
        _columnsPlannedDayOffs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("targetDate", new TableInfo.Column("targetDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("eventName", new TableInfo.Column("eventName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("chainHash", new TableInfo.Column("chainHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlannedDayOffs.put("previousChainHash", new TableInfo.Column("previousChainHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlannedDayOffs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlannedDayOffs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlannedDayOffs = new TableInfo("planned_day_offs", _columnsPlannedDayOffs, _foreignKeysPlannedDayOffs, _indicesPlannedDayOffs);
        final TableInfo _existingPlannedDayOffs = TableInfo.read(db, "planned_day_offs");
        if (!_infoPlannedDayOffs.equals(_existingPlannedDayOffs)) {
          return new RoomOpenHelper.ValidationResult(false, "planned_day_offs(com.showedup.app.data.entity.PlannedDayOffEntity).\n"
                  + " Expected:\n" + _infoPlannedDayOffs + "\n"
                  + " Found:\n" + _existingPlannedDayOffs);
        }
        final HashMap<String, TableInfo.Column> _columnsWeeklySchedules = new HashMap<String, TableInfo.Column>(8);
        _columnsWeeklySchedules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("activeDays", new TableInfo.Column("activeDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("inactiveDays", new TableInfo.Column("inactiveDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("effectiveFrom", new TableInfo.Column("effectiveFrom", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("effectiveTo", new TableInfo.Column("effectiveTo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("chainHash", new TableInfo.Column("chainHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeklySchedules.put("previousChainHash", new TableInfo.Column("previousChainHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeeklySchedules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWeeklySchedules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWeeklySchedules = new TableInfo("weekly_schedules", _columnsWeeklySchedules, _foreignKeysWeeklySchedules, _indicesWeeklySchedules);
        final TableInfo _existingWeeklySchedules = TableInfo.read(db, "weekly_schedules");
        if (!_infoWeeklySchedules.equals(_existingWeeklySchedules)) {
          return new RoomOpenHelper.ValidationResult(false, "weekly_schedules(com.showedup.app.data.entity.WeeklyScheduleEntity).\n"
                  + " Expected:\n" + _infoWeeklySchedules + "\n"
                  + " Found:\n" + _existingWeeklySchedules);
        }
        final HashMap<String, TableInfo.Column> _columnsSecurityEvents = new HashMap<String, TableInfo.Column>(6);
        _columnsSecurityEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecurityEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecurityEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecurityEvents.put("data", new TableInfo.Column("data", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecurityEvents.put("chainHash", new TableInfo.Column("chainHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecurityEvents.put("previousChainHash", new TableInfo.Column("previousChainHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSecurityEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSecurityEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSecurityEvents = new TableInfo("security_events", _columnsSecurityEvents, _foreignKeysSecurityEvents, _indicesSecurityEvents);
        final TableInfo _existingSecurityEvents = TableInfo.read(db, "security_events");
        if (!_infoSecurityEvents.equals(_existingSecurityEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "security_events(com.showedup.app.data.entity.SecurityEventEntity).\n"
                  + " Expected:\n" + _infoSecurityEvents + "\n"
                  + " Found:\n" + _existingSecurityEvents);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ebd4efb75b902281d2a09de1af82f506", "3b9ab6371f99c9c675069e4bd32b969d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "timetable_entries","attendance_records","day_off_records","planned_day_offs","weekly_schedules","security_events");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `timetable_entries`");
      _db.execSQL("DELETE FROM `attendance_records`");
      _db.execSQL("DELETE FROM `day_off_records`");
      _db.execSQL("DELETE FROM `planned_day_offs`");
      _db.execSQL("DELETE FROM `weekly_schedules`");
      _db.execSQL("DELETE FROM `security_events`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TimetableDao.class, TimetableDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AttendanceDao.class, AttendanceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DayOffDao.class, DayOffDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlannedDayOffDao.class, PlannedDayOffDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WeeklyScheduleDao.class, WeeklyScheduleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SecurityEventDao.class, SecurityEventDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TimetableDao timetableDao() {
    if (_timetableDao != null) {
      return _timetableDao;
    } else {
      synchronized(this) {
        if(_timetableDao == null) {
          _timetableDao = new TimetableDao_Impl(this);
        }
        return _timetableDao;
      }
    }
  }

  @Override
  public AttendanceDao attendanceDao() {
    if (_attendanceDao != null) {
      return _attendanceDao;
    } else {
      synchronized(this) {
        if(_attendanceDao == null) {
          _attendanceDao = new AttendanceDao_Impl(this);
        }
        return _attendanceDao;
      }
    }
  }

  @Override
  public DayOffDao dayOffDao() {
    if (_dayOffDao != null) {
      return _dayOffDao;
    } else {
      synchronized(this) {
        if(_dayOffDao == null) {
          _dayOffDao = new DayOffDao_Impl(this);
        }
        return _dayOffDao;
      }
    }
  }

  @Override
  public PlannedDayOffDao plannedDayOffDao() {
    if (_plannedDayOffDao != null) {
      return _plannedDayOffDao;
    } else {
      synchronized(this) {
        if(_plannedDayOffDao == null) {
          _plannedDayOffDao = new PlannedDayOffDao_Impl(this);
        }
        return _plannedDayOffDao;
      }
    }
  }

  @Override
  public WeeklyScheduleDao weeklyScheduleDao() {
    if (_weeklyScheduleDao != null) {
      return _weeklyScheduleDao;
    } else {
      synchronized(this) {
        if(_weeklyScheduleDao == null) {
          _weeklyScheduleDao = new WeeklyScheduleDao_Impl(this);
        }
        return _weeklyScheduleDao;
      }
    }
  }

  @Override
  public SecurityEventDao securityEventDao() {
    if (_securityEventDao != null) {
      return _securityEventDao;
    } else {
      synchronized(this) {
        if(_securityEventDao == null) {
          _securityEventDao = new SecurityEventDao_Impl(this);
        }
        return _securityEventDao;
      }
    }
  }
}
