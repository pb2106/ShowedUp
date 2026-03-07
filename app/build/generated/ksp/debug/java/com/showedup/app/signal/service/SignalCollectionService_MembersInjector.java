package com.showedup.app.signal.service;

import com.showedup.app.data.repository.AttendanceRepository;
import com.showedup.app.data.repository.SecurityRepository;
import com.showedup.app.signal.MultiModalSignalCollector;
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
public final class SignalCollectionService_MembersInjector implements MembersInjector<SignalCollectionService> {
  private final Provider<MultiModalSignalCollector> signalCollectorProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  private final Provider<SecurityRepository> securityRepositoryProvider;

  public SignalCollectionService_MembersInjector(
      Provider<MultiModalSignalCollector> signalCollectorProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<SecurityRepository> securityRepositoryProvider) {
    this.signalCollectorProvider = signalCollectorProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
    this.securityRepositoryProvider = securityRepositoryProvider;
  }

  public static MembersInjector<SignalCollectionService> create(
      Provider<MultiModalSignalCollector> signalCollectorProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<SecurityRepository> securityRepositoryProvider) {
    return new SignalCollectionService_MembersInjector(signalCollectorProvider, attendanceRepositoryProvider, securityRepositoryProvider);
  }

  @Override
  public void injectMembers(SignalCollectionService instance) {
    injectSignalCollector(instance, signalCollectorProvider.get());
    injectAttendanceRepository(instance, attendanceRepositoryProvider.get());
    injectSecurityRepository(instance, securityRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.showedup.app.signal.service.SignalCollectionService.signalCollector")
  public static void injectSignalCollector(SignalCollectionService instance,
      MultiModalSignalCollector signalCollector) {
    instance.signalCollector = signalCollector;
  }

  @InjectedFieldSignature("com.showedup.app.signal.service.SignalCollectionService.attendanceRepository")
  public static void injectAttendanceRepository(SignalCollectionService instance,
      AttendanceRepository attendanceRepository) {
    instance.attendanceRepository = attendanceRepository;
  }

  @InjectedFieldSignature("com.showedup.app.signal.service.SignalCollectionService.securityRepository")
  public static void injectSecurityRepository(SignalCollectionService instance,
      SecurityRepository securityRepository) {
    instance.securityRepository = securityRepository;
  }
}
