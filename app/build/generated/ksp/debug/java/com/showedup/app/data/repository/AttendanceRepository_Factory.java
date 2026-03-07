package com.showedup.app.data.repository;

import com.showedup.app.crypto.CanonicalJson;
import com.showedup.app.crypto.HashUtils;
import com.showedup.app.crypto.KeystoreManager;
import com.showedup.app.data.dao.AttendanceDao;
import com.showedup.app.data.dao.DayOffDao;
import com.showedup.app.data.dao.PlannedDayOffDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AttendanceRepository_Factory implements Factory<AttendanceRepository> {
  private final Provider<AttendanceDao> attendanceDaoProvider;

  private final Provider<DayOffDao> dayOffDaoProvider;

  private final Provider<PlannedDayOffDao> plannedDayOffDaoProvider;

  private final Provider<HashUtils> hashUtilsProvider;

  private final Provider<CanonicalJson> canonicalJsonProvider;

  private final Provider<KeystoreManager> keystoreManagerProvider;

  public AttendanceRepository_Factory(Provider<AttendanceDao> attendanceDaoProvider,
      Provider<DayOffDao> dayOffDaoProvider, Provider<PlannedDayOffDao> plannedDayOffDaoProvider,
      Provider<HashUtils> hashUtilsProvider, Provider<CanonicalJson> canonicalJsonProvider,
      Provider<KeystoreManager> keystoreManagerProvider) {
    this.attendanceDaoProvider = attendanceDaoProvider;
    this.dayOffDaoProvider = dayOffDaoProvider;
    this.plannedDayOffDaoProvider = plannedDayOffDaoProvider;
    this.hashUtilsProvider = hashUtilsProvider;
    this.canonicalJsonProvider = canonicalJsonProvider;
    this.keystoreManagerProvider = keystoreManagerProvider;
  }

  @Override
  public AttendanceRepository get() {
    return newInstance(attendanceDaoProvider.get(), dayOffDaoProvider.get(), plannedDayOffDaoProvider.get(), hashUtilsProvider.get(), canonicalJsonProvider.get(), keystoreManagerProvider.get());
  }

  public static AttendanceRepository_Factory create(Provider<AttendanceDao> attendanceDaoProvider,
      Provider<DayOffDao> dayOffDaoProvider, Provider<PlannedDayOffDao> plannedDayOffDaoProvider,
      Provider<HashUtils> hashUtilsProvider, Provider<CanonicalJson> canonicalJsonProvider,
      Provider<KeystoreManager> keystoreManagerProvider) {
    return new AttendanceRepository_Factory(attendanceDaoProvider, dayOffDaoProvider, plannedDayOffDaoProvider, hashUtilsProvider, canonicalJsonProvider, keystoreManagerProvider);
  }

  public static AttendanceRepository newInstance(AttendanceDao attendanceDao, DayOffDao dayOffDao,
      PlannedDayOffDao plannedDayOffDao, HashUtils hashUtils, CanonicalJson canonicalJson,
      KeystoreManager keystoreManager) {
    return new AttendanceRepository(attendanceDao, dayOffDao, plannedDayOffDao, hashUtils, canonicalJson, keystoreManager);
  }
}
