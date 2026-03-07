package com.showedup.app.ui.screens.calendar;

import com.showedup.app.data.repository.AttendanceRepository;
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
public final class CalendarViewModel_Factory implements Factory<CalendarViewModel> {
  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  public CalendarViewModel_Factory(Provider<AttendanceRepository> attendanceRepositoryProvider) {
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
  }

  @Override
  public CalendarViewModel get() {
    return newInstance(attendanceRepositoryProvider.get());
  }

  public static CalendarViewModel_Factory create(
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    return new CalendarViewModel_Factory(attendanceRepositoryProvider);
  }

  public static CalendarViewModel newInstance(AttendanceRepository attendanceRepository) {
    return new CalendarViewModel(attendanceRepository);
  }
}
