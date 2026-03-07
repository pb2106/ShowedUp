package com.showedup.app.scheduler;

import com.showedup.app.data.dao.TimetableDao;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<TimetableDao> timetableDaoProvider;

  public BootReceiver_MembersInjector(Provider<TimetableDao> timetableDaoProvider) {
    this.timetableDaoProvider = timetableDaoProvider;
  }

  public static MembersInjector<BootReceiver> create(Provider<TimetableDao> timetableDaoProvider) {
    return new BootReceiver_MembersInjector(timetableDaoProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectTimetableDao(instance, timetableDaoProvider.get());
  }

  @InjectedFieldSignature("com.showedup.app.scheduler.BootReceiver.timetableDao")
  public static void injectTimetableDao(BootReceiver instance, TimetableDao timetableDao) {
    instance.timetableDao = timetableDao;
  }
}
