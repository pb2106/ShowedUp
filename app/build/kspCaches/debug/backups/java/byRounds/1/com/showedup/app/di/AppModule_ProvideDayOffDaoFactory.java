package com.showedup.app.di;

import com.showedup.app.data.ShowedUpDatabase;
import com.showedup.app.data.dao.DayOffDao;
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
public final class AppModule_ProvideDayOffDaoFactory implements Factory<DayOffDao> {
  private final Provider<ShowedUpDatabase> dbProvider;

  public AppModule_ProvideDayOffDaoFactory(Provider<ShowedUpDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DayOffDao get() {
    return provideDayOffDao(dbProvider.get());
  }

  public static AppModule_ProvideDayOffDaoFactory create(Provider<ShowedUpDatabase> dbProvider) {
    return new AppModule_ProvideDayOffDaoFactory(dbProvider);
  }

  public static DayOffDao provideDayOffDao(ShowedUpDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDayOffDao(db));
  }
}
