package com.showedup.app.ui.screens.schedule;

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
public final class ScheduleViewModel_Factory implements Factory<ScheduleViewModel> {
  private final Provider<ScheduleRepository> scheduleRepositoryProvider;

  public ScheduleViewModel_Factory(Provider<ScheduleRepository> scheduleRepositoryProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
  }

  @Override
  public ScheduleViewModel get() {
    return newInstance(scheduleRepositoryProvider.get());
  }

  public static ScheduleViewModel_Factory create(
      Provider<ScheduleRepository> scheduleRepositoryProvider) {
    return new ScheduleViewModel_Factory(scheduleRepositoryProvider);
  }

  public static ScheduleViewModel newInstance(ScheduleRepository scheduleRepository) {
    return new ScheduleViewModel(scheduleRepository);
  }
}
