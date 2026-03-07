package com.showedup.app.ui.screens.home;

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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ScheduleRepository> scheduleRepositoryProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  public HomeViewModel_Factory(Provider<ScheduleRepository> scheduleRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(scheduleRepositoryProvider.get(), attendanceRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<ScheduleRepository> scheduleRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    return new HomeViewModel_Factory(scheduleRepositoryProvider, attendanceRepositoryProvider);
  }

  public static HomeViewModel newInstance(ScheduleRepository scheduleRepository,
      AttendanceRepository attendanceRepository) {
    return new HomeViewModel(scheduleRepository, attendanceRepository);
  }
}
