package com.showedup.app.data.repository;

import com.showedup.app.crypto.CanonicalJson;
import com.showedup.app.crypto.HashUtils;
import com.showedup.app.data.dao.TimetableDao;
import com.showedup.app.data.dao.WeeklyScheduleDao;
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
public final class ScheduleRepository_Factory implements Factory<ScheduleRepository> {
  private final Provider<TimetableDao> timetableDaoProvider;

  private final Provider<WeeklyScheduleDao> weeklyScheduleDaoProvider;

  private final Provider<HashUtils> hashUtilsProvider;

  private final Provider<CanonicalJson> canonicalJsonProvider;

  public ScheduleRepository_Factory(Provider<TimetableDao> timetableDaoProvider,
      Provider<WeeklyScheduleDao> weeklyScheduleDaoProvider, Provider<HashUtils> hashUtilsProvider,
      Provider<CanonicalJson> canonicalJsonProvider) {
    this.timetableDaoProvider = timetableDaoProvider;
    this.weeklyScheduleDaoProvider = weeklyScheduleDaoProvider;
    this.hashUtilsProvider = hashUtilsProvider;
    this.canonicalJsonProvider = canonicalJsonProvider;
  }

  @Override
  public ScheduleRepository get() {
    return newInstance(timetableDaoProvider.get(), weeklyScheduleDaoProvider.get(), hashUtilsProvider.get(), canonicalJsonProvider.get());
  }

  public static ScheduleRepository_Factory create(Provider<TimetableDao> timetableDaoProvider,
      Provider<WeeklyScheduleDao> weeklyScheduleDaoProvider, Provider<HashUtils> hashUtilsProvider,
      Provider<CanonicalJson> canonicalJsonProvider) {
    return new ScheduleRepository_Factory(timetableDaoProvider, weeklyScheduleDaoProvider, hashUtilsProvider, canonicalJsonProvider);
  }

  public static ScheduleRepository newInstance(TimetableDao timetableDao,
      WeeklyScheduleDao weeklyScheduleDao, HashUtils hashUtils, CanonicalJson canonicalJson) {
    return new ScheduleRepository(timetableDao, weeklyScheduleDao, hashUtils, canonicalJson);
  }
}
