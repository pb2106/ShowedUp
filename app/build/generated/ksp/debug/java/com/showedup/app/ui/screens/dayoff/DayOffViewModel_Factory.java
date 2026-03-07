package com.showedup.app.ui.screens.dayoff;

import com.showedup.app.data.repository.AttendanceRepository;
import com.showedup.app.data.repository.ScheduleRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DayOffViewModel_Factory implements Factory<DayOffViewModel> {
  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  private final Provider<ScheduleRepository> scheduleRepositoryProvider;

  public DayOffViewModel_Factory(Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<ScheduleRepository> scheduleRepositoryProvider) {
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
  }

  @Override
  public DayOffViewModel get() {
    return newInstance(attendanceRepositoryProvider.get(), scheduleRepositoryProvider.get());
  }

  public static DayOffViewModel_Factory create(
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<ScheduleRepository> scheduleRepositoryProvider) {
    return new DayOffViewModel_Factory(attendanceRepositoryProvider, scheduleRepositoryProvider);
  }

  public static DayOffViewModel newInstance(AttendanceRepository attendanceRepository,
      ScheduleRepository scheduleRepository) {
    return new DayOffViewModel(attendanceRepository, scheduleRepository);
  }
}
