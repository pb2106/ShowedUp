package com.showedup.app.scheduler;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.showedup.app.data.repository.AttendanceRepository;
import com.showedup.app.data.repository.ScheduleRepository;
import com.showedup.app.data.repository.SecurityRepository;
import com.showedup.app.signal.MultiModalSignalCollector;
import dagger.internal.DaggerGenerated;
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
public final class AttendanceTriggerWorker_Factory {
  private final Provider<ScheduleRepository> scheduleRepositoryProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  private final Provider<MultiModalSignalCollector> signalCollectorProvider;

  private final Provider<SecurityRepository> securityRepositoryProvider;

  public AttendanceTriggerWorker_Factory(Provider<ScheduleRepository> scheduleRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<MultiModalSignalCollector> signalCollectorProvider,
      Provider<SecurityRepository> securityRepositoryProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
    this.signalCollectorProvider = signalCollectorProvider;
    this.securityRepositoryProvider = securityRepositoryProvider;
  }

  public AttendanceTriggerWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, scheduleRepositoryProvider.get(), attendanceRepositoryProvider.get(), signalCollectorProvider.get(), securityRepositoryProvider.get());
  }

  public static AttendanceTriggerWorker_Factory create(
      Provider<ScheduleRepository> scheduleRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<MultiModalSignalCollector> signalCollectorProvider,
      Provider<SecurityRepository> securityRepositoryProvider) {
    return new AttendanceTriggerWorker_Factory(scheduleRepositoryProvider, attendanceRepositoryProvider, signalCollectorProvider, securityRepositoryProvider);
  }

  public static AttendanceTriggerWorker newInstance(Context appContext,
      WorkerParameters workerParams, ScheduleRepository scheduleRepository,
      AttendanceRepository attendanceRepository, MultiModalSignalCollector signalCollector,
      SecurityRepository securityRepository) {
    return new AttendanceTriggerWorker(appContext, workerParams, scheduleRepository, attendanceRepository, signalCollector, securityRepository);
  }
}
