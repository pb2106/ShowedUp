package com.showedup.app.ui.screens.log;

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
public final class AttendanceLogViewModel_Factory implements Factory<AttendanceLogViewModel> {
  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  public AttendanceLogViewModel_Factory(
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
  }

  @Override
  public AttendanceLogViewModel get() {
    return newInstance(attendanceRepositoryProvider.get());
  }

  public static AttendanceLogViewModel_Factory create(
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    return new AttendanceLogViewModel_Factory(attendanceRepositoryProvider);
  }

  public static AttendanceLogViewModel newInstance(AttendanceRepository attendanceRepository) {
    return new AttendanceLogViewModel(attendanceRepository);
  }
}
