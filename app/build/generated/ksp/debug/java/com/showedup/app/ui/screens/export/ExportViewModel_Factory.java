package com.showedup.app.ui.screens.export;

import android.content.Context;
import com.showedup.app.data.repository.AttendanceRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ExportViewModel_Factory implements Factory<ExportViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  public ExportViewModel_Factory(Provider<Context> contextProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
  }

  @Override
  public ExportViewModel get() {
    return newInstance(contextProvider.get(), attendanceRepositoryProvider.get());
  }

  public static ExportViewModel_Factory create(Provider<Context> contextProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    return new ExportViewModel_Factory(contextProvider, attendanceRepositoryProvider);
  }

  public static ExportViewModel newInstance(Context context,
      AttendanceRepository attendanceRepository) {
    return new ExportViewModel(context, attendanceRepository);
  }
}
