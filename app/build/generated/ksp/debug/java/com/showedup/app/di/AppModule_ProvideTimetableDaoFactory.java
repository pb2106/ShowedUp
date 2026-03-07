package com.showedup.app.di;

import com.showedup.app.data.ShowedUpDatabase;
import com.showedup.app.data.dao.TimetableDao;
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
public final class AppModule_ProvideTimetableDaoFactory implements Factory<TimetableDao> {
  private final Provider<ShowedUpDatabase> dbProvider;

  public AppModule_ProvideTimetableDaoFactory(Provider<ShowedUpDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TimetableDao get() {
    return provideTimetableDao(dbProvider.get());
  }

  public static AppModule_ProvideTimetableDaoFactory create(Provider<ShowedUpDatabase> dbProvider) {
    return new AppModule_ProvideTimetableDaoFactory(dbProvider);
  }

  public static TimetableDao provideTimetableDao(ShowedUpDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTimetableDao(db));
  }
}
