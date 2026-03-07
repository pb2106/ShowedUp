package com.showedup.app.di;

import com.showedup.app.data.ShowedUpDatabase;
import com.showedup.app.data.dao.AttendanceDao;
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
public final class AppModule_ProvideAttendanceDaoFactory implements Factory<AttendanceDao> {
  private final Provider<ShowedUpDatabase> dbProvider;

  public AppModule_ProvideAttendanceDaoFactory(Provider<ShowedUpDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AttendanceDao get() {
    return provideAttendanceDao(dbProvider.get());
  }

  public static AppModule_ProvideAttendanceDaoFactory create(
      Provider<ShowedUpDatabase> dbProvider) {
    return new AppModule_ProvideAttendanceDaoFactory(dbProvider);
  }

  public static AttendanceDao provideAttendanceDao(ShowedUpDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAttendanceDao(db));
  }
}
