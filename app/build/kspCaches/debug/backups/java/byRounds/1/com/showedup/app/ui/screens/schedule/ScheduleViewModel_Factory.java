package com.showedup.app.ui.screens.schedule;

import android.content.Context;
import com.showedup.app.data.repository.ScheduleRepository;
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
public final class ScheduleViewModel_Factory implements Factory<ScheduleViewModel> {
  private final Provider<ScheduleRepository> scheduleRepositoryProvider;

  private final Provider<Context> appContextProvider;

  public ScheduleViewModel_Factory(Provider<ScheduleRepository> scheduleRepositoryProvider,
      Provider<Context> appContextProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.appContextProvider = appContextProvider;
  }

  @Override
  public ScheduleViewModel get() {
    return newInstance(scheduleRepositoryProvider.get(), appContextProvider.get());
  }

  public static ScheduleViewModel_Factory create(
      Provider<ScheduleRepository> scheduleRepositoryProvider,
      Provider<Context> appContextProvider) {
    return new ScheduleViewModel_Factory(scheduleRepositoryProvider, appContextProvider);
  }

  public static ScheduleViewModel newInstance(ScheduleRepository scheduleRepository,
      Context appContext) {
    return new ScheduleViewModel(scheduleRepository, appContext);
  }
}
