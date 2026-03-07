package com.showedup.app.di;

import com.showedup.app.data.ShowedUpDatabase;
import com.showedup.app.data.dao.WeeklyScheduleDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideWeeklyScheduleDaoFactory implements Factory<WeeklyScheduleDao> {
  private final Provider<ShowedUpDatabase> dbProvider;

  public AppModule_ProvideWeeklyScheduleDaoFactory(Provider<ShowedUpDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public WeeklyScheduleDao get() {
    return provideWeeklyScheduleDao(dbProvider.get());
  }

  public static AppModule_ProvideWeeklyScheduleDaoFactory create(
      Provider<ShowedUpDatabase> dbProvider) {
    return new AppModule_ProvideWeeklyScheduleDaoFactory(dbProvider);
  }

  public static WeeklyScheduleDao provideWeeklyScheduleDao(ShowedUpDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWeeklyScheduleDao(db));
  }
}
