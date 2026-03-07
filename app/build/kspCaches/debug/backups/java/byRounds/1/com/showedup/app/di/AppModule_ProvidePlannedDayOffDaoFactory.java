package com.showedup.app.di;

import com.showedup.app.data.ShowedUpDatabase;
import com.showedup.app.data.dao.PlannedDayOffDao;
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
public final class AppModule_ProvidePlannedDayOffDaoFactory implements Factory<PlannedDayOffDao> {
  private final Provider<ShowedUpDatabase> dbProvider;

  public AppModule_ProvidePlannedDayOffDaoFactory(Provider<ShowedUpDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PlannedDayOffDao get() {
    return providePlannedDayOffDao(dbProvider.get());
  }

  public static AppModule_ProvidePlannedDayOffDaoFactory create(
      Provider<ShowedUpDatabase> dbProvider) {
    return new AppModule_ProvidePlannedDayOffDaoFactory(dbProvider);
  }

  public static PlannedDayOffDao providePlannedDayOffDao(ShowedUpDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePlannedDayOffDao(db));
  }
}
